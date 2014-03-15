package hk.ssutt.api.parsing.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.List;

/**
 * Created by fau on 16/03/14.
 */
public abstract class JSONFormatter {
    public static String jsonList(List<String> list) {
        JSONArray result = new JSONArray();
        for (String s : list)
            result.add(s);
        return JSONValue.toJSONString(result);
    }
}
