package com.mateussdev.chemosyntehsis.Entities.Projectiles;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BulbProjectileEntity_Render extends GeoEntityRenderer<BulbProjectileEntity> {
    public BulbProjectileEntity_Render(EntityRendererProvider.Context context) {
        super(context, new BulbProjectileEntity_Model());
    }


}
