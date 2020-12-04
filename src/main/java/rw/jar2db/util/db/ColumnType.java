package rw.jar2db.util.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public abstract class ColumnType<T> {
    public final String sqlType;

    private ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    public abstract void set(PreparedStatement st, int pos, T value) throws SQLException;

    public static ColumnType<String> VARCHAR = new ColumnType<String>("varchar") {
        @Override
        public void set(PreparedStatement st, int pos, String value) throws SQLException {
            st.setString(pos, value);
        }
    };

    public static ColumnType<Long> INTEGER = new ColumnType<Long>("integer") {
        @Override
        public void set(PreparedStatement st, int pos, Long value) throws SQLException {
            if (value != null)
                st.setLong(pos, value);
            else
                st.setNull(pos, Types.INTEGER);
        }
    };
    public static ColumnType<Boolean> BOOLEAN = new ColumnType<Boolean>("integer") {
        @Override
        public void set(PreparedStatement st, int pos, Boolean value) throws SQLException {
            if (value != null)
                st.setLong(pos, value ? 1 : 0);
            else
                st.setNull(pos, Types.INTEGER);
        }
    };

}
