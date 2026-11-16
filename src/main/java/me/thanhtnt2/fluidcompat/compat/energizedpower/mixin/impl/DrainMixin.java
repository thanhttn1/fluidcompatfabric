package me.thanhtnt2.fluidcompat.compat.energizedpower.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.thanhtnt2.fluidcompat.compat.energizedpower.EnergizedPowerBridge;
import me.thanhtnt2.fluidcompat.compat.energizedpower.EnergizedPowerCompatModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Drain. Same partial bucket problem as the pumps. The tick method is static, so the handlers are too, and the
 * insert hook only takes the first insert (the bucket branch, not the cauldron branch).
 */
@Pseudo
@Mixin(targets = "me.jddev0.ep.block.entity.DrainBlockEntity", remap = false)
public abstract class DrainMixin {

    @Redirect(method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;"
            + "Lnet/minecraft/world/level/block/state/BlockState;Lme/jddev0/ep/block/entity/DrainBlockEntity;)V",
            require = 1, remap = false,
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/world/level/block/BucketPickup;pickupBlock(Lnet/minecraft/world/entity/LivingEntity;"
                            + "Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"))
    private static ItemStack fluidcompat$notePickup(BucketPickup pickup, LivingEntity user, LevelAccessor level,
            BlockPos pos, BlockState state) {
        ItemStack stack = pickup.pickupBlock(user, level, pos, state);
        EnergizedPowerBridge.notePickup(EnergizedPowerCompatModule.DRAIN_ID, stack, state.getFluidState().getType());
        return stack;
    }

    @ModifyArg(method = "tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;"
            + "Lnet/minecraft/world/level/block/state/BlockState;Lme/jddev0/ep/block/entity/DrainBlockEntity;)V",
            require = 1, remap = false, index = 1,
            at = @At(value = "INVOKE", ordinal = 0, remap = false,
                    target = "Lme/jddev0/ep/fluid/EnergizedPowerFluidStorage;insert(Ljava/lang/Object;J"
                            + "Lnet/fabricmc/fabric/api/transfer/v1/transaction/TransactionContext;)J"))
    private static long fluidcompat$capIntake(long amount) {
        return EnergizedPowerBridge.capIntake(EnergizedPowerCompatModule.DRAIN_ID, amount);
    }
}
