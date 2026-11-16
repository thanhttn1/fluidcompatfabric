package me.thanhtnt2.fluidcompat.compat.energizedpower;

/**
 * Carries what the last bucket pickup learned to the calls that follow it in the same method: the levels the
 * bucket holds (for the tank insert) and whether the fluid is finite (for the cobblestone block).
 * Server thread only; every pickup overwrites both and every use clears its own value.
 */
final class PickupLedger {

    private static int levelsHeld = PartialBucketRule.NOT_PARTIAL;
    private static boolean finiteFluid;

    private PickupLedger() {
    }

    static void note(int levels, boolean finite) {
        levelsHeld = levels;
        finiteFluid = finite;
    }

    static int takeLevels() {
        int levels = levelsHeld;
        levelsHeld = PartialBucketRule.NOT_PARTIAL;
        return levels;
    }

    static boolean takeFinite() {
        boolean finite = finiteFluid;
        finiteFluid = false;
        return finite;
    }

    static void clear() {
        note(PartialBucketRule.NOT_PARTIAL, false);
    }
}
