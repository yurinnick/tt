package hk.ssutt.testing;

import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.deploy.DeployDB;
import hk.ssutt.deploy.DeploySSUTT;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
        d.deploy();
        SQLHandler sqlm = SQLHandler.getInstance(DeployDB.getConnection());
        System.out.println(sqlm.getEvenTT("knt","151"));
    }
}