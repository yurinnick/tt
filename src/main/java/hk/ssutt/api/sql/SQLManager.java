package hk.ssutt.api.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by fau on 14/03/14.
 */
public class SQLManager {
    private static SQLManager sqlm;
    private static Connection c = null;

    private SQLManager() {
    }

    public static SQLManager getInstance() {
        if (sqlm == null) {
            sqlm = new SQLManager();
        }
        return sqlm;
    }

    public void createConnection(String dbPath) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            } catch (Exception e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
    }

    public static Connection getConnection() {
        return c;
    }

    public void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
