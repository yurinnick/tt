package hk.ssutt.api.parsing.xml;

import hk.ssutt.api.fs.FSHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by Севак on 22.02.14.
 */

public class XMLParser {
    private static XMLParser parser;

    private XMLParser() {
    }

    public static XMLParser getInstance() {
        if (parser == null) {
            parser = new XMLParser();
        }

        return parser;
    }

    private static File skipFirstLine(File inputFile) {
        File outputFile = new File(inputFile.getParent() + "/skipped_" + inputFile.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (count == 0 && line.equals("")) {
                    ++count;
                    continue;
                }

                writer.write(line);
                writer.write("\n");
                ++count;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }

    public String[][] parse(String path) {
        String[][] table = new String[8][6];

        File data = new File(path);
        data = skipFirstLine(data);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(data);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("Data");

        int rowIndex = 0;
        int columnIndex = 0;

        for (int i = 0; i < nodeList.getLength(); ++i) {
            if (i > 7 && !((i - 14) % 7 == 0)) { // Пропускаем дни недели и строки с часами пар
                Node node = nodeList.item(i);
                String line = node.getTextContent().replaceAll("\\t+", " "); // убираем множественную табуляцию
                line = line.replace("\n", " ");

                if (columnIndex >= 6) {
                    columnIndex = 0;
                    ++rowIndex;
                }

                table[rowIndex][columnIndex++] = line;
            }
        }

        return table;
    }
}

