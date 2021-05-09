package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class projectParserTest {

	// methods to test:
	// getProjectData
	// writeFile
	@Test
	void testGetProjectData() {
		projectParser test = new projectParser(Paths.get(System.getProperty("user.dir") + "/junitTestFiles"));
		test.parseJavaFiles();
		HashMap<String, String> testAgainst = new HashMap<String, String>();
		HashMap<String, String> stats = test.getProjectData();
		testAgainst.put("packages", "42");
		testAgainst.put("classCounter", "50");
		testAgainst.put("totalLOC", "5264");
		testAgainst.put("methodCountID", "256");
		assertEquals(testAgainst, stats);
	}
	

}
