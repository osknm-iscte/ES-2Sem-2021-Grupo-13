package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.util.List;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;

public class MethodFeatures {
	
	private MethodDeclaration method;
	private NodeList<Parameter> methodParameters;
	
	public String getMethodNameAndParameters() {
		
		return method.getDeclarationAsString(true, true, true);
	}
	
	

	public MethodFeatures(MethodDeclaration method, NodeList<Parameter> methodParameters) {
		super();
		this.method = method;
		this.methodParameters = methodParameters;
	}
	

}
