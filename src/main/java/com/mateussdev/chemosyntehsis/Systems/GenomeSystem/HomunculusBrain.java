package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.genome.GenomeCarrier;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPType;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.IDspReceptor;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HomunculusBrain{

    // ===== Config ===== //
    public static final int CYCLE_DURATION_TICKS = 600;
    public static final int BASE_GENE_POOL_POINTS = 50;
    public static final float MUTATION_DEVIATION = 0.2f;
    public static final int MAX_MUTATIONS_PER_GENE = 3;
    public static final float MAX_RATIO = 4.0f;

    // ===== State management ===== //
    private int genePoolBalance;
    private int cycleTimer;

    private final Map<UUID, GeneFitnessData> trackedGenes = new ConcurrentHashMap<>();
    private final List<Gene> templateList = new ArrayList<>();

    //Outside world context
    private final IHomunculusContext context;

    public HomunculusBrain(IHomunculusContext context) {
        this.context = context;
        this.genePoolBalance = BASE_GENE_POOL_POINTS;
        this.cycleTimer = 0;
    }

    //Brain tick
    public void tickBrain() {
        if(++cycleTimer >= CYCLE_DURATION_TICKS) {
            endCycle();
            startNewCycle();
            this.cycleTimer = 0;
        }

//        StaticSiliconiteMethods.debugLog(trackedGenes.size()+" - tracked genes");
//        StaticSiliconiteMethods.debugLog(exampleList.size()+" - example list");
    }

    // ===== Cycle management ===== //
    public void startNewCycle() {
        if((context.getServerLevel() == null)) return;

        //Buy and choose the next generation of genes
        List<Gene> chosenGenes = generateNewGeneration();

        //Send out genome carriers to specific mobs around the homunculus
        List<Mob> assignedTargets = new ArrayList<>();
        for (Gene gene : chosenGenes) {
            trackGene(gene, gene.id);
            Mob target = findUniqueTarget(assignedTargets, context.getServerLevel(), context.getPosition(), 64.0);
            if (target == null) {
                //Not enough mobs - release DSP
                if(context.getHomunculusMob() instanceof IDspReceptor receptor) {
                    receptor.emitDSP(DSPType.D_MM_MOBDEFICIT, 1000, context.getHomunculusMob());
                }
                break;
            }
            assignedTargets.add(target);

            GenomeCarrier carrier = new GenomeCarrier(ModEntities.GENOME_CARRIER.get(), context.getServerLevel());
            carrier.setPos(context.getPosition().x, context.getPosition().y + 1, context.getPosition().z);
            carrier.carriedGene = gene;
            carrier.hostHomunculus = context.getHomunculusId();
            carrier.setForcedTarget(target);   // assign specific target
            context.getServerLevel().addFreshEntity(carrier);
        }

        //Add to 0 most of the time. Could be changed by having other mobs generating biomass to create more gene pool points
        genePoolBalance += BASE_GENE_POOL_POINTS;
    }

    public void endCycle(){
        //Determine top 3 best genes
        Random rand = new Random();
        List<GeneFitnessData> top = trackedGenes.values().stream().sorted(Comparator.comparingDouble(GeneFitnessData::getFitness).reversed())
                .limit(3).toList();

        templateList.clear();
        for(GeneFitnessData fitnessData : top) templateList.add(fitnessData.getGene().copy(rand));

        //Reset tracked genes
        trackedGenes.clear();

        context.onCycleCompleted(templateList);
    }

    //Fitness tracking
    public void addFitnessToGene(UUID geneID, float points) {
        GeneFitnessData data = trackedGenes.get(geneID);
        if(data != null) {
            data.addFitness(points);
        }

        //TODO: Handle old but well-performing genes
    }

    //Tracking genes
    public void trackGene(Gene gene, UUID id) {
        trackedGenes.put(id, new GeneFitnessData(gene, id));
    }

    // ===== Genetic algorithms ===== //
    private List<Gene> generateNewGeneration(){
        List<Gene> result = new ArrayList<>();
        int remainingPoints = genePoolBalance;

        List<Gene> templates = templateList.isEmpty() ? Collections.emptyList() : new ArrayList<>(templateList);
        Random rand = new Random();

        int attempts = 0;
        while (remainingPoints > 0 && attempts < 100) {
            attempts++;
            Gene candidate;
            if (!templates.isEmpty()) {
                //Create new gene by mutating an existing template gene
                Gene template = templates.get(rand.nextInt(templates.size()));
                candidate = mutateGene(template);
            } else {
                //Create brand new fully random gene
                candidate = generateRandomGene();
            }
            int cost = candidate.getCost();
            if (cost <= remainingPoints) {
                result.add(candidate);
                remainingPoints -= cost;
                attempts = 0;
            } else {
                //If we cannot afford even the cheapest possible gene, break to avoid infinite loop
                if (result.isEmpty() && templates.isEmpty()) break;
                //Otherwise try another template or random gene (may succeed with lower cost)
            }
            //If we've tried many times and still can't afford anything, just break
            if (attempts >= 100 && result.isEmpty()) break;
        }

        genePoolBalance = 0; //We used up all the points. Set the gene pool to zero
        return result;
    }

    private Gene generateRandomGene() {
        Gene gene = new Gene();
        Random rand = new Random();

        //Create new random ratio for each
        GeneAttributeMutation attributeMutation = gene.attributeMutation;
        attributeMutation.health_speed = randomRatio(rand, 3f);
        attributeMutation.health_armor = randomRatio(rand, 3f);
        attributeMutation.armor_attackDamage = randomRatio(rand, 3f);
        attributeMutation.armorToughness_armor = randomRatio(rand, 3f);
        attributeMutation.attackSpeed_attackDamage = randomRatio(rand, 3f);
        attributeMutation.attackSpeed_attackKnockback = randomRatio(rand, 3f);
        attributeMutation.speed_knockbackResistance = randomRatio(rand, 3f);
        attributeMutation.followRange_attackDamage = randomRatio(rand, 3f);
        attributeMutation.followRange_speed = randomRatio(rand, 3f);

        int numMutations = rand.nextInt(MAX_MUTATIONS_PER_GENE + 1);
        List<MutationType> allTypes = ModMutations.MUTATION_TYPES.getEntries().stream()
                .map(RegistryObject::get).toList();
        for (int i = 0; i < numMutations; i++) {
            if (!allTypes.isEmpty()) {
                MutationType type = allTypes.get(rand.nextInt(allTypes.size()));
                Mutation mutation = type.create(rand.nextInt(Integer.MAX_VALUE));
                if(!gene.mutations.contains(mutation)) {
                    gene.addMutation(mutation);
                }
            }
        }
        return gene;
    }

    private Gene mutateGene(Gene template){
        Random rand = new Random();
        Gene mutated = template.copy(rand);

        //Mutate slightly each ratio
        GeneAttributeMutation attributeMutation = mutated.attributeMutation;
        //Add a random amount between 0 and deviation, and have uniform 50/50 for it to get added or get subtracted
        attributeMutation.health_speed = mutateRatio(rand, attributeMutation.health_speed);
        attributeMutation.health_armor = mutateRatio(rand, attributeMutation.health_armor);
        attributeMutation.armor_attackDamage = mutateRatio(rand, attributeMutation.armor_attackDamage);
        attributeMutation.armorToughness_armor = mutateRatio(rand, attributeMutation.armorToughness_armor);
        attributeMutation.attackSpeed_attackDamage = mutateRatio(rand, attributeMutation.attackSpeed_attackDamage);
        attributeMutation.attackSpeed_attackKnockback = mutateRatio(rand, attributeMutation.attackSpeed_attackKnockback);
        attributeMutation.speed_knockbackResistance = mutateRatio(rand, attributeMutation.speed_knockbackResistance);
        attributeMutation.followRange_attackDamage = mutateRatio(rand, attributeMutation.followRange_attackDamage);
        attributeMutation.followRange_speed = mutateRatio(rand, attributeMutation.followRange_speed);

        if (rand.nextFloat() < 0.3f && mutated.mutations.size() < MAX_MUTATIONS_PER_GENE) {
            RegistryObject<MutationType> randomType = ModMutations.MUTATION_TYPES.getEntries().stream().toList()
                    .get(rand.nextInt(ModMutations.MUTATION_TYPES.getEntries().size()));
            Mutation newMut = randomType.get().create(rand.nextInt(Integer.MAX_VALUE));
            mutated.addMutation(newMut);
        }
        if (rand.nextFloat() < 0.2f && !mutated.mutations.isEmpty()) {
            mutated.mutations.remove(rand.nextInt(mutated.mutations.size()));
        }
        return mutated;
    }

    private float mutateRatio(Random rand, float value) {
        //Since ratios are not linear but logarithmic, adjust
        float log = (float) Math.log(Math.max(value, 1e-6f));
        log += rand.nextFloat() * MUTATION_DEVIATION * (rand.nextBoolean() ? 1 : -1);
        return clampRatio((float) Math.exp(log));
    }

    private float clampRatio(float value) {
        return Math.min(Math.max(value, 1f / MAX_RATIO), MAX_RATIO);
    }

    private float randomRatio(Random rng, float maxDiff) {
        float sideA = Math.max(rng.nextFloat()*maxDiff, 0.00001f); //generate random float between 0 and max
        float sideB = Math.max(rng.nextFloat()*maxDiff, 0.00001f); //generate random float between 0 and max
        float ratio = sideA/sideB;

        return Math.min(Math.max(ratio, 1f / MAX_RATIO), MAX_RATIO);
    }

    private Mob findUniqueTarget(List<Mob> alreadyAssigned, ServerLevel level, Vec3 center, double radius) {
        List<Mob> candidates = level.getEntitiesOfClass(Mob.class,
                AABB.ofSize(center, radius, radius, radius),
                mob -> mob instanceof IGenomeModifiable && !alreadyAssigned.contains(mob));
        if (candidates.isEmpty()) return null;
        return candidates.get(new Random().nextInt(candidates.size()));
    }

    // ----- NBT serialization (saved by the homunculus entity) -----
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("genePoolBalance", genePoolBalance);
        tag.putInt("cycleTimer", cycleTimer);

        CompoundTag exampleTag = new CompoundTag();
        int i = 0;
        for (Gene g : templateList) {
            exampleTag.put("gene_" + i, g.serialize());
            i++;
        }
        tag.put("exampleList", exampleTag);


        return tag;
    }

    public void deserialize(CompoundTag tag) {
        genePoolBalance = tag.getInt("genePoolBalance");
        cycleTimer = tag.getInt("cycleTimer");
        templateList.clear();
        CompoundTag exampleTag = tag.getCompound("exampleList");
        for (String key : exampleTag.getAllKeys()) {
            CompoundTag geneTag = exampleTag.getCompound(key);
            Gene g = Gene.deserialize(geneTag);
            if (g != null) templateList.add(g);
        }
    }

    //util
    public int getGenePoolBalance() { return genePoolBalance; }
    public int getCycleTimer() { return cycleTimer; }
    public int getCycleDuration() { return CYCLE_DURATION_TICKS; }
    public List<Gene> getTemplateList() { return templateList; }
    public Map<UUID, GeneFitnessData> getTrackedGenes() { return trackedGenes; }
}
