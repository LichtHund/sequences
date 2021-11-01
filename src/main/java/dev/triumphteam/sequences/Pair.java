package dev.triumphteam.sequences;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(@NotNull final K key, @NotNull final V value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <K, V> Pair<K, V> of(@NotNull final K key, @NotNull final V value) {
        return new Pair<>(key, value);
    }

    @NotNull
    public K getKey() {
        return key;
    }

    @NotNull
    public V getValue() {
        return value;
    }
}
