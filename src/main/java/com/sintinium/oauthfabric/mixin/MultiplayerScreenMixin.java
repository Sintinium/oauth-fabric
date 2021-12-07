package com.sintinium.oauthfabric.mixin;

import com.sintinium.oauthfabric.gui.TextWidget;
import com.sintinium.oauthfabric.gui.profile.ProfileSelectionScreen;
import com.sintinium.oauthfabric.login.LoginUtil;
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
public class MultiplayerScreenMixin extends Screen {

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        try {
//            Method addButtonMethod = ObfuscationReflectionHelper.findMethod(Screen.class, "addButton", Widget.class);
//            Method addButtonMethod = ObfuscationReflectionHelper.findMethod(Screen.class, "func_230480_a_", Widget.class);
            addDrawableChild(new ButtonWidget(10, 6, 66, 20, new LiteralText("OAuth Login"), p_onPress_1_ -> MinecraftClient.getInstance().setScreen(new ProfileSelectionScreen())));
            final TextWidget textWidget = new TextWidget(10 + 66 + 3, 6, 0, 20, "Status: loading");
            textWidget.setColor( 0xFFFFFF);
            Thread thread = new Thread(() -> {
                boolean isOnline = LoginUtil.isOnline();
                if (isOnline) {
                    textWidget.setMessage(new LiteralText("Status: online"));
                    textWidget.setColor(0x55FF55);
                } else {
                    textWidget.setMessage(new LiteralText("Status: offline"));
                    textWidget.setColor(0xFF5555);
                }
            }, "Oauth status");
            thread.setDaemon(true);
            thread.start();

            this.addDrawableChild(textWidget);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
