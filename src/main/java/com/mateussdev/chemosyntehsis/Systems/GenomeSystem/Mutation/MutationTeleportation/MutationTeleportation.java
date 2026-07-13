package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationTeleportation;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Mixin.MobAccessor;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Random;

public class MutationTeleportation extends Mutation {

    public MutationTeleportation(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationTeleportation> model = new MutationTeleportation_Model();
    private final GeoRenderer<MutationTeleportation> renderer = new MutationTeleportation_Renderer(model);

    public static final int TELEPORTATION_COOLDOWN = 100;
    public static final float TELEPORTATION_DISTANCE = 8.0f;
    public int teleportationT = 0;


    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "body"; }

    @Override
    public int tier() { return 1; }

    @Override public int getCost() { return 4; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationTeleportation_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            if (mob.tickCount % 2 == 0) {
                //Enderman particles
                slvl.sendParticles(ParticleTypes.REVERSE_PORTAL, mob.position().x, mob.position().y, mob.position().z, 2, mob.getBoundingBox().getXsize(), mob.getBoundingBox().getYsize(), mob.getBoundingBox().getZsize(), 0.5f);
            }

            if(teleportationT > 0) --teleportationT;
        }
    }

    @Override
    public void onInit(Mob mob) {
        //Save the to-be changed values

        //Add floating goal
        mob.goalSelector.addGoal(0, new TeleportNearTargetMutGoal(mob, this, TELEPORTATION_DISTANCE));
    }

    @Override
    public float onHurt(Mob mob, DamageSource source, float amount) {
        if(amount <= 0f) return 0f;

        if(source.getDirectEntity() instanceof Projectile projectile) {
            teleportRandomly(mob);
            return 0f;
        }
        if(mob.getRandom().nextFloat() < 0.75f) {
            teleportRandomly(mob);
        }
        return 1.0f;
    }

    private void teleportRandomly(Mob mob) {
        double angle = mob.getRandom().nextDouble() * Math.PI * 2;
        double distance = 8 + mob.getRandom().nextDouble() * 12;
        double x = mob.blockPosition().getX() + Math.cos(angle) * distance;
        double z = mob.blockPosition().getZ() + Math.sin(angle) * distance;
        double y = mob.blockPosition().getY();

        Vec3 teleportPos = new Vec3(x, y, z);
        teleport(teleportPos, mob);
    }

    @Override
    public void onRemove(Mob mob) {
        //Reset values
    }

    @Override
    public boolean canMutateMob(Mob mob) {

        //Generic mob mask
        List<EntityType<?>> disabledMobs = List.of(
                ModEntities.TETH_ENDERMAN.get()
        );

        for(EntityType<?> type : disabledMobs) {
            if(mob.getType() == type) {
                return false;
            }
        }

        return true;
    }

    public boolean teleport(Vec3 teleportPosition, Mob mob) {
        if (mob.level() instanceof ServerLevel slvl && mob.canBeSeenByAnyone()) {
            for (int i = 0; i < 5; i++) {
                double x = teleportPosition.x + ((mob.getRandom().nextFloat() * 2 - 1) * i);
                double y = teleportPosition.y + ((mob.getRandom().nextFloat() * 2 - 1) * i);
                double z = teleportPosition.z + ((mob.getRandom().nextFloat() * 2 - 1) * i);
                boolean teleportSuccessful = checkTeleportPosition(new Vec3(x, y, z), slvl, mob);
                if (teleportSuccessful) {
                    mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.75f);
                    mob.playSound(SoundEvents.SHULKER_TELEPORT, 0.75f, 1f);
                    mob.playSound(SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, 0.75f, 1f);

                    slvl.sendParticles(ParticleTypes.REVERSE_PORTAL, mob.position().x, mob.position().y, mob.position().z, 15, mob.getBoundingBox().getXsize(), mob.getBoundingBox().getYsize(), mob.getBoundingBox().getZsize(), 2f);
                    slvl.sendParticles(ParticleTypes.FLASH, mob.position().x, mob.getEyePosition().y, mob.position().z, 1, 0, 0, 0, 2f);
                    slvl.sendParticles(ParticleTypes.REVERSE_PORTAL, x, y, z, 15, mob.getBoundingBox().getXsize(), mob.getBoundingBox().getYsize(), mob.getBoundingBox().getZsize(), 2f);
                    mob.randomTeleport(x, y, z, true);

                    mob.xo = x;
                    mob.yo = y;
                    mob.zo = z;
                    mob.level().gameEvent(GameEvent.TELEPORT, mob.position(), GameEvent.Context.of(mob));

                    slvl.broadcastEntityEvent(mob, (byte) 46);

                    mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.75f);
                    mob.playSound(SoundEvents.SHULKER_TELEPORT, 0.75f, 1f);
                    mob.playSound(SoundEvents.IRON_GOLEM_ATTACK, 0.75f, 1f);
                    return true;
                }
            }

            //Failed the teleport
            mob.hurt(mob.damageSources().generic(), 3);
            mob.playSound(SoundEvents.ENDERMAN_DEATH);
