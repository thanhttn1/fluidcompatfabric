package me.thanhtnt2.fluidcompat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.thanhtnt2.fluidcompat.compat.CompatBootstrap;
import me.thanhtnt2.fluidcompat.config.FluidCompatConfig;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.core.FluidCompatBootstrap;
import me.thanhtnt2.fluidcompat.logging.FluidCompatLog;
import net.fabricmc.api.ModInitializer;

public final class FluidCompat implements ModInitializer {

    public static final String MOD_ID = "fluidcompat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        FluidCompatConfig.load();
        FluidCompatModulesConfig.load();
        FluidCompatLog.initialize();
        CompatBootstrap.registerBuiltInModules();
        FluidCompatBootstrap.initialize();
        CompatBootstrap.initialize();
    }
}
