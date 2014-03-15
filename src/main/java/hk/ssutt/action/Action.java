package hk.ssutt.action;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.parsing.global.GlobalParser;
import hk.ssutt.api.parsing.json.JSONFormatter;
import hk.ssutt.api.parsing.json.JSONHandler;
import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.api.sql.SQLManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Action {
    private static Action ac;

    private FSHandler fsh;

    private SQLHandler sqlh;
    private SQLManager sqlm;


    private JSONHandler jsh;

    private Action() {
        fsh = FSHandler.getInstance();
        sqlm = sqlm.getInstance();

        if (sqlm.getConnection() == null)
            sqlm.createConnection(fsh.getTTDirPath());

        sqlh = SQLHandler.getInstance(sqlm.getConnection());

        jsh = JSONHandler.getInstance();
    }

    public static Action getInstance() {
        if (ac == null)
            ac = new Action();
        return ac;
    }

    //============TT fill
    public void fillGroupSchedule(String faculty, String group) {
        String file = sqlh.getGroupFile(faculty, group);
        String groupAddress = sqlh.getGroupWebAddress(faculty, group);

        GlobalParser gp = new GlobalParser(groupAddress);

        String[][] table = gp.parse();

        jsh.fillTimetableFile(table, file);
        System.out.printf("Added timetable for: %s/%s\n", faculty, group);
    }

    public void fillFacultySchedule(String faculty) {
        for (String group : sqlh.getGroupID(faculty)) {
            fillGroupSchedule(faculty, group);
        }
    }

    public void fillAllSchedule() {
        for (String fac : sqlh.getAllFacultiesID()) {
            fillFacultySchedule(fac);
        }
    }

    public void managedFillGroup(String faculty, String group) {
        if (!sqlh.isGroupManaged(faculty, group)) {
            fillGroupSchedule(faculty, group);
        } else {

            //send message to manager
        }

    }

    public void managedFillFaculty(String faculty) {
        for (String group : sqlh.getGroupID(faculty)) {
            managedFillGroup(faculty, group);
        }
    }

    public void managedFillAll() {
        for (String fac : sqlh.getAllFacultiesID()) {
            managedFillFaculty(fac);
        }
    }

    //================TT get timetable
    public String getTTByCode(String faculty, String group) {
        String file = sqlh.getGroupFile(faculty, group);
        Path p = Paths.get(file);
        return fsh.printContents(p);
    }

    public String getTTByName(String faculty, String group) {
        String code = sqlh.getFacultyIDFromName(faculty);
        return getTTByCode(code, group);
    }

    public String getFacultiesList() {
        return JSONFormatter.jsonList(sqlh.getAllFacultiesNames());
    }

    public String getFacultyCodeList() {
        return JSONFormatter.jsonList(sqlh.getAllFacultiesID());
    }

    public String getGroups(String id) {
        return JSONFormatter.jsonList(sqlh.getGroupID(id));
    }

    public String getGroupsByName(String name) {
        String code = sqlh.getFacultyIDFromName(name);
        return getGroups(code);
    }


}
