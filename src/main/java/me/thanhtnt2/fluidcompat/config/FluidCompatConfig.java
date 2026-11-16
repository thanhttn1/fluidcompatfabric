package me.thanhtnt2.fluidcompat.config;

/** fluidcompat-common.json: which fluids are finite and how the world is drained. */
public final class FluidCompatConfig {

    private static final ConfigFile FILE = new ConfigFile("fluidcompat-common.json");

    public static final Setting<Boolean> ENABLE_FINITE_FLUIDS = FILE.bool("general", "enableFiniteFluids", true);
    public static final Setting<BackendPreference> BACKEND_PREFERENCE =
            FILE.enumeration("general", "backendPreference", BackendPreference.AUTO);
    public static final Setting<Boolean> FINITE_WATER = FILE.bool("fluids", "finiteWater", true);
    public static final Setting<Boolean> FINITE_LAVA = FILE.bool("fluids", "finiteLava", true);
    public static final Setting<Boolean> FINITE_TAGGED_FLUIDS = FILE.bool("fluids", "finiteTaggedFluids", true);
    public static final Setting<Integer> DRAIN_PERCENT = FILE.integer("pumps", "drainPercent", 100, 1, 1000);
    public static final Setting<Integer> MIN_TICKS_BETWEEN_INTAKES =
            FILE.integer("pumps", "minTicksBetweenIntakes", 0, 0, 72000);

    private FluidCompatConfig() {
    }

    public static void load() {
        FILE.load();
    }

    public static void save() {
        FILE.save();
    }
}
