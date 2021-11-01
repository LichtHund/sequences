package dev.triumphteam.sequences;

import dev.triumphteam.sequences.operations.FilterSequence;
import dev.triumphteam.sequences.operations.TransformingIndexedSequence;
import dev.triumphteam.sequences.operations.TransformingSequence;
import dev.triumphteam.sequences.operations.WindowedSequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

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
    public <R> Sequence<R> windowed(final int size, final int step, final boolean partialWindows, @NotNull final Function<List<T>, R> transform) {
        // TODO: 10/30/2021 Kotlin uses reuseBuffer = true, however in here it's only working with false
        return new WindowedSequence<>(iterator(), size, step, partialWindows, false).map(transform);
    }

    @NotNull
    @Override
    public Sequence<List<T>> windowed(final int size, final int step, final boolean partialWindows) {
        return new WindowedSequence<>(iterator(), size, step, partialWindows, false);
    }

    @NotNull
    @Override
    public Sequence<List<T>> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    @NotNull
    @Override
    public Sequence<List<T>> windowed(final int size) {
        return windowed(size, 1);
    }

    @NotNull
    @Override
    public Sequence<List<T>> chunked(final int size) {
        return windowed(size, size, true);
    }

    @NotNull
    @Override
    public <K, V> Map<K, V> associate(@NotNull final Function<T, Pair<K, V>> transform) {
        return associateTo(new LinkedHashMap<>(), transform);
    }

    @NotNull
    @Override
    public <K> Map<K, T> associateBy(@NotNull final Function<T, K> keySelector) {
        return associateByTo(new LinkedHashMap<>(), keySelector);
    }

    @NotNull
    @Override
    public <K, V> Map<K, V> associateBy(@NotNull final Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform) {
        return associateByTo(new LinkedHashMap<>(), keySelector, valueTransform);
    }

    @NotNull
    @Override
    public <K, M extends Map<? super K, ? super T>> M associateByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector) {
        for (final T element : this) {
            destination.put(keySelector.apply(element), element);
        }
        return destination;
    }

    @NotNull
    @Override
    public <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform) {
        for (final T element : this) {
            destination.put(keySelector.apply(element), valueTransform.apply(element));
        }
        return destination;
    }

    @NotNull
    @Override
    public <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull final M destination, @NotNull final Function<T, Pair<K, V>> transform) {
        for (final T element : this) {
            final Pair<K, V> entry = transform.apply(element);
            destination.put(entry.getKey(), entry.getValue());
        }
        return destination;
    }

    @NotNull
    @Override
    public <V> Map<T, V> associateWith(@NotNull final Function<T, V> valueSelector) {
        return associateWithTo(new LinkedHashMap<>(), valueSelector);
    }

    @NotNull
    @Override
    public <V, M extends Map<? super T, ? super V>> M associateWithTo(@NotNull final M destination, @NotNull final Function<T, V> valueSelector) {
        for (final T element : this) {
            destination.put(element, valueSelector.apply(element));
        }
        return destination;
    }

    @NotNull
    @Override
    public <K> Map<K, List<T>> groupBy(@NotNull final Function<T, K> keySelector) {
        return groupByTo(new LinkedHashMap<>(), keySelector);
    }

    @NotNull
    @Override
    public <K, V> Map<K, List<V>> groupBy(@NotNull final Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform) {
        return groupByTo(new LinkedHashMap<>(), keySelector, valueTransform);
    }

    @NotNull
    @Override
    public <K, M extends Map<? super K, List<T>>> M groupByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector) {
        for (final T element : this) {
            final K key = keySelector.apply(element);
            final List<T> list = destination.computeIfAbsent(key, ignored -> new ArrayList<>());
            list.add(element);
        }

        return destination;
    }

    @NotNull
    @Override
    public <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform) {
        for (final T element : this) {
            final K key = keySelector.apply(element);
            final List<V> list = destination.computeIfAbsent(key, ignored -> new ArrayList<>());
            list.add(valueTransform.apply(element));
        }

        return destination;
    }

    @NotNull
    @Override
    public <R> R fold(@NotNull final R initial, @NotNull final BiFunction<R, T, R> operation) {
        final Iterator<T> iterator = iterator();
        R accumulator = initial;
        while (iterator.hasNext()) {
            accumulator = operation.apply(accumulator, iterator.next());
        }
        return accumulator;
    }

    @NotNull
    @Override
    public <A, R> R to(@NotNull final Collector<? super T, A, R> collector) {
        // TODO: 10/30/2021 IMPLEMENTATION
        return null;
    }

    @NotNull
    @Override
    public <R> R to(@NotNull final Supplier<R> supplier, @NotNull final BiConsumer<R, ? super T> accumulator, @NotNull final BiConsumer<R, R> combiner) {
        // TODO: 10/30/2021 IMPLEMENTATION
        return null;
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
        for (final T t : this) {
            destination.add(t);
        }
        return destination;
    }

}
