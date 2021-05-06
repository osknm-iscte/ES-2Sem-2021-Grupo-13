package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void testingProjectParsing() {
		projectParser test = new projectParser(Paths.get(System.getProperty("user.dir") + "/" + "mongotest.java"));
		test.parseJavaFiles();

	}
	
	
}
