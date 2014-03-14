package hk.ssutt.api.parsing.global;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.parsing.html.HTMLParser;
import hk.ssutt.api.parsing.xml.XMLParser;

import java.io.File;

/**
 * Created by fau on 14/03/14.
 */
public class GlobalParser {
    private XMLParser xmlp;
    private HTMLParser htmlp;
    private FSHandler fsh;

    private File xmlFile;
    private String htmlPath;

    public GlobalParser(String xmlPath, String htmlPath) {
        xmlp = XMLParser.getInstance();
        htmlp = HTMLParser.getInstance();

        fsh = FSHandler.getInstance();
        System.out.println(fsh.generateXMLFilePath());

    }
}
