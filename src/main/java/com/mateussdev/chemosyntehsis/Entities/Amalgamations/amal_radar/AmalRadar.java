package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar;

import com.mateussdev.chemosyntehsis.Core.ModSounds;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.Interfaces.IBiomassGenerator;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.vasc_roller.VascularRoller;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.*;

public class AmalRadar extends BaseAmalgamation implements VibrationSystem {

    public static final EntityDataAccessor<Boolean> IS_ALERTED = SynchedEntityData.defineId(AmalRadar.class, EntityDataSerializers.BOOLEAN);

    // Vibration system components
    private final VibrationSystem.Data vibrationData;
    private final VibrationSystem.User vibrationUser;

    // Radar tracking
    private final Map<UUID, RadarTracker> radarTrackers = new HashMap<>();
    private final Map<BlockPos, RadarTracker> positionTrackers = new HashMap<>();
    private static final int MAX_TRACKERS = 10;
    private int ticksUntilNextRadarUpdate = 0;

    public AmalRadar(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.lookControl = new EarLookControl(this);

        this.vibrationData = new VibrationSystem.Data();
        this.vibrationUser = new RadarVibrationUser(this, 24.0F); // 24 block range
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 38D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(entityData.get(IS_ALERTED) ? RawAnimation.begin().thenLoop("alerted") : RawAnimation.begin()
                        .thenLoop("idle"));
        }));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        //Seek out
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
    }

    // ===== Radar system ===== //
    protected boolean updateRadar = true;
    public static class RadarTracker {
        public enum TrackerType {
            ENTITY,
            POSITION
        }

        private final TrackerType type;
        private UUID entityUUID;
        private BlockPos position;
        public float score;
        private int age;

        public RadarTracker(UUID entityUUID) {
            this.type = TrackerType.ENTITY;
            this.entityUUID = entityUUID;
            this.score = 1f;
            this.age = 0;
        }

        public RadarTracker(BlockPos position) {
            this.type = TrackerType.POSITION;
            this.position = position.immutable();
            this.score = 1f;
            this.age = 0;
        }

        public void changeScore(float delta) {
            score = Math.max(0, Math.min(100, score + delta));
        }

        public void tick() {
            age++;
            // Decay faster as time goes on
            float decayRate = age < 100 ? 0.05f : 0.1f;
            score = Math.max(0, score - decayRate);
        }

        public TrackerType getType() { return type; }
        public UUID getEntityUUID() { return entityUUID; }
        public BlockPos getPosition() { return position; }
        public int getAge() { return age; }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Update vibration system
            VibrationSystem.Ticker.tick(this.level(), this.vibrationData, this.vibrationUser);

            // Update radar trackers less frequently
            if (--ticksUntilNextRadarUpdate <= 0) {
                updateRadarTrackers();
                ticksUntilNextRadarUpdate = 10; // Update every 10 ticks
            }
        }
    }

    private void updateRadarTrackers() {
        // Update and clean up entity trackers
        Iterator<Map.Entry<UUID, RadarTracker>> entityIterator = radarTrackers.entrySet().iterator();
        while (entityIterator.hasNext()) {
            Map.Entry<UUID, RadarTracker> entry = entityIterator.next();
            RadarTracker tracker = entry.getValue();

            tracker.tick();

            if (tracker.score <= 0) {
                entityIterator.remove();
            } else if (tracker.score >= 100) {
                alertNearbyMobsToTracker(tracker);
                entityIterator.remove(); // Remove after alerting
            }
        }

        // Update and clean up position trackers
        Iterator<Map.Entry<BlockPos, RadarTracker>> positionIterator = positionTrackers.entrySet().iterator();
        while (positionIterator.hasNext()) {
            Map.Entry<BlockPos, RadarTracker> entry = positionIterator.next();
            RadarTracker tracker = entry.getValue();

            tracker.tick();

            if (tracker.score <= 0) {
                positionIterator.remove();
            } else if (tracker.score >= 100) {
                alertNearbyMobsToTracker(tracker);
                positionIterator.remove(); // Remove after alerting
            }
        }
    }

    // Handle vibration events
    private void onVibrationDetected(VibrationInfo vibrationInfo) {
        if (!(level() instanceof ServerLevel slvl)) return;

        // Get the source entity of the vibration
        Entity sourceEntity = vibrationInfo.getEntity(slvl).orElse(null);

        // Check if it's a player or living entity that's NOT a BaseSiliconite
        if (sourceEntity instanceof LivingEntity livingEntity) {
            // Skip if it's a siliconite (friendly to radar)
            if (sourceEntity instanceof BaseSiliconite) {
                return;
            }

            // Player or other hostile mob - set score to 100
            UUID entityId = livingEntity.getUUID();

            // Update or create tracker
            RadarTracker tracker = radarTrackers.get(entityId);
            if (tracker == null) {
                // Limit number of trackers
                if (radarTrackers.size() >= MAX_TRACKERS) {
                    removeOldestTracker();
                }
                tracker = new RadarTracker(entityId);
                radarTrackers.put(entityId, tracker);
            }

            // Set score to 100 (maximum alert)
            tracker.score = 100f;

            // Mark as alerted
            this.entityData.set(IS_ALERTED, true);

            // Play alert sound
                slvl.playSound(
                        null,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        ModSounds.AMAL_RADAR_SCREAM.get(),
                        SoundSource.HOSTILE,
                        4.0f,
                        1.0f
                );

        } else if (sourceEntity == null) {
            // Vibration from a block or unknown source - track position
            BlockPos vibrationPos = BlockPos.containing(
                    vibrationInfo.pos().x(),
                    vibrationInfo.pos().y(),
                    vibrationInfo.pos().z()
            );

            // Create position tracker
            RadarTracker tracker = new RadarTracker(vibrationPos);
            tracker.score = 50f; // Lower score for position tracking

            if (positionTrackers.size() >= MAX_TRACKERS) {
                removeOldestPositionTracker();
            }
            positionTrackers.put(vibrationPos, tracker);
        }
    }

    private void removeOldestTracker() {
        UUID oldestId = null;
        int oldestAge = Integer.MAX_VALUE;

        for (Map.Entry<UUID, RadarTracker> entry : radarTrackers.entrySet()) {
            if (entry.getValue().getAge() < oldestAge) {
                oldestAge = entry.getValue().getAge();
                oldestId = entry.getKey();
            }
        }

        if (oldestId != null) {
            radarTrackers.remove(oldestId);
        }
    }

    private void removeOldestPositionTracker() {
        BlockPos oldestPos = null;
        int oldestAge = Integer.MAX_VALUE;

        for (Map.Entry<BlockPos, RadarTracker> entry : positionTrackers.entrySet()) {
            if (entry.getValue().getAge() < oldestAge) {
                oldestAge = entry.getValue().getAge();
                oldestPos = entry.getKey();
            }
        }

        if (oldestPos != null) {
            positionTrackers.remove(oldestPos);
        }
    }

    public void alertNearbyMobsToTracker(RadarTracker tracker) {
        if (!(this.level() instanceof ServerLevel slvl)) return;

        List<BaseSiliconite> nearbySiliconites = slvl.getEntitiesOfClass(
                BaseSiliconite.class,
                new AABB(this.blockPosition()).inflate(32),
                e -> e != this && e.getTarget() == null && !(e instanceof BaseOrganelle)
        );

        if (tracker.getType() == RadarTracker.TrackerType.ENTITY) {
            // Send attackers to target entity
            Entity targetEntity = slvl.getEntity(tracker.getEntityUUID());
            if (targetEntity instanceof LivingEntity livingTarget) {
                for (BaseSiliconite siliconite : nearbySiliconites) {
                    siliconite.setTarget(livingTarget);
                    siliconite.setLastHurtByMob(livingTarget);

                    // Visual effect
                    slvl.sendParticles(
                            ParticleTypes.ENCHANT,
                            this.getX(), this.getY() + 1, this.getZ(),
                            1,
                            siliconite.getX() - this.getX(),
                            siliconite.getY() - this.getY(),
                            siliconite.getZ() - this.getZ(),
                            0.1
                    );
                }
            }
        } else {
            // Send attackers to block position
            BlockPos targetPos = tracker.getPosition();
            for (BaseSiliconite siliconite : nearbySiliconites) {
                // Create a pathfinding goal to the position
                siliconite.getNavigation().moveTo(
                        targetPos.getX() + 0.5,
                        targetPos.getY(),
                        targetPos.getZ() + 0.5,
                        1.0
                );

                // Visual effect
                slvl.sendParticles(
                        ParticleTypes.ENCHANT,
                        this.getX(), this.getY() + 1, this.getZ(),
                        1,
                        targetPos.getX() + 0.5 - this.getX(),
                        targetPos.getY() - this.getY(),
                        targetPos.getZ() + 0.5 - this.getZ(),
                        0.1
                );
            }
        }

        // Play a sound when alerting others
        slvl.playSound(
                null,
                this.blockPosition(),
                ModSounds.AMAL_RADAR_SCREAM.get(),
                SoundSource.HOSTILE,
                2.0f,
                1.2f
        );
    }

    // VibrationSystem implementation
    @Override
    public Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public User getVibrationUser() {
        return this.vibrationUser;
    }

    // Custom VibrationSystem.User for the radar
    private static class RadarVibrationUser implements VibrationSystem.User {
        private final AmalRadar radar;
        private final float range;

        public RadarVibrationUser(AmalRadar radar, float range) {
            this.radar = radar;
            this.range = range;
        }

        @Override
        public int getListenerRadius() {
            return (int) range;
        }

        @Override
        public PositionSource getPositionSource() {
            return new EntityPositionSource(this.radar, this.radar.getEyeHeight());
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            // Listen to important game events
            return GameEventTags.WARDEN_CAN_LISTEN;
        }

        @Override
        public boolean canTriggerAvoidVibration() {
            return true;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, GameEvent gameEvent, GameEvent.Context context) {
            // Only receive vibrations within range
            return this.radar.position().closerThan(Vec3.atCenterOf(pos), this.range);
        }

        @Override
        public void onReceiveVibration(ServerLevel serverLevel, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity, @Nullable Entity entity1, float v) {
            // Create vibration info and pass to radar
            VibrationInfo vibrationInfo = new VibrationInfo(
                    gameEvent,
                    v,
                    Vec3.atCenterOf(blockPos),
                    entity1
            );

            this.radar.onVibrationDetected(vibrationInfo);
        }

        @Override
        public void onDataChanged() {
            // Handle when vibration data changes
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_ALERTED, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(IS_ALERTED, tag.getBoolean("is_alerted"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("is_alerted", entityData.get(IS_ALERTED));
    }

    public class EarLookControl extends LookControl {

        public EarLookControl(Mob pMob) {
            super(pMob);
        }

        @Override
        protected void clampHeadRotationToBody() {
            //do not
        }
    }
}
