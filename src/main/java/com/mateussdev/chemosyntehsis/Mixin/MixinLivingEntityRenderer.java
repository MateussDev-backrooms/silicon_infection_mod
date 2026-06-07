package com.mateussdev.chemosyntehsis.Mixin;

import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.TetheredOverlayLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(
            net.minecraft.client.renderer.entity.EntityRendererProvider.Context ctx,
            EntityModel<T> model,
            float shadowRadius,
            CallbackInfo ci
    ) {
        @SuppressWarnings("unchecked")
        LivingEntityRenderer<T, M> self = (LivingEntityRenderer<T, M>) (Object) this;
        self.addLayer(new TetheredOverlayLayer<>(self));
    }
}
