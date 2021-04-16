package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;



/**
 * Hello world!
 *
 */
public class App {
	private static final String FILE_PATH = "C:/Users/omely/OneDrive/Ambiente de Trabalho/EI-2021/";
	private static final String FILE_PATH_TEST = "C:/Users/omely/OneDrive/Ambiente de Trabalho/DataMining/testCYCLO.java";
	private CompilationUnit compunit;
	private List<MethodDeclaration> methods;
	private List<ClassOrInterfaceDeclaration> classes;
	private PackageDeclaration pack;


	private static class ConditionalStatementExplorer extends VoidVisitorAdapter<methodComplexityInfo> {

		@Override
		public void visit(IfStmt conditional, methodComplexityInfo info) {

			super.visit(conditional, info);
			info.updateNodes(1);

		}

		@Override
		public void visit(ForStmt conditional, methodComplexityInfo info) {

			super.visit(conditional, info);
			info.updateNodes(1);

		}

		@Override
		public void visit(WhileStmt conditional, methodComplexityInfo info) {

			super.visit(conditional, info);
			info.updateNodes(1);

		}

		@Override
		public void visit(SwitchStmt conditional, methodComplexityInfo info) {
			super.visit(conditional, info);
			info.updateNodes(conditional.getEntries().size());

		}

		@Override
		public void visit(TryStmt conditional, methodComplexityInfo info) {

			super.visit(conditional, info);
			info.updateNodes(1);

		}

		@Override
		public void visit(ForEachStmt conditional, methodComplexityInfo info) {

			super.visit(conditional, info);
			info.updateNodes(1);

		}
	}

	
	public App(CompilationUnit compunit) {

		this.compunit = compunit;
		methods = compunit.findAll(MethodDeclaration.class);
		classes = compunit.findAll(ClassOrInterfaceDeclaration.class);
		pack = compunit.getPackageDeclaration().get();

	}

	private void getMetrics() {

		LexicalPreservingPrinter.setup(compunit);

		for (ClassOrInterfaceDeclaration c : classes) {

			int classLOC = getLOC(LexicalPreservingPrinter.print(c));

			List<MethodDeclaration> classMethods = c.findAll(MethodDeclaration.class);
			writeOutClassMetrics(c.getName().toString(), classLOC, classMethods.size(), classMethods);

		}
	}

	private void writeOutClassMetrics(String className, int classLOC, int NOM_class, List<MethodDeclaration> methods) {

		System.out.println("------------------------------");
		int complexitySum = 0;
		for (MethodDeclaration m : methods) {
			LexicalPreservingPrinter.setup(m);
			int method_LOC = getLOC(LexicalPreservingPrinter.print(m));
			int method_CYCLO = getMethodCYCLO(m);
			int methodComplexity = getMethodCYCLO(m);
			complexitySum += methodComplexity;

			System.out.println("class package: " + pack.getNameAsString() + "class name: " + className + " "
					+ "classLOC: " + classLOC + " " + "NOM_class: " + NOM_class + " method name: " + " " + m.getName()
					+ " " + "method LOC: " + method_LOC + "  method CYCLO: " + method_CYCLO);
		}
		System.out.println("Class complexity: " + complexitySum);
		System.out.println("------------------------------");

	}

	// Extrai linhas de código das classes e dos métodos
	private int getLOC(String NodeString) {
		// retira todas as linhas vazias dentro da String
		String curr_class = NodeString.replaceAll("(?m)^[ \t]*\r?\n", "");
		int classWithoutEmptyLines = curr_class.split("\n").length;

		ParserConfiguration configuration = new ParserConfiguration();
		configuration.setLexicalPreservationEnabled(true);
		JavaParser javaParser = new JavaParser(configuration);
		ParseResult<CompilationUnit> compunit2 = javaParser.parse(curr_class);

		CommentsCollection comments = compunit2.getCommentsCollection().get();
		TreeSet tree = comments.getComments();
		Iterator itr = tree.iterator();
		int commentLengthCounter = 0;
		while (itr.hasNext()) {
			commentLengthCounter = commentLengthCounter + itr.next().toString().split("\n").length;

		}

		return classWithoutEmptyLines - commentLengthCounter;

	}

	/*
	 * Inicializa a extração da complexidade ciclomática do método. É instanciado o
	 * VoidVIsitor que vai percorrer todos os ifs,whiles,swtich statement cases,
	 * TryCatch clauses, dentro do método e extrair a complexidade.
	 */
	private int getMethodCYCLO(MethodDeclaration method) {

		VoidVisitor<methodComplexityInfo> method_cyclo_info = new ConditionalStatementExplorer();
		JavaParser javaParser = new JavaParser();
		methodComplexityInfo cyclo_info = new methodComplexityInfo(method);
		method.accept(method_cyclo_info, cyclo_info);
		// System.out.println(" nodes: " + cyclo_info.getNodes()+1);

		return cyclo_info.getNodes() + 1;

	}

	public static void main(String[] args) {

		try {
			Path path = Paths.get("C:\\Users\\omely\\OneDrive\\Ambiente de Trabalho\\EI-2021");
			List<String> paths = listFiles(path);
			System.out.println("------------------------------");
			paths.forEach(x -> System.out.println(x));
			System.out.println("------------------------------");

			ParserConfiguration configuration = new ParserConfiguration();
			configuration.setLexicalPreservationEnabled(true);
			JavaParser javaParser = new JavaParser(configuration);

			for (String s : paths) {
				CompilationUnit compunit = javaParser.parse(new File(s)).getResult().get();
				App app = new App(compunit);
				app.getMetrics();

			}
			// getMethodCYCLO(new JavaParser().parse(new
			// File(FILE_PATH_TEST)).getResult().get()
			// .findFirst(MethodDeclaration.class).get());
			boolean long_method=false;
			boolean long_class=false;
			int LOC_method=60;
			int CYCLO_method=40;
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine engine= scriptEngineManager.getEngineByName("graal.js");
			Bindings bindings = engine.createBindings();
			bindings.put("long_method", long_method);
			bindings.put("long_class", long_class);
			bindings.put("LOC_method", LOC_method);
			bindings.put("CYCLO_method", CYCLO_method);
			String script= "if(LOC_method>50 && CYCLO_method>10)long_method=true; else long_method=false";
			long_method=(boolean) engine.eval(script, bindings);
			System.out.println("and now: "+long_method);
		} catch (IOException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<String> listFiles(Path path) throws IOException {

		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}

		List<String> result;

		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(p -> !Files.isDirectory(p))

					.map(p -> p.toString().toLowerCase()).filter(f -> f.endsWith("java")).collect(Collectors.toList());
		}

		return result;
	}

}
