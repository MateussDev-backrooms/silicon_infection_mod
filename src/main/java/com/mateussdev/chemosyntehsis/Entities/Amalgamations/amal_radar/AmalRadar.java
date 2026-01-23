package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.*;
import java.util.function.BiConsumer;

public class AmalRadar extends BaseAmalgamation {

    public static final EntityDataAccessor<Boolean> IS_ALERTED = SynchedEntityData.defineId(AmalRadar.class, EntityDataSerializers.BOOLEAN);

    // Vibration system components

    // Radar tracking
    private final DynamicGameEventListener<EarSonarListener> dynamicSonarListener;
    public Map<BlockPos, RadarTracker> sonarPingData = new HashMap<>();
    private static final int MAX_TRACKERS = 10;
    private final EntityPositionSource positionSource;

    public static final Map<GameEvent, Integer> sonarEventScores = new HashMap<>();

    static {
        sonarEventScores.put(GameEvent.EXPLODE, 80);
        sonarEventScores.put(GameEvent.BLOCK_DESTROY, 15);
        sonarEventScores.put(GameEvent.BLOCK_PLACE, 15);
        sonarEventScores.put(GameEvent.CONTAINER_OPEN, 10);
        sonarEventScores.put(GameEvent.CONTAINER_CLOSE, 10);
        sonarEventScores.put(GameEvent.HIT_GROUND, 2);
        sonarEventScores.put(GameEvent.STEP, 1);
    }


    public AmalRadar(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.lookControl = new EarLookControl(this);
        this.positionSource = new EntityPositionSource(this, 0f);

        this.dynamicSonarListener = new DynamicGameEventListener(new EarSonarListener(positionSource));
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
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> pListenerConsumer) {
        if (level() instanceof ServerLevel slvl) {
            pListenerConsumer.accept(this.dynamicSonarListener, slvl);
        }
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

    public static class RadarTracker {
        private BlockPos position;
        public float score;
        private int age;

        public RadarTracker(BlockPos position) {
            this.position = position.immutable();
            this.score = 1f;
            this.age = 0;
        }

        public void changeScore(int delta) {
            score = Math.max(0, Math.min(100, score + delta));
        }

        public void tick() {
            age++;
            // Decay faster as time goes on
            float decayRate = age < 100 ? 0.05f : 0.1f;
            score = Math.max(0, score - decayRate);
        }

        public BlockPos getPosition() {
            return position;
        }

        public int getAge() {
            return age;
        }
    }


    public void onHeardGameEvent(GameEvent gameEvent, GameEvent.Context context, Vec3 eventPos) {
        //Doodelidoo add score depending on where the event happened and what type it is
        if (context.sourceEntity() != null) {
            if (context.sourceEntity() instanceof LivingEntity LE) {
                if (LE instanceof BaseSiliconite) return;

                BlockPos pos = BlockPos.containing(eventPos);
                int pointsToAdd = Math.min(100, sonarEventScores.get(gameEvent) * Mth.ceil((48 - distanceTo(LE)) / 4));
                pingLocation(pos, pointsToAdd);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level() instanceof ServerLevel slvl) {
            Iterator<Map.Entry<BlockPos, RadarTracker>> positionIterator = sonarPingData.entrySet().iterator();
            while (positionIterator.hasNext()) {
                Map.Entry<BlockPos, RadarTracker> entry = positionIterator.next();
                RadarTracker tracker = entry.getValue();

                tracker.tick();
                slvl.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                        tracker.position.getX(),
                        tracker.position.getY(),
                        tracker.position.getZ(),
                        Mth.ceil(tracker.score),
                        0.5,
                        0.5,
                        0.5,
                        0.2);

                if (tracker.score <= 0) {
                    positionIterator.remove();
                } else if (tracker.score >= 100) {
                    //TODO : ALERT TO LOCATION
                    positionIterator.remove(); // Remove after alerting
                }
            }
        }
    }

    public void pingLocation(BlockPos pos, int points) {
        if (sonarPingData.containsKey(pos)) {
            sonarPingData.get(pos).changeScore(points);
        } else {
            sonarPingData.put(pos.immutable(), new RadarTracker(pos.immutable()));
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

    public class EarSonarListener implements GameEventListener {

        private final PositionSource listenerSource;
        private final int LISTENER_RADIUS = 48;

        public EarSonarListener(PositionSource listenerSource) {
            this.listenerSource = listenerSource;
        }

        @Override
        public PositionSource getListenerSource() {
            return listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return LISTENER_RADIUS;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3) {
            StaticSiliconiteMethods.debugLog("Hi hello hi there, " + gameEvent.getName());
            onHeardGameEvent(gameEvent, context, vec3);
            return false;
        }
    }
}
