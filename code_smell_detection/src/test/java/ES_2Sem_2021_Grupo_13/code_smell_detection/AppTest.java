package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void testingProjectParsing() {

		try {
			projectParser test = new projectParser(Paths.get(System.getProperty("user.dir") + "/junitTestFiles"));
			test.parseJavaFiles();
			String[][] arrTest = XLSX_read_write
					.readyExcelForGUI(System.getProperty("user.dir") + "/" + "Code_Smells_Testes.xlsx");
			String[][] arrTest2 = test.getParsedFilesTabularData();
		//	assertTrue(Arrays.deepEquals(arrTest, arrTest2));
			System.out.println("hello there...");
			HashMap<String, String> testAgainst = new HashMap<String, String>();
			HashMap<String, String> stats = test.getProjectData();
			testAgainst.put("packages", "0");
			testAgainst.put("classCounter", "2");
			testAgainst.put("totalLOC", "76");
			testAgainst.put("methodCountID", "8");
			assertEquals(testAgainst, stats);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
