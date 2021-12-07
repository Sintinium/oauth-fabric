package com.sintinium.oauthfabric.gui.profile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FakePlayer extends ClientPlayerEntity {

    private static FakePlayer instance;
    private Identifier skin;
    private Identifier cape = null;
    private String skinModel = "default";
    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public FakePlayer() {
        super(MinecraftClient.getInstance(), FakeWorld.getInstance(), FakeClientPlayNetHandler.getInstance(), null, null, false, false);
        MinecraftClient.getInstance().getSkinProvider().loadSkin(getGameProfile(), (type, resourceLocation, minecraftProfileTexture) -> {
            skin = resourceLocation;
        }, true);
    }

    public static FakePlayer getInstance() {
        if (instance == null) {
            instance = new FakePlayer();
        }
        return instance;
    }

    public void setSkin(GameProfile profile) {
        try {
            if (profile == null) {
                skin = DefaultSkinHelper.getTexture();
                cape = null;
                skinModel = "default";
                return;
            }

            if (cache.containsKey(profile.getId())) {
                PlayerData data = cache.get(profile.getId());
                this.skin = data.skin;
                this.cape = data.cape;
                this.skinModel = data.skinModel;
                return;
            }

            PlayerData data = new PlayerData();
            cape = null;
            MinecraftClient.getInstance().getSkinProvider().loadSkin(profile, (type, resourceLocation, minecraftProfileTexture) -> {
                if (type == MinecraftProfileTexture.Type.SKIN) {
                    skin = resourceLocation;
                    this.skinModel = minecraftProfileTexture.getMetadata("model");
                    if (this.skinModel == null) {
                        this.skinModel = "default";
                    }
                    data.skin = skin;
                    data.skinModel = skinModel;
                    cache.put(profile.getId(), data);
                }
                if (type == MinecraftProfileTexture.Type.CAPE) {
                    cape = resourceLocation;
                    data.cape = cape;
                }
            }, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    @Override
    public Identifier getSkinTexture() {
        if (skin == null) return DefaultSkinHelper.getTexture();
        return skin;
    }

    @Override
    public String getModel() {
        return skinModel;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return true;
    }

    @Override
    public boolean canRenderCapeTexture() {
        return true;
    }

    @Nullable
    @Override
    public Identifier getCapeTexture() {
        return cape;
    }

    public void clearCache() {
        // clear the cache so skins are updated
        cache.clear();
    }

    private static class PlayerData {
        private Identifier skin;
        private Identifier cape;
        private String skinModel;
    }

    @Override
    public float distanceTo(Entity entity) {
        return Float.MAX_VALUE;
    }

    @Override
    public double squaredDistanceTo(Entity entity) {
        return Float.MAX_VALUE;
    }

}
