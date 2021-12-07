package com.sintinium.oauthfabric.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicReference;

public class OAuthScreen extends Screen {
    private static final AtomicReference<Screen> screenToSet = new AtomicReference<>(null);

    protected OAuthScreen(Text pTitle) {
        super(pTitle);
    }

    /**
     * Safely sets screen even if called in a different thread.
     */
    public static void setScreen(Screen screen) {
        screenToSet.set(screen);
    }

    @Override
    public void tick() {
        super.tick();
        Screen screenToSet = OAuthScreen.screenToSet.getAndSet(null);
        if (screenToSet != null) {
            MinecraftClient.getInstance().setScreen(screenToSet);
        }
    }
}
