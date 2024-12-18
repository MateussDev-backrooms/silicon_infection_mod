package net.Mateuss.Chemosynthesis.block.blockentity;

import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseOrganelleRootEntity extends BlockEntity {
    protected final ContainerData data;
    private int spawn_progress = 0;
    private int has_already_spawned = 0; //0 for false and 1 for true

    public BaseOrganelleRootEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ORGANELLE_ROOT_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {

            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> BaseOrganelleRootEntity.this.spawn_progress;
                    case 1 -> BaseOrganelleRootEntity.this.has_already_spawned;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i) {
                    case 0 -> BaseOrganelleRootEntity.this.spawn_progress = i1;
                    case 1 -> BaseOrganelleRootEntity.this.has_already_spawned = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("organelle_root.spawn_progress", spawn_progress);
        pTag.putInt("organelle_root.has_spawned", has_already_spawned);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        spawn_progress = pTag.getInt("organelle_root.spawn_progress");
        has_already_spawned = pTag.getInt("organelle_root.has_spawned");
        super.load(pTag);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {

    }
}
