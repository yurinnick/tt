package hk.ssutt.testing;

import hk.ssutt.deploy.SQLDeploy;

import java.sql.Connection;

public class TestingMain {
    public static void main(String[] args) {
        SQLDeploy sqld  = new SQLDeploy();
        Connection myDB = sqld.getDB();
    }
}
