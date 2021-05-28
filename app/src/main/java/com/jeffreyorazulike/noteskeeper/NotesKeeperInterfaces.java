package com.jeffreyorazulike.noteskeeper;

public interface NotesKeeperInterfaces {

    interface Consumer<T, V>{ void consume(T t, V v);}

    interface Function<T, R>{ R fun(T t);}

    interface Observable<T> { void work(T t); }

    interface Observer<T> { void bind(Observable<T> observable); }
}