package hk.ssutt.api.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Севак on 22.02.14.
 */

public class DOMParser {
	private static DOMParser parser;
	private File file;
	private String path;

	private DOMParser() {
	}

	public static DOMParser getInstance() {
		if (parser == null) {
			parser = new DOMParser();
		}

		return parser;
	}

	public String[][] parse(File file) throws ParserConfigurationException, SAXException, IOException {
		String[][] table = new String[8][6];

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);

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