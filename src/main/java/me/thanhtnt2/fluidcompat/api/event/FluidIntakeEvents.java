package me.thanhtnt2.fluidcompat.api.event;

import me.thanhtnt2.fluidcompat.api.FluidIntakeRequest;
import me.thanhtnt2.fluidcompat.api.FluidIntakeResult;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Events fired by the Fluid Compat service for every managed intake, on the server thread.
 */
public final class FluidIntakeEvents {

    /** Fired before the world is drained. A listener may lower the amount or cancel the intake. */
    public static final Event<Pre> PRE = EventFactory.createArrayBacked(Pre.class, listeners -> (request, intake) -> {
        for (Pre listener : listeners) {
            listener.onPre(request, intake);
            if (intake.isCanceled()) {
                return;
            }
        }
    });

    /** Fired after the world was drained. */
    public static final Event<Post> POST = EventFactory.createArrayBacked(Post.class, listeners -> (request, result) -> {
        for (Post listener : listeners) {
            listener.onPost(request, result);
        }
    });

    private FluidIntakeEvents() {
    }

    @FunctionalInterface
    public interface Pre {
        void onPre(FluidIntakeRequest request, PreIntake intake);
    }

    @FunctionalInterface
    public interface Post {
        void onPost(FluidIntakeRequest request, FluidIntakeResult result);
    }

    /** Mutable view of an intake that has not happened yet. Cancelling hands the machine back its original behaviour. */
    public static final class PreIntake {

        private int requestedLevels;
        private boolean canceled;

        public PreIntake(int requestedLevels) {
            this.requestedLevels = requestedLevels;
        }

        public int getRequestedLevels() {
            return requestedLevels;
        }

        public void setRequestedLevels(int requestedLevels) {
            this.requestedLevels = Math.max(0, requestedLevels);
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void cancel() {
            this.canceled = true;
        }
    }
}
