package net.mateuss.chemosynthesis.entity.custom;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.block.ModBlocks;
import net.mateuss.chemosynthesis.block.advanced.IBloodFillable;
import net.mateuss.chemosynthesis.block.advanced.SiliconVeinBlock;
import net.mateuss.chemosynthesis.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class EntityHomunculus extends Monster {
    public EntityHomunculus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState AS_isIdle = new AnimationState();
    private int idleAnimTimeout = 0;
    private int t = 0;

    private int _i = 0, _j = 0, _k = 0;

    private int protection_radius = 3;
    private int spread_radius = 1;

    private TetherMobAction tetherMobAction = new TetherMobAction();

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if(!this.level().isClientSide) {
            ServerLevel lvl = (ServerLevel) this.level();
            this.t++;
            if(t%20==0) {
                lvl.playSound(null, this.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE);
                injectBlood();
            }

        }

        this.setAirSupply(this.getMaxAirSupply());
    }

    private void spreadOrganism(ServerLevel lvl) {
        for(int _x=this.getBlockX()-this.protection_radius; _x<this.getBlockX()+this.protection_radius; _x++) {
            for(int _y=this.getBlockY()-this.protection_radius; _y<this.getBlockY()+this.protection_radius; _y++) {
                for(int _z=this.getBlockZ()-this.protection_radius; _z<this.getBlockZ()+this.protection_radius; _z++) {
                    if(distanceToSqr(_x, _y, _z) > 2) {
                        lvl.setBlock(new BlockPos(_x, _y, _z), ModBlocks.SILICATE_BLOCK.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private void setupAnimationStates() {
        if(this.idleAnimTimeout <= 0) {
            this.idleAnimTimeout = this.random.nextInt(40)+80;
            this.AS_isIdle.start(this.tickCount);
        } else {
            --this.idleAnimTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f=0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    protected void registerGoals() {

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 5D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.DRIPSTONE_BLOCK_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void remove(RemovalReason pReason) {

        super.remove(pReason);
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        this.setDeltaMovement(0, 0, 0);
    }

    private void injectBlood() {
        BlockPos pos = this.blockPosition();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = this.level().getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof IBloodFillable interacted) {
                if(interacted.canReceiveBlood((ServerLevel) this.level(), neighborPos)) {
                    interacted.onBloodFlow(this.level(), neighborPos, this.level().getBlockState(neighborPos), 2);
//                    break;
                }
            }
        }
    }
}
