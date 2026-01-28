package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar;

import com.mateussdev.chemosyntehsis.Core.ModSounds;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.*;
import java.util.function.BiConsumer;

public class AmalRadar extends BaseAmalgamation {

    public static final EntityDataAccessor<Boolean> IS_ALERTED = SynchedEntityData.defineId(AmalRadar.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<CompoundTag> SONAR_DATA = SynchedEntityData.defineId(AmalRadar.class, EntityDataSerializers.COMPOUND_TAG);

    // Alertion
    private int alertT = 0;

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
        sonarEventScores.put(GameEvent.CONTAINER_OPEN, 5);
        sonarEventScores.put(GameEvent.CONTAINER_CLOSE, 5);
        sonarEventScores.put(GameEvent.HIT_GROUND, 5);
        sonarEventScores.put(GameEvent.STEP, 3);
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
            score = Math.max(0, Math.min(200, score + delta));
        }

        public void tick() {
            age++;
            // Decay faster as time goes on
            float decayRate = 0.005f;
            score = Math.max(0, score - decayRate);
        }

        public BlockPos getPosition() {
            return position;
        }

        public int getAge() {
            return age;
        }

        public CompoundTag convertToTag() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("age", age);
            tag.putInt("pos_x", position.getX());
            tag.putInt("pos_y", position.getY());
            tag.putInt("pos_z", position.getZ());
            tag.putFloat("score", score);
            return tag;
        }
    }


    public void onHeardGameEvent(GameEvent gameEvent, GameEvent.Context context, Vec3 eventPos) {
        //Doodelidoo add score depending on where the event happened and what type it is
        if (context.sourceEntity() != null) {
            if (context.sourceEntity() instanceof LivingEntity LE) {
                if (LE instanceof BaseSiliconite) return;

                BlockPos pos = BlockPos.containing(eventPos);
                int eventScore = 2;
                if(sonarEventScores.get(gameEvent) != null) {
                    eventScore = sonarEventScores.get(gameEvent);
                }

                int pointsToAdd = Math.min(200, eventScore * Mth.ceil((48 - distanceTo(LE)) / 4));
                pingLocation(pos, pointsToAdd);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level() instanceof ServerLevel slvl) {

            if(entityData.get(IS_ALERTED) && alertT>0) {
                alertT--;
                if(alertT<=0) {
                    entityData.set(IS_ALERTED, false);
                }
            }

            //Update trackers
            Iterator<Map.Entry<BlockPos, RadarTracker>> positionIterator = sonarPingData.entrySet().iterator();
            while (positionIterator.hasNext()) {
                Map.Entry<BlockPos, RadarTracker> entry = positionIterator.next();
                RadarTracker tracker = entry.getValue();

                if(!entityData.get(IS_ALERTED)) {
                    if(tickCount % 5 == 0) {
                        tracker.tick();
                    }

                    if (tracker.score <= 0) {
                        positionIterator.remove();
                    } else if (tracker.score >= 200) {
                        //TODO : ALERT TO LOCATION
                        sendPatrol(tracker.position);
                        scream(tracker.position);
                        positionIterator.remove(); // Remove after alerting
                    }
                }
            }

            //Sync to client
            // Sync data to client
            if (this.tickCount % 5 == 0) { // Sync every 5 ticks (4 times per second)
                syncPingDataToClient();
//                StaticSiliconiteMethods.debugLog(sonarPingData.size()+" <- Server. Client -> "+getClientPingData().size());
            }
        }
    }

    private void syncPingDataToClient() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (RadarTracker tracker : sonarPingData.values()) {
            CompoundTag trackerTag = tracker.convertToTag();
            list.add(trackerTag);
        }

        tag.put("pingData", list);
        entityData.set(SONAR_DATA, tag);
    }

    public Map<BlockPos, RadarTracker> getClientPingData() {
        Map<BlockPos, RadarTracker> clientData = new HashMap<>();
        CompoundTag tag = entityData.get(SONAR_DATA);

        if (tag.contains("pingData")) {
            ListTag list = tag.getList("pingData", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag trackerTag = list.getCompound(i);
                BlockPos pos = new BlockPos(trackerTag.getInt("pos_x"), trackerTag.getInt("pos_y"), trackerTag.getInt("pos_z"));
                float score = trackerTag.getFloat("score");
                int age = trackerTag.getInt("age");

                RadarTracker tracker = new RadarTracker(pos);
                tracker.score = score;
                tracker.age = age;
                clientData.put(pos, tracker);
            }
        }

        return clientData;
    }

    public void pingLocation(BlockPos pos, int points) {
        if (sonarPingData.containsKey(pos)) {
            sonarPingData.get(pos).changeScore(points);
        } else {
            sonarPingData.put(pos.immutable(), new RadarTracker(pos.immutable()));
        }
    }

    public void sendPatrol(BlockPos pos) {
        AABB boundingBox = new AABB(blockPosition()).inflate(32, 12, 32);

        if(level() instanceof ServerLevel slvl) {
            List<BaseSiliconite> all = slvl.getEntitiesOfClass(
                    BaseSiliconite.class,
                    boundingBox,
                    c -> !(c instanceof BaseOrganelle) && c.getTarget() == null
            );


            StaticSiliconiteMethods.debugLog("Alerted "+all.size()+" siliconites to location");
            List<LivingEntity> potentialTargets = slvl.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1f), StaticSiliconiteMethods::shouldAttackMob);

            LivingEntity potentialTarget = null;

            if(!potentialTargets.isEmpty()) {
                potentialTarget = potentialTargets.get(0);
                StaticSiliconiteMethods.debugLog("Targeting mob: "+potentialTarget.getType().toString());
            }

            for(BaseSiliconite siliconite : all) {
                siliconite.getNavigation().stop();
                siliconite.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0f);
                if(potentialTarget != null && !(potentialTarget instanceof BaseSiliconite)) {
                    siliconite.setTarget(potentialTarget);
                    siliconite.setLastHurtByMob(potentialTarget);
                }
            }

        }
    }

    private void scream(BlockPos pos) {
        entityData.set(IS_ALERTED, true);
        alertT = 221;
        playSound(ModSounds.AMAL_RADAR_SCREAM.get(), 3f, 1f+(random.nextFloat()*2-1)/5);

//        AABB boundingBox = new AABB(blockPosition()).inflate(5);
//
//        if(level() instanceof ServerLevel slvl) {
//            List<AmalRadar> radars = slvl.getEntitiesOfClass(
//                    AmalRadar.class,
//                    boundingBox,
//                    e -> e.alertT <= 0
//            );
//
//            for(AmalRadar radar : radars) {
//                radar.sendPatrol(pos);
//                radar.scream(pos);
//            }
//        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_ALERTED, false);
        entityData.define(SONAR_DATA, new CompoundTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(IS_ALERTED, tag.getBoolean("is_alerted"));

        if (tag.contains("pingData")) {
            ListTag list = tag.getList("pingData", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag trackerTag = list.getCompound(i);
                BlockPos pos = new BlockPos(trackerTag.getInt("pos_x"), trackerTag.getInt("pos_y"), trackerTag.getInt("pos_z"));
                float score = trackerTag.getFloat("score");
                int age = trackerTag.getInt("age");

                RadarTracker tracker = new RadarTracker(pos);
                tracker.score = score;
                tracker.age = age;
                sonarPingData.put(pos, tracker);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("is_alerted", entityData.get(IS_ALERTED));

        ListTag list = new ListTag();

        for (Map.Entry<BlockPos, RadarTracker> entry : sonarPingData.entrySet()) {
            CompoundTag trackerTag = entry.getValue().convertToTag();
            list.add(trackerTag);
        }

        tag.put("pingData", list);
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
        private final int LISTENER_RADIUS = 72;

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
//            StaticSiliconiteMethods.debugLog("Hi hello hi there, " + gameEvent.getName());
            onHeardGameEvent(gameEvent, context, vec3);
            return false;
        }
    }
}
