package com.jeffreyorazulike.noteskeeper.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 18-Jun-21 at 8:02 AM
 */
public final class Constants {
    private Constants (){}

    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static <T> Interfaces.Function<Future<T>, T> FUTURE_GETTER(){
        return RuntimeWrappableFunctionMapper.wrap(Future::get);
    }
}