package com.sintinium.oauthfabric.mixin;

import com.sintinium.oauthfabric.util.PlayerRenderers;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderers.class)
public class EntityRendererMixin {

    @Inject(method = "reloadPlayerRenderers", at = @At("HEAD"))
    private static void onCreatePlayerRenderers(EntityRendererFactory.Context p_174052_, CallbackInfoReturnable<Map<String, EntityRenderer<? extends PlayerEntity>>> cir) {
        PlayerRenderers.createPlayerRenderers(p_174052_);
    }

}
