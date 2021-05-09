package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XML_read_writeTest {
	
	static final String PATH  = System.getProperty("user.dir") + "/" + "Smells.xml";
	
	@Test
	public void testwriteXMLFile() {
			
		
		String ruleName = "rule1";
		String NOM_Class_Parameters ="2";
		String LOC_Class_Parameters="3";
		String WMC_class_Parameters="4";
		String is_God_Class_Parameters="5";
		String LOC_method_Parameters="6";
		String CYCLO_method_Parameters="7";
		String is_Long_Method_Parameters="8";

		
		boolean thrown = false;
		try {
			XML_read_write.toTestWriteXMLFile(PATH, ruleName, NOM_Class_Parameters, LOC_Class_Parameters, WMC_class_Parameters, is_God_Class_Parameters, LOC_method_Parameters, CYCLO_method_Parameters, is_Long_Method_Parameters);
		} catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
			
			thrown = true;			
			
		}
		
		assertTrue(thrown); //true se for num computador onde se possa escrever aquele ficheiro, false en caso contrario
		
	}
	
	@Test	
	public void testreadXMLFile() {
		XML_read_write.toTestreadXMLFile(PATH);
	}
	
	@Test
	public void testjustRules () {
		
		LinkedList<String> data = new LinkedList<String>();
		
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		
		XML_read_write.toTestjustRules(data);
		
	}
	
	@Test
	public void testformatText() {
		
		String [] data = {"rulename","NOM_Class", "LOC_Class", "WMC_Class", "is_God_Method", "LOC_Method", "CYCLO_Method", "CYCLO_Method", "is_Long_Method"};
		boolean thrown = false;
		
		try {
			XML_read_write.formatText(PATH, data);
		} catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {

			thrown = true;
		
		}
		assertTrue(thrown);
	}
		
	
	
	


	
	
	
	
	
	
	

}
