package me.thanhtnt2.fluidcompat.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public final class FluidCompatTags {

    /** Fluids that count as finite besides water and lava. */
    public static final TagKey<Fluid> MANAGED_FLUIDS = TagKey.create(Registries.FLUID, id("managed_fluids"));

    /** Blocks that never push fluid out of their cell (pipes, conduits). */
    public static final TagKey<Block> FLUID_PASSABLE_BLOCKS =
            TagKey.create(Registries.BLOCK, id("fluid_passable_blocks"));

    private FluidCompatTags() {
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(FluidCompatApi.MOD_ID, path);
    }
}
