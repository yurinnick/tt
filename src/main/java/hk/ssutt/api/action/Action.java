package hk.ssutt.api.action;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.parsing.global.GlobalParser;
import hk.ssutt.api.parsing.html.HTMLParser;
import hk.ssutt.api.parsing.json.JSONHandler;
import hk.ssutt.api.parsing.xml.XMLParser;
import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.api.sql.SQLManager;

import java.util.List;

public class Action {
    private static Action ac;

    private FSHandler fsh;

    private SQLHandler sqlh;
    private SQLManager sqlm;


    private JSONHandler jsh;

    private Action(){
        fsh = FSHandler.getInstance();
        sqlm = sqlm.getInstance();

        if (sqlm.getConnection() == null)
            sqlm.createConnection(fsh.getTTDirPath());

        sqlh = SQLHandler.getInstance(sqlm.getConnection());

        jsh = JSONHandler.getInstance();
    }

    public static Action getInstance(){
        if (ac==null)
            ac = new Action();
        return ac;
    }

    public void fillGroupSchedule(String faculty, String group){
        String file = sqlh.getGroupFile(faculty, group);
        String groupAddress = sqlh.getGroupWebAddress(faculty, group);
        System.out.println(file);

        GlobalParser gp = new GlobalParser(groupAddress);

        String[][] table = gp.parse();

        jsh.fillTimetableFile(table, file);
    }

    public void fillFacultySchedule(String faculty) {
        for (String group: sqlh.getGroupID(faculty)) {
            fillGroupSchedule(faculty, group);
        }
    }

    public void fillAllSchedule() {
        for (String fac: sqlh.getAllFacultiesID()){
            fillFacultySchedule(fac);
        }
    }

    public void managedFillGroup(String faculty, String group) {
        if (!sqlh.isGroupManaged(faculty,group)) {
            fillGroupSchedule(faculty, group);
        }
        else {

            //send message to manager
        }

    }

    public void managedFillFaculty(String faculty) {
        for (String group: sqlh.getGroupID(faculty)) {
            managedFillGroup(faculty, group);
        }
    }

    public void managedFillAll() {
        for (String fac: sqlh.getAllFacultiesID()){
            managedFillFaculty(fac);
        }
    }


}
