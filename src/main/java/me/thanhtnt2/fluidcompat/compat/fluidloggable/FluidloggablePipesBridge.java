package me.thanhtnt2.fluidcompat.compat.fluidloggable;

import me.thanhtnt2.fluidcompat.compat.CompatMixinState;
import me.thanhtnt2.fluidcompat.compat.common.CompatGate;
import me.thanhtnt2.fluidcompat.compat.common.PipeBlockMatcher;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import net.minecraft.world.level.block.Block;

/**
 * Answers Fluidloggable's "is this block configured to hold fluid?" for pipe blocks. Runs when blocks are built and
 * on every fluid read of a fluidloggable block, so the answer per block class is cached.
 */
public final class FluidloggablePipesBridge {

    private static final ClassValue<Boolean> PIPE_CLASSES = new ClassValue<>() {
        @Override
        protected Boolean computeValue(Class<?> type) {
            return PipeBlockMatcher.matchesClass(type);
        }
    };

    private FluidloggablePipesBridge() {
    }

    public static boolean isPipe(String hookId, Block block) {
        try {
            // Blocks are built before the mod initialiser runs, so the settings may not be read yet.
            FluidCompatModulesConfig.ensureLoaded();
            if (!isActive() || !PIPE_CLASSES.get(block.getClass())) {
                return false;
            }
            CompatMixinState.markHookReached(hookId);
            return true;
        } catch (RuntimeException | LinkageError error) {
            return false;
        }
    }

    public static boolean isActive() {
        return CompatGate.masterEnabled()
                && FluidCompatModulesConfig.FLUIDLOGGABLE_PIPES_ENABLED.get()
                && FluidCompatModulesConfig.FLUIDLOGGABLE_ACCEPT_DUPLICATION_RISK.get();
    }
}
