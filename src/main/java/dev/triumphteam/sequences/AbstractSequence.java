package dev.triumphteam.sequences;

import dev.triumphteam.sequences.operations.FilterSequence;
import dev.triumphteam.sequences.operations.IndexingSequence;
import dev.triumphteam.sequences.operations.MergingSequence;
import dev.triumphteam.sequences.operations.TransformingIndexedSequence;
import dev.triumphteam.sequences.operations.TransformingSequence;
import dev.triumphteam.sequences.operations.WindowedSequence;
import dev.triumphteam.sequences.util.IndexedValue;
import dev.triumphteam.sequences.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static dev.triumphteam.sequences.util.SequenceUtils.checkIndexOverflow;

@SuppressWarnings("unchecked")
public abstract class AbstractSequence<T, S extends Sequence<T>> implements Sequence<T> {

    private static final SplittableRandom RANDOM = new SplittableRandom();

    @NotNull
    @Override
    public S filter(@NotNull final Predicate<T> predicate) {
        return (S) new FilterSequence<>(this, true, predicate);
    }

    @NotNull
    @Override
    public S filterNot(@NotNull final Predicate<T> predicate) {
        return (S) new FilterSequence<>(this, false, predicate);
    }

    @NotNull
    @Override
    public <R> Sequence<R> filterIsInstance(@NotNull final Class<R> filterClass) {
        return (Sequence<R>) filter(filterClass::isInstance);
    }

    @NotNull
    @Override
    public S filterNotNull() {
        return filterNot(Objects::isNull);
    }

    @NotNull
    @Override
    public S filterIndexed(@NotNull final BiPredicate<Integer, T> predicate) {
        // TODO: 11/2/2021 This implementation is really stupid it can definitely be improved
        return (S) new TransformingSequence<>(
                new FilterSequence<>(
                        new IndexingSequence<>(this),
                        it -> predicate.test(it.getIndex(), it.getValue())
                ),
                IndexedValue::getValue
        );
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
    public S onEach(@NotNull final Consumer<T> action) {
        return (S) map(it -> {
            action.accept(it);
            return it;
        });
    }

    @NotNull
    @Override
    public S onEachIndexed(@NotNull final BiConsumer<Integer, T> action) {
        return (S) mapIndexed((index, it) -> {
            action.accept(index, it);
            return it;
        });
    }

    @NotNull
    @Override
    public <R> Sequence<Pair<T, R>> zip(@NotNull final Sequence<R> other) {
        return zip(other, Pair::of);
    }

    @NotNull
    @Override
    public <R, V> Sequence<V> zip(@NotNull final Sequence<R> other, @NotNull final BiFunction<T, R, V> transform) {
        return new MergingSequence<>(this, other, transform);
    }

    @NotNull
    @Override
    public <R> Sequence<R> windowed(
            final int size,
            final int step,
            final boolean partialWindows,
            @NotNull final Function<List<T>, R> transform
    ) {
        // TODO: 10/30/2021 Kotlin uses reuseBuffer = true, however in here it's only working with false
        //  Kotlin uses coroutines in the windowed sequence which makes it very hard to reproduce here.
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
    public <K, M extends Map<? super K, ? super T>> M associateByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector
    ) {
        for (final T element : this) {
            destination.put(keySelector.apply(element), element);
        }
        return destination;
    }

    @NotNull
    @Override
    public <K, V, M extends Map<? super K, ? super V>> M associateByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector,
            @NotNull final Function<T, V> valueTransform
    ) {
        for (final T element : this) {
            destination.put(keySelector.apply(element), valueTransform.apply(element));
        }
        return destination;
    }

