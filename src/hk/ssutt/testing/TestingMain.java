package hk.ssutt.testing;

import hk.ssutt.api.sql.SQLMethods;
import hk.ssutt.deploy.SQLDeploy;

public class TestingMain {
    public static void main(String[] args) {
        SQLDeploy sqld  = new SQLDeploy();
        SQLMethods sqlm = new SQLMethods(sqld.getDB());
    }
}
