package me.thanhtnt2.fluidcompat.compat.common;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

/** Finds pipe blocks by Java class name, so no pipe mod is needed at compile time. */
public final class PipeBlockMatcher {

    private static final String CREATE_CLASS_PREFIX = "com.simibubi.create.";

    private PipeBlockMatcher() {
    }

    /** True when the class, or one of its superclasses, is in the configured pipe class list. */
    public static boolean matchesClass(Class<?> type) {
        List<String> classNames = FluidCompatModulesConfig.FLUIDLOGGABLE_PIPES_BLOCK_CLASSES.get();
        if (classNames.isEmpty() || isHandledByCreate(type)) {
            return false;
        }
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            if (classNames.contains(current.getName())) {
                return true;
            }
        }
        return false;
    }

    /** Every registered pipe block. Only meaningful once all mods have registered their blocks. */
    public static List<Block> collectBlocks() {
        Set<Block> matched = new LinkedHashSet<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (matchesClass(block.getClass())) {
                matched.add(block);
            }
        }
        return List.copyOf(matched);
    }

    // Create runs its own fluid pipe logic and Flowing Fluids already has settings for it.
    private static boolean isHandledByCreate(Class<?> type) {
        return type.getName().startsWith(CREATE_CLASS_PREFIX);
    }
}