    @NotNull
    @Override
    public <K, V, M extends Map<? super K, ? super V>> M associateTo(
            @NotNull final M destination,
            @NotNull final Function<T, Pair<K, V>> transform
    ) {
        for (final T element : this) {
            final Pair<K, V> entry = transform.apply(element);
            destination.put(entry.getFirst(), entry.getSecond());
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
    public <V, M extends Map<? super T, ? super V>> M associateWithTo(
            @NotNull final M destination,
            @NotNull final Function<T, V> valueSelector
    ) {
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
    public <K, M extends Map<? super K, List<T>>> M groupByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector
    ) {
        for (final T element : this) {
            final K key = keySelector.apply(element);
            final List<T> list = destination.computeIfAbsent(key, ignored -> new ArrayList<>());
            list.add(element);
        }
        return destination;
    }

    @NotNull
    @Override
    public <K, V, M extends Map<? super K, List<V>>> M groupByTo(
            @NotNull final M destination,
            @NotNull final Function<T, K> keySelector,
            @NotNull final Function<T, V> valueTransform
    ) {
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
    public Optional<T> first() {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) return Optional.empty();
        return Optional.of(iterator.next());
    }

    @NotNull
    @Override
    public Optional<T> first(@NotNull final Predicate<T> predicate) {
        for (final T element : this) {
            if (predicate.test(element)) return Optional.of(element);
        }
        return Optional.empty();
    }

    @NotNull
    @Override
    public Optional<T> last() {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) return Optional.empty();
        T last = iterator.next();
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return Optional.of(last);
    }

    @NotNull
    @Override
    public Optional<T> last(@NotNull final Predicate<T> predicate) {
        T last = null;
        boolean found = false;

        for (final T element : this) {
            if (predicate.test(element)) {
                last = element;
                found = true;
            }
        }

        if (!found) return Optional.empty();
        return Optional.of(last);
    }

    @NotNull
    @Override
    public Optional<T> find(@NotNull final Predicate<T> predicate) {
        return first(predicate);
    }

    @NotNull
    @Override
    public Optional<T> firstLast(@NotNull final Predicate<T> predicate) {
        return last(predicate);
    }

    @NotNull
    @Override
    public T elementAt(final int index) {
        final Optional<T> element = optionalElementAt(index);
        if (element.isEmpty()) throw new NoSuchElementException();
        return element.get();
    }

    @NotNull
    @Override
    public Optional<T> optionalElementAt(final int index) {
        if (index < 0) return Optional.empty();

        final Iterator<T> iterator = iterator();
        int count = 0;
        while (iterator.hasNext()) {
            final T element = iterator.next();
            if (index == count++) return Optional.of(element);
        }

        return Optional.empty();
    }

    @NotNull
    @Override
    public T random() {
        // Iterator doesn't have a size, so it uses the toList to get it.
        // But I think this could be improved.
        return elementAt(RANDOM.nextInt(toList().size()));
    }

    @Override
    public boolean any(@NotNull final Predicate<T> predicate) {
        for (final T element : this) {
            if (predicate.test(element)) return true;
        }
        return false;
    }

    @Override
    public boolean none(@NotNull final Predicate<T> predicate) {
        for (final T element : this) {
            if (predicate.test(element)) return false;
        }
        return true;
    }

    @NotNull
    @Override
    public <A extends Appendable> A joinTo(
            @NotNull final A buffer,
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence postfix,
            final int limit,
            @NotNull final CharSequence truncated,
            @Nullable final Function<T, CharSequence> transform
    ) {
        try {
            buffer.append(prefix);
            int count = 0;
            for (final T element : this) {
                if (++count > 1) buffer.append(separator);
                if (limit >= 0 && count > limit) break;

                if (transform != null) buffer.append(transform.apply(element));
                else if (element instanceof CharSequence) buffer.append((CharSequence) element);
                else if (element instanceof Character) buffer.append((char) element);
                else buffer.append(element.toString());
            }
            if (limit >= 0 && count > limit) buffer.append(truncated);
            buffer.append(postfix);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    @NotNull
    @Override
    public String joinToString() {
        return joinToString(", ");
    }

    @NotNull
    @Override
    public String joinToString(@NotNull final CharSequence separator) {
        return joinToString(separator, null);
    }

    @NotNull
    @Override
    public String joinToString(@Nullable final Function<T, CharSequence> transform) {
        return joinToString(", ", transform);
    }

    @NotNull
    @Override
    public String joinToString(@NotNull final CharSequence separator, @Nullable final Function<T, CharSequence> transform) {
        return joinToString(separator, "", transform);
    }

    @NotNull
    @Override
    public String joinToString(
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @Nullable final Function<T, CharSequence> transform
    ) {
        return joinToString(separator, prefix, "", transform);
    }

    @NotNull
    @Override
    public String joinToString(
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence postfix,
            @Nullable final Function<T, CharSequence> transform
    ) {
        return joinToString(separator, prefix, postfix, -1, "...", transform);
    }

    @NotNull
    @Override
    public String joinToString(
            @NotNull final CharSequence separator,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence postfix,
            final int limit,
            @NotNull final CharSequence truncated,
            final @Nullable Function<T, CharSequence> transform
    ) {
        return joinTo(new StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString();
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

    @Override
    public void forEachIndexed(@NotNull final BiConsumer<Integer, T> action) {
        int index = 0;
        for (final T item : this) {
            action.accept(checkIndexOverflow(index++), item);
        }
    }
}
