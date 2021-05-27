package com.jeffreyorazulike.noteskeeper;

import androidx.viewbinding.ViewBinding;

public interface NotesKeeperInterfaces {

    interface Consumer<T, V>{ void consume(T t, V v);}

    interface Function<T, R>{ R fun(T t);}
}