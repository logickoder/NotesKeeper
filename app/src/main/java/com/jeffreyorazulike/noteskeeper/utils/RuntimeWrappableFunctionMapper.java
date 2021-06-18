package com.jeffreyorazulike.noteskeeper.utils;

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 17-Jul-21 at 9:04 AM
 */
public class RuntimeWrappableFunctionMapper {
    public static <T, R> Interfaces.Function<T, R> wrap(Interfaces.RuntimeWrappableFunction<T, R> wrappable){
        return t -> {
            try{
                return wrappable.apply(t);
            }catch (Exception exception){
                throw new RuntimeException(exception);
            }
        };
    }


}
