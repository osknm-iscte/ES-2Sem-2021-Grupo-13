package ES_2Sem_2021_Grupo_13.code_smell_detection;

import org.junit.Test;

public class MethodComplexityInfoTest {
	
	@Test
	public void testgetEdges() {
		
		methodComplexityInfo.getEdges();
		
	}
	
	@Test
	public void testupdateEdges() {
		
		int edges = 2;
		methodComplexityInfo.updateEdges(edges);
		
		testgetEdges();
		
		
	}
	
	@Test
	public void testgetNodes() {
		
		methodComplexityInfo.getNodes();
		
	}
	
	@Test
	public void testupdateNodes() {
		
		int nodes = 2;
		methodComplexityInfo.updateNodes(nodes);
		
		testgetNodes();
		
	}
	
	@Test
	public void testgetMethod() {
		
		methodComplexityInfo.getMethod();
		
		
		
	}
	
	
	
	
	
	

}
