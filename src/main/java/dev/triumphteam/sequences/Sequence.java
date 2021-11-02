package dev.triumphteam.sequences;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface Sequence<T> extends Iterable<T> {

    @NotNull
    @Override
    Iterator<T> iterator();

    @NotNull
    Sequence<T> filter(@NotNull final Predicate<T> predicate);

    @NotNull
    Sequence<T> filterNot(@NotNull final Predicate<T> predicate);

    @NotNull
    <R> Sequence<R> filterIsInstance(@NotNull final Class<R> filterClass);

    @NotNull
    Sequence<T> filterNotNull();

    @NotNull
    Sequence<T> filterIndexed(@NotNull final BiPredicate<Integer, T> predicate);

    @NotNull <R> Sequence<R> map(@NotNull final Function<T, R> transformer);

    @NotNull <R> Sequence<R> mapIndexed(@NotNull final BiFunction<Integer, T, R> transformer);

    @NotNull <R> Sequence<R> windowed(final int size, final int step, final boolean partialWindows, @NotNull final Function<List<T>, R> transform);

    @NotNull
    Sequence<List<T>> windowed(final int size, final int step, final boolean partialWindows);

    @NotNull
    Sequence<List<T>> windowed(final int size, final int step);

    @NotNull
    Sequence<List<T>> windowed(final int size);

    @NotNull
    Sequence<List<T>> chunked(final int size);

    @NotNull
    <R> Sequence<Pair<T, R>> zip(@NotNull final Sequence<R> other);

    @NotNull
    <R, V> Sequence<V> zip(@NotNull final Sequence<R> other, @NotNull final BiFunction<T, R, V> transform);

    @NotNull <K, V> Map<K, V> associate(@NotNull final Function<T, Pair<K, V>> transform);

    @NotNull <K> Map<K, T> associateBy(@NotNull final Function<T, K> keySelector);

    @NotNull <K, V> Map<K, V> associateBy(@NotNull final Function<T, K> keySelector, Function<T, V> valueTransform);

    @NotNull <K, M extends Map<? super K, ? super T>> M associateByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector);

    @NotNull <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform);

    @NotNull <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull final M destination, @NotNull final Function<T, Pair<K, V>> transform);

    @NotNull <V> Map<T, V> associateWith(@NotNull final Function<T, V> valueSelector);

    @NotNull <V, M extends Map<? super T, ? super V>> M associateWithTo(@NotNull final M destination, @NotNull final Function<T, V> valueSelector);

    @NotNull <K> Map<K, List<T>> groupBy(@NotNull final Function<T, K> keySelector);

    @NotNull <K, V> Map<K, List<V>> groupBy(@NotNull final Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform);

    @NotNull <K, M extends Map<? super K, List<T>>> M groupByTo(@NotNull final M destination, @NotNull final Function<T, K> keySelector);

    @NotNull <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull final M destination, final @NotNull Function<T, K> keySelector, @NotNull final Function<T, V> valueTransform);

    @NotNull <R> R fold(@NotNull final R initial, @NotNull final BiFunction<R, T, R> operation);

    @NotNull <A, R> R to(@NotNull final Collector<? super T, A, R> collector);

    @NotNull <R> R to(@NotNull final Supplier<R> supplier, @NotNull final BiConsumer<R, ? super T> accumulator, @NotNull final BiConsumer<R, R> combiner);

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

    void forEachIndexed(@NotNull final BiConsumer<Integer, T> action);

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
