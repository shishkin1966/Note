package com.cleanarchitecture.common.utils;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;


import java.util.Collection;
import java.util.Comparator;

/**
 * Created by Shishkin on 10.01.2018.
 */

public class StreamUtils {

    private StreamUtils() {
    }

    public static <T> Stream<T> filter(final Collection<T> list, final Predicate<? super T> predicate) {
        return Stream.of(list).filter(predicate);
    }

    public static <T> Stream<T> sorted(final Collection<T> list, final Comparator<? super T> comparator) {
        return Stream.of(list).sorted(comparator);
    }

    public static <T> Stream<T> distinct(final Collection<T> list) {
        return Stream.of(list).distinct();
    }

    public static <T> Stream<T> filterNot(final Collection<T> list, final Predicate<? super T> predicate) {
        return Stream.of(list).filterNot(predicate);
    }

    public static <R, T> Stream<T> map(final Collection<R> list, final Function<? super R, ? extends T> mapper) {
        return Stream.of(list).map(mapper);
    }

    public static <R, T> Stream<T> flatMap(final Collection<R> list, final Function<? super R, ? extends Stream<? extends T>> mapper) {
        return Stream.of(list).flatMap(mapper);
    }

    public static <T> Stream<T> toStream(final Collection<T> list) {
        return Stream.of(list);
    }
}
