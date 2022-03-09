package com.sintinium.oauthfabric.gui.profile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class FakeWorld extends ClientWorld {

    private static FakeWorld instance;

    public static FakeWorld getInstance() {
        if (instance == null) {
            instance = new FakeWorld();
        }
        return instance;
    }
    public FakeWorld() {
        super(FakeClientPlayNetHandler.getInstance(), new Properties(Difficulty.EASY, false, true), World.OVERWORLD, new RegistryEntry.Direct<>(FakeDimensionType.getInstance()), 0, 0, () -> null, new WorldRenderer(MinecraftClient.getInstance(), MinecraftClient.getInstance().getBufferBuilders()), false, 0L);
    }
}
