package com.mateussdev.chemosyntehsis.Entities.Projectiles;

import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public abstract class AbstractProperHarpoonProjectile extends AbstractArrow implements GeoAnimatable {

    public AbstractProperHarpoonProjectile(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    public AbstractProperHarpoonProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //===== Constantz =====//
    private static final float HARPOON_CLICK_SOUND_PERIOD = 1f; //Will play a click sound every X blocks
    private static final float RETRACT_SPEED = 0.3f;

    public enum HarpoonState {
        SHOT,
        ATTACHED_BLOCK,
        ATTACHED_MOB,
        RETRACTING
    }
    public HarpoonState state;
    public float currentHarpoonLength = 0f;
    private float oldLength = 0f;

    public float getMaxHarpoonLength() {
        //Returns the maximum length the harpoon can be in blocks
        return 20f;
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        state = HarpoonState.SHOT;
    }

    @Override
    public void tick() {
        super.tick();

        //Do not tick if theres no owner
        if(getOwner() == null) return;
        if(state == null) return;

        //Get server
        if(!(this.level() instanceof ServerLevel slvl)) return;

        switch(state) {
            case SHOT -> {
                //keep track of length
                currentHarpoonLength = getOwner().distanceTo(this);
                if(currentHarpoonLength != oldLength) {
                    oldLength = currentHarpoonLength;
                    //Play sound
                    if(Mth.floor(oldLength) % HARPOON_CLICK_SOUND_PERIOD == 0) {
                        slvl.playSound(null, this.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.NEUTRAL, 0.33f, 1f - currentHarpoonLength/getMaxHarpoonLength());
                    }
                }


                //Retract harpoon when reached length
                if(currentHarpoonLength >= getMaxHarpoonLength()) {
                    retractHarpoon();
                    slvl.playSound(null, getOwner().blockPosition(), SoundEvents.TRIDENT_HIT, SoundSource.NEUTRAL, 1f, 0.5f);
                }
            }
            case ATTACHED_BLOCK -> {
                //Reel in the owner to the block
                //TODO: that
            }
            case ATTACHED_MOB -> {
                //Reel in the mob to the owner
                Vec3 ownerDirection = getOwner().position().subtract(this.position()).normalize();
                this.setDeltaMovement(ownerDirection.scale(RETRACT_SPEED));
                //TODO: this also
            }
            case RETRACTING -> {
                Vec3 ownerDirection = getOwner().position().subtract(this.position()).normalize();
                this.setDeltaMovement(ownerDirection.scale(RETRACT_SPEED));
                //keep track of length
                currentHarpoonLength = getOwner().distanceTo(this);
                if(currentHarpoonLength != oldLength) {
                    oldLength = currentHarpoonLength;
                    //Play sound
                    if(Mth.floor(oldLength) % HARPOON_CLICK_SOUND_PERIOD == 0) {
                        slvl.playSound(null, this.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.NEUTRAL, 0.33f, 1f - currentHarpoonLength/getMaxHarpoonLength());
                    }
                }



                if(currentHarpoonLength < 2f) this.discard();
            }
        }
    }

    public void retractHarpoon() {
        this.state = HarpoonState.RETRACTING;
        this.setDeltaMovement(new Vec3(0, 0, 0));
        this.setNoGravity(true);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.state = HarpoonState.ATTACHED_MOB;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.state = HarpoonState.ATTACHED_BLOCK;
    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        boolean flag1 = state != HarpoonState.RETRACTING; //Cannot hit mobs when retracting
        return super.canHitEntity(p_36743_);
    }
}
