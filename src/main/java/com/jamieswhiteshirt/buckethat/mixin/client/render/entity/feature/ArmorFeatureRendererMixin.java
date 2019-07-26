package com.jamieswhiteshirt.buckethat.mixin.client.render.entity.feature;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
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
    @Shadow protected abstract void method_4170(A var1, EquipmentSlot var2);
    @Shadow private float red;
    @Shadow private float green;
    @Shadow private float blue;
    @Shadow private float alpha;
    @Shadow private boolean ignoreGlint;
    @Shadow public static <T extends Entity> void renderEnchantedGlint(Consumer<Identifier> consumer_1, T entity_1, EntityModel<T> entityModel_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7) {}

    private static final Identifier BUCKET_ARMOR_TEXTURE = new Identifier("bucket-hat", "textures/models/armor/bucket.png");

    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext_1) {
        super(featureRendererContext_1);
    }

    @Inject(
        method = "renderArmor(Lnet/minecraft/entity/LivingEntity;FFFFFFFLnet/minecraft/entity/EquipmentSlot;)V",
        at = @At("HEAD")
    )
    private void renderArmor(LivingEntity livingEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7, EquipmentSlot equipmentSlot_1, CallbackInfo ci) {
        if (equipmentSlot_1 == EquipmentSlot.HEAD) {
            ItemStack itemStack_1 = livingEntity_1.getEquippedStack(equipmentSlot_1);
            if (itemStack_1.getItem() == Items.BUCKET) {
                A bipedEntityModel_1 = this.getArmor(equipmentSlot_1);
                this.getModel().setAttributes(bipedEntityModel_1);
                bipedEntityModel_1.method_17086((T) livingEntity_1, float_1, float_2, float_3);
                this.method_4170(bipedEntityModel_1, equipmentSlot_1);
                this.bindTexture(BUCKET_ARMOR_TEXTURE);

                GlStateManager.color4f(this.red, this.green, this.blue, this.alpha);
                bipedEntityModel_1.method_17088((T) livingEntity_1, float_1, float_2, float_4, float_5, float_6, float_7);
                if (!this.ignoreGlint && itemStack_1.hasEnchantments()) {
                    renderEnchantedGlint(this::bindTexture, (T) livingEntity_1, bipedEntityModel_1, float_1, float_2, float_3, float_4, float_5, float_6, float_7);
                }
            }
        }
    }
}
