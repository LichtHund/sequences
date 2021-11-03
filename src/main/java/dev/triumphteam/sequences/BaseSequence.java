package dev.triumphteam.sequences;

import dev.triumphteam.sequences.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface BaseSequence<T, S extends BaseSequence<T, S>> extends Iterable<T> {

    /**
     * Returns an Iterator that returns the values from the sequence.
     * Throws an exception if the sequence is constrained to be iterated once and iterator is invoked the second time.
     *
     * @return The sequence's iterator.
     */
    @NotNull
    @Override
    Iterator<T> iterator();

    /**
     * Returns a sequence containing only elements matching the given {@link Predicate}.
     * The operation is intermediate and stateless.
     *
     * @param predicate Predicate to which the sequence should filter.
     * @return A {@link dev.triumphteam.sequences.operations.FilterSequence}.
     */
    @NotNull
    S filter(@NotNull final Predicate<T> predicate);

    /**
     * Returns a sequence containing all elements not matching the given {@link Predicate}.
     * The operation is intermediate and stateless.
     *
     * @param predicate Predicate to which the sequence should filter.
     * @return A {@link dev.triumphteam.sequences.operations.FilterSequence}.
     */
    @NotNull
    S filterNot(@NotNull final Predicate<T> predicate);

    @NotNull <R> Sequence<R> filterIsInstance(@NotNull final Class<R> filterClass);

    @NotNull
    S filterNotNull();

    @NotNull
    S filterIndexed(@NotNull final BiPredicate<Integer, T> predicate);

    @NotNull <R> Sequence<R> map(@NotNull final Function<T, R> transformer);

    @NotNull <R> Sequence<R> mapIndexed(@NotNull final BiFunction<Integer, T, R> transformer);

    @NotNull <R> Sequence<R> windowed(
            final int size,
            final int step,
            final boolean partialWindows,
            @NotNull final Function<List<T>, R> transform
    );

    @NotNull
    Sequence<List<T>> windowed(final int size, final int step, final boolean partialWindows);

    @NotNull
    Sequence<List<T>> windowed(final int size, final int step);

    @NotNull
    Sequence<List<T>> windowed(final int size);

    @NotNull
    Sequence<List<T>> chunked(final int size);

    @NotNull
    S onEach(@NotNull final Consumer<T> action);

    @NotNull
    S onEachIndexed(@NotNull final BiConsumer<Integer, T> action);

    @NotNull <R> Sequence<Pair<T, R>> zip(@NotNull final Sequence<R> other);

    @NotNull <R, V> Sequence<V> zip(@NotNull final Sequence<R> other, @NotNull final BiFunction<T, R, V> transform);

    @NotNull <K, V> Map<K, V> associate(@NotNull final Function<T, Pair<K, V>> transform);

    @NotNull <K> Map<K, T> associateBy(@NotNull final Function<T, K> keySelector);

    @NotNull <K, V> Map<K, V> associateBy(@NotNull final Function<T, K> keySelector, Function<T, V> valueTransform);

    @NotNull <K, M extends Map<? super K, ? super T>> M associateByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector
    );

    @NotNull <K, V, M extends Map<? super K, ? super V>> M associateByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector,
            @NotNull final Function<T, V> valueTransform
    );

    @NotNull <K, V, M extends Map<? super K, ? super V>> M associateTo(
            @NotNull final M destination,
            @NotNull final Function<T, Pair<K, V>> transform
    );

    @NotNull <V> Map<T, V> associateWith(@NotNull final Function<T, V> valueSelector);

    @NotNull <V, M extends Map<? super T, ? super V>> M associateWithTo(
            @NotNull final M destination,
            @NotNull final Function<T, V> valueSelector
    );

    @NotNull <K> Map<K, List<T>> groupBy(@NotNull final Function<T, K> keySelector);

    @NotNull <K, V> Map<K, List<V>> groupBy(
            @NotNull final Function<T, K> keySelector,
            @NotNull final Function<T, V> valueTransform
    );

    @NotNull <K, M extends Map<? super K, List<T>>> M groupByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector
    );

    @NotNull <K, V, M extends Map<? super K, List<V>>> M groupByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector,
            @NotNull final Function<T, V> valueTransform
    );

    @NotNull <R> R fold(@NotNull final R initial, @NotNull final BiFunction<R, T, R> operation);

    @NotNull Optional<T> first();

    @NotNull Optional<T> first(@NotNull final Predicate<T> predicate);

    @NotNull Optional<T> last();

    @NotNull Optional<T> last(@NotNull final Predicate<T> predicate);

    @NotNull Optional<T> find(@NotNull final Predicate<T> predicate);

    @NotNull Optional<T> firstLast(@NotNull final Predicate<T> predicate);

    @NotNull Optional<T> optionalElementAt(final int index);

    @NotNull T elementAt(final int index);

    @NotNull T random();

    boolean any(@NotNull final Predicate<T> predicate);

    boolean none(@NotNull final Predicate<T> predicate);

    @NotNull <A extends Appendable> A joinTo(
            @NotNull final A buffer,
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence postfix,
            final int limit,
            @NotNull final CharSequence truncated,
            @Nullable Function<T, CharSequence> transform
    );

    @NotNull
    String joinToString();

    @NotNull
    String joinToString(@NotNull final CharSequence separator);

    @NotNull
    String joinToString(@Nullable final Function<T, CharSequence> transform);

    @NotNull
    String joinToString(@NotNull final CharSequence separator, @Nullable final Function<T, CharSequence> transform);

    @NotNull
    String joinToString(
            @NotNull final CharSequence separator,
            @NotNull final CharSequence postfix,
            @Nullable final Function<T, CharSequence> transform
    );

    @NotNull
    String joinToString(
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence postfix,
            @Nullable final Function<T, CharSequence> transform
    );

    @NotNull
    String joinToString(
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence postfix,
            final int limit,
            @NotNull final CharSequence truncated,
            @Nullable final Function<T, CharSequence> transform
    );

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

}
