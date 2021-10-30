package dev.triumphteam.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Sequence<T> {

    @NotNull
    Iterator<T> getIterator();

    @NotNull
    Sequence<T> filter(@NotNull final Predicate<T> predicate);

    @NotNull <R> Sequence<R> map(@NotNull final Function<T, R> transformer);

    @NotNull <R> Sequence<R> mapIndexed(@NotNull final BiFunction<Integer, T, R> transformer);

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

    static <T> Sequence<T> of(@NotNull final Iterable<T> iterable) {
        return new BaseSequence<T>() {
            @Override
            public @NotNull Iterator<T> getIterator() {
                return iterable.iterator();
            }
        };
    }
}
