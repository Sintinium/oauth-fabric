package com.sintinium.oauthfabric.mixin;

import com.sintinium.oauthfabric.gui.profile.FakePlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    @Shadow protected abstract void setModelPose(AbstractClientPlayerEntity player);

    public PlayerRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }


    // Bypass forge events to prevent errors with other mods
    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public void onRender(AbstractClientPlayerEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack matrixStack, VertexConsumerProvider pBuffer, int pPackedLight, CallbackInfo ci) {
        if (!(pEntity instanceof FakePlayer)) return;
        ci.cancel();
        this.setModelPose(pEntity);
        super.render(pEntity, pEntityYaw, pPartialTicks, matrixStack, pBuffer, pPackedLight);
    }

}