//            stunT = 60;
            mob.getNavigation().stop();
        }
        return false;
    }

    protected boolean checkTeleportPosition(Vec3 position, ServerLevel slvl, Mob mob) {

        //Find the actually checked position
        BlockPos blockpos = BlockPos.containing(position);
        BlockState blockstate = mob.level().getBlockState(BlockPos.containing(position));

        double d3 = position.y;
        Level level = mob.level();
        if (level.hasChunkAt(blockpos)) {
            boolean flagchk = false;

            //If initial position is in air
            if (blockstate.isAir()) {
                while (!flagchk && blockpos.getY() > level.getMinBuildHeight()) {
                    BlockPos blockpos1 = blockpos.below();
                    blockstate = level.getBlockState(blockpos1);
                    if (blockstate.blocksMotion()) {
                        flagchk = true;
                    } else {
                        --d3;
                        blockpos = blockpos1;
                    }
                }
            } else if (blockstate.blocksMotion()) {

                //Check up block until we emerge
                while (!flagchk && blockpos.getY() < level.getMaxBuildHeight()) {
                    BlockPos blockpos1 = blockpos.above();
                    blockstate = level.getBlockState(blockpos1);
                    if (blockstate.blocksMotion()) {
                        flagchk = true;
                    } else {
                        ++d3;
                        blockpos = blockpos1;
                    }
                }
            }
        }

        //check final updated position
        blockstate = level.getBlockState(blockpos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        boolean flag2 = !slvl.noCollision(mob.getBoundingBox().deflate(0.2f).move(position.subtract(mob.position())));

        return !flag && !flag1 && !flag2;
    }

    protected Vec3 cleanTeleportPosition(Vec3 position, ServerLevel slvl, Mob mob) {

        //Find the actually checked position
        BlockPos blockpos = BlockPos.containing(position);
        BlockState blockstate = mob.level().getBlockState(BlockPos.containing(position));

        double d3 = position.y;
        Level level = mob.level();
        if (level.hasChunkAt(blockpos)) {
            boolean flagchk = false;

            //If initial position is in air
            if (blockstate.isAir()) {
                while (!flagchk && blockpos.getY() > level.getMinBuildHeight()) {
                    BlockPos blockpos1 = blockpos.below();
                    blockstate = level.getBlockState(blockpos1);
                    if (blockstate.blocksMotion()) {
                        flagchk = true;
                    } else {
                        --d3;
                        blockpos = blockpos1;
                    }
                }
            } else if (blockstate.blocksMotion()) {

                //Check up block until we emerge
                while (!flagchk && blockpos.getY() < level.getMaxBuildHeight()) {
                    BlockPos blockpos1 = blockpos.above();
                    blockstate = level.getBlockState(blockpos1);
                    if (blockstate.blocksMotion()) {
                        flagchk = true;
                    } else {
                        ++d3;
                        blockpos = blockpos1;
                    }
                }
            }
        }

        return blockpos.getCenter();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mutation_flight", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("active"));
        }));
    }

    @Override
    public Mutation copy(Random rng) {
        return new MutationTeleportation(this.getTypeId(), rng.nextInt(Integer.MAX_VALUE));
    }
}
