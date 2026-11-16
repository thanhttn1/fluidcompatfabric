package me.thanhtnt2.fluidcompat.client.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import me.thanhtnt2.fluidcompat.config.BackendPreference;
import me.thanhtnt2.fluidcompat.config.FluidCompatConfig;
import me.thanhtnt2.fluidcompat.config.FluidCompatModulesConfig;
import me.thanhtnt2.fluidcompat.config.Setting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Settings screen, loaded only when YetAnotherConfigLib is installed. */
public final class YaclConfigScreenFactory {

    private YaclConfigScreenFactory() {
    }

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("fluidcompat.config.title"))
                .category(coreCategory())
                .category(modCompatCategory())
                .category(loggingCategory())
                .save(() -> {
                    FluidCompatConfig.save();
                    FluidCompatModulesConfig.save();
                })
                .build()
                .generateScreen(parent);
    }

    private static ConfigCategory coreCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("fluidcompat.config.core"))
                .option(toggle(FluidCompatConfig.ENABLE_FINITE_FLUIDS))
                .option(backendOption())
                .option(toggle(FluidCompatConfig.FINITE_WATER))
                .option(toggle(FluidCompatConfig.FINITE_LAVA))
                .option(toggle(FluidCompatConfig.FINITE_TAGGED_FLUIDS))
                .option(slider(FluidCompatConfig.DRAIN_PERCENT, 1, 1000, 1))
                .option(slider(FluidCompatConfig.MIN_TICKS_BETWEEN_INTAKES, 0, 72000, 20))
                .build();
    }

    private static ConfigCategory modCompatCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("fluidcompat.config.mod_compat_list"))
                .option(toggle(FluidCompatModulesConfig.COMPAT_ENABLED))
                .option(toggle(FluidCompatModulesConfig.ENERGIZED_POWER_ENABLED))
                .option(toggle(FluidCompatModulesConfig.ENERGIZED_POWER_PLACE_COBBLESTONE))
                .build();
    }

    private static ConfigCategory loggingCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("fluidcompat.config.logging"))
                .option(toggle(FluidCompatModulesConfig.LOG_FILE_ENABLED))
                .option(toggle(FluidCompatModulesConfig.LOG_VERBOSE))
                .option(toggle(FluidCompatModulesConfig.LOG_DEBUG_MODE))
                .option(slider(FluidCompatModulesConfig.LOG_VERBOSE_THROTTLE_MS, 0, 60000, 100))
                .build();
    }

    private static Option<Boolean> toggle(Setting<Boolean> setting) {
        return Option.<Boolean>createBuilder()
                .name(name(setting))
                .description(OptionDescription.of(description(setting)))
                .binding(setting.defaultValue(), setting::get, setting::set)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    private static Option<Integer> slider(Setting<Integer> setting, int min, int max, int step) {
        return Option.<Integer>createBuilder()
                .name(name(setting))
                .description(OptionDescription.of(description(setting)))
                .binding(setting.defaultValue(), setting::get, setting::set)
                .controller(option -> IntegerSliderControllerBuilder.create(option).range(min, max).step(step))
                .build();
    }

    private static Option<BackendPreference> backendOption() {
        Setting<BackendPreference> setting = FluidCompatConfig.BACKEND_PREFERENCE;
        return Option.<BackendPreference>createBuilder()
                .name(name(setting))
                .description(OptionDescription.of(description(setting)))
                .binding(setting.defaultValue(), setting::get, setting::set)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(BackendPreference.class))
                .build();
    }

    private static Component name(Setting<?> setting) {
        return Component.translatable(translationKey(setting));
    }

    private static Component description(Setting<?> setting) {
        return Component.translatable(translationKey(setting) + ".tooltip");
    }

    private static String translationKey(Setting<?> setting) {
        return "fluidcompat.config." + setting.section() + "." + setting.key();
    }
}
