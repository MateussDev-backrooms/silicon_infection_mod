package com.mateussdev.chemosyntehsis.Entities.Projectiles.web_shot;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WebShotEntity_Render extends GeoEntityRenderer<WebShotEntity> {
    public WebShotEntity_Render(EntityRendererProvider.Context context) {
        super(context, new WebShotEntity_Model());
    }


}
