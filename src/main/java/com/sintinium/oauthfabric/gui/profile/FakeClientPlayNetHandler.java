package com.sintinium.oauthfabric.gui.profile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;

public class FakeClientPlayNetHandler extends ClientPlayNetworkHandler {

    private static FakeClientPlayNetHandler instance;

    public static FakeClientPlayNetHandler getInstance() {
        if (instance == null) {
            instance = new FakeClientPlayNetHandler();
        }
        return instance;
    }

    public FakeClientPlayNetHandler() {
        super(MinecraftClient.getInstance(), null, new ClientConnection(NetworkSide.CLIENTBOUND), MinecraftClient.getInstance().getSession().getProfile(), null);
    }


}
