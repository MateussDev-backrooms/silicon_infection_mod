package net.Mateuss.Chemosynthesis.block.advanced;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.VegetativeBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
        if(nearAVegetativeForm(pLevel, pPos)) {
            this.spreader.spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
        } else {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
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

        return !nearbyList.isEmpty();
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if(!isSiliconite(pEntity)) {
            List<Entity> nearbyList = pLevel.getEntities(
                    (Entity) null, new AABB(pPos).inflate(4),
                    entity -> entity instanceof VegetativeBase
            );
            if(!nearbyList.isEmpty()) {
                Vec3 mov = nearbyList.get(0).position().subtract(pEntity.position());
                pEntity.addDeltaMovement(mov.scale(0.033d));
            }
        }
    }
    protected boolean isSiliconite(Entity entity) {

        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        // Check if the entity is not part of your mod
        return entityTypeKey == null || entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

}
