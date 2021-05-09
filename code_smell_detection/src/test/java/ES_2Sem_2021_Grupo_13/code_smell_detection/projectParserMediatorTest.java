package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class projectParserMediatorTest {
	
	@Test
	public void testgetMethodCountID() {
		
		projectParserMediator.getMethodCountID();
		
	}
	
	@Test
	public void testincrementMethodCount() {
		
		projectParserMediator.incrementMethodCountID();
		int result= projectParserMediator.getMethodCountID();
		assertEquals(result, 265);
		
				
		
	}
	
	@Test
	public void getClassCounter() {
		
		projectParserMediator.getClassCounter();
		
		
	}
	
	@Test
	public void testincrementClassCounter() {
		projectParserMediator.incrementClassCounter();
		int result= projectParserMediator.getClassCounter();
		assertEquals(result, 53);
		
	}
	
	@Test
	public void testgetTotalLOC() {
		
		projectParserMediator.getTotalLOC();
		int totalLoc = 2;
		projectParserMediator.addTotalLOC(totalLoc);
		
	}
	

	@Test
	public void testgetNumberOfPackages() {
		projectParserMediator.getNumberOfPackages();
		
	}
	
	@Test
	public void testincrementNumberOfPackages() {
		projectParserMediator.incrementNumberOfPackages();
		int result= projectParserMediator.getNumberOfPackages();
		assertEquals(result, 43);
	}

}
