package me.thanhtnt2.fluidcompat.config;

/** How Flowing Fluids should treat waterlogged blocks (pipes). Mirrors its own modes plus "leave it alone". */
public enum WaterlogFlowOverride {
    KEEP_FLOWING_FLUIDS_SETTING,
    ONLY_IN,
    ONLY_OUT,
    IN_FROM_TOP_ELSE_OUT,
    OUT_DOWN_ELSE_IN,
    IGNORE
}
