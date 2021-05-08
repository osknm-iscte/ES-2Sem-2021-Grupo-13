package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class projectParserMediatorTest {

	projectParserMediator classTest= new projectParserMediator();
	
	
	
	@Test
	public void getMethodCountIDTest() {
		int test=classTest.getMethodCountID();
		test=2;
		assertEquals(test, 2);
	}
	
	
	@Test
	public void getClassCounterTest() {
		
		int test =classTest.getClassCounter();
		test=0;
		assertEquals(test,0);
	
	}
	
	@Test 
	public void getTotalLOCTest()  {
		
		int test =classTest.getTotalLOC();
		test=2;
		assertEquals(test,2);
	
		
	}
	
	

}
