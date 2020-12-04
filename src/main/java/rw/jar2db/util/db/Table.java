package rw.jar2db.util.db;

import rw.jar2db.util.Noex;
import rw.jar2db.util.TriFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table<T, Ctx, S extends Table<T, Ctx, S>> {
    public final String name;
    public List<Column<?>> columns = new ArrayList<>();
    private final List<BiFunction<T, Ctx, ?>> getters = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public <C> S add(Column<C> column, BiFunction<T, Ctx, C> getter) {
        columns.add(column);
        getters.add(getter);
        return (S) this;
    }

    public <C> S add(Column<C> column, TriFunction<T, Ctx, S, C> getter) {
        columns.add(column);
        getters.add((entity, ctx) -> getter.apply(entity, ctx, (S) this));
        return (S) this;
    }

    public <C> S add(Column<C> column, Function<T, C> getter) {
        return add(column, (entity, ctx) -> getter.apply(entity));
    }

    public void create(Connection conn) throws SQLException {
        conn.createStatement().execute(String.format("create table %s (%s)",
                name,
                columns.stream().map(Column::sqlCreate).collect(Collectors.joining(", "))));
    }

    public PreparedStatement prepareInsert(Connection conn) throws SQLException {
        return conn.prepareStatement(String.format("insert into %s(%s) values(%s)",
                name,
                columns.stream().map(c -> c.name).collect(Collectors.joining(", ")),
                columns.stream().map(c -> "?").collect(Collectors.joining(", "))));
    }

    public void insert(Connection conn, Ctx ctx, T entity) throws SQLException {
        insert(prepareInsert(conn), ctx, entity);
    }

    public void insert(PreparedStatement st, Ctx ctx, T entity) throws SQLException {
        for (int i = 0; i < columns.size(); i++)
            ((ColumnType) columns.get(i).type).set(st, i + 1, getters.get(i).apply(entity, ctx));
        st.executeUpdate();
    }

    public void insert(Connection conn, Ctx ctx, Stream<T> entities) throws SQLException {
        PreparedStatement st = prepareInsert(conn);
        entities.forEach(Noex.Consumer.noex(entity -> insert(st, ctx, entity)));
    }

}
