package dev.triumphteam.sequences;

import dev.triumphteam.sequences.range.CharRange;
import dev.triumphteam.sequences.range.IntRange;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public interface Sequence<T> extends BaseSequence<T, Sequence<T>> {

    @NotNull
    @Contract("_ -> new")
    static <T> Sequence<T> of(@NotNull final Iterable<T> iterable) {
        return new AbstractSequence<T, Sequence<T>>() {
            @NotNull
            @Override
            public Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    @NotNull
    @SafeVarargs
    @Contract("_ -> new")
    static <T> Sequence<T> of(@Nullable final T... elements) {
        final List<T> iterable = Arrays.asList(elements);
        return new AbstractSequence<T, Sequence<T>>() {
            @NotNull
            @Override
            public Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    @NotNull
    @Contract("_, _ -> new")
    static IntSequence range(final int start, final int end) {
        final IntRange range = new IntRange(start, end);
        return new IntSequence() {
            @NotNull
            @Override
            public Iterator<Integer> iterator() {
                return range.iterator();
            }
        };
    }

    @NotNull
    @Contract("_, _ -> new")
    static Sequence<Character> range(final char start, final char end) {
        final CharRange range = new CharRange(start, end);
        return new AbstractSequence<Character, Sequence<Character>>() {
            @NotNull
            @Override
            public Iterator<Character> iterator() {
                return range.iterator();
            }
        };
    }

}
