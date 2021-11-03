package dev.triumphteam.sequences.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class IndexedValue<T> {

    private final int index;
    private final T value;

    public IndexedValue(final int index, @NotNull final T value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    @NotNull
    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final IndexedValue<?> that = (IndexedValue<?>) o;
        return index == that.index && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "IndexedValue{" +
                "index=" + index +
                ", value=" + value +
                '}';
    }
}
