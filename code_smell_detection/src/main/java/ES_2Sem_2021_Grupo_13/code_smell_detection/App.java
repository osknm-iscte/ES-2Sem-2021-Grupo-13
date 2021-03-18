package ES_2Sem_2021_Grupo_13.code_smell_detection;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Hello world!
 *
 */
public class App {
	
	private static class MethodNamePrinter extends VoidVisitorAdapter<List<MethodFeatures>> {
		
		 @Override
		 public void visit(MethodDeclaration md, List<MethodFeatures> methodfeatures) {
		 super.visit(md, methodfeatures);
		 methodfeatures.add(new MethodFeatures(md,md.getParameters()));
		 
		 
		// methodNames.add(md.getNameAsString());
		}
	}
	private static final String FILE_PATH="C:/Users/omely/OneDrive/Ambiente de Trabalho/EI-2021/MongoWorker.java";
    public static void main( String[] args ){
    	
    	try {
    		List<MethodFeatures> methodfeatures = new ArrayList<MethodFeatures>();
			CompilationUnit compunit = StaticJavaParser.parse(new File(FILE_PATH));
			VoidVisitor<List<MethodFeatures>> methodNameVisitor = new MethodNamePrinter();
			 methodNameVisitor.visit(compunit, methodfeatures);
			 
			 for(MethodFeatures m:methodfeatures) {
				 System.out.println(m.getMethodNameAndParameters());
			 }
			 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
