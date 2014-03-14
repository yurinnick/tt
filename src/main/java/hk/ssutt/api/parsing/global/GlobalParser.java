package hk.ssutt.api.parsing.global;

import hk.ssutt.api.fs.FSHandler;
import hk.ssutt.api.parsing.html.HTMLParser;
import hk.ssutt.api.parsing.xml.XMLParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fau on 14/03/14.
 */
public class GlobalParser {


    private URL xmlURL;
    private URL groupURL;

    public GlobalParser(String groupURL) {
        try {
            this.xmlURL = new URL(groupURL + "/lesson");
            this.groupURL = new URL(groupURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(xmlURL.toString());
        System.out.println(groupURL.toString());
    }

    public String[][] parse(){
        String[][] result = null;

        FSHandler fsh = FSHandler.getInstance();

        try {
            XMLParser xmlp = XMLParser.getInstance();

            String localTable = fsh.generateXMLFilePath();
            fsh.downloadFile(xmlURL, localTable);

            result = xmlp.parse(localTable);

            fsh.removeTempFile(localTable);
        } catch (IOException e) {
            e.printStackTrace();
            //if some shit with XML happens, we have HTML
            HTMLParser htmlp = HTMLParser.getInstance();
            try {
                result = htmlp.parse(groupURL);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return result;
    }
}
