package com.mateussdev.chemosyntehsis.Entities.util;

import com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar.AmalRadar;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidType;

import java.util.*;

public class BaseGraphNodeEntity extends Entity {

    private static final EntityDataAccessor<CompoundTag> CONNECTIONS = SynchedEntityData.defineId(BaseGraphNodeEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<CompoundTag> CONNECTION_POSITIONS = SynchedEntityData.defineId(BaseGraphNodeEntity.class, EntityDataSerializers.COMPOUND_TAG);



    private List<UUID> connectionList = new ArrayList<>();

    public BaseGraphNodeEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setInvulnerable(true);
        this.noPhysics = true;
        this.noCulling = true;
    }

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            List<BaseGraphNodeEntity> nearbyNodes = slvl.getEntitiesOfClass(
                    BaseGraphNodeEntity.class,
                    new AABB(blockPosition()).inflate(getConnectionRadius()),
                    e -> e.getUUID() != this.getUUID()
            );

            this.moveTo(this.blockPosition().getCenter());

            for(BaseGraphNodeEntity node : nearbyNodes) {
                if(!node.hasConnection(this.getUUID())) {
                    this.connectTo(node);
                }
            }

            //Remove invalid connections
            Iterator<UUID> connectionIterator = connectionList.iterator();
            while (connectionIterator.hasNext()) {
                UUID entry = connectionIterator.next();
                if(slvl.getEntity(entry) == null) {
                    connectionIterator.remove();
                }
            }

            if(tickCount % 5 == 0) {
                syncConnectionDataToClient();
            }
        }

    }

    public void connectTo(BaseGraphNodeEntity other) {
        if(!hasConnection(other.getUUID())) {
            this.connectionList.add(other.getUUID());
            other.connectTo(this);
        }
    }

    public boolean hasConnection(UUID other) {
        return this.connectionList.contains(other);
    }

    public void removeConnection(BaseGraphNodeEntity other) {
        UUID otherUUID = other.getUUID();

        Iterator<UUID> connectionIterator = connectionList.iterator();
        while (connectionIterator.hasNext()) {
            UUID entry = connectionIterator.next();
            if(entry == otherUUID) {
                connectionIterator.remove();
            }
        }

        other.removeConnection(this);
    }

    private void syncConnectionDataToClient() {
        if(level() instanceof ServerLevel slvl) {
            CompoundTag tag = new CompoundTag();
            CompoundTag tag2 = new CompoundTag();
            ListTag list = new ListTag();
            ListTag list2 = new ListTag();

            for (UUID connectionID : connectionList) {
                CompoundTag connectionTag = new CompoundTag();
                connectionTag.putUUID("connection_uuid", connectionID);
                list.add(connectionTag);

                CompoundTag positionTag = new CompoundTag();
                positionTag.putLong("connection_position", slvl.getEntity(connectionID).blockPosition().asLong());
                list2.add(positionTag);
            }

            tag.put("connections", list);
            tag2.put("connection_positions", list2);
            entityData.set(CONNECTIONS, tag);
            entityData.set(CONNECTION_POSITIONS, tag2);
        }
    }

    public List<UUID> getClientConnections() {
        List<UUID> curr = new ArrayList<>();
        CompoundTag tag = entityData.get(CONNECTIONS);

        if(tag.contains("connections")) {
            ListTag list = tag.getList("connections", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag trackerTag = list.getCompound(i);
                curr.add(trackerTag.getUUID("connection_uuid"));
            }
        }

        return curr;
    }

    public List<BlockPos> getClientConnectionPositions() {
        List<BlockPos> curr = new ArrayList<>();
        CompoundTag tag = entityData.get(CONNECTION_POSITIONS);

        if(tag.contains("connection_positions")) {
            ListTag list = tag.getList("connection_positions", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag trackerTag = list.getCompound(i);
                curr.add(BlockPos.of(trackerTag.getLong("connection_position")).immutable());
            }
        }


        return curr;
    }

    // ===== MODIFIERS ===== //
    protected int getConnectionRadius() {
        return 8; // Default connects to other that are 8 blocks away
    }

    // ===== OVERRIDES ===== //
    @Override
    public boolean isInWall() {
        return false; // Fixes black tint
    }

    @Override
    public boolean displayFireAnimation() {
        return false; // Prevents fire overlay
    }

    @Override
    public boolean isAttackable() {
        return false; // Can't be attacked
    }

    @Override
    public boolean isPickable() {
        return false; // Can't be interacted with
    }

    @Override
    public boolean canBeCollidedWith() {
        return false; // No collision
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false; // Projectiles pass through
    }

    @Override
    public boolean isPushable() {
        return false; // Can't be pushed
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        BlockPos pos = this.blockPosition();
        return this.level().getMaxLocalRawBrightness(pos);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(CONNECTIONS, new CompoundTag());
        entityData.define(CONNECTION_POSITIONS, new CompoundTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if(compoundTag.contains("connections")) {
            ListTag list = compoundTag.getList("connections", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag trackerTag = list.getCompound(i);
                connectionList.add(trackerTag.getUUID("connection_uuid"));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if(level() instanceof ServerLevel slvl) {
            ListTag list = new ListTag();
            ListTag list2 = new ListTag();

            for (UUID connectionID : connectionList) {
                CompoundTag connectionTag = new CompoundTag();
                connectionTag.putUUID("connection_uuid", connectionID);
                list.add(connectionTag);

                CompoundTag positionTag = new CompoundTag();
                positionTag.putLong("connection_position", slvl.getEntity(connectionID).blockPosition().asLong());
                list2.add(positionTag);
            }

            compoundTag.put("connections", list);
            compoundTag.put("connection_positions", list2);
        }
    }
}
