package me.thanhtnt2.fluidcompat.compat.energizedpower.mixin;

import me.thanhtnt2.fluidcompat.compat.GuardedCompatMixinPlugin;
import me.thanhtnt2.fluidcompat.compat.energizedpower.EnergizedPowerCompatModule;
import me.thanhtnt2.fluidcompat.compat.energizedpower.guard.DrainShape;

public final class EnergizedPowerDrainMixinPlugin extends GuardedCompatMixinPlugin {

    public EnergizedPowerDrainMixinPlugin() {
        super(EnergizedPowerCompatModule.DRAIN_ID, "me.jddev0.ep.block.entity.DrainBlockEntity", DrainShape::matches);
    }
}
