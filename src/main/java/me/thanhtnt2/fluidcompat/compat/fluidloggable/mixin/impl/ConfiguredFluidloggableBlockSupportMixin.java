package me.thanhtnt2.fluidcompat.compat.fluidloggable.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.thanhtnt2.fluidcompat.compat.fluidloggable.FluidloggablePipesBridge;
import me.thanhtnt2.fluidcompat.compat.fluidloggable.FluidloggablePipesCompatModule;
import net.minecraft.world.level.block.Block;

/** Adds the configured pipe blocks to the blocks Fluidloggable lets hold fluid. */
@Pseudo
@Mixin(targets = "com.moigferdsrte.fluidloggable.block.ConfiguredFluidloggableBlockSupport", remap = false)
public abstract class ConfiguredFluidloggableBlockSupportMixin {

    @Inject(method = "matchesConfiguration(Lnet/minecraft/world/level/block/Block;)Z", require = 1, remap = false,
            at = @At("RETURN"), cancellable = true)
    private static void fluidcompat$coverPipes(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && FluidloggablePipesBridge.isPipe(FluidloggablePipesCompatModule.HOOK_ID, block)) {
            cir.setReturnValue(true);
        }
    }
}
