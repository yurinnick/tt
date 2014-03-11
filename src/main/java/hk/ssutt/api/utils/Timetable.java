package hk.ssutt.api.utils;

/**
 * Created by fau on 11/03/14.
 */
public class Timetable {
    private String evenTTFile;
    private String oddTTFile;
    private String xmlPath;
    private String htmlPath;

    public Timetable(String evenTTFile, String oddTTFile, String xmlPath, String htmlPath) {
        this.evenTTFile = evenTTFile;
        this.oddTTFile = oddTTFile;
        this.xmlPath = xmlPath;
        this.htmlPath = htmlPath;
    }

    public String getEvenTTFile() {
        return evenTTFile;
    }

    public String getOddTTFile() {
        return oddTTFile;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public String getHtmlPath() {
        return htmlPath;
    }
}
