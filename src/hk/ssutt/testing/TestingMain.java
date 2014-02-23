package hk.ssutt.testing;

import hk.ssutt.api.sql.SQLMethods;
import hk.ssutt.deploy.DeploySSUTT;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
        SQLMethods sqlm = SQLMethods.getInstance();

        //PLEASSE DON'T FORGET TO CREATE CONNECTION
        sqlm.setConnection(d.getDBConnection());
        System.out.println(sqlm.getAllFacultiesIDs());
    }
}