package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class XML_read_write {
	
	private static List<MethodDeclaration> methods;
	
	private final static String XMLWRITEPATH = "C:\\Users\\maria\\Documents\\xml_k.xml"; //has to be changed between computers	
	
	private static void writeXMLFile(String path, String ruleName, String NOM_Class_Parameters, String LOC_Class_Parameters, String WMC_class_Parameters, String is_God_Class_Parameters, String LOC_method_Parameters, String CYCLO_method_Parameters, String is_Long_Method_Parameters ) throws ParserConfigurationException, TransformerException { 
		
//	private static void writeXMLFile(LinkedList<String> valuesToWrite) throws ParserConfigurationException, TransformerException {
		
	//writes parameters to a xml file
		
//		App.writeOutClassMetrics("a", 1,1, methods);
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		 
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        // root element
        Element root = document.createElement("root");
        document.appendChild(root);
        
        // configurations element
        Element config  = document.createElement("configurations");
        root.appendChild(config);
        
       
        
	     // nome da regra element
	      Element nome_regra  = document.createElement("nome_regra");
	      nome_regra.appendChild(document.createTextNode(ruleName));
	      config.appendChild(nome_regra);
      
     
        
        
        // NOM_Class element
        Element NOM_Class  = document.createElement("NOM_Class");
        NOM_Class.appendChild(document.createTextNode(NOM_Class_Parameters));
        config.appendChild(NOM_Class);
        
        // LOC_Class element
        Element LOC_Class  = document.createElement("LOC_Class");
        LOC_Class.appendChild(document.createTextNode(LOC_Class_Parameters));
        config.appendChild(LOC_Class);
        
        // WMC_class element
        Element WMC_class  = document.createElement("WMC_class");
        WMC_class.appendChild(document.createTextNode(WMC_class_Parameters));
        config.appendChild(WMC_class);
        
        // is_God_Class element
        Element is_God_Class  = document.createElement("is_God_Class");
        is_God_Class.appendChild(document.createTextNode(is_God_Class_Parameters));
        config.appendChild(is_God_Class);
        
        // LOC_method element
        Element LOC_method  = document.createElement("LOC_method");
        LOC_method.appendChild(document.createTextNode(LOC_method_Parameters));
        config.appendChild(LOC_method);
        
        // CYCLO_method element
        Element CYCLO_method  = document.createElement("CYCLO_method");
        CYCLO_method.appendChild(document.createTextNode(CYCLO_method_Parameters));
        config.appendChild(CYCLO_method);
        
        // is_Long_Method element
        Element is_Long_Method  = document.createElement("is_Long_Method");
        is_Long_Method.appendChild(document.createTextNode(is_Long_Method_Parameters));
        config.appendChild(is_Long_Method);
        
        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(path));

		
        transformer.transform(domSource, streamResult);
        
        System.out.println("Done creating XML File");

		
	}
	
	private static LinkedList<String> readXMLFile(String path) {
		LinkedList<String> dataFromXML = new LinkedList<String>();
		
        try {
        	 
            File xmlFile = new File(path);           
 
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
            Document doc = documentBuilder.parse(xmlFile); 
            doc.getDocumentElement().normalize();
 
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName()); 
            NodeList nodeList = doc.getElementsByTagName("configurations"); 
            System.out.println("===============================================================");
 

            for (int itr = 0; itr < nodeList.getLength(); itr++) {
 
                Node node = nodeList.item(itr);
 
                System.out.println("\nNode Name :" + node.getNodeName());
 
                if (node.getNodeType() == Node.ELEMENT_NODE) {
 
                    Element eElement = (Element) node;
                    
                    System.out.println("Nome regra : "+ eElement.getElementsByTagName("nome_regra").item(0).getTextContent()); 
                    dataFromXML.add(eElement.getElementsByTagName("nome_regra").item(0).getTextContent());
 
                    System.out.println("NOM_Class : "+ eElement.getElementsByTagName("NOM_Class").item(0).getTextContent()); 
                    dataFromXML.add(eElement.getElementsByTagName("NOM_Class").item(0).getTextContent());
                    
                    System.out.println("LOC_Class : "+ eElement.getElementsByTagName("LOC_Class").item(0).getTextContent());
                    dataFromXML.add(eElement.getElementsByTagName("LOC_Class").item(0).getTextContent());
                    
                    System.out.println("WMC_class : "+ eElement.getElementsByTagName("WMC_class").item(0).getTextContent());
                    dataFromXML.add(eElement.getElementsByTagName("WMC_class").item(0).getTextContent());
                    
                    System.out.println("is_God_Class : "+ eElement.getElementsByTagName("is_God_Class").item(0).getTextContent());
                    dataFromXML.add(eElement.getElementsByTagName("is_God_Class").item(0).getTextContent());
                    
                    System.out.println("LOC_method : "+ eElement.getElementsByTagName("LOC_method").item(0).getTextContent());
                    dataFromXML.add(eElement.getElementsByTagName("LOC_method").item(0).getTextContent());
                    
                    System.out.println("CYCLO_method : "+ eElement.getElementsByTagName("CYCLO_method").item(0).getTextContent());
                    dataFromXML.add(eElement.getElementsByTagName("CYCLO_method").item(0).getTextContent());
                    
                    System.out.println("is_Long_Method : "+ eElement.getElementsByTagName("is_Long_Method").item(0).getTextContent());
                    dataFromXML.add(eElement.getElementsByTagName("is_Long_Method").item(0).getTextContent());
                    
//        
 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
//        System.out.println(dataFromXML);
        
        
		return dataFromXML;	//se acharem que é melhor outro tipo de dados digam, isto é placeholder
		
    }
	
	
	
	private static LinkedList<String> justRules(LinkedList<String> dataFromXML) { //takes out empty spaces, to print on gui
		
		LinkedList<String> justTheRules = new LinkedList<String>();
		
		for(int i = 0; i < dataFromXML.size(); i++) {
			
			if (dataFromXML.get(i) != "") {
				
				justTheRules.add(dataFromXML.get(i));				
				
			}			
		}
		
		System.out.println(justTheRules);
		return justTheRules;
		
	}
	
	
		
		
	public static void main(String[] args) {
		
//		try {
//			
//			writeXMLFile(XMLWRITEPATH, "ruleName","1", "2", "3", "" , "5", "6", "7");		
//		
//		} catch (ParserConfigurationException | TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 	
		
//		formatText(data);
		
		
		LinkedList<String> kant = readXMLFile(XMLWRITEPATH);
		justRules(kant);


	}

	static void formatText(String path, String [] data) throws ParserConfigurationException, TransformerException {
		
		String ruleName = "";
		String nom_class = "";
		String loc_class= "";
		String wmc_class= "";
		String isgodclass_class= "";
		String loc_method= "";
		String cyclo_method= "";
		String islong_method= "";
		
		
		
		for(int i = 0; i < data.length; i++) {
			
			if(i == 0) {
				ruleName = data[i];				
			} else {
				
				if (data[i].contains("NOM_Class")) { //missing the other conditions
					nom_class = data[i];
				}
				
				if (data[i].contains("LOC_Class")) { 
					loc_class = data[i];
				}
				
				if (data[i].contains("WMC_Class")) { 
					wmc_class = data[i];
				}
				
				if (data[i].contains("is_God_Method")) { 
					isgodclass_class = data[i];
				}
				
				if (data[i].contains("LOC_Method")) { 
					loc_method = data[i];
				}
				
				if (data[i].contains("CYCLO_Method")) { 
					cyclo_method = data[i];
				}
				
				if (data[i].contains("is_Long_Method")) { 
					islong_method = data[i];
				}
				
			}
			
			
		}
		
		
		
		writeXMLFile(path, ruleName, nom_class, loc_class, wmc_class, isgodclass_class, loc_method, cyclo_method, islong_method);
		
	}


		


}
