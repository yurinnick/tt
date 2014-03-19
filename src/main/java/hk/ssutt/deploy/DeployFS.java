package hk.ssutt.deploy;

import hk.ssutt.api.sql.SQLHandler;

import java.io.File;
import java.util.List;

/**
 * Created by fau on 23/02/14.
 */
public class DeployFS {
    /*
    Filesystem must be deployed after creating tables to resemble
    http://sgu.ru/schedule/$FACULTY$/do/$GROUP$ structure
    However all the file operations, including creating directories should be
    placed here, so we create this class just before DeployDB starts
    */

//    private static final String ttDir = "/var/timetables/";
    private static final String ttDir = "timetables/";
    private static final String tempDir = "tmp";
    public static final String dbName = "timetables.db";


    public DeployFS() {
        new File("timetables/").mkdir();
    }

    public void deployFS(SQLHandler sqlm) {
        long start = System.currentTimeMillis();

        List<String> faculties = sqlm.getAllFacultiesID();

        for (String s : faculties) {
            List<String> groups = sqlm.getGroupID(s);
            for (String g : groups) {
	            new File("timetables/" + s).mkdirs();
            }
        }
        new File(tempDir).mkdirs();
        double time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.println("FileSystem deployed in: " + time + " sec");
    }

    public static String getTtDir() {
        return ttDir;
    }

    public static String getDbName() {
        return dbName;
    }
}
