package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class XMLParserTest {

	@Test
	void testGetRulesName() {
		ArrayList<String> str=new ArrayList<String>();
		str.add("rule 1");
		str.add("rule 2");
		
		ArrayList<String> str2= (ArrayList<String>) XMLParser.getRulesName(System.getProperty("user.dir")+"/"+"code_smell_rule_definitions.xml");
		assertEquals(str, str2);
		
		
	}

}
