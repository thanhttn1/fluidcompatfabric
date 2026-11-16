package me.thanhtnt2.fluidcompat.compat.energizedpower.mixin;

import me.thanhtnt2.fluidcompat.compat.GuardedCompatMixinPlugin;
import me.thanhtnt2.fluidcompat.compat.energizedpower.EnergizedPowerCompatModule;
import me.thanhtnt2.fluidcompat.compat.energizedpower.guard.PumpShape;

public final class EnergizedPowerPumpMixinPlugin extends GuardedCompatMixinPlugin {

    public EnergizedPowerPumpMixinPlugin() {
        super(EnergizedPowerCompatModule.PUMP_ID, "me.jddev0.ep.block.entity.AbstractFluidPumpBlockEntity",
                PumpShape::matches);
    }
}
