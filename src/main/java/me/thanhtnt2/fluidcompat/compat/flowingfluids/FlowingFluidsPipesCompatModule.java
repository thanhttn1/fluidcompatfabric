package me.thanhtnt2.fluidcompat.compat.flowingfluids;

import me.thanhtnt2.fluidcompat.api.compat.CompatModule;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

/** Teaches Flowing Fluids that pipe blocks are special: they never push fluid out of their cell. */
public final class FlowingFluidsPipesCompatModule implements CompatModule {

    public static final String ID = "flowing_fluids_pipes";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String modId() {
        return "flowing_fluids";
    }

    @Override
    public String displayName() {
        return "Flowing Fluids (submerged pipes)";
    }

    @Override
    public void initialize() {
        if (!FluidCompatModulesConfig.FF_PIPES_ENABLED.get()) {
            FluidCompatLog.info("[flowing_fluids_pipes] disabled in config");
            return;
        }
        // Blocks of other mods are only all registered once a world starts.
        ServerLifecycleEvents.SERVER_STARTING.register(server -> FlowingFluidsPipeRules.registerNonDisplacers());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> FlowingFluidsPipeRules.applyWaterlogFlowMode());
    }
}
