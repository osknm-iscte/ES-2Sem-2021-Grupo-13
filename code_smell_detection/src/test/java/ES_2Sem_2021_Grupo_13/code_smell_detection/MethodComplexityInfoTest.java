package ES_2Sem_2021_Grupo_13.code_smell_detection;

import org.junit.Test;

import com.github.javaparser.ast.body.CallableDeclaration;

public class MethodComplexityInfoTest {
	

	
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
	

	
	
	
	
	
	

}
