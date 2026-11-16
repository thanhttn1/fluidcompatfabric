package me.thanhtnt2.fluidcompat.compat.energizedpower;

/** The accounting rule, kept free of Minecraft types so it can be tested on its own. */
public final class PartialBucketRule {

    public static final int NOT_PARTIAL = -1;

    private PartialBucketRule() {
    }

    /**
     * @param amount            what the machine is about to put in its tank, in droplets
     * @param levelsHeld        levels the bucket really holds, or {@link #NOT_PARTIAL}
     * @param dropletsPerLevel  droplets in one level (1/8 bucket)
     */
    public static long cap(long amount, int levelsHeld, long dropletsPerLevel) {
        if (levelsHeld == NOT_PARTIAL) {
            return amount;
        }
        return Math.min(amount, levelsHeld * dropletsPerLevel);
    }
}
