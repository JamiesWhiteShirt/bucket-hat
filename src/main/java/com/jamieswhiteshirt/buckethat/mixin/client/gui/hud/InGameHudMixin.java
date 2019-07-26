package com.jamieswhiteshirt.buckethat.mixin.client.gui.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
abstract public class InGameHudMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledHeight;
    @Shadow private int scaledWidth;

    private static final Identifier BUCKET_BLUR = new Identifier("bucket-hat", "textures/misc/bucket_blur.png");

    @Inject(
        method = "render(F)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(float f1, CallbackInfo ci, TextRenderer textRenderer, ItemStack stack) {
        if (client.options.perspective == 0 && stack.getItem() == Items.BUCKET) {
            GlStateManager.disableDepthTest();
            GlStateManager.depthMask(false);
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableAlphaTest();
            client.getTextureManager().bindTexture(BUCKET_BLUR);
            Tessellator tessellator_1 = Tessellator.getInstance();
            BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
            bufferBuilder_1.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV);
            bufferBuilder_1.vertex(0.0D, scaledHeight, -90.0D).texture(0.0D, 1.0D).next();
            bufferBuilder_1.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0D, 1.0D).next();
            bufferBuilder_1.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0D, 0.0D).next();
            bufferBuilder_1.vertex(0.0D, 0.0D, -90.0D).texture(0.0D, 0.0D).next();
            tessellator_1.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepthTest();
            GlStateManager.enableAlphaTest();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
