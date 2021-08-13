package com.sintinium.oauth.oauthfabric.gui;

import com.sintinium.oauth.oauthfabric.login.LoginUtil;
import com.sintinium.oauth.oauthfabric.login.MicrosoftLogin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LoginTypeScreen extends OAuthScreen {

    private MultiplayerScreen lastScreen;

    public LoginTypeScreen(Screen last) {
        super(new LiteralText("Select Account Type"));
        lastScreen = (MultiplayerScreen) last;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 - 20 - 2, 200, 20, new LiteralText("Mojang Login"), button -> {
            MinecraftClient.getInstance().setScreen(new LoginScreen(this, lastScreen));
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 2, 200, 20, new LiteralText("Microsoft Login"), (p_213031_1_) -> {
            final MicrosoftLogin login = new MicrosoftLogin();
            if (login.getErrorMsg() != null) {
                System.err.println(login.getErrorMsg());
            }
            LoginLoadingScreen loginLoadingScreen = new LoginLoadingScreen(lastScreen, this, login::cancelLogin, true);
            MinecraftClient.getInstance().setScreen(loginLoadingScreen);
            Thread thread = new Thread(() -> {
                login.login(() -> {
                    LoginUtil.updateOnlineStatus();
                    loginLoadingScreen.addToQueue(() -> MinecraftClient.getInstance().setScreen(lastScreen));
                });
            });
            thread.start();
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 60, 200, 20, ScreenTexts.CANCEL, (button) -> {
            MinecraftClient.getInstance().setScreen(lastScreen);
        }));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        drawCenteredTextWithShadow(p_230430_1_, this.textRenderer, this.title.asOrderedText(), this.width / 2, this.height / 2 - 60, 16777215);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
