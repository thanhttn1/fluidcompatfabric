package me.thanhtnt2.fluidcompat.compat.fluidloggable;

import me.thanhtnt2.fluidcompat.api.compat.CompatModule;
import me.thanhtnt2.fluidcompat.compat.CompatMixinState;
import me.thanhtnt2.fluidcompat.compat.common.CompatGate;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

/** Lets the pipe blocks of other mods hold fluid through Fluidloggable. Off until the duplication risk is accepted. */
public final class FluidloggablePipesCompatModule implements CompatModule {

    public static final String ID = "fluidloggable_pipes";
    public static final String HOOK_ID = "fluidloggable_pipes";

    private static final String SUPPORT_CLASS = "com.moigferdsrte.fluidloggable.block.ConfiguredFluidloggableBlockSupport";
    private static final String CONFIG_FILE = "fluidcompat.compat.fluidloggable.pipes.mixins.json";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String modId() {
        return "fluidloggable";
    }

    @Override
    public String displayName() {
        return "Fluidloggable (pipes)";
    }

    @Override
    public void initialize() {
        if (!FluidCompatModulesConfig.FLUIDLOGGABLE_PIPES_ENABLED.get()) {
            FluidCompatLog.info("[{}] disabled in config", ID);
            return;
        }
        if (!FluidCompatModulesConfig.FLUIDLOGGABLE_ACCEPT_DUPLICATION_RISK.get()) {
            FluidCompatLog.info("[{}] not active: fluidloggable_pipes.acceptDuplicationRisk is false. Fluidloggable keeps "
                    + "its own copy of the fluid in each chunk section, next to the block state; Fluidlogged did the same "
                    + "and duplicated water together with Flowing Fluids", ID);
            return;
        }
        reportVerdict();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> reportCoverage());
    }

    private static void reportVerdict() {
        loadSupportClass();
        if (!CompatMixinState.hasVerdict(HOOK_ID)) {
            FluidCompatLog.warn("[{}] hook never got a verdict: its mixin config was not loaded (check {})", HOOK_ID,
                    CONFIG_FILE);
            return;
        }
        if (CompatMixinState.isApplied(HOOK_ID)) {
            FluidCompatLog.info("[{}] hook allowed: the code has the expected shape. It logs 'hook reached' when a "
                    + "pipe block is first covered", HOOK_ID);
            return;
        }
        FluidCompatLog.warn("[{}] hook is NOT allowed (Fluidloggable's block selection is not what it expects); pipes "
                + "stay as they are", HOOK_ID);
    }

    // Loading the class makes Mixin ask the plugin, so the verdict exists before it is read.
    private static void loadSupportClass() {
        try {
            Class.forName(SUPPORT_CLASS, false, FluidloggablePipesCompatModule.class.getClassLoader());
        } catch (ClassNotFoundException | LinkageError ignored) {
        }
    }

    private static void reportCoverage() {
        if (!CompatGate.masterEnabled()) {
            return;
        }
        if (CompatMixinState.hasVerdict(HOOK_ID) && CompatMixinState.wasHookReached(HOOK_ID)) {
            FluidCompatLog.info("[{}] hook reached: at least one pipe block was made fluidloggable", HOOK_ID);
        } else {
            FluidCompatLog.info("[{}] no pipe block was covered (no installed block matches fluidloggable_pipes.blockClasses)",
                    HOOK_ID);
        }
    }
}
