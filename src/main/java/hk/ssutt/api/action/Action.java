package hk.ssutt.api.action;

import hk.ssutt.api.fs.FSHandler;
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

    private XMLParser xmlp;
    private HTMLParser htmlp;

    private JSONHandler jsh;


    private Action(){
        fsh = FSHandler.getInstance();
        sqlm = sqlm.getInstance();

        if (sqlm.getConnection() == null)
            sqlm.createConnection(fsh.getTTDirPath());

        sqlh = SQLHandler.getInstance(sqlm.getConnection());

        xmlp = XMLParser.getInstance();
        htmlp = HTMLParser.getInstance();

        jsh = JSONHandler.getInstance();
    }

    public static Action getInstance(){
        if (ac==null)
            ac = new Action();
        return ac;
    }

    public void fillGroupSchedule(String faculty, String group){
        String file = sqlh.getGroupFile(faculty, group);
        System.out.println(file);
       // String[][] table = ...;
      //  jsh.fillTimetableFile(table, file);

    }



}
