package dev.triumphteam.sequences;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Sequence<T> {

    @NotNull
    Iterator<T> iterator();

    @NotNull
    Sequence<T> filter(@NotNull final Predicate<T> predicate);

    @NotNull <R> Sequence<R> map(@NotNull final Function<T, R> transformer);

    @NotNull <R> Sequence<R> mapIndexed(@NotNull final BiFunction<Integer, T, R> transformer);

    @NotNull
    <R> Sequence<R> windowed(final int size, final int step, final boolean partialWindows, @NotNull final Function<List<T>, R> transform);

    @NotNull
    Sequence<List<T>> windowed(final int size, final int step, final boolean partialWindows);

    @NotNull
    Sequence<List<T>> windowed(final int size, final int step);

    @NotNull
    Sequence<List<T>> windowed(final int size);

    @NotNull
    Sequence<List<T>> chunked(final int size);

    @NotNull <R> R fold(@NotNull final R initial, @NotNull final BiFunction<R, T, R> operation);

    @NotNull
    List<T> toList();

    @NotNull
    List<T> toImmutableList();

    @NotNull
    Set<T> toSet();

    @NotNull
    Set<T> toSortedSet();

    @NotNull
    Set<T> toSortedSet(@NotNull final Comparator<T> comparator);

    @NotNull <C extends Collection<T>> Collection<T> toCollection(@NotNull final C destination);

    @NotNull
    @Contract("_ -> new")
    static <T> Sequence<T> of(@NotNull final Iterable<T> iterable) {
        return new BaseSequence<T>() {
            @Override
            public @NotNull Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    @NotNull
    @SafeVarargs
    @Contract("_ -> new")
    static <T> Sequence<T> of(@NotNull final T @Nullable ... elements) {
        final List<T> iterable = Arrays.asList(elements);
        return new BaseSequence<T>() {
            @Override
            public @NotNull Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }
}
