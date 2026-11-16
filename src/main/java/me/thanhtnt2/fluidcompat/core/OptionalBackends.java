package me.thanhtnt2.fluidcompat.core;

import java.util.Optional;

import me.thanhtnt2.fluidcompat.api.DrainBackend;
import me.thanhtnt2.fluidcompat.core.backend.FlowingFluidsDrainBackend;
import net.fabricmc.loader.api.FabricLoader;

final class OptionalBackends {

    private static final String FLOWING_FLUIDS_MOD_ID = "flowing_fluids";

    private OptionalBackends() {
    }

    static Optional<DrainBackend> createFlowingFluidsBackend() {
        if (!FabricLoader.getInstance().isModLoaded(FLOWING_FLUIDS_MOD_ID)) {
            return Optional.empty();
        }
        return Optional.of(new FlowingFluidsDrainBackend());
    }
}
