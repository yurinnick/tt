package hk.ssutt.api.parsing.xml;

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

    public String[][] parse(String path)  {
        String[][] table = new String[8][6];

        File data = new File(path);
       // checkForEmptyLines(data);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder  = null;
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

    //it appeared on knt/151 file, so empty lines in the beginning of the file that caused fatal error
    private void checkForEmptyLines(File f) {
        try {
            RandomAccessFile raf = new RandomAccessFile(f,"rw");
            while (raf.getFilePointer()!=raf.length()){
               //What should be here?
               Byte b = raf.readByte();
               if (b!=10)
                   raf.write(b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

