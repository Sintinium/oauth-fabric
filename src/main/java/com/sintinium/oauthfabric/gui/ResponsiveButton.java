package com.sintinium.oauthfabric.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ResponsiveButton extends ButtonWidget {
    private final Runnable onHover;
    private final Runnable onUnhover;
    private final boolean wasHovered = false;

    public ResponsiveButton(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress, Runnable onHover, Runnable onUnhover) {
        super(x, y, width, height, message, onPress);
        this.onHover = onHover;
        this.onUnhover = onUnhover;
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        if (!this.active) {
            onUnhover.run();
            return;
        }
        if (this.isHovered()) {
            onHover.run();
        } else {
            onUnhover.run();
        }
    }
}
