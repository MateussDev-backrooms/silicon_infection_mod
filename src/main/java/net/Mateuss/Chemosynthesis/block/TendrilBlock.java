package net.Mateuss.Chemosynthesis.block;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.VegetativeBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class TendrilBlock extends MultifaceBlock {
    public final MultifaceSpreader spreader;

    private static final Map<Entity, Integer> tethering = new WeakHashMap<>();

    public TendrilBlock(Properties pProperties) {
        super(pProperties);
        this.spreader = new MultifaceSpreader(this);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if(pRandom.nextFloat() < 0.4f) {
            if(nearAVegetativeForm(pLevel, pPos)) {
                this.spreader.spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
            } else {
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    private boolean nearAVegetativeForm(ServerLevel lvl, BlockPos pos) {
        List<Entity> nearbyList = lvl.getEntities(
                (Entity) null, new AABB(pos).inflate(4),
                entity -> entity instanceof VegetativeBase
        );
        for (Entity entity : nearbyList) {
            if(Mth.sqrt((float) entity.distanceToSqr(new Vec3(pos.getX(),pos.getY(),pos.getZ()))) <= 4) {
                return true;
            }
        }

        return false;
    }

    protected boolean isSiliconite(Entity entity) {

        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        // Check if the entity is not part of your mod
        return entityTypeKey == null || entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

}
