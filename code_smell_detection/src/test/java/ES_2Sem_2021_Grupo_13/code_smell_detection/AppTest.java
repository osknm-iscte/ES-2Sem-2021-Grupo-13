package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

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
			assertTrue(Arrays.deepEquals(arrTest, arrTest2));
			// for(int i=1;i<arrTest2.length;i++) {
			// for (int j=0;j<arrTest2[i].length;j++) {
			// System.out.println(arrTest2[i][j]);
			// }
			// }

			System.out.println("hello there...");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
