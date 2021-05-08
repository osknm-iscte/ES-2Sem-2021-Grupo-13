package ES_2Sem_2021_Grupo_13.code_smell_detection;

import org.junit.Test;

public class projectParserMediatorTest {
	
	@Test
	public void testgetMethodCountID() {//TODO
		
		projectParserMediator.getMethodCountID();
		
	}
	
	@Test
	public void testincrementMethodCount() {//TODO
		
		projectParserMediator.incrementMethodCountID();
				
		
	}
	
	@Test
	public void getClassCounter() {//TODO
		
		projectParserMediator.getClassCounter();
		
		
	}
	
	@Test
	public void testincrementClassCounter() {//TODO
		projectParserMediator.incrementClassCounter();
		
	}
	
	@Test
	public void testgetTotalLOC() {//TODO
		
		projectParserMediator.getTotalLOC();
		int totalLoc = 2;
		projectParserMediator.addTotalLOC(totalLoc);
		
	}
	

	@Test
	public void testgetNumberOfPackages() {//TODO
		projectParserMediator.getNumberOfPackages();
		
	}
	
	@Test
	public void testincrementNumberOfPackages() {//TODO
		projectParserMediator.incrementNumberOfPackages();
		
	}

}
