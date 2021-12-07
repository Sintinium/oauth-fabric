package com.sintinium.oauthfabric.mixin;

import com.sintinium.oauthfabric.gui.profile.FakePlayer;
import com.sintinium.oauthfabric.util.PlayerRenderers;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRendererManagerMixin {

    // Replace the player renderer with our own. This is to stop other mods from attempting to alter the player before it exists.
    // The main fix is for the hats mod
    @Inject(method = "getRenderer", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void onGetRenderer(T pEntity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
        if (!(pEntity instanceof FakePlayer)) return;
        String s = ((AbstractClientPlayerEntity) pEntity).getModel();
        PlayerEntityRenderer playerrenderer = null;
        if (s.equals("default")) playerrenderer = PlayerRenderers.fakePlayerRenderer;
        else if (s.equals("slim")) playerrenderer = PlayerRenderers.fakePlayerRendererSlim;
        cir.setReturnValue((EntityRenderer<? super T>) playerrenderer);
    }
}
