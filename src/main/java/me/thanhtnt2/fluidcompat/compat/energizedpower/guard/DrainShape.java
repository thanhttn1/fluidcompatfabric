package me.thanhtnt2.fluidcompat.compat.energizedpower.guard;

import static me.thanhtnt2.fluidcompat.compat.energizedpower.guard.WorldFluidCalls.*;

import org.objectweb.asm.tree.ClassNode;

/**
 * Energized Power's Drain tick: one bucket pickup, one read of the bucket's amount, and two inserts into its
 * tank (first the bucket, then the cauldron branch). The hook only touches the first.
 */
public final class DrainShape {

    public static final String METHOD = "tick";
    public static final String METHOD_DESC = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;"
            + "Lnet/minecraft/world/level/block/state/BlockState;Lme/jddev0/ep/block/entity/DrainBlockEntity;)V";
    public static final String TANK = "me/jddev0/ep/fluid/EnergizedPowerFluidStorage";

    private DrainShape() {
    }

    public static boolean matches(ClassNode drainClass) {
        BytecodeCalls calls = BytecodeCalls.of(drainClass, METHOD, METHOD_DESC);
        if (calls == null) {
            return false;
        }
        if (calls.count(BUCKET_PICKUP, PICKUP_BLOCK, PICKUP_BLOCK_DESC) != 1
                || calls.count(STORAGE_VIEW, GET_AMOUNT, GET_AMOUNT_DESC) != 1
                || calls.count(TANK, INSERT, INSERT_DESC) != 2) {
            return false;
        }
        int pickup = calls.indexOf(BUCKET_PICKUP, PICKUP_BLOCK, PICKUP_BLOCK_DESC, 0);
        int amount = calls.indexOf(STORAGE_VIEW, GET_AMOUNT, GET_AMOUNT_DESC, 0);
        int firstInsert = calls.indexOf(TANK, INSERT, INSERT_DESC, 0);
        return pickup < amount && amount < firstInsert && noOtherWorldRemoval(calls);
    }
}
