package rw.jar2db;

import rw.jar2db.model.Class;
import rw.jar2db.model.Field;
import rw.jar2db.model.Method;
import rw.jar2db.util.Noex;
import rw.jar2db.util.Pair;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class EntityCreator {
    private final NestedIdCache<Tables.Table<?>> cache = new NestedIdCache<>();
    private final Tables tables = new Tables();
    private final Map<Class, ClassSource> classToArtifact = new HashMap<>();
    private final Connection conn;

    public EntityCreator(Connection conn) {this.conn = conn;}

    public Long getId(Tables.Table<?> table, Object entity) {
        return cache.getId(table, entity);
    }

    public void addMapping(Class clazz, ClassSource artifact) {
        classToArtifact.put(clazz, artifact);
    }

    private <T> boolean createIfNotExists(Tables.Table<T> table, T entity) {
        if (!cache.exists(table, entity)) {
            create(table, entity);
            return true;
        } else
            return false;
    }

    private <T> void create(Tables.Table<T> table, T entity) {
        Noex.exec(() -> table.insert(conn, this, entity));
    }

    private <T> Long getReference(Tables.Table<T> table, T entity) {
        createIfNotExists(table, entity);
        return getId(table, entity);
    }

    public Long getReference(ClassSource artifact) {
        return getReference(tables.artifact, artifact);
    }

    public Long getReference(Class clazz) {
        return getReference(tables.clazz, clazz);
    }

    public Long getReference(Field field) {
        return getReference(tables.field, field);
    }

    public void create(Method referrer, Method referred) {
        create(tables.methodReference, new Pair<>(referrer, referred));
    }

    public void create(Method referrer, Field referred) {
        create(tables.fieldReference, new Pair<>(referrer, referred));
    }

    public Long getReference(Method method) {
        if (createIfNotExists(tables.method, method))
            IntStream.range(0, method.argumentTypes.length).mapToObj(i -> new Pair<>(method, (long) i)).
                    forEach(ma -> create(tables.methodArg, ma));
        return getId(tables.method, method);
    }

    public ClassSource getClassSource(Class clazz) {
        ClassSource classSource = classToArtifact.get(clazz);
        return classSource == null ? ClassSource.jre : classSource;
    }


}
