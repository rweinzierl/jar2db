package rw.jar2db.util.db;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {

    public static Connection connect(Path dbPath, boolean overwrite) throws SQLException {
        if (overwrite) {
            File file = dbPath.toFile();
            if (file.exists() && !file.delete())
                throw new RuntimeException("Cannot delete file " + file);
        }
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        conn.setAutoCommit(false);
        return conn;
    }

}
