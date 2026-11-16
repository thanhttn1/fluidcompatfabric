package me.thanhtnt2.fluidcompat.client;

import me.thanhtnt2.fluidcompat.client.yacl.YaclConfigScreenFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class FluidCompatClient implements ClientModInitializer {

    private static final String YACL_MOD_ID = "yet_another_config_lib_v3";

    @Override
    public void onInitializeClient() {
        if (!FabricLoader.getInstance().isModLoaded(YACL_MOD_ID)) {
            return;
        }
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) ->
                dispatcher.register(ClientCommands.literal("fluidcompat")
                        .then(ClientCommands.literal("config").executes(context -> {
                            Minecraft client = context.getSource().getClient();
                            // The chat screen closes right after a command, so the settings screen opens a moment later.
                            client.execute(() -> client.setScreenAndShow(YaclConfigScreenFactory.create(null)));
                            context.getSource().sendFeedback(Component.translatable("fluidcompat.config.opening"));
                            return 1;
                        }))));
    }
}
