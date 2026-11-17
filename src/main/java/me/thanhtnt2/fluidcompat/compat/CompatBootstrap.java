package me.thanhtnt2.fluidcompat.compat;

import java.util.List;

import me.thanhtnt2.fluidcompat.api.compat.CompatModule;
import me.thanhtnt2.fluidcompat.api.compat.CompatModuleRegistry;
import me.thanhtnt2.fluidcompat.api.compat.CompatStatus;
import me.thanhtnt2.fluidcompat.api.event.FluidIntakeEvents;
import me.thanhtnt2.fluidcompat.compat.energizedpower.EnergizedPowerCompatModule;
import me.thanhtnt2.fluidcompat.compat.flowingfluids.FlowingFluidsPipesCompatModule;
import me.thanhtnt2.fluidcompat.compat.fluidloggable.FluidloggablePipesCompatModule;
import me.thanhtnt2.fluidcompat.compat.info.InfoCompatModule;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;
import me.thanhtnt2.fluidcompat.logging.IntakeAuditLogger;
import net.fabricmc.loader.api.FabricLoader;

public final class CompatBootstrap {

    private static final List<String> MODS_TO_REPORT = List.of("fabric-api", "flowing_fluids", "energizedpower", "fluidloggable");

    private CompatBootstrap() {
    }

    public static void registerBuiltInModules() {
        CompatModuleRegistry.register(new InfoCompatModule("flowing_fluids", "flowing_fluids", "Flowing Fluids (drain backend)"));
        CompatModuleRegistry.register(new EnergizedPowerCompatModule());
        CompatModuleRegistry.register(new FlowingFluidsPipesCompatModule());
        CompatModuleRegistry.register(new FluidloggablePipesCompatModule());
    }

    public static void initialize() {
        reportEnvironment();
        for (CompatModule module : CompatModuleRegistry.snapshot()) {
            activate(module);
        }
        FluidIntakeEvents.POST.register(IntakeAuditLogger::onIntake);
        FluidCompatLog.info("Compat setup finished: {}", summary());
    }

    private static void activate(CompatModule module) {
        if (!FabricLoader.getInstance().isModLoaded(module.modId())) {
            CompatModuleRegistry.updateStatus(module.id(), CompatStatus.MISSING_MOD);
            return;
        }
        if (!FluidCompatModulesConfig.COMPAT_ENABLED.get()) {
            CompatModuleRegistry.updateStatus(module.id(), CompatStatus.DISABLED);
            return;
        }
        try {
            module.initialize();
            CompatModuleRegistry.updateStatus(module.id(), CompatStatus.ACTIVE);
        } catch (RuntimeException | LinkageError error) {
            CompatModuleRegistry.updateStatus(module.id(), CompatStatus.FAILED);
            FluidCompatLog.error("Compat module '" + module.id() + "' failed to initialise", error);
        }
    }

    private static void reportEnvironment() {
        StringBuilder mods = new StringBuilder();
        for (String modId : MODS_TO_REPORT) {
            FabricLoader.getInstance().getModContainer(modId).ifPresent(container ->
                    mods.append(modId).append('=').append(container.getMetadata().getVersion().getFriendlyString()).append(' '));
        }
        FluidCompatLog.info("Relevant mods: {}", mods.toString().trim());
    }

    private static String summary() {
        StringBuilder text = new StringBuilder();
        for (CompatModule module : CompatModuleRegistry.snapshot()) {
            text.append(module.id()).append('=').append(CompatModuleRegistry.statusOf(module.id())).append(' ');
        }
        return text.toString().trim();
    }
}
