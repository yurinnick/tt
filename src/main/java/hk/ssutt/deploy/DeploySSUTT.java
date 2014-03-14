package hk.ssutt.deploy;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.sql.SQLHandler;

/**
 * Created by fau on 23/02/14.
 */

public class DeploySSUTT {
    private static DeploySSUTT deploy;

    private DeploySSUTT() {
    }

    public static DeploySSUTT getInstance() {
        if (deploy == null) {
            deploy = new DeploySSUTT();
        }

        return deploy;
    }

    public void deploy() {
        long start = System.currentTimeMillis();

        DeployFS fsd = new DeployFS();
        FSHandler fsm = FSHandler.getInstance();
        FSHandler.setTtDir(DeployFS.getTtDir());
        FSHandler.setTtDBName(DeployFS.getDbName());

        if (!fsm.hasTTInstance()) {
            new DeployDB(fsm.getTTDirPath());
            double time = (System.currentTimeMillis() - start) / 1_000.0;

            System.out.println("Database deployed in: " + time + " sec");
            fsd.deployFS(SQLHandler.getInstance(DeployDB.getConnection()));

            DeployJSON.deploy(SQLHandler.getInstance(DeployDB.getConnection()));
            DeployDB.closeConnection();
        } else {
            System.out.println("TT was deployed before.");
            new DeployDB(fsm);
        }

    }

}

