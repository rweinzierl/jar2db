package rw.jar2db.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {

    public static <T> Stream<T> toStream(Iterable<T> it) {
        return StreamSupport.stream(it.spliterator(), false);
    }

}
