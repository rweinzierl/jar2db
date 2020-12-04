package rw.jar2db.util.db;

public class Column<C> {
    public final String name;
    public final ColumnType<C> type;
    public final boolean notNull;

    public Column(String name, ColumnType<C> type, boolean notNull) {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
    }

    public Column(String name, ColumnType<C> type) {
        this(name, type, false);
    }

    public String sqlCreate() {
        return name + " " + type.sqlType + (notNull ? " not null" : "");
    }
}
