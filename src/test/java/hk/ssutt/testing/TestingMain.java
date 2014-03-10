package hk.ssutt.testing;

import hk.ssutt.api.sql.SQLHandler;
import hk.ssutt.deploy.DeployDB;
import hk.ssutt.deploy.DeploySSUTT;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();

        SQLHandler sqlm = SQLHandler.getInstance(d.deploy());
        System.out.println(sqlm.getGroupWebAddress("knt","151"));

    }
}