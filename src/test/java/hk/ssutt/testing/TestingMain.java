package hk.ssutt.testing;

import hk.ssutt.deploy.DeploySSUTT;

public class TestingMain {
    public static void main(String[] args) {
        DeploySSUTT d = DeploySSUTT.getInstance();
	    d.deploy();
    }
}