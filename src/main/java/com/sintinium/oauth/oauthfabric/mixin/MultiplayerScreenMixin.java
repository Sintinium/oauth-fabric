package com.sintinium.oauth.oauthfabric.mixin;

import com.sintinium.oauth.oauthfabric.gui.LoginTypeScreen;
import com.sintinium.oauth.oauthfabric.gui.TextWidget;
import com.sintinium.oauth.oauthfabric.login.LoginUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        addButton(new ButtonWidget(10, 6, 66, 20, new LiteralText("Oauth Login"), button -> {
            MinecraftClient.getInstance().openScreen(new LoginTypeScreen(this));
        }));
        final TextWidget textWidget = new TextWidget(10 + 66 + 3, 6, 0, 20, "Status: offline");
        addButton(textWidget);
        textWidget.setColor(0xFF5555);
        Thread thread = new Thread(() -> {
            boolean isOnline = LoginUtil.isOnline();
            if (isOnline) {
                textWidget.setMessage(new LiteralText("Status: online"));
                textWidget.setColor(0x55FF55);
            } else {
                textWidget.setMessage(new LiteralText("Status: offline"));
                textWidget.setColor(0xFF5555);
            }
        });
        thread.start();
    }
}
