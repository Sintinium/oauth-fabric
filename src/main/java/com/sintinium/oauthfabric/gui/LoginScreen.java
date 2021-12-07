package com.sintinium.oauthfabric.gui;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.sintinium.oauthfabric.gui.components.PasswordBox;
import com.sintinium.oauthfabric.gui.profile.ProfileSelectionScreen;
import com.sintinium.oauthfabric.login.LoginUtil;
import com.sintinium.oauthfabric.profile.MojangProfile;
import com.sintinium.oauthfabric.profile.OfflineProfile;
import com.sintinium.oauthfabric.profile.ProfileManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class LoginScreen extends OAuthScreen {

    private ButtonWidget mojangLoginButton;
    private TextFieldWidget passwordWidget;
    private TextFieldWidget usernameWidget;
    private final AtomicReference<String> status = new AtomicReference<>();

    private final List<Runnable> toRun = new CopyOnWriteArrayList<>();

    public LoginScreen() {
        super(new LiteralText("OAuth Login"));
    }

    public void tick() {
        super.tick();
        this.usernameWidget.tick();
        this.passwordWidget.tick();
        if (usernameWidget.isFocused()) passwordWidget.changeFocus(false);
        if (passwordWidget.isFocused()) usernameWidget.changeFocus(false);
        if (!toRun.isEmpty()) {
            for (Runnable r : toRun) {
                r.run();
            }
            toRun.clear();
        }
//        OAuth.savePassword = this.savePasswordButton.selected();
    }

    protected void init() {
        MinecraftClient.getInstance().keyboard.setRepeatEvents(true);


        this.usernameWidget = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 60, 200, 20, new LiteralText("Username/Email"));
        this.usernameWidget.changeFocus(true);
        this.usernameWidget.setChangedListener(this::onEdited);

        this.passwordWidget = new PasswordBox(this.textRenderer, this.width / 2 - 100, this.height / 2 - 20, 200, 20, new LiteralText("Password"));
        this.passwordWidget.setMaxLength(128);
        this.passwordWidget.setChangedListener(this::onEdited);

        this.addSelectableChild(this.usernameWidget);
        this.addSelectableChild(this.passwordWidget);

        this.mojangLoginButton = this.addDrawableChild(new ResponsiveButton(this.width / 2 - 100, this.height / 2 + 36, 200, 20, new LiteralText("Add Profile"), (b) -> {
            Thread thread = new Thread(() -> {
                if (usernameWidget.getText().isEmpty()) {
                    toRun.add(() -> this.status.set("Missing username!"));
                } else {
                    if (passwordWidget.getText().isEmpty()) {
                        ProfileManager.getInstance().addProfile(new OfflineProfile(usernameWidget.getText(), UUID.nameUUIDFromBytes(usernameWidget.getText().getBytes())));
                        toRun.add(() -> setScreen(new ProfileSelectionScreen()));
                        return;
                    }
                    MojangProfile profile;
                    try {
                        profile = LoginUtil.tryGetMojangProfile(usernameWidget.getText(), passwordWidget.getText());
                    } catch (InvalidCredentialsException e) {
                        toRun.add(() -> this.status.set("Invalid username or password!"));
                        return;
                    } catch (AuthenticationUnavailableException e) {
                        toRun.add(() -> this.status.set("You seem to be offline. Check your connection!"));
                        e.printStackTrace();
                        return;
                    } catch (AuthenticationException e) {
                        toRun.add(() -> setScreen(new ErrorScreen(false, e)));
                        e.printStackTrace();
                        return;
                    }
                    if (profile == null) {
                        toRun.add(() -> this.status.set("Invalid username or password!"));
                    } else {
                        LoginUtil.updateOnlineStatus();
                        ProfileManager.getInstance().addProfile(profile);
                        toRun.add(() -> setScreen(new ProfileSelectionScreen()));
                    }
                }
            }, "Oauth mojang");
            thread.setDaemon(true);
            thread.start();
        }, this::updateLoginButton, () -> this.mojangLoginButton.setMessage(new LiteralText("Add Profile"))));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 60, 200, 20, new LiteralText("Cancel"), (p_213029_1_) -> {
            setScreen(new ProfileSelectionScreen());
        }));

        this.cleanUp();
    }

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
            this.mojangLoginButton.setMessage(new LiteralText("Add Offline Profile"));
        } else {
            this.mojangLoginButton.setMessage(new LiteralText("Add Profile"));
        }
    }

    public void removed() {
        MinecraftClient.getInstance().keyboard.setRepeatEvents(false);
    }

    public void onClose() {
        this.cleanUp();
        MinecraftClient.getInstance().setScreen(new ProfileSelectionScreen());
    }

    private void cleanUp() {
        this.mojangLoginButton.active = !this.usernameWidget.getText().isEmpty();
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        drawCenteredText(p_230430_1_, this.textRenderer, this.title, this.width / 2, 17, 16777215);
        drawStringWithShadow(p_230430_1_, this.textRenderer, "Username/Email", this.width / 2 - 100, this.height / 2 - 60 - 12, 10526880);
        drawStringWithShadow(p_230430_1_, this.textRenderer, "Password", this.width / 2 - 100, this.height / 2 - 20 - 12, 10526880);
        if (status.get() != null) {
            drawCenteredText(p_230430_1_, MinecraftClient.getInstance().textRenderer, status.get(), this.width / 2, this.height / 2 + 20, 0xFF0000);
        }
        this.usernameWidget.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.passwordWidget.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
//        if (this.savePasswordButton.isHovered()) {
//            List<ITextProperties> tooltips = new ArrayList<>();
//            String tooltip = "This will save your password encrypted to your config file. While the password is encrypted if a hacker accesses your computer they could easily unencrypt it.";
//            tooltips.add(ITextProperties.of(tooltip));
//            renderWrappedToolTip(p_230430_1_, tooltips, p_230430_2_, p_230430_3_, this.font);
//        }

        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
