package dev.triumphteam.sequences;

import dev.triumphteam.sequences.operations.FilterSequence;
import dev.triumphteam.sequences.operations.TransformingIndexedSequence;
import dev.triumphteam.sequences.operations.TransformingSequence;
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

public abstract class BaseSequence<T> implements Sequence<T> {

    @NotNull
    @Override
    public Sequence<T> filter(@NotNull final Predicate<T> predicate) {
        return new FilterSequence<>(this, predicate);
    }

    @NotNull
    @Override
    public <R> Sequence<R> map(@NotNull final Function<T, R> transformer) {
        return new TransformingSequence<>(this, transformer);
    }

    @NotNull
    @Override
    public <R> Sequence<R> mapIndexed(@NotNull final BiFunction<Integer, T, R> transformer) {
        return new TransformingIndexedSequence<>(this, transformer);
    }

    @NotNull
    @Override
    public <R> R fold(@NotNull final R initial, @NotNull final BiFunction<R, T, R> operation) {
        final Iterator<T> iterator = getIterator();
        R accumulator = initial;
        while (iterator.hasNext()) {
            accumulator = operation.apply(accumulator, iterator.next());
        }
        return accumulator;
    }

    @NotNull
    @Override
    public List<T> toList() {
        return (List<T>) toCollection(new ArrayList<>());
    }

    @NotNull
    @Override
    public List<T> toImmutableList() {
        return Collections.unmodifiableList((List<T>) toCollection(new ArrayList<>()));
    }

    @NotNull
    @Override
    public Set<T> toSet() {
        return (Set<T>) toCollection(new LinkedHashSet<>());
    }

    @NotNull
    @Override
    public Set<T> toSortedSet() {
        return (Set<T>) toCollection(new TreeSet<>());
    }

    @NotNull
    @Override
    public Set<T> toSortedSet(@NotNull final Comparator<T> comparator) {
        return (Set<T>) toCollection(new TreeSet<>(comparator));
    }

    @NotNull
    @Override
    public <C extends Collection<T>> Collection<T> toCollection(@NotNull final C destination) {
        final Iterator<T> iterator = getIterator();
        while (iterator.hasNext()) {
            destination.add(iterator.next());
        }
        return destination;
    }

}
