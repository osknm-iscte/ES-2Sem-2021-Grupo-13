package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

/**
 * Hello world!
 *
 */
public class App {
	private static final String FILE_PATH = "C:/Users/Utilizador/Documents/LEI/Aulas Práticas/Semanas.java";

	private CompilationUnit compunit;
	private List<MethodDeclaration> methods;
	private List<ClassOrInterfaceDeclaration> classes;

	public App(CompilationUnit compunit) {

		this.compunit = compunit;
		methods = compunit.findAll(MethodDeclaration.class);
		classes = compunit.findAll(ClassOrInterfaceDeclaration.class);

	}

	private void getMetrics() {

		LexicalPreservingPrinter.setup(compunit);
		for (ClassOrInterfaceDeclaration c : classes) {

			int LOC_class = getLOC(LexicalPreservingPrinter.print(c));
			List<MethodDeclaration> classMethods = c.findAll(MethodDeclaration.class);
			int WMC_class = historicCYCLO(classMethods.size(), classMethods);
			writeOutClassMetrics(c.getName().toString(), classMethods.size(), LOC_class, WMC_class, classMethods);

		}
	}

	private void writeOutClassMetrics(String className, int NOM_class, int LOC_class, int WMC, List<MethodDeclaration> methods) {

		for (MethodDeclaration m : methods) {
			LexicalPreservingPrinter.setup(m);
			int LOC_method = getLOC(LexicalPreservingPrinter.print(m));
			int CYCLO_method = getCYCLO(LOC_method, LexicalPreservingPrinter.print(m));
			int WMC_class = WMC;
			System.out.println("class name: " + className + " " + "NOM_class: " + NOM_class + " " + "LOC_class: "
					+ LOC_class + " " + "WMC_class: " + WMC_class + " method name: " + " " + m.getName() + " " + "LOC_method: " + LOC_method + " " + "CYCLO_method: " + CYCLO_method);
		}

	}

	
	
	private int getLOC(String NodeString) {
		String curr_class = NodeString.replaceAll("(?m)^[ \t]*\r?\n", "");
		int classWithoutEmptyLines = curr_class.split("\n").length;
		
		ParserConfiguration configuration = new ParserConfiguration();
		configuration.setLexicalPreservationEnabled(true);
		JavaParser javaParser = new JavaParser(configuration);
		ParseResult<CompilationUnit> compunit2 = javaParser.parse(curr_class);
		
		CommentsCollection comments= compunit2.getCommentsCollection().get();
		TreeSet tree=comments.getComments();
		Iterator itr=tree.iterator();
		int commentLengthCounter = 0;
		while(itr.hasNext()) {
			commentLengthCounter = commentLengthCounter + itr.next().toString().split("\n").length;
			
		}
		
		
		
		
		return classWithoutEmptyLines - commentLengthCounter;

		
	}
	
	private int getCYCLO(int N, String NodeString) {
		
		String zip = NodeString.replaceAll("(?m)^[ \t]*\r?\n", "");
		Scanner s = new Scanner (zip);
		
		String[] items = {"for", "while", "else"};
		int E = 0;
		while(s.hasNext()) {
			for (String item : items) {
			       if (s.next().contains(item)) {
			    	   E=N+1;
			       }
			}
		}
		s.close();
		
		if(E==0) {
			E= 1;
		}
		return (E - N + 2);
	}
	
	private int historicCYCLO(int NOM_class, List<MethodDeclaration> methods) {
		
		int WMC_class = 0;
		int tempWMC = 0;
		for (MethodDeclaration m : methods) {
			LexicalPreservingPrinter.setup(m);
			int LOC_method = getLOC(LexicalPreservingPrinter.print(m));
			int CYCLO_method = getCYCLO(LOC_method, LexicalPreservingPrinter.print(m));
			tempWMC = tempWMC + CYCLO_method;
			
			if(CYCLO_method > 1) {
				
				WMC_class = tempWMC;
			
			} else {
				
				WMC_class = NOM_class;
			}
		}
		return WMC_class;
	}
			
	public static void main(String[] args) {

		try {

			ParserConfiguration configuration = new ParserConfiguration();
			configuration.setLexicalPreservationEnabled(true);
			JavaParser javaParser = new JavaParser(configuration);
			CompilationUnit compunit = javaParser.parse(new File(FILE_PATH)).getResult().get();

			List<MethodDeclaration> methods = compunit.findAll(MethodDeclaration.class);

			for (MethodDeclaration m : methods) {
				System.out.println(m.getDeclarationAsString(true, true, true));

			}

			App app = new App(compunit);
			app.getMetrics();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
