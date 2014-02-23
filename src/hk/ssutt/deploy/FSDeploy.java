package hk.ssutt.deploy;

import hk.ssutt.api.sql.SQLMethods;

import java.io.File;
import java.sql.Connection;
import java.util.List;

/**
 * Created by fau on 23/02/14.
 */
public class FSDeploy {
    /*
    Filesystem must be deployed after creating tables to resemble
    http://sgu.ru/schedule/$FACULTY$/do/$GROUP$ structure
    However all the file operations, including creating directories should be
    placed here, so we create this class just before DeployDB starts
    */

    private static final String ttDir = "/var/timetables/";
    private static File f;

    public FSDeploy() {
        //in production we should use ttDir!
        f = new File("timetables/");

        //we delegate rights management to deployment script
        f.mkdir();
    }

    public boolean hasTTInstance() {
        f = new File("timetables/timetables.db");
        if (f.isFile()) {
            String[] names = new File("timetables/").list();

            //not a really good directory check
            //at least something
            for (String s : names)
                if (new File("timetables/" + s).isDirectory())
                    return true;

        }
        return false;
    }

    public String getTTDirPath() {
        return f.getAbsolutePath();
    }

    public void deployFS(Connection c) {
        long start = System.currentTimeMillis();

        SQLMethods sqlm = SQLMethods.getInstance();
        sqlm.setConnection(c);

        List<String> faculties = sqlm.getAllFacultiesIDs();

        for (String s : faculties) {
            List<String> groups = sqlm.getGroupListOnFaculty(s);
            for (String g : groups) new File("timetables/" + s + "/" + g).mkdirs();
        }
        long end = System.currentTimeMillis();
        long deployTime = (end - start) / 1_000;

        System.out.println("FileSystem deployed in: " + deployTime + " sec");
    }

}
