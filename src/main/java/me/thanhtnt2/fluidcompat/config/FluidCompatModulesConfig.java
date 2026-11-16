package me.thanhtnt2.fluidcompat.config;

/** fluidcompat-compat.json: switches and settings of the per-mod compat modules. Sections are only ever added. */
public final class FluidCompatModulesConfig {

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

    private FluidCompatModulesConfig() {
    }

    public static void load() {
        FILE.load();
    }

    public static void save() {
        FILE.save();
    }
}
