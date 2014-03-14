package hk.ssutt.testing;

import hk.ssutt.api.action.Action;
import hk.ssutt.api.parsing.global.GlobalParser;
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
        Action a = Action.getInstance();
        a.fillGroupSchedule("bf","211");

    }
}