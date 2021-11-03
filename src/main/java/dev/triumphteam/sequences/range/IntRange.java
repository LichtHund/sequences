package dev.triumphteam.sequences.range;

import org.jetbrains.annotations.NotNull;

public final class IntRange extends IntProgression implements ClosedRange<Integer> {

    public static final IntRange EMPTY = new IntRange(1, 0);

    private final int start;
    private final int endInclusive;

    public IntRange(final int start, final int endInclusive) {
        super(start, endInclusive, 1);
        this.start = start;
        this.endInclusive = endInclusive;
    }

    @Override
    public boolean contains(final @NotNull Integer value) {
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

    @Override
    public Integer getStart() {
        return start;
    }

    @Override
    public Integer getEndInclusive() {
        return endInclusive;
    }

    @NotNull
    @Override
    public String toString() {
        return "$first..$last";
    }
}
