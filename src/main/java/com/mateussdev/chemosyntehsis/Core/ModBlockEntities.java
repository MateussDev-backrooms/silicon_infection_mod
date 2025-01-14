package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.BlockEntities.vein_block.BEVeinBlock;
import com.mateussdev.chemosyntehsis.Blocks.VeinBlock;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Chemosynthesis.MODID);

    public static final RegistryObject<BlockEntityType<BEVeinBlock>> VEIN_BLOCK =
            BLOCK_ENTITIES.register("vein_block", () ->
                    BlockEntityType.Builder.of(
                            BEVeinBlock::new,
                            ModBlocks.VEIN_BLOCK.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
