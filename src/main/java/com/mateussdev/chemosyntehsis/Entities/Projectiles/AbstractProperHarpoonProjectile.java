package com.mateussdev.chemosyntehsis.Entities.Projectiles;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
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
    private static final int REQUIRED_HARPOONS_TO_TEAR_TO_SHREDS = 3;
    private static final int HARPOON_ARROW_COUNT = 256; //How many arrows will be added to the arrow count for the entity. Needed for differentiating

    public enum HarpoonState {
        SHOT,
        ATTACHED_BLOCK,
        ATTACHED_MOB,
        RETRACTING
    }
    public HarpoonState state;
    public float currentHarpoonLength = 0f;
    private float oldLength = 0f;

    private Entity attachedEntity;
    private Vec3 attachPosition;

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
        if(getOwner() == null) {
            this.discard();
            return;
        }
        if(state == null) return;

        //Get server
        if(!(this.level() instanceof ServerLevel slvl)) return;

        switch(state) {
            case SHOT -> {
                setNoGravity(false);
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
                //Prevent immobile mobs to launch itself at blocks
                if(getOwner() instanceof BaseOrganelle) {
                    retractHarpoon();
                    return;
                }

                //Reel in the owner to the block
                Vec3 launchDirection = getOwner().position().subtract(attachPosition).normalize();
                if(getOwner() instanceof LivingEntity LE) {
                    LE.setDeltaMovement(LE.getDeltaMovement().add(launchDirection.scale(RETRACT_SPEED/1)));
//                    StaticSiliconiteMethods.debugLog("delta: "+LE.getDeltaMovement());
                }

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
            case ATTACHED_MOB -> {
                setNoGravity(true);
                //Check if target is alive and exists
                if(attachedEntity == null || (attachedEntity instanceof LivingEntity attachedLE && attachedLE.isDeadOrDying())) retractHarpoon();
                //Reel in the mob to the owner
                Vec3 ownerDirection = getOwner().position().subtract(attachedEntity.position()).normalize();
                attachedEntity.setDeltaMovement(attachedEntity.getDeltaMovement().add(ownerDirection.scale(RETRACT_SPEED/2)));
                this.setPos(attachedEntity.getEyePosition());

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
            case RETRACTING -> {
                setNoGravity(true);
                Vec3 ownerDirection = getOwner().getEyePosition().subtract(this.position()).normalize();
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
        this.pickup = Pickup.ALLOWED;
        this.setDeltaMovement(new Vec3(0, 0, 0));
        this.setNoGravity(true);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.state = HarpoonState.ATTACHED_MOB;

        Entity entity = pResult.getEntity();

        boolean flag1 = entity instanceof Player player && player.isBlocking();
        boolean flag2 = entity instanceof EnderMan;

        if (entity instanceof LivingEntity) {

            //Check each flag separately
            if(flag1 || flag2) {
                retractHarpoon();
                return;
            }

            //Keep track of harpoons by adding a very large amount of arrows
            LivingEntity livingentity = (LivingEntity) entity;
            if (!this.level().isClientSide && this.getPierceLevel() <= 0) {
                livingentity.setArrowCount(livingentity.getArrowCount() + 256);
                //If arrow count is 768 or more - rip entity to shreds
                if(livingentity.getArrowCount() > REQUIRED_HARPOONS_TO_TEAR_TO_SHREDS*256) {

                }
            }
        }
        this.attachedEntity = entity;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if(this.state == HarpoonState.ATTACHED_MOB) return;
        this.state = HarpoonState.ATTACHED_BLOCK;
        this.attachPosition = pResult.getLocation();
//        super.onHitBlock(pResult);
    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        boolean flag1 = state != HarpoonState.RETRACTING; //Cannot hit mobs when retracting
        return super.canHitEntity(p_36743_);
    }
}
