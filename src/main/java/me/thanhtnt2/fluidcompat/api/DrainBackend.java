package me.thanhtnt2.fluidcompat.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;

/**
 * Knows how to physically remove fluid from the world. Register through {@link DrainBackendRegistry};
 * the supporting backend with the highest priority wins.
 */
public interface DrainBackend {

    Identifier id();

    /** Higher wins. The vanilla fallback uses 0, the Flowing Fluids backend 1000. */
    int priority();

    boolean supports(ServerLevel level, Fluid fluid);

    /**
     * Removes up to {@code maxLevels} fluid levels around {@code sourcePos}.
     *
     * @return the number of levels actually removed, between 0 and {@code maxLevels}
     */
    int drain(ServerLevel level, BlockPos sourcePos, Fluid fluid, int maxLevels);
}
