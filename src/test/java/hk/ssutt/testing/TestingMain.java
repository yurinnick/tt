package hk.ssutt.testing;

import hk.ssutt.api.parsing.json.JSONHandler;
import hk.ssutt.api.parsing.xml.XMLParser;
import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.deploy.DeployDB;
import hk.ssutt.deploy.DeploySSUTT;

import java.io.*;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
        d.deploy();
        SQLHandler sqlm = SQLHandler.getInstance(DeployDB.getConnection());
        XMLParser xmlParser = XMLParser.getInstance();
        String[][] table = xmlParser.parse("schedule_do_151.xls");
        JSONHandler jsh = JSONHandler.getInstance();
        jsh.fillTimetableFile(table, "test");
    }
}