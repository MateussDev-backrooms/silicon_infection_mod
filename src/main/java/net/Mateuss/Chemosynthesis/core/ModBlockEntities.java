package net.Mateuss.Chemosynthesis.core;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.block.blockentity.BEOrganelleConnector;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Chemosynthesis.MODID);

    public static final RegistryObject<BlockEntityType<BEOrganelleConnector>> ORGANELLE_CONNECTOR =
            BLOCK_ENTITIES.register("organelle_connector_be", () ->
                    BlockEntityType.Builder.of(
                            BEOrganelleConnector::new,
                            ModBlocks.HOORGANELLE_CONNECTOR.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
