package com.sintinium.oauth.oauthfabric.gui;

import com.sintinium.oauth.oauthfabric.login.LoginUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class LoginScreen extends OAuthScreen {
    private final Screen lastScreen;
    private final MultiplayerScreen multiplayerScreen;
    private ButtonWidget mojangLoginButton;
    private TextFieldWidget passwordWidget;
    private TextFieldWidget usernameWidget;
    private AtomicReference<String> status = new AtomicReference<>();

    public LoginScreen(Screen last, MultiplayerScreen multiplayerScreen) {
        super(new LiteralText("OAuth Login"));
        this.lastScreen = last;
        this.multiplayerScreen = multiplayerScreen;
    }

    public void tick() {
        this.usernameWidget.tick();
        this.passwordWidget.tick();
        if (!toRun.isEmpty()) {
            for (Runnable r : toRun) {
                r.run();
            }
            toRun.clear();
        }
    }

    protected void init() {
        this.client.keyboard.setRepeatEvents(true);

        this.passwordWidget = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 20, 200, 20, new LiteralText("Password"));
        this.passwordWidget.setMaxLength(128);
        this.passwordWidget.setChangedListener(this::onEdited);

        this.usernameWidget = new UsernameFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 60, 200, 20, new LiteralText("Username/Email"), passwordWidget);
        this.usernameWidget.changeFocus(true);
        if (LoginUtil.lastMojangUsername != null) {
            this.usernameWidget.setText(LoginUtil.lastMojangUsername);
        }
        this.usernameWidget.setChangedListener(this::onEdited);

        this.addDrawableChild(this.usernameWidget);
        this.addDrawableChild(this.passwordWidget);

        this.mojangLoginButton = this.addDrawableChild(new ResponsiveButton(this.width / 2 - 100, this.height / 2 + 36, 200, 20, new LiteralText("Login"), (p_213030_1_) -> {
            Thread thread = new Thread(() -> {
                if (usernameWidget.getText().isEmpty()) {
                    toRun.add(() -> this.status.set("Missing username!"));
                } else {
                    Optional<Boolean> didSuccessfullyLogIn = LoginUtil.loginMojangOrLegacy(usernameWidget.getText(), passwordWidget.getText());
                    if (!didSuccessfullyLogIn.isPresent()) {
                        toRun.add(() -> this.status.set("You seem to be offline. Check your connection!"));
                    } else if (!didSuccessfullyLogIn.get()) {
                        toRun.add(() -> this.status.set("Wrong password or username!"));
                    } else {
                        LoginUtil.updateOnlineStatus();
                        toRun.add(() -> MinecraftClient.getInstance().setScreen(multiplayerScreen));
                    }
                }
            });
            thread.start();
        }, this::updateLoginButton, () -> this.mojangLoginButton.setMessage(new LiteralText("Login"))));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 60, 200, 20, ScreenTexts.CANCEL, (p_213029_1_) -> {
            MinecraftClient.getInstance().setScreen(lastScreen);
        }));
        this.cleanUp();
    }

    @Override
    public void resize(MinecraftClient p_231152_1_, int p_231152_2_, int p_231152_3_) {
        String s = this.passwordWidget.getText();
        String s1 = this.usernameWidget.getText();
        this.init(p_231152_1_, p_231152_2_, p_231152_3_);
        this.passwordWidget.setText(s);
        this.usernameWidget.setText(s1);
    }

    private void onEdited(String p_213028_1_) {
        this.cleanUp();
    }

    private void updateLoginButton() {
        if (this.passwordWidget.getText().isEmpty()) {
            this.mojangLoginButton.setMessage(new LiteralText("Login Offline"));
        } else {
            this.mojangLoginButton.setMessage(new LiteralText("Login"));
        }
    }

    public void removed() {
        this.client.keyboard.setRepeatEvents(false);
    }

    public void onClose() {
        this.cleanUp();
        this.client.setScreen(this.lastScreen);
    }

    private void cleanUp() {
        this.mojangLoginButton.active = !this.usernameWidget.getText().isEmpty();
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        drawCenteredTextWithShadow(p_230430_1_, this.textRenderer, this.title.asOrderedText(), this.width / 2, 17, 16777215);
        drawStringWithShadow(p_230430_1_, this.textRenderer, "Username/Email", this.width / 2 - 100, this.height / 2 - 60 - 12, 10526880);
        drawStringWithShadow(p_230430_1_, this.textRenderer, "Password", this.width / 2 - 100, this.height / 2 - 20 - 12, 10526880);
        if (status.get() != null) {
            drawCenteredTextWithShadow(p_230430_1_, this.textRenderer, new LiteralText(status.get()).asOrderedText(), this.width / 2, this.height / 2 + 10, 0xFF0000);
        }
        this.usernameWidget.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.passwordWidget.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
