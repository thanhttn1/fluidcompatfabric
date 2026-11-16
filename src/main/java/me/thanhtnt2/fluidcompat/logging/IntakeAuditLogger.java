package me.thanhtnt2.fluidcompat.logging;

import me.thanhtnt2.fluidcompat.api.FluidIntakeRequest;
import me.thanhtnt2.fluidcompat.api.FluidIntakeResult;
import net.minecraft.core.registries.BuiltInRegistries;

/** Writes one throttled line per machine for every managed intake, only while detail logging is on. */
public final class IntakeAuditLogger {

    private IntakeAuditLogger() {
    }

    public static void onIntake(FluidIntakeRequest request, FluidIntakeResult result) {
        if (!FluidCompatLog.isDetailLoggingActive()) {
            return;
        }
        String machine = request.machineId() + "@" + request.machinePos().toShortString();
        FluidCompatLog.debugThrottled(machine,
                "Intake machine={} fluid={} requestedLevels={} drainedLevels={} sources={} fullyServed={}",
                machine, BuiltInRegistries.FLUID.getKey(request.fluid()), result.requestedLevels(),
                result.drainedLevels(), request.sourcePositions().size(), result.isFullyServed());
    }
}
