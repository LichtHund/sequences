package dev.triumphteam.sequences.range.progression;

import dev.triumphteam.sequences.iterator.IntProgressionIterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.triumphteam.sequences.util.SequenceUtils.getProgressionLastElement;

public class IntProgression implements Iterable<Integer> {
    private final int start;
    private final int endInclusive;
    private final int step;

    public IntProgression(final int start, final int endInclusive, final int step) {
        if (step == 0) throw new IllegalArgumentException("Step must be non-zero.");
        if (step == Integer.MIN_VALUE)
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");

        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
    }

    /**
     * Creates IntProgression within the specified bounds of a closed range.
     * <p>
     * The progression starts with the [rangeStart] value and goes toward the [rangeEnd] value not excluding it, with the specified [step].
     * In order to go backwards the [step] must be negative.
     * <p>
     * [step] must be greater than `Int.MIN_VALUE` and not equal to zero.
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static IntProgression fromClosedRange(final int rangeStart, final int rangeEnd, final int step) {
        return new IntProgression(rangeStart, rangeEnd, step);
    }

    /**
     * The first element in the progression.
     */
    public int getFirst() {
        return start;
    }

    /**
     * The last element in the progression.
     */
    public int getLast() {
        return getProgressionLastElement(start, endInclusive, step);
    }

    /**
     * The step of the progression.
     */
    public int getStep() {
        return step;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntProgressionIterator(start, endInclusive, step);
    }

    /**
     * Checks if the progression is empty.
     * <p>
     * Progression with a positive step is empty if its first element is greater than the last element.
     * Progression with a negative step is empty if its first element is less than the last element.
     */
    public boolean isEmpty() {
        if (step > 0) return start > endInclusive;
        return start < endInclusive;
    }

    @Override
    public String toString() {
        if (step > 0) return "$first..$last step $step";
        return "$first downTo $last step ${-step}";
    }

}
