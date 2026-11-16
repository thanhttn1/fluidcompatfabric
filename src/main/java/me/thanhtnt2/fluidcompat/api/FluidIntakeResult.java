package me.thanhtnt2.fluidcompat.api;

/**
 * Outcome of an intake request.
 *
 * @param managed false when the fluid is not finite and the machine should keep its original behaviour
 */
public record FluidIntakeResult(int requestedLevels, int drainedLevels, boolean managed) {

    public static FluidIntakeResult unmanaged(int requestedLevels) {
        return new FluidIntakeResult(requestedLevels, requestedLevels, false);
    }

    public boolean isFullyServed() {
        return drainedLevels >= requestedLevels;
    }
}
