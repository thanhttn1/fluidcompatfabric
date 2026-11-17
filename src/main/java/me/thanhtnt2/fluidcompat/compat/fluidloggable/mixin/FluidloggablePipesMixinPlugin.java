package me.thanhtnt2.fluidcompat.compat.fluidloggable.mixin;

import me.thanhtnt2.fluidcompat.compat.GuardedCompatMixinPlugin;
import me.thanhtnt2.fluidcompat.compat.fluidloggable.FluidloggablePipesCompatModule;
import me.thanhtnt2.fluidcompat.compat.fluidloggable.guard.ConfiguredSupportShape;

public final class FluidloggablePipesMixinPlugin extends GuardedCompatMixinPlugin {

    public FluidloggablePipesMixinPlugin() {
        super(FluidloggablePipesCompatModule.HOOK_ID,
                "com.moigferdsrte.fluidloggable.block.ConfiguredFluidloggableBlockSupport",
                ConfiguredSupportShape::matches);
    }
}
