package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.BlockEntities.vein_block.BEVeinBlock_Renderer;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlockEntities;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity_Render;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh_Renderer;
import com.mateussdev.chemosyntehsis.Entities.hybt1_erythrocyte.HybridErythrocyte_Renderer;
import com.mateussdev.chemosyntehsis.Entities.hybt1_thrombocyte.HybridThrombocyte_Renderer;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller_Renderer;
import com.mateussdev.chemosyntehsis.Entities.teth_cow.TethCow_Renderer;
import com.mateussdev.chemosyntehsis.Entities.teth_skeleton.TethSkeleton_Renderer;
import com.mateussdev.chemosyntehsis.Entities.teth_zombie.TethZombie_Renderer;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb_Renderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventRegisterRenderer {
    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        //Pure
        EntityRenderers.register(ModEntities.SILICON_ROLLER.get(), SiliconRoller_Renderer::new);
        EntityRenderers.register(ModEntities.CHUNK_OF_FLESH.get(), ChunkOfFlesh_Renderer::new);

        //Tethered mobs
        EntityRenderers.register(ModEntities.TETH_ZOMBIE.get(), TethZombie_Renderer::new);
        EntityRenderers.register(ModEntities.TETH_SKELETON.get(), TethSkeleton_Renderer::new);
        EntityRenderers.register(ModEntities.TETH_COW.get(), TethCow_Renderer::new);

        //Vegetated mobs
        EntityRenderers.register(ModEntities.VEG_BULB.get(), VegetativeBulb_Renderer::new);

        //Hybrids

        //Tier1
        EntityRenderers.register(ModEntities.THROMBOCYTE.get(), HybridThrombocyte_Renderer::new);
        EntityRenderers.register(ModEntities.ERYTHROCYTE.get(), HybridErythrocyte_Renderer::new);


        //Projectile
        EntityRenderers.register(ModEntities.BULB_PROJECTILE.get(), BulbProjectileEntity_Render::new);

        //Block entities
        BlockEntityRenderers.register(ModBlockEntities.VEIN_BLOCK.get(), BEVeinBlock_Renderer::new);
    }
}
