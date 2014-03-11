package hk.ssutt.api.parsing.json;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fau on 11/03/14.
 */
public class JSONHandler {
    private static JSONHandler handler;
    private static String[] days = {"mon", "tue", "wed", "thu", "fri", "sat"};

    private JSONHandler() {
    }

    public static JSONHandler getInstance() {
        if (handler == null) {
            handler = new JSONHandler();
        }

        return handler;
    }

    public void fillTimetableFiles(String[][] table) {
        JSONObject result = new JSONObject();

        Map tm = new TimetableMap();


        Map evenClass = new DayMap();
        Map oddClass = new DayMap();

        for (int i = 0; i < 7; i++) {
            Map evenDay = new ClassMap();
            Map oddDay = new ClassMap();


            for (int j = 0; j < table[i].length; j++) {
                String[] divided = classesDivision(table[i][j]);


                evenDay.put(days[j], divided[0].trim());
                oddDay.put(days[j], divided[1].trim());

            }
            evenClass.put(i + 1, evenDay);
            oddClass.put(i + 1, oddDay);

        }

        tm.put("even", evenClass);
        tm.put("odd", oddClass);

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(tm, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonText = out.toString();
        System.out.print(jsonText);
    }

    public String[] classesDivision(String aClass) {
        String[] result = new String[2];
        //has any?
        if (aClass.startsWith("чис.") || aClass.startsWith("знам.")) {
            if (aClass.startsWith("чис.")) {
                aClass = aClass.substring(aClass.indexOf("чис."), aClass.indexOf("знам."));
                result[0] = aClass.replace("чис.", "");
                result[1] = "";
                return result;
            } else {
                aClass = aClass.substring(aClass.indexOf("знам."), aClass.length() - 1);
                result[0] = "";
                result[1] = aClass.replace("знам.", "");
                return result;
            }
        } else {
            result[0] = result[1] = aClass;
        }

        return result;
    }

}

class DayMap extends LinkedHashMap<String, String> {
}

//1: monday: ..., tuesday: ...; 2: monday: ...
class ClassMap extends LinkedHashMap<Integer, DayMap> {
}

//even: 1: monday and so on; odd: 2: ...
class TimetableMap extends LinkedHashMap<String, ClassMap> {
}

