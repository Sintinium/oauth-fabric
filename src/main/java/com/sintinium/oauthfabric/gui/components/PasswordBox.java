package com.sintinium.oauthfabric.gui.components;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class PasswordBox extends TextFieldWidget {

    public PasswordBox(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, (TextFieldWidget)null, text);
    }
}
