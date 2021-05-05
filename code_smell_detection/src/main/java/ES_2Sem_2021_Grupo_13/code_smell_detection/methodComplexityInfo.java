package ES_2Sem_2021_Grupo_13.code_smell_detection;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class methodComplexityInfo {

	private int edges = 0;
	private int nodes = 0;
	private CallableDeclaration m;

	public int getEdges() {
		return edges;
	}

	public void updateEdges(int edges) {
		this.edges = this.edges+edges;
	}

	public int getNodes() {
		return nodes;
	}

	public methodComplexityInfo(CallableDeclaration m2) {
		super();
		this.m=m2;

	}

	public CallableDeclaration getMethod() {
		return m;
	}
	public void updateNodes(int nodes) {
		this.nodes = this.nodes+nodes;
	}

}
