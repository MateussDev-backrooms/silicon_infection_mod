package net.Mateuss.Chemosynthesis.block.homulculus;

import net.Mateuss.Chemosynthesis.block.IBloodFillable;
import net.Mateuss.Chemosynthesis.block.blockentity.BEVaucoleRootEntity;
import net.Mateuss.Chemosynthesis.block.blockentity.BaseOrganelleRootEntity;
import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.HomunculoidBase;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

public class BaseOrganelleRoot extends BaseEntityBlock implements IBloodFillable {
    public static final BooleanProperty HAS_SPAWNED = BooleanProperty.create("organelle_has_spawned");
    public static final IntegerProperty SPAWN_PROGRESS = IntegerProperty.create("organelle_spawn_progress", 0, 10);
    protected HomunculoidBase organelle = null;

    public BaseOrganelleRoot(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(HAS_SPAWNED, false)
                .setValue(SPAWN_PROGRESS, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder
                .add(HAS_SPAWNED, SPAWN_PROGRESS);
    }





    @Override
    public void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel) {

    }

    @Override
    public boolean canReceiveBlood(ServerLevel level, BlockPos pos) {
        return true;
    }

    /*ENSURE to set organelle to a value*/
    public void spawnOrganelle(ServerLevel level, BlockPos pos) {
        //override the organelle spawning here
    }



    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BEVaucoleRootEntity(blockPos, blockState);
    }
}
