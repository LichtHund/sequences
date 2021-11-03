package dev.triumphteam.sequences.range;

import dev.triumphteam.sequences.range.progression.CharProgression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CharRange extends CharProgression implements ClosedRange<Character> {

    public static final CharRange EMPTY = new CharRange((char) 1, (char) 0);

    private final char start;
    private final char endInclusive;

    public CharRange(final char start, final char endInclusive) {
        super(start, endInclusive, 1);
        this.start = start;
        this.endInclusive = endInclusive;
    }

    @Override
    public boolean contains(@NotNull final Character value) {
        return start <= value && value <= endInclusive;
    }

    /**
     * Checks whether the range is empty.
     * <p>
     * The range is empty if its start value is greater than the end value.
     */
    @Override
    public boolean isEmpty() {
        return start > endInclusive;
    }

    @NotNull
    @Override
    public Character getStart() {
        return start;
    }

    @NotNull
    @Override
    public Character getEndInclusive() {
        return endInclusive;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CharRange integers = (CharRange) o;
        return start == integers.start && endInclusive == integers.endInclusive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, endInclusive);
    }

    @NotNull
    @Override
    public String toString() {
        return start + ".." + endInclusive;
    }
}
