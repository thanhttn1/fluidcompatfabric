package me.thanhtnt2.fluidcompat.core;

import java.util.Optional;

import me.thanhtnt2.fluidcompat.api.DrainBackend;
import me.thanhtnt2.fluidcompat.api.DrainBackendRegistry;
import me.thanhtnt2.fluidcompat.api.FiniteFluidService;
import me.thanhtnt2.fluidcompat.api.FluidCompatTags;
import me.thanhtnt2.fluidcompat.api.FluidIntakeRequest;
import me.thanhtnt2.fluidcompat.api.FluidIntakeResult;
import me.thanhtnt2.fluidcompat.api.event.FluidIntakeEvents;
import me.thanhtnt2.fluidcompat.config.BackendPreference;
import me.thanhtnt2.fluidcompat.config.FluidCompatConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public final class FiniteFluidServiceImpl implements FiniteFluidService {

    @Override
    public boolean isFinite(Fluid fluid) {
        if (!FluidCompatConfig.ENABLE_FINITE_FLUIDS.get()) {
            return false;
        }
        FluidState state = fluid.defaultFluidState();
        if (state.is(FluidTags.WATER)) {
            return FluidCompatConfig.FINITE_WATER.get();
        }
        if (state.is(FluidTags.LAVA)) {
            return FluidCompatConfig.FINITE_LAVA.get();
        }
        return state.is(FluidCompatTags.MANAGED_FLUIDS) && FluidCompatConfig.FINITE_TAGGED_FLUIDS.get();
    }

    @Override
    public FluidIntakeResult requestIntake(FluidIntakeRequest request) {
        if (!isFinite(request.fluid())) {
            return FluidIntakeResult.unmanaged(request.requestedLevels());
        }
        FluidIntakeEvents.PreIntake intake = new FluidIntakeEvents.PreIntake(request.requestedLevels());
        FluidIntakeEvents.PRE.invoker().onPre(request, intake);
        if (intake.isCanceled()) {
            return FluidIntakeResult.unmanaged(request.requestedLevels());
        }
        Optional<DrainBackend> backend = activeBackendFor(request.level(), request.fluid());
        if (backend.isEmpty()) {
            return FluidIntakeResult.unmanaged(request.requestedLevels());
        }
        int levelsToDrain = intake.getRequestedLevels();
        int drainedLevels = drainAcrossSources(backend.get(), request, levelsToDrain);
        FluidIntakeResult result = new FluidIntakeResult(levelsToDrain, drainedLevels, true);
        FluidIntakeEvents.POST.invoker().onPost(request, result);
        return result;
    }

    @Override
    public Optional<DrainBackend> activeBackendFor(ServerLevel level, Fluid fluid) {
        BackendPreference preference = FluidCompatConfig.BACKEND_PREFERENCE.get();
        for (DrainBackend backend : DrainBackendRegistry.snapshot()) {
            if (preference.accepts(backend.id()) && backend.supports(level, fluid)) {
                return Optional.of(backend);
            }
        }
        return Optional.empty();
    }

    // The clamp keeps a misbehaving backend from creating fluid: it can never report more than was asked.
    private int drainAcrossSources(DrainBackend backend, FluidIntakeRequest request, int levelsToDrain) {
        int remaining = levelsToDrain;
        for (BlockPos sourcePos : request.sourcePositions()) {
            if (remaining <= 0) {
                break;
            }
            int removed = backend.drain(request.level(), sourcePos, request.fluid(), remaining);
            remaining -= Math.max(0, Math.min(removed, remaining));
        }
        return levelsToDrain - remaining;
    }
}
