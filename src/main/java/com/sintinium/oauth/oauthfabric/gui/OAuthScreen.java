package com.sintinium.oauth.oauthfabric.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class OAuthScreen extends Screen {

    protected List<Runnable> toRun = new ArrayList<>();

    protected OAuthScreen(Text title) {
        super(title);
    }

    public void addToQueue(Runnable runnable) {
        this.toRun.add(runnable);
    }

    @Override
    public void tick() {
        for (Runnable runnable : toRun) {
            runnable.run();
        }
        toRun.clear();
    }
}
