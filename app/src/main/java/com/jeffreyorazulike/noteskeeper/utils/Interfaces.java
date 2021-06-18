package com.jeffreyorazulike.noteskeeper.utils;

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 17-Jul-21 at 9:06 AM
 */
public interface Interfaces{
    @FunctionalInterface
    interface Function<T, R> { R apply(T t);}

    @FunctionalInterface
    interface Supplier<R> {R get();}

    @FunctionalInterface
    interface RuntimeWrappableFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
