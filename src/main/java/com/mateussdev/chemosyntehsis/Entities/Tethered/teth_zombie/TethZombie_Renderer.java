package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight_Layer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethZombie_Renderer extends GeoEntityRenderer<TethZombie> {
    public TethZombie_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethZombie_Model());
    }
}
