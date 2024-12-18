package net.Mateuss.Chemosynthesis.block.blockentity;

import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.HomunculoidBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.UUID;

public class BEVaucoleRootEntity extends BlockEntity {

    private int spawn_progress = 0;
    private int has_already_spawned = 0; //0 for false and 1 for true
    private UUID spawned_organelle_uuid = null;


    public BEVaucoleRootEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.VAUCOLE_ROOT_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("organelle_root.spawn_progress", spawn_progress);
        pTag.putInt("organelle_root.has_spawned", has_already_spawned);
        if(spawned_organelle_uuid != null) {
            pTag.putUUID("organelle_uuid", spawned_organelle_uuid);
        }

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        spawn_progress = pTag.getInt("organelle_root.spawn_progress");
        has_already_spawned = pTag.getInt("organelle_root.has_spawned");
        if(pTag.hasUUID("organelle_uuid")) {
            spawned_organelle_uuid = pTag.getUUID("organelle_uuid");
        }
        super.load(pTag);
    }

    public void onBlockFilled(Level level, BlockPos pos, BlockState state, int bloodLevel) {
        if(level instanceof ServerLevel slvl) {
            if(organelle_alive(slvl)) {
                getSpawnedOrganelle(slvl).ifPresent(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.heal(1);
                    }
                });
            } else {
                spawn_progress++;
                if(spawn_progress >= 10) {
                    HomunculoidBase organelle = getOrganelleSpawn(slvl);
                    if(organelle != null) {
                        organelle.moveTo(worldPosition.getX() + 0.5f, worldPosition.getY()+1f, worldPosition.getZ()+0.5f);
                        slvl.addFreshEntity(organelle);
                        spawned_organelle_uuid = organelle.getUUID();
                        setChanged();
                        spawn_progress = 0;
                    }
                }
            }
        }
    }

    private boolean organelle_alive(ServerLevel lvl) {
        return getSpawnedOrganelle(lvl).isPresent();
    }

    private Optional<Entity> getSpawnedOrganelle(ServerLevel lvl) {
        if(spawned_organelle_uuid == null) return Optional.empty();
        return Optional.ofNullable(lvl.getEntity(spawned_organelle_uuid));
    }

    //override organelle here
    protected HomunculoidBase getOrganelleSpawn(Level lvl) {
        return ModEntities.HOMUNCULUS_VAUCOLE.get().create(lvl);
    }
}
