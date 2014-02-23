package hk.ssutt.deploy;

import hk.ssutt.api.fs.FSMethods;
import hk.ssutt.api.sql.SQLMethods;

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

            DeployFS fsd = new DeployFS();
            FSMethods fsm = FSMethods.getInstance();
            FSMethods.setTtDir(DeployFS.getTtDir());
            FSMethods.setTtDBName(DeployFS.getDbName());

            if (!fsm.hasTTInstance()) {
                d = new DeployDB(fsm.getTTDirPath());
                long end = System.currentTimeMillis();

                long deployTime = (end - start) / 1_000;

                System.out.println("Database deployed in: " + deployTime + " sec");
                fsd.deployFS(SQLMethods.getInstance(DeployDB.getConnection()));

                DeployXML.deploy(SQLMethods.getInstance(DeployDB.getConnection()));

            } else
                System.out.println("TT was deployed before.");
            d = new DeployDB(fsm);
        }
        return deploy;
    }

}

