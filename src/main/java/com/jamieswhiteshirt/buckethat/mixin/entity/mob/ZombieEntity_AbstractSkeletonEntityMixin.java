package com.jamieswhiteshirt.buckethat.mixin.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ZombieEntity.class, AbstractSkeletonEntity.class})
public abstract class ZombieEntity_AbstractSkeletonEntityMixin extends HostileEntity {
    protected ZombieEntity_AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(
        method = "initEquipment(Lnet/minecraft/world/LocalDifficulty;)V",
        at = @At("TAIL")
    )
    private void initEquipment(LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (random.nextInt(30) == 0) {
            equipStack(EquipmentSlot.HEAD, new ItemStack(Items.BUCKET));
        }
    }
}
