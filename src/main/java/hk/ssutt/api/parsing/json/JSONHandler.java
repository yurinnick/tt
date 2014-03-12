package hk.ssutt.api.parsing.json;

import org.json.simple.JSONValue;

import java.io.*;
import java.util.LinkedHashMap;

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

    public void fillTimetableFile(String[][] table, String path) {
        TimetableMap tm = new TimetableMap();
        ClassMap evenDay = new ClassMap();
        ClassMap oddDay = new ClassMap();

        for (int i = 0; i < 7; i++) {
            DayMap evenClass = new DayMap();
            DayMap oddClass = new DayMap();

            for (int j = 0; j < table[i].length; j++) {
                String[] divided = classesDivision(table[i][j]);
                evenClass.put(days[j], divided[0].trim());
                oddClass.put(days[j], divided[1].trim());
            }

            evenDay.put(i + 1, evenClass);
            oddDay.put(i + 1, oddClass);
        }

        tm.put("even", evenDay);
        tm.put("odd", oddDay);

        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            StringWriter out = new StringWriter();) {

            JSONValue.writeJSONString(tm, out);
            writer.write(out.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
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

