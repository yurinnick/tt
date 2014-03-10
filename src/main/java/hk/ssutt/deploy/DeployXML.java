package hk.ssutt.deploy;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.sql.SQLHandler;

import java.io.File;
import java.util.List;

/**
 * Created by fau on 23/02/14.
 */
public abstract class DeployXML {
    public static void deploy(SQLHandler sqlm) {
        long start = System.currentTimeMillis();

        FSHandler fsm = FSHandler.getInstance();

        List<String> faculties = sqlm.getAllFacultiesID();
        for (String f : faculties) {
            List<String> groups = sqlm.getGroupsFiles(f);
            for (String g : groups) {
                fsm.touch(new File(g));
            }
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("XML deployed in: " + time + " sec");
    }
}
