package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.BlockEntities.vein_block.BEVeinBlock_Renderer;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlockEntities;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity_Render;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh_Renderer;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte.HybridAstrocyte_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte.HybridErythrocyte_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte.HybridThrombocyte_Renderer;
import com.mateussdev.chemosyntehsis.Entities.met_zombie.MetZombie_Renderer;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow.TethCow_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton.TethSkeleton_Renderer;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie.TethZombie_Renderer;
import com.mateussdev.chemosyntehsis.Entities.vasc_roller.VascularRoller_Renderer;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb_Renderer;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller_Renderer;
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
        EntityRenderers.register(ModEntities.CLUSTER_OF_FLESH.get(), ClusterOfFlesh_Renderer::new);

        //Tethered mobs
        EntityRenderers.register(ModEntities.TETH_ZOMBIE.get(), TethZombie_Renderer::new);
        EntityRenderers.register(ModEntities.TETH_SKELETON.get(), TethSkeleton_Renderer::new);
        EntityRenderers.register(ModEntities.TETH_COW.get(), TethCow_Renderer::new);

        //Metabolized mobs
        EntityRenderers.register(ModEntities.MET_ZOMBIE.get(), MetZombie_Renderer::new);


        //Vegetated mobs
        EntityRenderers.register(ModEntities.VEG_BULB.get(), VegetativeBulb_Renderer::new);
        EntityRenderers.register(ModEntities.VEG_ROLLER.get(), VegetativeRoller_Renderer::new);
        EntityRenderers.register(ModEntities.VASC_ROLLER.get(), VascularRoller_Renderer::new);

        //Hybrids

        //Tier1
        EntityRenderers.register(ModEntities.THROMBOCYTE.get(), HybridThrombocyte_Renderer::new);
        EntityRenderers.register(ModEntities.ERYTHROCYTE.get(), HybridErythrocyte_Renderer::new);
        EntityRenderers.register(ModEntities.ASTROCYTE.get(), HybridAstrocyte_Renderer::new);


        //Projectile
        EntityRenderers.register(ModEntities.BULB_PROJECTILE.get(), BulbProjectileEntity_Render::new);

        //GibEntities
        EntityRenderers.register(ModEntities.GIB_FLESH.get(), GibFlesh_Renderer::new);

        //Block entities
        BlockEntityRenderers.register(ModBlockEntities.VEIN_BLOCK.get(), BEVeinBlock_Renderer::new);
    }
}
