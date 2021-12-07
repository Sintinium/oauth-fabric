package com.sintinium.oauthfabric.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sintinium.oauthfabric.gui.components.PasswordBox;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.BiFunction;

@Mixin(TextFieldWidget.class)
public abstract class PasswordBoxMixin extends ClickableWidget {

    @Shadow
    private String suggestion;

    public PasswordBoxMixin(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Text p_93633_) {
        super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
    }

    private static String getHiddenValue(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            builder.append("*");
        }
        return builder.toString();
    }

    @Shadow
    public abstract boolean isVisible();

    @Shadow
    public abstract int getInnerWidth();

    @Shadow
    protected abstract int getMaxLength();

    @Shadow protected abstract boolean drawsBackground();

    @Shadow private boolean editable;

    @Shadow private int editableColor;

    @Shadow private int uneditableColor;

    @Shadow private int selectionStart;

    @Shadow private int firstCharacterIndex;

    @Shadow private int selectionEnd;

    @Shadow @Final private TextRenderer textRenderer;

    @Shadow private String text;

    @Shadow private int focusedTicks;

    @Shadow private boolean drawsBackground;

    @Shadow private BiFunction<String, Integer, OrderedText> renderTextProvider;

    @Shadow protected abstract void drawSelectionHighlight(int x1, int y1, int x2, int y2);

    @Inject(method = "renderButton", at = @At("HEAD"), cancellable = true)
    public void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TextFieldWidget instance = (TextFieldWidget) (Object) this;
        if (instance instanceof PasswordBox) ci.cancel();
        else return;

        if (this.isVisible()) {
            int i;
            if (this.drawsBackground()) {
                i = this.isFocused() ? -1 : -6250336;
                fill(matrices, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, i);
                fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            }
            i = this.editable ? this.editableColor : this.uneditableColor;
            int j = this.selectionStart - this.firstCharacterIndex;
            int k = this.selectionEnd - this.firstCharacterIndex;
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
            boolean bl = j >= 0 && j <= string.length();
            boolean bl2 = this.isFocused() && this.focusedTicks / 6 % 2 == 0 && bl;
            int l = this.drawsBackground ? this.x + 4 : this.x;
            int m = this.drawsBackground ? this.y + (this.height - 8) / 2 : this.y;
            int n = l;
            if (k > string.length()) {
                k = string.length();
            }

            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, j) : string;
                n = this.textRenderer.drawWithShadow(matrices, (OrderedText)this.renderTextProvider.apply(string2, this.firstCharacterIndex), (float)l, (float)m, i);
            }

            // Added
            matrices.push();
            matrices.scale(1.5f, 1.5f, 1.5f);
            l /= 1.5f;
            m /= 1.5f;
            // End

            boolean string2 = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
            int o = n;
            if (!bl) {
                o = j > 0 ? l + this.width : l;
            } else if (string2) {
                o = n - 1;
                --n;
            }

            if (!string.isEmpty() && bl && j < string.length()) {
                this.textRenderer.drawWithShadow(matrices, (OrderedText)this.renderTextProvider.apply(string.substring(j), this.selectionStart), (float)n, (float)m, i);
            }

            if (!string2 && this.suggestion != null) {
                this.textRenderer.drawWithShadow(matrices, this.suggestion, (float)(o - 1), (float)m, -8355712);
            }

            // Added
            matrices.pop();
            l *= 1.5;
            m *= 1.5;
            o *= 1.5;
            // End

            int var10002;
            int var10003;
            int var10004;
            if (bl2) {
                if (string2) {
                    var10002 = m - 1;
                    var10003 = o + 1;
                    var10004 = m + 1;
                    Objects.requireNonNull(this.textRenderer);
                    DrawableHelper.fill(matrices, o, var10002, var10003, var10004 + 9, -3092272);
                } else {
                    this.textRenderer.drawWithShadow(matrices, "_", (float)o, (float)m, i);
                }
            }

            // Added
            matrices.push();
            matrices.scale(1.5f, 1.5f, 1.5f);
            l /= 1.5;
            m /= 1.5;
            o /= 1.5;
            // End

            if (k != j) {
                int p = l + this.textRenderer.getWidth(string.substring(0, k));
                var10002 = m - 1;
                var10003 = p - 1;
                var10004 = m + 1;
                Objects.requireNonNull(this.textRenderer);
                this.drawSelectionHighlight(o, var10002, var10003, var10004 + 9);
            }

            // Added
            matrices.pop();
            // End
        }
    }

    @Inject(method = "drawSelectionHighlight", at = @At("HEAD"), cancellable = true)
    public void onRenderHighlight(int p_94136_, int p_94137_, int p_94138_, int p_94139_, CallbackInfo ci) {
        TextFieldWidget instance = (TextFieldWidget) (Object) this;
        if (instance instanceof PasswordBox) ci.cancel();
        else return;


        if (p_94136_ < p_94138_) {
            int i = p_94136_;
            p_94136_ = p_94138_;
            p_94138_ = i;
        }

        if (p_94137_ < p_94139_) {
            int j = p_94137_;
            p_94137_ = p_94139_;
            p_94139_ = j;
        }

        if (p_94138_ > this.x + this.width) {
            p_94138_ = this.x + this.width;
        }

        if (p_94136_ > this.x + this.width) {
            p_94136_ = this.x + this.width;
        }

        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);

        // Added
        p_94136_ *= 1.5;
        p_94138_ *= 1.5;
        p_94139_ *= 1.5;
        p_94137_ *= 1.5;
        // End

        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(p_94136_, p_94139_, 0.0D).next();
        bufferBuilder.vertex(p_94138_, p_94139_, 0.0D).next();
        bufferBuilder.vertex(p_94138_, p_94137_, 0.0D).next();
        bufferBuilder.vertex(p_94136_, p_94137_, 0.0D).next();
        tesselator.draw();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }
}
