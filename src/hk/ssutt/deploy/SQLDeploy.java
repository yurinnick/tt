package hk.ssutt.deploy;

import java.io.File;
import java.sql.Connection;

public class SQLDeploy {
    private static DeployDB d = null;

    public SQLDeploy() {
        long start = System.currentTimeMillis();
        d = new DeployDB(new File("").getAbsolutePath());
        long end = System.currentTimeMillis();
        System.out.println("Database deployed in: " + (end - start) / 1_000 + " sec");
    }

    public  Connection getDB() {
        return d.getConnection();
    }
}
