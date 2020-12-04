package rw.jar2db;

import java.util.HashMap;
import java.util.Map;

public class NestedIdCache<T> {
    public static class IdCache {
        Map<Object, Long> cache = new HashMap<>();
        long lastId = 0;

        public Boolean exists(Object entity) {
            return cache.containsKey(entity);
        }

        public Long get(Object entity) {
            return cache.computeIfAbsent(entity, __ -> ++lastId);
        }
    }

    private final Map<T, IdCache> cache = new HashMap<>();

    public IdCache getIdCache(T table) {
        return cache.computeIfAbsent(table, __ -> new IdCache());
    }

    public boolean exists(T table, Object entity) {
        return entity == null || getIdCache(table).exists(entity);
    }

    public Long getId(T table, Object entity) {
        return entity == null ? null : getIdCache(table).get(entity);
    }

}
