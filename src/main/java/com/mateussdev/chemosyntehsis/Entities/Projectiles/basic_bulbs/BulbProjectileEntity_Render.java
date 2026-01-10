package com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BulbProjectileEntity_Render extends GeoEntityRenderer<BulbProjectileEntity> {
    public BulbProjectileEntity_Render(EntityRendererProvider.Context context) {
        super(context, new BulbProjectileEntity_Model());
    }


}
