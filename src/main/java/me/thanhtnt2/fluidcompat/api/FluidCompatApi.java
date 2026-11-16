package me.thanhtnt2.fluidcompat.api;

/**
 * Entry point of the Fluid Compat addon API.
 *
 * <p>One fluid "level" is 1/8 of a bucket, the unit Flowing Fluids uses internally.
 */
public final class FluidCompatApi {

    public static final String MOD_ID = "fluidcompat";

    /** Bumped only when a breaking change is made to the api package. */
    public static final int API_VERSION = 1;

    public static final int LEVELS_PER_BUCKET = 8;

    private static volatile FiniteFluidService service;

    private FluidCompatApi() {
    }

    /**
     * Returns the active service. Safe to call once Fluid Compat has finished its initialisation.
     *
     * @throws IllegalStateException if called too early
     */
    public static FiniteFluidService service() {
        FiniteFluidService current = service;
        if (current == null) {
            throw new IllegalStateException("Fluid Compat API was used before it finished initialising");
        }
        return current;
    }

    public static boolean isReady() {
        return service != null;
    }

    /** Internal: called once by Fluid Compat itself. Addons must never call this. */
    public static void bind(FiniteFluidService newService) {
        if (service != null) {
            throw new IllegalStateException("Fluid Compat API is already bound");
        }
        service = newService;
    }
}
