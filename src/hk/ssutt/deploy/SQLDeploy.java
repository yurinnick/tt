package hk.ssutt.deploy;

import java.io.File;
import java.sql.Connection;

public class SQLDeploy {
    private static DeployDB d = null;

    public SQLDeploy() {
        long start = System.currentTimeMillis();
        d = new DeployDB(new File("").getAbsolutePath());
        long end = System.currentTimeMillis();

        long deployTime = (end - start) / 1_000;

        if (deployTime != 0)
            System.out.println("Database deployed in: " + (end - start) / 1_000 + " sec");
        else
            System.out.println("Database was deployed before.");
    }

    public  Connection getDB() {
        return d.getConnection();
    }
}
