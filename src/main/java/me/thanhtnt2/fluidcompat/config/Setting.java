package me.thanhtnt2.fluidcompat.config;

import java.util.Optional;
import java.util.function.Function;

import com.google.gson.JsonElement;

/** One typed value of a {@link ConfigFile}. Reads never fail: before the file is loaded they return the default. */
public final class Setting<T> {

    private final String section;
    private final String key;
    private final T defaultValue;
    private final Function<JsonElement, Optional<T>> parser;
    private final Function<T, JsonElement> writer;
    private volatile T value;

    Setting(String section, String key, T defaultValue, Function<JsonElement, Optional<T>> parser,
            Function<T, JsonElement> writer) {
        this.section = section;
        this.key = key;
        this.defaultValue = defaultValue;
        this.parser = parser;
        this.writer = writer;
        this.value = defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        this.value = parser.apply(writer.apply(newValue)).orElse(defaultValue);
    }

    public T defaultValue() {
        return defaultValue;
    }

    public String section() {
        return section;
    }

    public String key() {
        return key;
    }

    Optional<T> parse(JsonElement element) {
        return parser.apply(element);
    }

    JsonElement toJson() {
        return writer.apply(value);
    }

    void reset() {
        this.value = defaultValue;
    }

    void assign(T parsed) {
        this.value = parsed;
    }
}
