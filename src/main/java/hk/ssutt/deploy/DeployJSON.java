package hk.ssutt.deploy;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.sql.SQLHandler;

import java.io.File;

/**
 * Created by fau on 23/02/14.
 */
public abstract class DeployJSON {
    public static void deploy(SQLHandler sqlm) {
        long start = System.currentTimeMillis();

        FSHandler fsm = FSHandler.getInstance();

        for (String f : sqlm.getAllFacultiesID()) {
            for (String g : sqlm.getGroupsFiles(f)) {
                fsm.touch(new File(g));
            }
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("JSON deployed in: " + time + " sec");
    }
}
