package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class XMLParser {
	
	public static List<String> getRulesName(String path) {
	
		   // Instantiate the Factory
		
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	      try {
	    	  List<String>ruleNames=new ArrayList<String>();
	          // optional, but recommended
	          // process XML securely, avoid attacks like XML External Entities (XXE)
	          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	          // parse XML file
	          DocumentBuilder db = dbf.newDocumentBuilder();

	          Document doc = db.parse(new File(path));

	          // optional, but recommended
	          // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	          doc.getDocumentElement().normalize();

	          System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
	          System.out.println("------");

	          // get <staff>
	          NodeList list = (NodeList) doc.getElementsByTagName("rule");

	          for (int temp = 0; temp < ((org.w3c.dom.NodeList) list).getLength(); temp++) {

	              Node node =((org.w3c.dom.NodeList) list).item(temp);

	              if (node.getNodeType() == Node.ELEMENT_NODE) {
	            	  Element element = (Element) node;
	            	  String name = element.getElementsByTagName("name").item(0).getTextContent();
	            	  ruleNames.add(name);
	                 
	              }
	          }
	          return ruleNames;

	      } catch (ParserConfigurationException | SAXException | IOException e) {
	          e.printStackTrace();
	      }
		return null;

	  }
		
	}


