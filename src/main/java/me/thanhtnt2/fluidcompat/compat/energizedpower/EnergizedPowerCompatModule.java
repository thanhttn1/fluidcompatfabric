package me.thanhtnt2.fluidcompat.compat.energizedpower;

import me.thanhtnt2.fluidcompat.api.compat.CompatModule;
import me.thanhtnt2.fluidcompat.compat.CompatMixinState;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;

public final class EnergizedPowerCompatModule implements CompatModule {

    public static final String ID = "energized_power";
    public static final String PUMP_ID = "energized_power_pump";
    public static final String DRAIN_ID = "energized_power_drain";

    private static final String PUMP_CLASS = "me.jddev0.ep.block.entity.AbstractFluidPumpBlockEntity";
    private static final String DRAIN_CLASS = "me.jddev0.ep.block.entity.DrainBlockEntity";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String modId() {
        return "energizedpower";
    }

    @Override
    public String displayName() {
        return "Energized Power (Fluid Pump, Advanced Fluid Pump, Drain)";
    }

    @Override
    public void initialize() {
        loadClass(PUMP_CLASS);
        loadClass(DRAIN_CLASS);
        report(PUMP_ID, "Fluid Pump and Advanced Fluid Pump", "fluidcompat.compat.energizedpower.pump.mixins.json");
        report(DRAIN_ID, "Drain", "fluidcompat.compat.energizedpower.drain.mixins.json");
    }

    // Loading the target class makes Mixin ask the plugin, so the verdict exists before it is read below.
    private static void loadClass(String className) {
        try {
            Class.forName(className, false, EnergizedPowerCompatModule.class.getClassLoader());
        } catch (ClassNotFoundException | LinkageError ignored) {
        }
    }

    private static void report(String verdictId, String machine, String configFile) {
        if (!CompatMixinState.hasVerdict(verdictId)) {
            FluidCompatLog.warn("[{}] {} hook never got a verdict: its mixin config was not loaded (check {})",
                    verdictId, machine, configFile);
            return;
        }
        if (CompatMixinState.isApplied(verdictId)) {
            FluidCompatLog.info("[{}] {} hook allowed: the code has the expected shape. The first use logs "
                    + "'hook reached' if Mixin really injected it", verdictId, machine);
            return;
        }
        FluidCompatLog.warn("[{}] {} hook is NOT allowed (the code is not what it expects); the machine keeps its "
                + "original behaviour", verdictId, machine);
    }
}
