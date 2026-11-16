package me.thanhtnt2.fluidcompat.compat.energizedpower.guard;

/** Names shared by the pump and drain checks. */
final class WorldFluidCalls {

    static final String BUCKET_PICKUP = "net/minecraft/world/level/block/BucketPickup";
    static final String PICKUP_BLOCK = "pickupBlock";
    static final String PICKUP_BLOCK_DESC = "(Lnet/minecraft/world/entity/LivingEntity;"
            + "Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;"
            + "Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;";

    static final String STORAGE_VIEW = "net/fabricmc/fabric/api/transfer/v1/storage/StorageView";
    static final String GET_AMOUNT = "getAmount";
    static final String GET_AMOUNT_DESC = "()J";

    static final String INSERT = "insert";
    static final String INSERT_DESC =
            "(Ljava/lang/Object;JLnet/fabricmc/fabric/api/transfer/v1/transaction/TransactionContext;)J";

    static final String LEVEL = "net/minecraft/world/level/Level";
    static final String SET_BLOCK = "setBlock";
    static final String SET_BLOCK_DESC =
            "(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z";

    static final String BLOCKS = "net/minecraft/world/level/block/Blocks";
    static final String COBBLESTONE_FIELD = BytecodeCalls.FIELD_PREFIX + "COBBLESTONE";
    static final String BLOCK_DESC = "Lnet/minecraft/world/level/block/Block;";

    static final String ITEM_HANDLER = "me/jddev0/ep/inventory/IEnergizedPowerItemStackHandler";
    static final String EXTRACT_ITEM = "extractItem";
    static final String EXTRACT_ITEM_DESC = "(II)Lnet/minecraft/world/item/ItemStack;";

    private WorldFluidCalls() {
    }

    /** True when the method gives the world no other way to lose a block that the hook would not see. */
    static boolean noOtherWorldRemoval(BytecodeCalls calls) {
        return calls.countNamed("removeBlock") == 0 && calls.countNamed("destroyBlock") == 0;
    }
}
