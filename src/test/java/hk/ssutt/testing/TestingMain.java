package hk.ssutt.testing;

import hk.ssutt.api.admin.PasswordHandler;
import hk.ssutt.api.admin.User;
import hk.ssutt.api.sql.SQLMethods;
import hk.ssutt.deploy.DeployDB;
import hk.ssutt.deploy.DeploySSUTT;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
	    d.deploy();
        DeployDB db = d.getDeployDB();
        SQLMethods sqlm = SQLMethods.getInstance(db.getConnection());
        System.out.println(sqlm.addHead("yoba","pass","knt","151"));
        User u = sqlm.getHead("knt", "151");
        System.out.println(u.getName());
        System.out.println(u.getPassword());
        
    }
}