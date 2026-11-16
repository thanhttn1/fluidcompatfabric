package me.thanhtnt2.fluidcompat.api;

import net.minecraft.resources.Identifier;

public final class DrainBackendIds {

    public static final Identifier VANILLA = Identifier.fromNamespaceAndPath(FluidCompatApi.MOD_ID, "vanilla");
    public static final Identifier FLOWING_FLUIDS =
            Identifier.fromNamespaceAndPath(FluidCompatApi.MOD_ID, "flowing_fluids");

    private DrainBackendIds() {
    }
}
