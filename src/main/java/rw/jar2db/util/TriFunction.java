package rw.jar2db.util;

public interface TriFunction<S, T, U, R> {
    R apply(S s, T t, U u);
}
