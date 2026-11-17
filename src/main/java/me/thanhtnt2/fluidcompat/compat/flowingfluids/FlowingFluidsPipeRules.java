package me.thanhtnt2.fluidcompat.compat.flowingfluids;

import java.util.List;

import me.thanhtnt2.fluidcompat.api.FluidCompatApi;
import me.thanhtnt2.fluidcompat.api.FluidCompatTags;
import me.thanhtnt2.fluidcompat.compat.common.CompatGate;
import me.thanhtnt2.fluidcompat.compat.common.PipeBlockMatcher;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.config.WaterlogFlowOverride;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import traben.flowing_fluids.api.FlowingFluidsAPI;
import traben.flowing_fluids.config.FFConfig;

/** The two things Fluid Compat changes in Flowing Fluids for pipes. Fails open: an error only logs. */
final class FlowingFluidsPipeRules {

    private static final List<Fluid> DISPLACEMENT_FLUIDS =
            List.of(Fluids.WATER, Fluids.FLOWING_WATER, Fluids.LAVA, Fluids.FLOWING_LAVA);

    private static boolean nonDisplacersRegistered;

    private FlowingFluidsPipeRules() {
    }

    static synchronized void registerNonDisplacers() {
        if (!CompatGate.masterEnabled() || !FluidCompatModulesConfig.FF_PIPES_NON_DISPLACERS.get()
                || nonDisplacersRegistered) {
            return;
        }
        try {
            List<Block> pipes = PipeBlockMatcher.collectBlocks();
            FluidCompatLog.info("[flowing_fluids_pipes] {} pipe block(s) matched", pipes.size());
            logMatchedBlocks(pipes);
            FlowingFluidsAPI api = FlowingFluidsAPI.getInstance(FluidCompatApi.MOD_ID);
            for (Fluid fluid : DISPLACEMENT_FLUIDS) {
                for (Block block : pipes) {
                    api.registerBlockThatWontDisplaceFluid(fluid, block);
                }
                api.registerBlockTagThatWontDisplaceFluid(fluid, FluidCompatTags.FLUID_PASSABLE_BLOCKS);
            }
            nonDisplacersRegistered = true;
            FluidCompatLog.info("[flowing_fluids_pipes] registered {} block(s) and the tag {} as non-displacers",
                    pipes.size(), FluidCompatTags.FLUID_PASSABLE_BLOCKS.location());
        } catch (RuntimeException | LinkageError error) {
            FluidCompatLog.error("Could not register the pipe blocks with Flowing Fluids", error);
        }
    }

    static void applyWaterlogFlowMode() {
        if (!CompatGate.masterEnabled()) {
            return;
        }
        try {
            WaterlogFlowOverride requested = safeRequestedMode();
            FFConfig config = FlowingFluidsAPI.getInstance(FluidCompatApi.MOD_ID).getConfig();
            FFConfig.WaterLogFlowMode current = config.waterLogFlowMode;
            if (requested == WaterlogFlowOverride.KEEP_FLOWING_FLUIDS_SETTING) {
                FluidCompatLog.info("[flowing_fluids_pipes] leaving the Flowing Fluids waterlog flow mode at {}", current);
                return;
            }
            FFConfig.WaterLogFlowMode target = FFConfig.WaterLogFlowMode.valueOf(requested.name());
            if (current == target) {
                FluidCompatLog.info("[flowing_fluids_pipes] Flowing Fluids waterlog flow mode is already {}", target);
                return;
            }
            config.waterLogFlowMode = target;
            FluidCompatLog.info("[flowing_fluids_pipes] Flowing Fluids waterlog flow mode changed {} -> {} (in memory only)",
                    current, target);
        } catch (RuntimeException | LinkageError error) {
            FluidCompatLog.error("Could not apply the Flowing Fluids waterlog flow mode", error);
        }
    }

    // Any mode but IGNORE lets waterlogged pipes take part in the simulation; water was duplicated that way.
    private static WaterlogFlowOverride safeRequestedMode() {
        WaterlogFlowOverride configured = FluidCompatModulesConfig.FF_PIPES_WATERLOG_MODE.get();
        boolean safe = configured == WaterlogFlowOverride.IGNORE
                || configured == WaterlogFlowOverride.KEEP_FLOWING_FLUIDS_SETTING;
        if (safe || FluidCompatModulesConfig.FF_PIPES_ALLOW_EXPERIMENTAL_MODES.get()) {
            return configured;
        }
        FluidCompatLog.warn("[flowing_fluids_pipes] waterlogFlowMode={} is experimental and can duplicate water; "
                + "using IGNORE instead. Set allowExperimentalWaterlogModes=true to force it.", configured);
        return WaterlogFlowOverride.IGNORE;
    }

    private static void logMatchedBlocks(List<Block> pipes) {
        StringBuilder ids = new StringBuilder();
        for (Block block : pipes) {
            ids.append(BuiltInRegistries.BLOCK.getKey(block)).append(' ');
        }
        FluidCompatLog.debug("[flowing_fluids_pipes] matched blocks: {}", ids.toString().trim());
    }
}
