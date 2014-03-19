package hk.ssutt.testing;

import hk.ssutt.action.Action;
import hk.ssutt.deploy.DeploySSUTT;
import org.json.simple.parser.ParseException;

public class TestingMain {
    public static void main(String[] args) throws ParseException {
        DeploySSUTT d = DeploySSUTT.getInstance();
        d.deploy();
        Action a = Action.getInstance();
        System.out.println(a.getTTByCode("knt","151"));

    }
}