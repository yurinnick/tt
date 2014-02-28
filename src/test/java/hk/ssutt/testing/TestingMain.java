package hk.ssutt.testing;

import hk.ssutt.api.sql.SQLMethods;
import hk.ssutt.deploy.DeployDB;
import hk.ssutt.deploy.DeploySSUTT;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
	    d.deploy();
        DeployDB db = d.getDeployDB();
        SQLMethods sqlm = SQLMethods.getInstance(db.getConnection());
       System.out.println(sqlm.dropAllFacultiesProtectedState()?"Good":"Bad");
    }
}