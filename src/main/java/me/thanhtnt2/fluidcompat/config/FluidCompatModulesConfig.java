package me.thanhtnt2.fluidcompat.config;

import java.util.List;

/** fluidcompat-compat.json: switches and settings of the per-mod compat modules. Sections are only ever added. */
public final class FluidCompatModulesConfig {

    /** Pipe blocks, by class name. The first is Energized Power's; the others are the same mods as on the other lines. */
    private static final List<String> DEFAULT_PIPE_BLOCK_CLASSES = List.of(
            "me.jddev0.ep.block.FluidPipeBlock",
            "aztech.modern_industrialization.pipes.impl.PipeBlock",
            "dev.technici4n.moderndynamics.pipe.PipeBlock",
            "mcjty.xnet.modules.cables.blocks.GenericCableBlock",
            "com.refinedmods.refinedstorage.common.networking.CableBlock",
            "com.refinedmods.refinedstorage.common.support.AbstractDirectionalCableBlock",
            "mekanism.common.block.transmitter.BlockTransmitter");

    private static final ConfigFile FILE = new ConfigFile("fluidcompat-compat.json");

    public static final Setting<Boolean> COMPAT_ENABLED = FILE.bool("general", "compatEnabled", true);

    public static final Setting<Boolean> LOG_FILE_ENABLED = FILE.bool("logging", "fileEnabled", true);
    public static final Setting<Boolean> LOG_VERBOSE = FILE.bool("logging", "verbose", false);
    public static final Setting<Boolean> LOG_DEBUG_MODE = FILE.bool("logging", "debugMode", false);
    public static final Setting<Integer> LOG_VERBOSE_THROTTLE_MS =
            FILE.integer("logging", "verboseThrottleMillis", 1000, 0, 60000);

    public static final Setting<Boolean> ENERGIZED_POWER_ENABLED = FILE.bool("energized_power", "enabled", true);
    public static final Setting<Boolean> ENERGIZED_POWER_PLACE_COBBLESTONE =
            FILE.bool("energized_power", "placeCobblestone", false);

    public static final Setting<Boolean> FLUIDLOGGABLE_PIPES_ENABLED = FILE.bool("fluidloggable_pipes", "enabled", true);
    public static final Setting<Boolean> FLUIDLOGGABLE_ACCEPT_DUPLICATION_RISK =
            FILE.bool("fluidloggable_pipes", "acceptDuplicationRisk", false);
    public static final Setting<List<String>> FLUIDLOGGABLE_PIPES_BLOCK_CLASSES =
            FILE.stringList("fluidloggable_pipes", "blockClasses", DEFAULT_PIPE_BLOCK_CLASSES);

    public static final Setting<Boolean> FF_PIPES_ENABLED = FILE.bool("flowing_fluids_pipes", "enabled", true);
    public static final Setting<Boolean> FF_PIPES_NON_DISPLACERS =
            FILE.bool("flowing_fluids_pipes", "registerAsNonDisplacers", true);
    public static final Setting<WaterlogFlowOverride> FF_PIPES_WATERLOG_MODE =
            FILE.enumeration("flowing_fluids_pipes", "waterlogFlowMode", WaterlogFlowOverride.IGNORE);
    public static final Setting<Boolean> FF_PIPES_ALLOW_EXPERIMENTAL_MODES =
            FILE.bool("flowing_fluids_pipes", "allowExperimentalWaterlogModes", false);

    private FluidCompatModulesConfig() {
    }

    public static void load() {
        FILE.load();
    }

    public static void ensureLoaded() {
        FILE.ensureLoaded();
    }

    public static void save() {
        FILE.save();
    }
}
