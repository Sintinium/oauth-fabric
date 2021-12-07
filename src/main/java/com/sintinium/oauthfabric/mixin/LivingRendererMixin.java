package com.sintinium.oauthfabric.mixin;

import com.sintinium.oauthfabric.gui.profile.FakePlayer;
import com.sintinium.oauthfabric.gui.profile.ProfileSelectionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    protected LivingRendererMixin(EntityRendererFactory.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void shouldShowNameMixin(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof FakePlayer || MinecraftClient.getInstance().currentScreen instanceof ProfileSelectionScreen)
            cir.setReturnValue(false);
    }
}
