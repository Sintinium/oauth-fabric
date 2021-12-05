package com.sintinium.oauth.oauthfabric.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LoginLoadingScreen extends OAuthScreen {

    private String loadingText = "Loading";
    private int dots = 0;
    private String renderText = loadingText;

    private Screen multiplayerScreen;
    private Screen lastScreen;
    private int tick = 0;
    private Runnable onCancel;
    private boolean isMicrosoft;

    protected LoginLoadingScreen(Screen multiplayerScreen, Screen callingScreen, Runnable onCancel, boolean isMicrosoft) {
        super(new LiteralText("Logging in"));
        this.multiplayerScreen = multiplayerScreen;
        this.lastScreen = callingScreen;
        this.onCancel = onCancel;
        this.isMicrosoft = isMicrosoft;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 60, 200, 20, ScreenTexts.CANCEL, (p_213029_1_) -> {
            onCancel.run();
            MinecraftClient.getInstance().setScreen(lastScreen);
        }));
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        if (tick % 20 != 0) return;
        dots++;
        if (dots >= 3) {
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
        drawCenteredText(p_230430_1_, this.textRenderer, new LiteralText(renderText), this.width / 2, this.height / 2 - 40, 0xFFFFFF);
        if (this.isMicrosoft) {
            drawCenteredText(p_230430_1_, this.textRenderer, new LiteralText("Check your browser"), this.width / 2, this.height / 2 - 28, 0xFFFFFF);
        }
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
