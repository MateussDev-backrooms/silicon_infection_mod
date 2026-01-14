package com.mateussdev.chemosyntehsis.Entities.vasc_roller;

import com.mateussdev.chemosyntehsis.Blocks.FleshPileBlock;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.AbstractHarpoonProjectile;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.Interfaces.IBiomassContainer;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

public class VascularRoller extends BaseOrganelle implements IBiomassContainer, RangedAttackMob {
    public VascularRoller(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    private int harpoon_cooldown = 80;

    public static final EntityDataAccessor<Integer> COLLECTED_BIOMASS = SynchedEntityData.defineId(VascularRoller.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> HARPOON_ATTACHED = SynchedEntityData.defineId(VascularRoller.class, EntityDataSerializers.BOOLEAN);

    private static final TargetingConditions HARPOON_CONDITIONS =
            TargetingConditions.forCombat()
                    .range(16.0)
                    .selector(StaticSiliconiteMethods::shouldAttackMob);
    private static final TargetingConditions GIB_CONDITIONS =
            TargetingConditions.forCombat()
                    .range(16.0);

    private static final int FLESH_CHECK_RADIUS = 8;

    public BulbHarpoonEntity harpoon = null;

    public LivingEntity targetedEntity;
    public BlockPos targetedBlock;

    private List<EntityType<? extends BaseAmalgamation>> default_amalgamations = List.of(
            ModEntities.AMAL_ZOMBIE.get(),
            ModEntities.AMAL_SPAWNER.get(),
            ModEntities.AMAL_TURRET.get()
    );

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(
                    entityData.get(HARPOON_ATTACHED) ? RawAnimation.begin().thenLoop("idle_harpooned") :
                            RawAnimation.begin().thenLoop("idle"));
        }));
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 16D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public boolean shouldSpawnTendrils() {
        return true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RangedAttackGoal(this, 1.5f, 7, 16));

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        //Seek out
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float v) {
        if (harpoon_cooldown <= 0 && !entityData.get(HARPOON_ATTACHED)) {
            this.targetedEntity = target;

            harpoon = new BulbHarpoonEntity(level(), this);
            harpoon.setPos(getX(), getY(), getZ());

            Vec3 shootDir = target.getEyePosition().subtract(this.position());
            level().playSound(null, blockPosition(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 1f, 1f);
            harpoon.shoot(shootDir, 1.2f, 0f, this, 0.08f, 500);

            level().addFreshEntity(harpoon);

            targetedBlock = null;
            entityData.set(HARPOON_ATTACHED, true);
        }
    }

    //Amalgamation

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel slvl) {
            if (harpoon_cooldown <= 0 && !entityData.get(HARPOON_ATTACHED)) {
                if (targetedEntity == null) {
                    //Connect to flesh blocks
                    BlockPos closestFleshBlock = findNearbyFlesh();
                    if (closestFleshBlock != null) {
                        StaticSiliconiteMethods.spawnBloodBurst(slvl, closestFleshBlock);
                        targetBlock(closestFleshBlock);
                    } else {
                        targetedBlock = null;
                        if (harpoon != null) {
                            retractHarpoon();
                        }
                    }
                }
            } else {
                harpoon_cooldown--;
            }

            //Validate target
            if (entityData.get(HARPOON_ATTACHED)) {
                if (targetedBlock == null) {
                    if (targetedEntity == null) retractHarpoon();
                    else if (targetedEntity.isDeadOrDying() || harpoon == null
                            || harpoon.getCurrentAttachType() == AbstractHarpoonProjectile.AttachTypes.Reeling.ordinal()) {
                        retractHarpoon();
                    }
                } else {
                    if (harpoon == null || harpoon.getCurrentAttachType() == AbstractHarpoonProjectile.AttachTypes.Reeling.ordinal()) {
                        retractHarpoon();
                    }
                }
            } else {
                //Allow for amalgamation

                if (getBiomass() > 30) {
                    //choose random non-mob based amalgamation
                    BaseAmalgamation amalgamation = default_amalgamations.get(random.nextInt(default_amalgamations.size())).create(slvl);
                    amalgamation.moveTo(position());
                    slvl.addFreshEntity(amalgamation);
                    this.discard();
                }
            }

        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (entityData.get(HARPOON_ATTACHED) && harpoon != null) {
            retractHarpoon();
        }
        return super.hurt(pSource, pAmount);
    }

    private void targetBlock(BlockPos target) {
        targetedBlock = target;
        harpoon = new BulbHarpoonEntity(level(), this);
        harpoon.setPos(getX(), getAttachedEyeY(), getZ());

        Vec3 shootDir = target.getCenter().subtract(0, 0.7, 0).subtract(this.position());
        level().playSound(null, blockPosition(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 1f, 1f);
        harpoon.shoot(shootDir, 1.2f, 0f, this, 0.08f, 500);

        level().addFreshEntity(harpoon);
        entityData.set(HARPOON_ATTACHED, true);
    }

    protected void retractHarpoon() {
        targetedEntity = null;
        harpoon.RetractHarpoon();
        entityData.set(HARPOON_ATTACHED, false);
        harpoon_cooldown = random.nextInt(160);
    }

    private BlockPos findNearbyFlesh() {
        BlockPos mobPos = this.blockPosition();
        BlockPos bestTarget = null;
        double closestDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.betweenClosed(
                mobPos.offset(-FLESH_CHECK_RADIUS, -FLESH_CHECK_RADIUS, -FLESH_CHECK_RADIUS),
                mobPos.offset(FLESH_CHECK_RADIUS, FLESH_CHECK_RADIUS, FLESH_CHECK_RADIUS))) {

            BlockState state = this.level().getBlockState(pos);

            if (state.getBlock() instanceof FleshPileBlock) {
                Vec3 startVec = this.position();
                Vec3 endVec = pos.getCenter();

                ClipContext context = new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
                BlockHitResult hit = this.level().clip(context);

                if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(pos)) {
                    double dist = this.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
                    if (dist < closestDist) {
                        closestDist = dist;
                        bestTarget = pos.immutable();
                    }
                }
            }
        }

        return bestTarget;
    }


    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        //Remove harpoon on world load to prevent issues with harpoons not being present
        entityData.set(HARPOON_ATTACHED, false);

        harpoon_cooldown = 20 + random.nextInt(120);
    }

    // ===== Saving ===== //


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(COLLECTED_BIOMASS, 0);
        entityData.define(HARPOON_ATTACHED, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(COLLECTED_BIOMASS, tag.getInt("collected_biomass"));
        entityData.set(HARPOON_ATTACHED, tag.getBoolean("harpoon_attached"));
        harpoon_cooldown = tag.getInt("harpoon_cld");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("collected_biomass", entityData.get(COLLECTED_BIOMASS));
        tag.putBoolean("harpoon_attached", entityData.get(HARPOON_ATTACHED));
        tag.putInt("harpoon_cld", harpoon_cooldown);
    }

    // ===== Biomass stuffs ===== //

    @Override
    public int getBiomass() {
        return entityData.get(COLLECTED_BIOMASS);
    }

    @Override
    public void addBiomass(int amount) {
        if (this.level() instanceof ServerLevel slvl) {
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());
            slvl.playSound(null, blockPosition(), SoundEvents.MUD_BREAK, SoundSource.HOSTILE, 1f, 1f);
            entityData.set(COLLECTED_BIOMASS, entityData.get(COLLECTED_BIOMASS) + amount);
        }
    }


}
