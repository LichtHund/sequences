package dev.triumphteam.sequences.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(@NotNull final A first, @NotNull final B second) {
        this.first = first;
        this.second = second;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <K, V> Pair<K, V> of(@NotNull final K first, @NotNull final V second) {
        return new Pair<>(first, second);
    }

    @NotNull
    public A getFirst() {
        return first;
    }

    @NotNull
    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
