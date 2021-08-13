package com.sintinium.oauth.oauthfabric.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class UsernameFieldWidget extends TextFieldWidget {

    private TextFieldWidget passwordFieldWidget;

    public UsernameFieldWidget(TextRenderer p_i232260_1_, int p_i232260_2_, int p_i232260_3_, int p_i232260_4_, int p_i232260_5_, Text p_i232260_6_, TextFieldWidget passwordFieldWidget) {
        super(p_i232260_1_, p_i232260_2_, p_i232260_3_, p_i232260_4_, p_i232260_5_, p_i232260_6_);
        this.passwordFieldWidget = passwordFieldWidget;
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        boolean result = super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        if (this.isFocused() && passwordFieldWidget.isFocused()) {
            passwordFieldWidget.changeFocus(false);
        }
        return result;
    }
}
