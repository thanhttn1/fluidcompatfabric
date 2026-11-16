package me.thanhtnt2.fluidcompat.api;

import java.util.List;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;

/**
 * What a machine wants to take from the world.
 *
 * @param machineId        id of the machine, used for logs and events
 * @param requestedLevels  fluid levels (1/8 bucket each) the machine wants; 0 is allowed and drains nothing
 * @param sourcePositions  cells to try, in order
 */
public record FluidIntakeRequest(
        ServerLevel level,
        BlockPos machinePos,
        Identifier machineId,
        Fluid fluid,
        int requestedLevels,
        List<BlockPos> sourcePositions) {

    public FluidIntakeRequest {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(machinePos, "machinePos");
        Objects.requireNonNull(machineId, "machineId");
        Objects.requireNonNull(fluid, "fluid");
        if (requestedLevels < 0) {
            throw new IllegalArgumentException("requestedLevels must not be negative");
        }
        machinePos = machinePos.immutable();
        sourcePositions = List.copyOf(sourcePositions);
    }
}
