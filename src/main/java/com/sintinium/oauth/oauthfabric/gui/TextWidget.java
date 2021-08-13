package com.sintinium.oauth.oauthfabric.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;

public class TextWidget extends ButtonWidget {
    private String text;
    private int color = 0xFFFFFF;

    public TextWidget(int x, int y, int width, int height, String text) {
        super(x, y, width, height, new LiteralText(text), p_onPress_1_ -> {
        });
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        drawStringWithShadow(p_230431_1_, MinecraftClient.getInstance().textRenderer, this.getMessage().asString(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
