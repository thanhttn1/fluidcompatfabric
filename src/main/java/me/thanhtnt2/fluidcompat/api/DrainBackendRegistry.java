package me.thanhtnt2.fluidcompat.api;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DrainBackendRegistry {

    private static final Comparator<DrainBackend> HIGHEST_PRIORITY_FIRST =
            Comparator.comparingInt(DrainBackend::priority).reversed();
    private static final List<DrainBackend> BACKENDS = new CopyOnWriteArrayList<>();
    private static volatile List<DrainBackend> orderedSnapshot = List.of();

    private DrainBackendRegistry() {
    }

    public static synchronized void register(DrainBackend backend) {
        Objects.requireNonNull(backend, "backend");
        boolean duplicate = BACKENDS.stream().anyMatch(existing -> existing.id().equals(backend.id()));
        if (duplicate) {
            throw new IllegalArgumentException("Drain backend already registered: " + backend.id());
        }
        BACKENDS.add(backend);
        BACKENDS.sort(HIGHEST_PRIORITY_FIRST);
        orderedSnapshot = List.copyOf(BACKENDS);
    }

    /** Backends, highest priority first. Cached, so it is cheap to call every tick. */
    public static List<DrainBackend> snapshot() {
        return orderedSnapshot;
    }
}
