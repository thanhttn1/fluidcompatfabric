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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fluid Pump and Advanced Fluid Pump. Three injectors that work as one:
 * a pickup on a thin layer returns a partial bucket, but the pump tanks a full one, so the tank gets only the
 * levels the world gave up; and the cobblestone is still used up but no block is placed, so the pump drains the
 * real water instead of plugging the cell.
 */
@Pseudo
@Mixin(targets = "me.jddev0.ep.block.entity.AbstractFluidPumpBlockEntity", remap = false)
public abstract class FluidPumpMixin {

    @Redirect(method = "onWorkCompleted(ILnet/minecraft/core/BlockPos;)V", require = 1, remap = false,
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/world/level/block/BucketPickup;pickupBlock(Lnet/minecraft/world/entity/LivingEntity;"
                            + "Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack fluidcompat$notePickup(BucketPickup pickup, LivingEntity user, LevelAccessor level, BlockPos pos,
            BlockState state) {
        ItemStack stack = pickup.pickupBlock(user, level, pos, state);
        EnergizedPowerBridge.notePickup(EnergizedPowerCompatModule.PUMP_ID, stack, state.getFluidState().getType());
        return stack;
    }

    @ModifyArg(method = "onWorkCompleted(ILnet/minecraft/core/BlockPos;)V", require = 1, remap = false, index = 1,
            at = @At(value = "INVOKE", ordinal = 0, remap = false,
                    target = "Lme/jddev0/ep/fluid/IEnergizedPowerFluidStorage;insert(Ljava/lang/Object;J"
                            + "Lnet/fabricmc/fabric/api/transfer/v1/transaction/TransactionContext;)J"))
    private long fluidcompat$capIntake(long amount) {
        return EnergizedPowerBridge.capIntake(EnergizedPowerCompatModule.PUMP_ID, amount);
    }

    @Redirect(method = "onWorkCompleted(ILnet/minecraft/core/BlockPos;)V", require = 1, remap = false,
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;"
                            + "Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean fluidcompat$placeCobblestone(Level level, BlockPos pos, BlockState cobblestone, int flags) {
        if (EnergizedPowerBridge.shouldSkipCobblestoneBlock(EnergizedPowerCompatModule.PUMP_ID)) {
            return true;
        }
        return level.setBlock(pos, cobblestone, flags);
    }
}
