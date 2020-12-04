package rw.jar2db;

import javassist.Modifier;
import rw.jar2db.model.Class;
import rw.jar2db.model.Field;
import rw.jar2db.model.Method;
import rw.jar2db.util.Pair;
import rw.jar2db.util.db.Column;
import rw.jar2db.util.db.ColumnType;

import java.util.ArrayList;
import java.util.List;

public class Tables {

    public static class Table<T> extends rw.jar2db.util.db.Table<T, EntityCreator, Table<T>> {
        public Table(String name) {
            super(name);
        }

        public Table<T> addId() {
            return add(new Column<>("id", ColumnType.INTEGER), (entity, ctx) -> ctx.getId(this, entity));
        }

    }

    public final List<Table<?>> tables = new ArrayList<>();

    private <T> Table<T> add(Table<T> table) {
        tables.add(table);
        return table;
    }

    public static Boolean hasModifier(Integer modifiers, int modifier) {
        if (modifiers == null) return null;
        else
            return (modifiers & modifier) != 0;
    }

    public Table<ClassSource> artifact = add(new Table<ClassSource>("artifact").
            addId().
            add(new Column<>("type", ColumnType.VARCHAR), x -> x.type.toString()).
            add(new Column<>("group_id", ColumnType.VARCHAR), x -> x.groupId).
            add(new Column<>("artifact_id", ColumnType.VARCHAR), x -> x.artifactId).
            add(new Column<>("version", ColumnType.VARCHAR), x -> x.version).
            add(new Column<>("local_path", ColumnType.VARCHAR), x -> x.localPath)
    );


    public Table<Class> clazz = add(new Table<Class>("class").
            addId().
            add(new Column<>("package_name", ColumnType.VARCHAR), x -> x.packageName).
            add(new Column<>("simple_name", ColumnType.VARCHAR), x -> x.simpleName).
            add(new Column<>("type", ColumnType.VARCHAR), x -> Modifiers.getClassType(x.modifiers)).
            add(new Column<>("is_primitive", ColumnType.BOOLEAN), x -> x.isPrimitive).
            add(new Column<>("is_array", ColumnType.BOOLEAN), x -> x.isArray).
            add(new Column<>("is_static", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.STATIC)).
            add(new Column<>("is_abstract", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.ABSTRACT)).
            add(new Column<>("is_final", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.FINAL)).
            add(new Column<>("visibility", ColumnType.VARCHAR), x -> Modifiers.getVisibility(x.modifiers)).
            add(new Column<>("artifact_id", ColumnType.INTEGER), (x, ctx) -> ctx.getReference(ctx.getClassSource(x))).
            add(new Column<>("declaring_class", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x.declaringClass)).
            add(new Column<>("component_class", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x.componentType)).
            add(new Column<>("super_class", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x.superClass)).
            add(new Column<>("major_version", ColumnType.INTEGER), (x, ctx) -> x.majorVersion)
    );

    public Table<Field> field = add(new Table<Field>("field").
            addId().
            add(new Column<>("name", ColumnType.VARCHAR), x -> x.name).
            add(new Column<>("is_static", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.STATIC)).
            add(new Column<>("is_final", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.FINAL)).
            add(new Column<>("visibility", ColumnType.VARCHAR), x -> Modifiers.getVisibility(x.modifiers)).
            add(new Column<>("declaring_class", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x.declaringType)).
            add(new Column<>("field_type", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x.type))
    );

    public Table<Method> method = add(new Table<Method>("method").
            addId().
            add(new Column<>("name", ColumnType.VARCHAR), x -> x.name).
            add(new Column<>("is_constructor", ColumnType.BOOLEAN), x -> x.isConstructor).
            add(new Column<>("is_static", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.STATIC)).
            add(new Column<>("is_abstract", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.ABSTRACT)).
            add(new Column<>("is_final", ColumnType.BOOLEAN), x -> hasModifier(x.modifiers, Modifier.FINAL)).
            add(new Column<>("visibility", ColumnType.VARCHAR), x -> Modifiers.getVisibility(x.modifiers)).
            add(new Column<>("declaring_class", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x.declaringType)).
            add(new Column<>("return_type_class", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference((x.isConstructor) ? x.returnType : null))
    );

    public Table<Pair<Method, Long>> methodArg = add(new Table<Pair<Method, Long>>("method_arg").
            add(new Column<>("method", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x._1)).
            add(new Column<>("pos", ColumnType.INTEGER), x -> x._2)).
            add(new Column<>("arg_type", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x._1.argumentTypes[x._2.intValue()])
            );

    public Table<Pair<Method, Method>> methodReference = add(new Table<Pair<Method, Method>>("method_reference").
            add(new Column<>("source_method", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x._1)).
            add(new Column<>("target_method", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x._2))
    );

    public Table<Pair<Method, Field>> fieldReference = add(new Table<Pair<Method, Field>>("field_reference").
            add(new Column<>("source_method", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x._1)).
            add(new Column<>("target_field", ColumnType.INTEGER), (x, ctx, t) -> ctx.getReference(x._2))
    );
}
