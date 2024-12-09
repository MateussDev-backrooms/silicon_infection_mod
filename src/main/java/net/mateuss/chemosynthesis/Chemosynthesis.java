package net.mateuss.chemosynthesis;

import com.mojang.logging.LogUtils;
import net.mateuss.chemosynthesis.block.ModBlocks;
import net.mateuss.chemosynthesis.entity.ModEntities;
import net.mateuss.chemosynthesis.entity.client.*;
import net.mateuss.chemosynthesis.item.ModCreativeModeTabs;
import net.mateuss.chemosynthesis.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Chemosynthesis.MODID)
public class Chemosynthesis
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "silicon_mod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Chemosynthesis(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            EntityRenderers.register(ModEntities.SILICON_ROLLER.get(), RendererSiliconRoller::new);
            EntityRenderers.register(ModEntities.SILICON_TRIPOD.get(), RendererSiliconTripod::new);
            EntityRenderers.register(ModEntities.SILIPEDE.get(), RendererSilipede::new);
            EntityRenderers.register(ModEntities.TETH_ZOMBIE.get(), RendererTethZombie::new);
            EntityRenderers.register(ModEntities.TETH_COW.get(), RendererTethCow::new);
            EntityRenderers.register(ModEntities.TETH_SHEEP.get(), RendererTethSheep::new);
            EntityRenderers.register(ModEntities.TETH_PIG.get(), RendererTethPig::new);
            EntityRenderers.register(ModEntities.HOMUNCULUS_HEART.get(), RendererHomunculus::new);
            EntityRenderers.register(ModEntities.BRACHATIC_STAGE.get(), RendererBrachaticStage::new);
            EntityRenderers.register(ModEntities.BRACHATIC_HARPOON.get(), context -> new RendererBrachaticHarpoon(context, ModModelLayers.BRACHATIC_HARPOON_LAYER));
        }
    }
}
