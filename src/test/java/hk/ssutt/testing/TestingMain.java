package hk.ssutt.testing;

import hk.ssutt.action.Action;
import hk.ssutt.deploy.DeploySSUTT;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class TestingMain {
    public static void main(String[] args) throws ParseException {
        DeploySSUTT d = DeploySSUTT.getInstance();
        d.deploy();
        Action a = Action.getInstance();
        String s = a.getTT("knt", "151");

        Object obj = JSONValue.parse(s);
        JSONArray even = (JSONArray) obj;
        System.out.println(even.get(1));
        JSONArray first = (JSONArray) even.get(1);

        JSONArray mon = (JSONArray) first.get(0);
        JSONObject cl = (JSONObject) mon.get(0);
        System.out.println(cl);

    }
}