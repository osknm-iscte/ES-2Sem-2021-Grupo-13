package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * @author iscte-iul grupo 13
 * Esta classe serve para ler/escrever as regras de detecção de code smells definidas pelos
 * utilizadores. As regras estão definidas num fichero XML. Os metodos desta classe permitem
 * criar regras, modificar regras, escolher regras no cálculo de code smells durante execução
 * do programa.
 *
 */
public class XMLParser {
	
	/**
	 * Devolve os nomes das regras e as suas definições a partir de um ficheiro xml.
	 * @param path caminho onde estão as regras
	 * @return HashMap com nome das regras existentes como chave e definição das regras como valor
	 */
	public static HashMap<String,String> getRulesName(String path) {
	
		   // Instantiate the Factory
		
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	      try {
	    	  HashMap<String, String>ruleNamesAndDefinitions=new HashMap<String,String>();
	          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);	
	          DocumentBuilder db = dbf.newDocumentBuilder();
	          Document doc = db.parse(new File(path));
	          doc.getDocumentElement().normalize();

	          System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
	          System.out.println("------");

	          NodeList list = (NodeList) doc.getElementsByTagName("rule");

	          for (int temp = 0; temp < ((org.w3c.dom.NodeList) list).getLength(); temp++) {

	              Node node =((org.w3c.dom.NodeList) list).item(temp);

	              if (node.getNodeType() == Node.ELEMENT_NODE) {
	            	  Element element = (Element) node;
	            	  String name = element.getElementsByTagName("name").item(0).getTextContent();
	            	  String ruleDefinition = element.getElementsByTagName("definition").item(0).getTextContent();
	            	  System.out.println(ruleDefinition);
	            	  ruleNamesAndDefinitions.put(name, ruleDefinition);
	            	  
	                 
	              }
	          }
	          return ruleNamesAndDefinitions;

	      } catch (ParserConfigurationException | SAXException | IOException e) {
	          e.printStackTrace();
	      }
		return null;

	  }
	
	/**
	 * Cria nova regra no ficheiro xml com os parâmetros recebidos.
	 * @param path caminho que representa o ficheiro xml onde estão definidas as regras
	 * @param elementID id da regra a ser criada
	 * @param ruleName nome da regra a ser criada
	 * @param ruleDefinition definição da regra
	 * @throws ParserConfigurationException pode mandar ParserConfigurationException 
	 * @throws SAXException pode mandar SAXException
	 * @throws IOException pode mandar IOException
	 * @throws TransformerException pode mandar TransformerException
	 */
	public static void createRule(String path,String elementID, String ruleName, String ruleDefinition) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(path);
        Node root = doc.getFirstChild();
        Element rule  = doc.createElement("rule");
        Attr attr = doc.createAttribute("id");
        attr.setValue(elementID);
        rule.setAttributeNode(attr);
        Element ruleNameTag=doc.createElement("name");
        ruleNameTag.appendChild(doc.createTextNode(ruleName));
        Element ruleDefinitionTag=doc.createElement("definition");
        if(ruleDefinition!=null) ruleDefinitionTag.appendChild(doc.createTextNode(ruleDefinition));
        rule.appendChild(ruleNameTag);
        rule.appendChild(ruleDefinitionTag);
        root.appendChild(rule);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        
        StreamResult result = new StreamResult(path);
        transformer.transform(source, result);

        
      //  rule.appendChild(doc.createTextNode(ruleDefinition));
        
	}

	/**
	 * Faz edição da definição da regras
	 * @param path caminho onde estão definidas as regras
	 * @param ruleName nome da regra a editar 
	 * @param ruleDef nova definição da regra
	 * @throws ParserConfigurationException pode mandar ParserConfigurationException 
	 * @throws SAXException pode mandar SAXException
	 * @throws IOException pode mandar IOException
	 * @throws TransformerException pode mandar TransformerException
	 */
	public static void editRule(String path, String ruleName, String ruleDef) throws TransformerException, ParserConfigurationException, SAXException, IOException {
		
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
	    	  HashMap<String, String>ruleNamesAndDefinitions=new HashMap<String,String>();
	          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	          DocumentBuilder db = dbf.newDocumentBuilder();
	          Document doc = db.parse(new File(path));	      
	          doc.getDocumentElement().normalize();
	          // get <staff>
	          NodeList list = (NodeList) doc.getElementsByTagName("rule");

	          for (int temp = 0; temp < ((org.w3c.dom.NodeList) list).getLength(); temp++) {

	              Node node =((org.w3c.dom.NodeList) list).item(temp);

	              if (node.getNodeType() == Node.ELEMENT_NODE) {
	            	  Element element = (Element) node;
	            	  String name = element.getElementsByTagName("name").item(0).getTextContent();
	            	  name.replace("\n", "").replace("\r", "");
	            	  ruleName.replace("\n", "").replace("\r", "");
	            	 // String ruleDefinition = element.getElementsByTagName("definition").item(0).getTextContent();
	            	  if(name.equals(ruleName)) {
	            		   element.getElementsByTagName("definition").item(0).setTextContent(ruleDef);
	            		   TransformerFactory transformerFactory = TransformerFactory.newInstance();
	         	          Transformer transformer = transformerFactory.newTransformer();
	         	          DOMSource source = new DOMSource(doc);
	         	          StreamResult result = new StreamResult(new File(path));
	         	          transformer.transform(source, result);
	         	          return;
	            	  }
	            	  
	            	    
	              }
	          }
	          return;   	
	}
	
	/**
	 * verifica se o nome da  regra existe. É usado sobretudo na GUI 
	 * na parte de criação das regras. Serve para prevenir situação de atribuir o
	 * mesmo nome a mais do que uma regra
	 * @param path caminho onde estão definidas as regras
	 * @param ruleName nome da regra a verificar se existe
	 * @return booleano que devolve true se oo nome da regra existe e false caso contrário
	 * @throws ParserConfigurationException pode mandar ParserConfigurationException 
	 * @throws SAXException pode mandar SAXException
	 * @throws IOException pode mandar IOEXception
	 */
	public static boolean ckeckIfRuleNameExists(String path, String ruleName) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
  	  HashMap<String, String>ruleNamesAndDefinitions=new HashMap<String,String>();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(path));
        

        
        doc.getDocumentElement().normalize();
       
        NodeList list = (NodeList) doc.getElementsByTagName("rule");

        for (int temp = 0; temp < ((org.w3c.dom.NodeList) list).getLength(); temp++) {

            Node node =((org.w3c.dom.NodeList) list).item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
          	  Element element = (Element) node;
          	  String name = element.getElementsByTagName("name").item(0).getTextContent();
          	  name.replace("\n", "").replace("\r", "");
          	  ruleName.replace("\n", "").replace("\r", "");
          	 // String ruleDefinition = element.getElementsByTagName("definition").item(0).getTextContent();
          	  if(name.equals(ruleName)) {
          		   return true;
          	  }
          	    
            }
        }
        
        return false;   	
	}
}

