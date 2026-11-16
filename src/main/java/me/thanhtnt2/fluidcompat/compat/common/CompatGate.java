package me.thanhtnt2.fluidcompat.compat.common;

import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;

/** One place that answers "is general.compatEnabled on?" for every optional hook. */
public final class CompatGate {

    private CompatGate() {
    }

    public static boolean masterEnabled() {
        return FluidCompatModulesConfig.COMPAT_ENABLED.get();
    }
}
