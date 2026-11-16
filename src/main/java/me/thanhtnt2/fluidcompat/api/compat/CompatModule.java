package me.thanhtnt2.fluidcompat.api.compat;

/** One supported mod. Registered at startup, initialised only when its mod is loaded. */
public interface CompatModule {

    String id();

    String modId();

    String displayName();

    void initialize();
}
