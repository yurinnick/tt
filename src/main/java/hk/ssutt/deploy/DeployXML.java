package hk.ssutt.deploy;

import hk.ssutt.api.fs.FSMethods;
import hk.ssutt.api.sql.SQLMethods;

import java.io.File;
import java.util.List;

/**
 * Created by fau on 23/02/14.
 */
public abstract class DeployXML {
    public static void deploy(SQLMethods sqlm) {
        long start = System.currentTimeMillis();

        FSMethods fsm = FSMethods.getInstance();

        List<String> faculties = sqlm.getAllFacultiesIDs();
        for (String f : faculties) {
            List<String> groups = sqlm.getAllGroupsFilesOnFaculty(f);
            for (String g : groups) {
                fsm.touch(new File(g));
            }
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("XML deployed in: " + time + " sec");
    }
}
