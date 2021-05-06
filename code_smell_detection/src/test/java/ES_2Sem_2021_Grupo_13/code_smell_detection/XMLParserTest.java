package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XMLParserTest {

	@Test
	void testGetRulesName() {

		HashMap<String, String> testAgainstThisHashMap = new HashMap<String, String>();
		testAgainstThisHashMap.put("rule 1", "\nif(LOC_method>10)long_method=true;\nif(LOC_class>10)god_class=true;\n");
		testAgainstThisHashMap.put("rule 2", "\nif(LOC_method>10)long_method=true;\nif(LOC_class>10)god_class=true;\n");
		testAgainstThisHashMap.put("myruletestwrite", "if(x>10)something cool will happen;");
		HashMap<String, String> hashTest = (HashMap<String, String>) XMLParser
				.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml");
		assertTrue(testAgainstThisHashMap.equals(hashTest));

	}

	@Test
	void testWriteRules() {

		try {

			XMLParser.createRule(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml",
					"TEST_ID", "DELETE_ME_AFTER_TEST", "if(x>10)something cool will happen;");
			HashMap<String, String> hashTest = (HashMap<String, String>) XMLParser
					.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml");
			assertTrue(hashTest.containsKey("DELETE_ME_AFTER_TEST"));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			HashMap<String, String> ruleNamesAndDefinitions = new HashMap<String, String>();
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db
					.parse(new File(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml"));
			NodeList nList = (NodeList) doc.getElementsByTagName("rule");
			for (int i = 0; i < ((org.w3c.dom.NodeList) nList).getLength(); i++) {
				Node node = ((org.w3c.dom.NodeList) nList).item(i);
				if (node.getNodeType() == Element.ELEMENT_NODE) {
					Element eElement = (Element) node;
					System.out.println(eElement.getAttribute("id"));
					if (eElement.getAttribute("id").equals("TEST_ID")) {

						node.getParentNode().removeChild(node);
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(
					System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml");
			transformer.transform(source, result);

		} catch (TransformerConfigurationException | ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	void testEditRules() {

		try {

			XMLParser.createRule(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml",
					"TEST_ID", "DELETE_ME_AFTER_TEST", "if(x>10)something cool will happen;");

			XMLParser.editRule(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml",
					"DELETE_ME_AFTER_TEST", "if(x>20) everything is good;");
			HashMap<String, String> hashTest = (HashMap<String, String>) XMLParser
					.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml");
			assertTrue(hashTest.containsKey("DELETE_ME_AFTER_TEST")
					&& hashTest.get("DELETE_ME_AFTER_TEST").equals("if(x>20) everything is good;"));

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			HashMap<String, String> ruleNamesAndDefinitions = new HashMap<String, String>();
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db
					.parse(new File(System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml"));
			NodeList nList = (NodeList) doc.getElementsByTagName("rule");
			for (int i = 0; i < ((org.w3c.dom.NodeList) nList).getLength(); i++) {
				Node node = ((org.w3c.dom.NodeList) nList).item(i);
				if (node.getNodeType() == Element.ELEMENT_NODE) {
					Element eElement = (Element) node;
					System.out.println(eElement.getAttribute("id"));
					if (eElement.getAttribute("id").equals("TEST_ID")) {

						node.getParentNode().removeChild(node);
					}
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(
					System.getProperty("user.dir") + "/" + "code_smell_rule_definitionsTests.xml");
			transformer.transform(source, result);

		} catch (TransformerConfigurationException | ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
