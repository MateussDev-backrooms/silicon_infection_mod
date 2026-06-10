package com.mateussdev.chemosyntehsis.Mixin;

import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.model.AgeableListModel.class)
public interface AgeableListModelAccessor {
    @Invoker
    Iterable<ModelPart> callBodyParts();

    @Invoker
    Iterable<ModelPart> callHeadParts();
}
