package com.jamieswhiteshirt.buckethat.mixin.client.render.entity.feature;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    @Shadow public abstract A getArmor(EquipmentSlot equipmentSlot_1);
    @Shadow protected abstract void setVisible(A var1, EquipmentSlot var2);

    private static final Identifier BUCKET_ARMOR_TEXTURE = new Identifier("bucket-hat", "textures/models/armor/bucket.png");

    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext_1) {
        super(featureRendererContext_1);
    }

    @Inject(
        method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/LivingEntity;FFFFFFLnet/minecraft/entity/EquipmentSlot;I)V",
        at = @At("HEAD")
    )
    private void renderArmor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, T livingEntity, float f, float g, float h, float i, float j, float k, EquipmentSlot equipmentSlot_1, int l, CallbackInfo ci) {
        if (equipmentSlot_1 == EquipmentSlot.HEAD) {
            ItemStack itemStack = livingEntity.getEquippedStack(equipmentSlot_1);
            if (itemStack.getItem() == Items.BUCKET) {
                A bipedEntityModel = this.getArmor(equipmentSlot_1);
                this.getContextModel().setAttributes(bipedEntityModel);
                bipedEntityModel.animateModel(livingEntity, f, g, h);
                this.setVisible(bipedEntityModel, equipmentSlot_1);
                bipedEntityModel.setAngles(livingEntity, f, g, i, j, k);

                boolean renderGlint = itemStack.hasEnchantmentGlint();
                VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumerProvider, RenderLayer.getEntityCutoutNoCull(BUCKET_ARMOR_TEXTURE), false, renderGlint);
                bipedEntityModel.render(matrixStack, vertexConsumer, l, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
