package hk.ssutt.deploy;

import java.io.File;

public class SQLDeploy {
    public SQLDeploy() {
        long start = System.currentTimeMillis();
        new DeployDB(new File("").getAbsolutePath());
        long end = System.currentTimeMillis();
        System.out.println("Database deployed in: " + (end - start) / 1_000 + " sec");
    }
}
