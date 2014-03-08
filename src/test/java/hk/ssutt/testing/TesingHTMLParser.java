package hk.ssutt.testing;

import hk.ssutt.api.parsing.html.HTMLParser;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Севак on 08.03.14.
 */
public class TesingHTMLParser {
    public static void main(String[] args) throws IOException {
        HTMLParser p = HTMLParser.getInstance();

        URL url = new URL("http://www.sgu.ru/schedule/knt/do/151");
        String[][] res = p.parse(url);

        for(String[] row : res) {
            for(String cur : row) {
                System.out.println(cur);
            }
            System.out.println();
        }
    }
}
