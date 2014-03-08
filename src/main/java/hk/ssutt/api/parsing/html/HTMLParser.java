package hk.ssutt.api.parsing.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Севак on 24.02.14.
 */
public class HTMLParser {
    private static HTMLParser parser;

    private HTMLParser(){}

    public static HTMLParser getInstance() {
        if(parser == null) {
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
}
