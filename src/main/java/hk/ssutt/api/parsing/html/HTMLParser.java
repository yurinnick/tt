package hk.ssutt.api.parsing.html;

import hk.ssutt.api.fs.FSHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Севак on 24.02.14.
 */
public class HTMLParser {
    private static final String globalScheduleURL = "http://www.sgu.ru/schedule";
    private static final Path exclFile = Paths.get(".excludeFaculty").toAbsolutePath();


    private static HTMLParser parser;

    private HTMLParser() {
    }

    public static HTMLParser getInstance() {
        if (parser == null) {
            parser = new HTMLParser();
        }

        return parser;
    }

    public String[][] parse(URL url) throws IOException {
        String[][] table = new String[8][6];

        Document doc = Jsoup.parse(url, 5000);
        Elements tr = doc.getElementsByTag("tr");

        for (int i = 1; i < 9; ++i) {
            Elements data = tr.get(i).getElementsByTag("td");
            for (int j = 0; j < data.size(); j++) {
                table[i - 1][j] = data.get(j).ownText();
            }
        }

        return table;
    }

    public List<String[]> getFacultiesFromSSU() {
        FSHandler fsm = FSHandler.getInstance();
        List<String[]> result = new ArrayList<>();
        Document doc = null;

        try {
            doc = Jsoup.connect(globalScheduleURL).get();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(2);
        }

        Elements links = doc.select("a[href]");
        //parsing exceptions
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/")) {
                String[] test = link.attr("abs:href").split("/"); //test[4] - the last token like knt,mm,ff

                if (fsm.notInExclusion(test[4], exclFile)) {
                    String[] aFaculty = new String[3];
                    aFaculty[0] = test[4];
                    aFaculty[1] = link.attr("abs:href");
                    aFaculty[2] = link.ownText();

                    result.add(aFaculty);
                }
            }
        }

        return result;

    }

    public List<String[]> getGroupsFromSSU(String url, String faculty) {
        List<String[]> result = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(4);
        }

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            if (link.attr("href").startsWith("/schedule/" + faculty + "/do/")) {
                String s[] = new String[2];
                s[0] = link.ownText();
                String[] esc = link.attr("abs:href").split("/");

                s[1] = esc[esc.length - 1];
                result.add(s);

            }

        }
        return result;
    }
}
