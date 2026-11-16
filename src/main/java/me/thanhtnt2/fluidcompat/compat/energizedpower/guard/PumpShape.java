package me.thanhtnt2.fluidcompat.compat.energizedpower.guard;

import static me.thanhtnt2.fluidcompat.compat.energizedpower.guard.WorldFluidCalls.*;

import org.objectweb.asm.tree.ClassNode;

/**
 * Energized Power's pump work step: one bucket pickup, one read of the bucket's amount, one insert into the
 * pump's tank, then the cobblestone is taken from the slot and placed (the one setBlock). If that order changes,
 * the hooks stay off.
 */
public final class PumpShape {

    public static final String METHOD = "onWorkCompleted";
    public static final String METHOD_DESC = "(ILnet/minecraft/core/BlockPos;)V";
    public static final String TANK = "me/jddev0/ep/fluid/IEnergizedPowerFluidStorage";

    private PumpShape() {
    }

    public static boolean matches(ClassNode pumpClass) {
        BytecodeCalls calls = BytecodeCalls.of(pumpClass, METHOD, METHOD_DESC);
        if (calls == null) {
            return false;
        }
        if (calls.count(BUCKET_PICKUP, PICKUP_BLOCK, PICKUP_BLOCK_DESC) != 1
                || calls.count(STORAGE_VIEW, GET_AMOUNT, GET_AMOUNT_DESC) != 1
                || calls.count(TANK, INSERT, INSERT_DESC) != 1) {
            return false;
        }
        if (calls.count(ITEM_HANDLER, EXTRACT_ITEM, EXTRACT_ITEM_DESC) != 1
                || calls.count(BLOCKS, COBBLESTONE_FIELD, BLOCK_DESC) != 1
                || calls.count(LEVEL, SET_BLOCK, SET_BLOCK_DESC) != 1) {
            return false;
        }
        int pickup = calls.indexOf(BUCKET_PICKUP, PICKUP_BLOCK, PICKUP_BLOCK_DESC, 0);
        int amount = calls.indexOf(STORAGE_VIEW, GET_AMOUNT, GET_AMOUNT_DESC, 0);
        int insert = calls.indexOf(TANK, INSERT, INSERT_DESC, 0);
        int extract = calls.indexOf(ITEM_HANDLER, EXTRACT_ITEM, EXTRACT_ITEM_DESC, 0);
        int cobblestone = calls.indexOf(BLOCKS, COBBLESTONE_FIELD, BLOCK_DESC, 0);
        int setBlock = calls.indexOf(LEVEL, SET_BLOCK, SET_BLOCK_DESC, 0);
        boolean inOrder = pickup < amount && amount < insert && insert < extract && extract < cobblestone
                && cobblestone < setBlock;
        return inOrder && noOtherWorldRemoval(calls);
    }
}
