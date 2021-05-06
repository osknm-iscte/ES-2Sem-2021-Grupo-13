package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class XMLParserTest {

	@Test
	void testGetRulesName() {
		ArrayList<String> str=new ArrayList<String>();
		str.add("rule 1");
		str.add("rule 2");
		str.add("myruletestwrite");
		
//		ArrayList<String> str2= (ArrayList<String>) XMLParser.getRulesName(System.getProperty("user.dir")+"/"+"code_smell_rule_definitions.xml");
//		assertEquals(str, str2);
		
		
		
	}
	
	@Test
	void testWriteRules() {
		
		try {
			XMLParser.createRule(System.getProperty("user.dir")+"/"+"code_smell_rule_definitions.xml",
													"123", "myruletestwrite", "if(x>10)something cool will happen;");
			//assertTrue( FileUtils.contentEquals(new File(System.getProperty("user.dir")+"/"+"code_smell_rule_definitions.xml"), 
			//		new File(System.getProperty("user.dir")+"/"+"code_smell_rule_definitionsTest.xml")));
			
		} catch (TransformerConfigurationException | ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
