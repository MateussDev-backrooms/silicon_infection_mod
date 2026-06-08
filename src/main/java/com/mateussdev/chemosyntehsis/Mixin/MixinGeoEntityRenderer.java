package com.mateussdev.chemosyntehsis.Mixin;

import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.TetheredGeoOverlayLayer;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

@Mixin(value = GeoEntityRenderer.class, remap = false)
public class MixinGeoEntityRenderer<T extends Entity & GeoAnimatable> {
    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/world/entity/EntityType;)V", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, EntityType<? extends T> entityType, CallbackInfo ci) {
        @SuppressWarnings("unchecked")
        GeoEntityRenderer<T> self = (GeoEntityRenderer<T>) (Object) this;
        self.addRenderLayer(new TetheredGeoOverlayLayer<>(self));
        StaticSiliconiteMethods.debugLog("oruwghw");
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lsoftware/bernie/geckolib/model/GeoModel;)V", at = @At("TAIL"))
    private void onInit2(EntityRendererProvider.Context renderManager, GeoModel<T> model, CallbackInfo ci) {
        @SuppressWarnings("unchecked")
        GeoEntityRenderer<T> self = (GeoEntityRenderer<T>) (Object) this;
        self.addRenderLayer(new TetheredGeoOverlayLayer<>(self));
        StaticSiliconiteMethods.debugLog("oruwghw");
    }
}
