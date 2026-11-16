package me.thanhtnt2.fluidcompat.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.thanhtnt2.fluidcompat.client.yacl.YaclConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;

/** Adds the config button to Mod Menu. The settings screen needs YetAnotherConfigLib, so without it there is no button. */
public final class FluidCompatModMenu implements ModMenuApi {

    private static final String YACL_MOD_ID = "yet_another_config_lib_v3";

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!FabricLoader.getInstance().isModLoaded(YACL_MOD_ID)) {
            return ModMenuApi.super.getModConfigScreenFactory();
        }
        return YaclConfigScreenFactory::create;
    }
}
