package me.thanhtnt2.fluidcompat.compat.energizedpower;

import me.thanhtnt2.fluidcompat.api.FluidCompatApi;
import me.thanhtnt2.fluidcompat.compat.CompatMixinState;
import me.thanhtnt2.fluidcompat.compat.common.CompatGate;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

/** The only entry point of the Energized Power mixins. Fails open: any problem leaves the machine's own behaviour. */
public final class EnergizedPowerBridge {

    private EnergizedPowerBridge() {
    }

    public static void notePickup(String hookId, ItemStack stack, Fluid fluid) {
        try {
            reportReached(hookId);
            if (!isActive()) {
                PickupLedger.clear();
                return;
            }
            PickupLedger.note(PartialBucket.levelsHeld(stack), isFinite(fluid));
        } catch (RuntimeException | LinkageError error) {
            PickupLedger.clear();
            FluidCompatLog.errorThrottled(hookId + ".pickup", "[" + hookId + "] pickup hook failed", error);
        }
    }

    public static long capIntake(String hookId, long amount) {
        int levelsHeld = PickupLedger.takeLevels();
        if (levelsHeld == PartialBucketRule.NOT_PARTIAL) {
            return amount;
        }
        try {
            long capped = PartialBucketRule.cap(amount, levelsHeld, PartialBucket.DROPLETS_PER_LEVEL);
            if (capped < amount) {
                reportCapped(hookId, amount, capped, levelsHeld);
            }
            return capped;
        } catch (RuntimeException | LinkageError error) {
            FluidCompatLog.errorThrottled(hookId + ".cap", "[" + hookId + "] intake hook failed", error);
            return amount;
        }
    }

    /**
     * Whether the pump should skip placing its cobblestone block. The cobblestone item is already taken from the
     * slot by then, so skipping the block only stops the cell from being plugged: the pump keeps draining real water.
     */
    public static boolean shouldSkipCobblestoneBlock(String hookId) {
        boolean finite = PickupLedger.takeFinite();
        try {
            if (!finite || !isActive() || FluidCompatModulesConfig.ENERGIZED_POWER_PLACE_COBBLESTONE.get()) {
                return false;
            }
            reportFirstSkip(hookId);
            return true;
        } catch (RuntimeException | LinkageError error) {
            FluidCompatLog.errorThrottled(hookId + ".cobble", "[" + hookId + "] cobblestone hook failed", error);
            return false;
        }
    }

    private static boolean isFinite(Fluid fluid) {
        return FluidCompatApi.isReady() && FluidCompatApi.service().isFinite(fluid);
    }

    private static boolean isActive() {
        return CompatGate.masterEnabled() && FluidCompatModulesConfig.ENERGIZED_POWER_ENABLED.get();
    }

    private static void reportReached(String hookId) {
        if (CompatMixinState.markHookReached(hookId)) {
            FluidCompatLog.info("[{}] hook reached for the first time", hookId);
        }
    }

    private static void reportCapped(String hookId, long amount, long capped, int levelsHeld) {
        if (CompatMixinState.markHookReached(hookId + ".capped")) {
            FluidCompatLog.info("[{}] capped a partial bucket for the first time: {} -> {} droplets ({} levels)",
                    hookId, amount, capped, levelsHeld);
        }
        FluidCompatLog.debug("[{}] partial bucket capped {} -> {} droplets", hookId, amount, capped);
    }

    private static void reportFirstSkip(String hookId) {
        if (CompatMixinState.markHookReached(hookId + ".cobblestone")) {
            FluidCompatLog.info("[{}] cobblestone used up without placing a block for the first time", hookId);
        }
    }
}
