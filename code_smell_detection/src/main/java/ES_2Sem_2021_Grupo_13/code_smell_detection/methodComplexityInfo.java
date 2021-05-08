package ES_2Sem_2021_Grupo_13.code_smell_detection;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class methodComplexityInfo {

	private static int edges = 0;
	private static int nodes = 0;
	private static CallableDeclaration m;

	public static int getEdges() {
		return edges;
	}

	public static void updateEdges(int newEdges) {
		edges = edges+newEdges;
	}

	public static int getNodes() {
		return nodes;
	}

	public methodComplexityInfo(CallableDeclaration m2) {
		super();
		this.m=m2;

	}

	public static CallableDeclaration getMethod() {
		return m;
	}
	public static void updateNodes(int newNodes) {
		nodes = nodes+newNodes;
	}

}
