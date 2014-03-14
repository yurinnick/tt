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

    private static final String ev = "чис.";
    private static final String od = "знам.";

    private JSONHandler() {
    }

    public static JSONHandler getInstance() {
        if (handler == null) {
            handler = new JSONHandler();
        }

        return handler;
    }

    public void fillTimetableFile(String[][] table, String path) {
        System.out.println("Filling " + path);
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

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
             StringWriter out = new StringWriter();) {

            JSONValue.writeJSONString(tm, out);
            writer.write(out.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] classesDivision(String aClass) {
        String[] result = new String[2];

        if (((aClass.indexOf(ev))!=-1)&&(aClass.indexOf(od)!=-1)) {
           if (aClass.indexOf(od)>aClass.indexOf(ev)) {
               result[0] = aClass.substring(0, aClass.indexOf(od)).replace(ev, "");
               result[1] = aClass.substring(aClass.indexOf(od), aClass.length()-1).replace(od, "");
               return result;
           }
           else
           {
               result[1] = aClass.substring(0, aClass.indexOf(ev)).replace(od, "");
               result[0] = aClass.substring(aClass.indexOf(ev), aClass.length()-1).replace(ev, "");
               return result;
           }
        }

        if ((aClass.indexOf(ev) != -1)) {
            //has even marker, in the beggining of the cell and has no odd marker
            if ((aClass.indexOf(ev) == 0) && (aClass.indexOf(od)) == -1) {
                result[0] = aClass.replace(ev, "");
                result[1] = "";
                return result;
            }
            //FAGGOT MODE: has only even marker, although classes are even/odd  (found @ bf/211)
            if ((aClass.indexOf(ev) != 0) && (aClass.indexOf(od)) == -1) {
                result[0] = aClass.substring(aClass.indexOf(ev), aClass.length() - 1).replace(ev, "");
                result[1] = aClass.substring(0, aClass.indexOf(ev));
                return result;
            }
        }

        //same for odd
        if ((aClass.indexOf(od) != -1)) {
            //has even marker, in the beggining of the cell and has no odd marker
            if ((aClass.indexOf(od) == 0) && (aClass.indexOf(ev)) == -1) {
                result[1] = aClass.replace(od, "");
                result[0] = "";
                return result;
            }
            //FAGGOT MODE: has only odd marker, although classes are even/odd  (found @ bf/211 as well)
            if ((aClass.indexOf(od) != 0) && (aClass.indexOf(ev)) == -1) {
                result[1] = aClass.substring(aClass.indexOf(od), aClass.length() - 1).replace(od, "");
                result[0] = aClass.substring(0, aClass.indexOf(od));
                return result;
            }
        }

        //has no markers, both weeks
        result[0] = result[1] = aClass;
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

