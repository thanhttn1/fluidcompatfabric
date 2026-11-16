package me.thanhtnt2.fluidcompat.compat.energizedpower;

import me.thanhtnt2.fluidcompat.api.FluidCompatApi;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.world.item.ItemStack;

/**
 * Flowing Fluids answers a bucket pickup on a thin layer with a bucket whose durability bar shows how empty it
 * is: max damage 8, damage 8 minus the levels it holds. Fabric's item fluid storage ignores that bar.
 */
final class PartialBucket {

    static final long DROPLETS_PER_LEVEL = FluidConstants.BUCKET / FluidCompatApi.LEVELS_PER_BUCKET;

    private PartialBucket() {
    }

    static int levelsHeld(ItemStack stack) {
        if (stack.isEmpty() || stack.getMaxDamage() != FluidCompatApi.LEVELS_PER_BUCKET) {
            return PartialBucketRule.NOT_PARTIAL;
        }
        int damage = stack.getDamageValue();
        if (damage <= 0 || damage >= FluidCompatApi.LEVELS_PER_BUCKET) {
            return PartialBucketRule.NOT_PARTIAL;
        }
        return FluidCompatApi.LEVELS_PER_BUCKET - damage;
    }
}
