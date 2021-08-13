package com.sintinium.oauth.oauthfabric.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ResponsiveButton extends ButtonWidget {
    private Runnable onHover;
    private Runnable onUnhover;
    private boolean wasHovered = false;

    public ResponsiveButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, Text p_i232255_5_, ButtonWidget.PressAction onPress, Runnable onHover, Runnable onUnhover) {
        super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, onPress);
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
