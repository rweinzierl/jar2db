package rw.jar2db;

import javassist.ClassPool;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import rw.jar2db.model.Field;
import rw.jar2db.model.FromJavassist;
import rw.jar2db.model.Method;
import rw.jar2db.util.Noex;
import rw.jar2db.util.db.SQLiteConnector;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.stream.Stream;

public class ModelCreator implements AutoCloseable {

    private final ClassPool classPool = new ClassPool();
    public final ClassSources classSources = new ClassSources(classPool);
    private final Connection conn;

    public ModelCreator(String dbPath) throws SQLException {
        this.conn = SQLiteConnector.connect(Paths.get(dbPath), true);
    }

    public void export() {
        new Tables().tables.forEach(Noex.Consumer.noex(table -> table.create(conn)));

        FromJavassist fromJavassist = new FromJavassist(classPool);

        EntityCreator rr = new EntityCreator(conn);
        classSources.getClassSourceMap().forEach((clazz, artifact) -> rr.addMapping(fromJavassist.get(clazz), artifact));

        classSources.getSources().forEach(rr::getReference);
        classSources.getClasses().forEach(c -> {
            rr.getReference(fromJavassist.get(c));
            Stream.of(c.getDeclaredMethods()).map(fromJavassist::get).forEach(rr::getReference);
            Stream.of(c.getDeclaredFields()).map(fromJavassist::get).forEach(rr::getReference);

            Stream.of(c.getDeclaredMethods()).forEach(m -> {
                LinkedHashSet<Method> referencedMethods = new LinkedHashSet<>();
                LinkedHashSet<Field> referencedFields = new LinkedHashSet<>();
                Noex.exec(() -> m.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall mc) {
                        referencedMethods.add(Noex.silent(mc::getMethod).map(fromJavassist::get).orElse(fromJavassist.getMethod(false, mc.getClassName(), mc.getMethodName(), mc.getSignature())));
                    }

                    @Override
                    public void edit(ConstructorCall mc) {
                        referencedMethods.add(Noex.silent(mc::getConstructor).map(fromJavassist::get).orElse(fromJavassist.getMethod(true, mc.getClassName(), mc.getMethodName(), mc.getSignature())));
                    }

                    @Override
                    public void edit(FieldAccess mc) {
                        referencedFields.add(Noex.silent(mc::getField).map(fromJavassist::get).orElse(fromJavassist.getField(mc.getClassName(), mc.getFieldName(), mc.getSignature())));
                    }
                }));
                referencedMethods.forEach(m2 -> rr.create(fromJavassist.get(m), m2));
                referencedFields.forEach(f -> rr.create(fromJavassist.get(m), f));
            });
        });
    }

    public void close() throws SQLException {
        conn.commit();
        conn.close();
    }
}
