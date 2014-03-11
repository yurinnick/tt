package hk.ssutt.testing;


import hk.ssutt.api.parsing.json.JSONHandler;
import hk.ssutt.api.parsing.xml.XMLParser;
import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.deploy.DeployDB;
import hk.ssutt.deploy.DeploySSUTT;

/**
 * Created by fau on 11/03/14.
 */
public class TestingJSON {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
        d.deploy();

        SQLHandler sqlh = SQLHandler.getInstance(DeployDB.getConnection());
        XMLParser p = XMLParser.getInstance();
        JSONHandler jsh = JSONHandler.getInstance();


        String[][] table = p.parse("lesson");
        jsh.fillTimetableFile(table, sqlh.getTT("knt","151"));
    }
}
