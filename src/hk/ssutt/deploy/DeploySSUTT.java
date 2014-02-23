package hk.ssutt.deploy;

import java.sql.Connection;

/**
 * Created by fau on 23/02/14.
 */
public class DeploySSUTT {
    private static DeployDB d;
    private static DeploySSUTT deploy;

    private DeploySSUTT() {
    }

    public static DeploySSUTT getInstance() {
        if (deploy == null) {
            deploy = new DeploySSUTT();

            long start = System.currentTimeMillis();

            FSDeploy fsd = new FSDeploy();

            if (!fsd.hasTTInstance()) {
                d = new DeployDB(fsd.getTTDirPath());
                long end = System.currentTimeMillis();

                long deployTime = (end - start) / 1_000;

                System.out.println("Database deployed in: " + deployTime + " sec");
                fsd.deployFS(d.getConnection());
            } else
                System.out.println("TT was deployed before.");
            d = new DeployDB(fsd);
        }
        return deploy;
    }


    public Connection getDBConnection() {
        return d.getConnection();
    }
}

