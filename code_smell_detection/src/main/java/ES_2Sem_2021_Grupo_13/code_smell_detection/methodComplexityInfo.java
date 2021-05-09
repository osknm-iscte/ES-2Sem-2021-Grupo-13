package ES_2Sem_2021_Grupo_13.code_smell_detection;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * @author iscte-iul grupo-13
 * 
 * POJO que é usado pelo Visitor para armazenar os valores extraidos para poder calcular complexidade ciclomática.
 *
 */
public class methodComplexityInfo {

	
	private static int nodes = 0;
	private CallableDeclaration m;

	public static int getNodes() {
		return nodes;
	}

	public methodComplexityInfo(CallableDeclaration m2) {
		super();
		this.m = m2;

	}

	public static void updateNodes(int newnodes) {
		nodes = nodes + newnodes;
	}

}
