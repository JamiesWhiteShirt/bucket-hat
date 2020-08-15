package com.jamieswhiteshirt.buckethat.mixin.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
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
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci, TextRenderer textRenderer, ItemStack stack) {
        if (client.options.getPerspective().isFirstPerson() && stack.getItem() == Items.BUCKET) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            client.getTextureManager().bindTexture(BUCKET_BLUR);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
            bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
            tessellator.draw();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
