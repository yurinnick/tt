package hk.ssutt.testing;


import hk.ssutt.api.parsing.json.JSONHandler;
import hk.ssutt.api.parsing.xml.XMLParser;

/**
 * Created by fau on 11/03/14.
 */
public class TestingJSON {
    public static void main(String[] args) {
        XMLParser p = XMLParser.getInstance();
        JSONHandler jsh = JSONHandler.getInstance();

        String[][] table = p.parse("lesson");
        jsh.fillTimetableFiles(table);
    }
}
