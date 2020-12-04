package rw.jar2db.util;

import java.util.Optional;

public interface Noex {

    interface Runnable {
        void run() throws Exception;

        static java.lang.Runnable noex(Runnable l) {
            return () -> {
                try {
                    l.run();
                } catch (Exception e) {
                    throw wrap(e);
                }
            };
        }
    }

    interface Supplier<T> {
        T get() throws Exception;

        static <T> java.util.function.Supplier<T> noex(Supplier<T> l) {
            return () -> {
                try {
                    return l.get();
                } catch (Exception e) {
                    throw wrap(e);
                }
            };
        }
    }

    interface Consumer<T> {
        void accept(T t) throws Exception;

        static <T> java.util.function.Consumer<T> noex(Consumer<T> l) {
            return t -> {
                try {
                    l.accept(t);
                } catch (Exception e) {
                    throw wrap(e);
                }
            };
        }
    }

    interface Function<T, R> {
        R apply(T t) throws Exception;

        static <T, R> java.util.function.Function<T, R> noex(Function<T, R> l) {
            return t -> {
                try {
                    return l.apply(t);
                } catch (Exception e) {
                    throw wrap(e);
                }
            };
        }
    }

    interface BiFunction<T, U, R> {
        R apply(T t, U u) throws Exception;

        static <T, U, R> java.util.function.BiFunction<T, U, R> noex(BiFunction<T, U, R> l) {
            return (t, u) -> {
                try {
                    return l.apply(t, u);
                } catch (Exception e) {
                    throw wrap(e);
                }
            };
        }
    }

    static void exec(Runnable l) {
        try {
            l.run();
        } catch (Exception e) {
            throw wrap(e);
        }
    }

    static <T> T exec(Supplier<T> l) {
        try {
            return l.get();
        } catch (Exception e) {
            throw wrap(e);
        }
    }

    static <T> Optional<T> silent(Supplier<T> l) {
        try {
            return Optional.ofNullable(l.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    static RuntimeException wrap(Exception e) {
        RuntimeException e2;
        if (e instanceof RuntimeException)
            e2 = (RuntimeException) e;
        else
            e2 = new RuntimeException(e);
        return e2;
    }
}
