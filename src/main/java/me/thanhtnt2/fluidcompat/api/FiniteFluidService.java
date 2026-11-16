package me.thanhtnt2.fluidcompat.api;

import java.util.Optional;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;

/**
 * Mediates between a fluid-extracting machine and the world. Server thread only.
 */
public interface FiniteFluidService {

    /** Whether the fluid is currently treated as a finite resource by the active configuration. */
    boolean isFinite(Fluid fluid);

    /**
     * Resolves an intake request. If the fluid is not finite the result is
     * {@linkplain FluidIntakeResult#managed() unmanaged} and the machine keeps its original behaviour.
     */
    FluidIntakeResult requestIntake(FluidIntakeRequest request);

    /** The backend that would currently handle the fluid, if any. Useful for diagnostics. */
    Optional<DrainBackend> activeBackendFor(ServerLevel level, Fluid fluid);
}
