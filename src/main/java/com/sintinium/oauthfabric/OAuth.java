package com.sintinium.oauthfabric;

import net.fabricmc.api.ClientModInitializer;

public class OAuth implements ClientModInitializer {
    private static OAuth INSTANCE;

    public OAuth() {
        INSTANCE = this;
    }

    public static OAuth getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitializeClient() {

    }
}
