package com.sintinium.oauthfabric.gui;

import com.google.common.base.Splitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ErrorScreen extends OAuthScreen {

    private String message = null;
    private Throwable e = null;
    private boolean isInfo = false;

    public ErrorScreen(boolean isMs, String message) {
        super(new LiteralText("Error logging into " + (isMs ? "Microsoft." : "Mojang.")));
        this.message = message;
        System.err.println(message);
    }

    public ErrorScreen(boolean isMs, Throwable e) {
        super(new LiteralText("Error logging into " + (isMs ? "Microsoft." : "Mojang.")));
        this.e = e;
        e.printStackTrace();
    }

    public void setInfo() {
        this.isInfo = true;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 60, 200, 20, new LiteralText("Cancel"), p_onPress_1_ -> {
            setScreen(new MultiplayerScreen(new TitleScreen()));
        }));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        this.renderBackground(p_230430_1_);
        if (isInfo) {
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, getMessage(), this.width / 2, this.height / 2 - 24, 0xFFFFFF);
        } else if (getMessage().toLowerCase().contains("no such host is known") || getMessage().toLowerCase().contains("connection reset")) {
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, "The servers could be down or it could be an internet problem.", this.width / 2, this.height / 2 - 28, 0xFFFFFF);
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, "If you believe this is a bug please create an issue at", this.width / 2, this.height / 2 - 12, 0xFFFFFF);
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, "https://github.com/Sintinium/oauth with your latest log file.", this.width / 2, this.height / 2, 0xFFFFFF);
        } else {
            Text github = new LiteralText("Please create an issue at https://github.com/Sintinium/oauth with your log file.")
                    .setStyle(Style.EMPTY.withUnderline(true));
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, "An error occurred. This could be a bug.", this.width / 2, this.height / 2 - 40, 0xFFFFFF);
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, github, this.width / 2, this.height / 2 - 28, 0xFFFFFF);
            float scale = .5f;
            p_230430_1_.scale(scale, scale, scale);
            String msg = getMessage();
            Iterable<String> messages = Splitter.fixedLength(Math.round(80 * (1f / scale))).limit(12).split(msg);
            int index = 0;
            for (String m : messages) {
                font.drawWithShadow(p_230430_1_, m, this.width / 2f - font.getWidth(m) / 2f * scale, (this.height / 2f - 16f) * (1f / scale) + (index * 12f), 0xFF4444);
                index++;
            }
            p_230430_1_.scale(1f / scale, 1f / scale, 1f / scale);
        }

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    private String getMessage() {
        String result = "";
        if (message != null) {
            result = message;
        } else if (e != null) {
            result = ExceptionUtils.getStackTrace(e);
        } else {
            return "Error getting error message.";
        }
        return result;
    }
}
