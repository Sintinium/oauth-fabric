package com.sintinium.oauthfabric.gui;

import com.sintinium.oauthfabric.gui.profile.ProfileSelectionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.concurrent.atomic.AtomicReference;

public class LoginLoadingScreen extends OAuthScreen {

    private final String loadingText = "Loading";
    private int dots = 0;
    private String renderText = loadingText;

    private int tick = 0;
    private final Runnable onCancel;
    private final boolean isMicrosoft;
    private final AtomicReference<String> updateText = new AtomicReference<>();

    public LoginLoadingScreen(Runnable onCancel, boolean isMicrosoft) {
        super(new LiteralText("Logging in"));
        this.onCancel = onCancel;
        this.isMicrosoft = isMicrosoft;

        if (this.isMicrosoft) {
            updateText.set("Check your browser");
        } else {
            updateText.set("Authorizing you with Mojang");
        }
    }

    public void updateText(String text) {
        updateText.set(text);
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 60, 200, 20, new LiteralText("Cancel"), (p_213029_1_) -> {
            onCancel.run();
            setScreen(new ProfileSelectionScreen());
        }));
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        if (tick % 20 != 0) return;
        dots++;
        if (dots > 3) {
            dots = 0;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(loadingText);
        for (int i = 0; i < dots; i++) {
            builder.append(".");
        }
        renderText = builder.toString();
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, renderText, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
        if (this.isMicrosoft) {
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, updateText.get(), this.width / 2, this.height / 2 - 28, 0xFFFFFF);
        }
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
