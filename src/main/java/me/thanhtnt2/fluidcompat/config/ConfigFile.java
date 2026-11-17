package me.thanhtnt2.fluidcompat.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import me.thanhtnt2.fluidcompat.FluidCompat;
import net.fabricmc.loader.api.FabricLoader;

/**
 * A small sectioned JSON settings file. Keys that are missing are added with their default, keys the file
 * already has are never reset, and keys this version does not know are kept.
 */
public final class ConfigFile {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path path;
    private final List<Setting<?>> settings = new ArrayList<>();
    private JsonObject root = new JsonObject();
    private boolean loaded;

    public ConfigFile(String fileName) {
        this.path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
    }

    public Setting<Boolean> bool(String section, String key, boolean defaultValue) {
        return add(new Setting<>(section, key, defaultValue,
                element -> isPrimitive(element) && element.getAsJsonPrimitive().isBoolean()
                        ? Optional.of(element.getAsBoolean()) : Optional.empty(),
                JsonPrimitive::new));
    }

    public Setting<Integer> integer(String section, String key, int defaultValue, int min, int max) {
        return add(new Setting<>(section, key, defaultValue,
                element -> isPrimitive(element) && element.getAsJsonPrimitive().isNumber()
                        ? Optional.of(Math.clamp(element.getAsLong(), min, max)) : Optional.empty(),
                JsonPrimitive::new));
    }

    public Setting<List<String>> stringList(String section, String key, List<String> defaultValue) {
        return add(new Setting<>(section, key, List.copyOf(defaultValue), ConfigFile::parseStringList, values -> {
            JsonArray array = new JsonArray();
            values.forEach(array::add);
            return array;
        }));
    }

    public <E extends Enum<E>> Setting<E> enumeration(String section, String key, E defaultValue) {
        Class<E> type = defaultValue.getDeclaringClass();
        return add(new Setting<>(section, key, defaultValue,
                element -> isPrimitive(element) && element.getAsJsonPrimitive().isString()
                        ? parseEnum(type, element.getAsString()) : Optional.empty(),
                value -> new JsonPrimitive(value.name())));
    }

    /** Loads the file once. Hooks that run before the mod initialiser use this instead of load(). */
    public synchronized void ensureLoaded() {
        if (!loaded) {
            load();
        }
    }

    public synchronized void load() {
        loaded = true;
        root = readRoot();
        boolean addedKeys = false;
        for (Setting<?> setting : settings) {
            addedKeys |= applyFromFile(setting);
        }
        if (addedKeys) {
            save();
        }
    }

    public synchronized void save() {
        for (Setting<?> setting : settings) {
            JsonObject section = root.has(setting.section()) && root.get(setting.section()).isJsonObject()
                    ? root.getAsJsonObject(setting.section()) : new JsonObject();
            section.add(setting.key(), setting.toJson());
            root.add(setting.section(), section);
        }
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
        } catch (IOException exception) {
            FluidCompat.LOGGER.warn("Could not write {}", path.getFileName(), exception);
        }
    }

    private <T> Setting<T> add(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }

    private <T> boolean applyFromFile(Setting<T> setting) {
        JsonElement raw = root.has(setting.section()) && root.get(setting.section()).isJsonObject()
                ? root.getAsJsonObject(setting.section()).get(setting.key()) : null;
        if (raw == null) {
            setting.reset();
            return true;
        }
        Optional<T> parsed = setting.parse(raw);
        if (parsed.isEmpty()) {
            FluidCompat.LOGGER.warn("{}: '{}.{}' has an invalid value, using the default", path.getFileName(),
                    setting.section(), setting.key());
            setting.reset();
            return false;
        }
        setting.assign(parsed.get());
        return false;
    }

    private JsonObject readRoot() {
        if (!Files.exists(path)) {
            return new JsonObject();
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement parsed = GSON.fromJson(reader, JsonElement.class);
            return parsed != null && parsed.isJsonObject() ? parsed.getAsJsonObject() : new JsonObject();
        } catch (IOException | JsonParseException exception) {
            keepBrokenFile();
            return new JsonObject();
        }
    }

    private void keepBrokenFile() {
        Path broken = path.resolveSibling(path.getFileName() + ".broken");
        try {
            Files.move(path, broken, StandardCopyOption.REPLACE_EXISTING);
            FluidCompat.LOGGER.warn("{} could not be read, it was kept as {} and defaults are used", path.getFileName(),
                    broken.getFileName());
        } catch (IOException exception) {
            FluidCompat.LOGGER.warn("{} could not be read and could not be moved aside", path.getFileName(), exception);
        }
    }

    private static Optional<List<String>> parseStringList(JsonElement element) {
        if (element == null || !element.isJsonArray()) {
            return Optional.empty();
        }
        List<String> values = new ArrayList<>();
        for (JsonElement entry : element.getAsJsonArray()) {
            if (!isPrimitive(entry) || !entry.getAsJsonPrimitive().isString()) {
                return Optional.empty();
            }
            String text = entry.getAsString().trim();
            if (!text.isEmpty()) {
                values.add(text);
            }
        }
        return Optional.of(Collections.unmodifiableList(values));
    }

    private static boolean isPrimitive(JsonElement element) {
        return element != null && element.isJsonPrimitive();
    }

    private static <E extends Enum<E>> Optional<E> parseEnum(Class<E> type, String name) {
        for (E constant : type.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name)) {
                return Optional.of(constant);
            }
        }
        return Optional.empty();
    }
}
