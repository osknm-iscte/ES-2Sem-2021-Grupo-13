
package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.graalvm.nativeimage.c.type.CCharPointer;
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
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
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
	private static final String WRITEPATH = null;
	private LinkedList<String> realTest = new LinkedList<String>();
	private CompilationUnit compunit;
	private List<MethodDeclaration> methods;
	private List<ConstructorDeclaration> constructors;
	private List<ClassOrInterfaceDeclaration> classes;
	private PackageDeclaration pack;
	private projectParserMediator metaDataStats;

	private int countMethod = 1;

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

	private static class methodCollector extends VoidVisitorAdapter<List<MethodDeclaration>> {

		@Override
		public void visit(MethodDeclaration visitedMethod, List<MethodDeclaration> methodList) {

			super.visit(visitedMethod, methodList);
			methodList.add(visitedMethod);

		}
	}

	public App(CompilationUnit compunit, projectParserMediator metaDataStats) {

		this.compunit = compunit;
		methods = compunit.findAll(MethodDeclaration.class);
		classes = compunit.findAll(ClassOrInterfaceDeclaration.class);
		constructors = compunit.findAll(ConstructorDeclaration.class);
		Optional<PackageDeclaration> packteste = compunit.getPackageDeclaration();
		packteste.ifPresentOrElse((value) -> {
			pack = value;
			metaDataStats.incrementNumberOfPackages();
		}, () -> {
			pack = null;
		});

		codeSmellRuleInterpreter ruleInterpreter = new codeSmellRuleInterpreter(
				"if(LOC_method>50 && CYCLO_method>10)long_method=true; else long_method=false");
		this.metaDataStats = metaDataStats;

	}

	public void getMetrics() {

		LexicalPreservingPrinter.setup(compunit);

		for (ClassOrInterfaceDeclaration c : classes) {
			if (c.isInterface()) {
				continue;
			}
			List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
			int sumOfLOCtoSubtract = getSumLOCnestedClasses(c);
			int classLOC = getLOC(LexicalPreservingPrinter.print(c));
			int classFinalLOC=classLOC-sumOfLOCtoSubtract;
			String classFullName = getFullCLassName(c);
			List<CallableDeclaration> classMethods = filterClassMethods(c);
			metaDataStats.incrementClassCounter();
			metaDataStats.addTotalLOC(classLOC);

			writeOutClassMetrics(classFullName, classFinalLOC, classMethods.size(), classMethods);

		}
	}

	private int getSumLOCnestedClasses(ClassOrInterfaceDeclaration c) {
		List<ClassOrInterfaceDeclaration> innerClasses = new ArrayList<ClassOrInterfaceDeclaration>();
		List <Node> classChildren= c.getChildNodes();
		int LOCcounter=0;
		for(Node n:classChildren) {
			if(n instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration)n).isInterface())
				continue;
			else if(n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)n).isInterface()) {
				 LOCcounter+=getLOC(LexicalPreservingPrinter.print(n));
			}
		}
		
		return LOCcounter;
	}

	private String getFullCLassName(ClassOrInterfaceDeclaration c) {

		Optional<Node> node = c.getParentNode();

		while (node.isPresent()) {
			Node n = node.get();
			if (n instanceof ClassOrInterfaceDeclaration) {
				return ((ClassOrInterfaceDeclaration) n).getNameAsString() + "." + c.getNameAsString();
			} else
				node = n.getParentNode();

		}
		return c.getNameAsString();
	}

	private List<CallableDeclaration> filterClassMethods(ClassOrInterfaceDeclaration c) {

		List<CallableDeclaration> methods = new ArrayList<CallableDeclaration>();
		for (Node d : c.getChildNodes()) {
			if (d instanceof MethodDeclaration || d instanceof ConstructorDeclaration) {
				methods.add(((CallableDeclaration) d));

			}

		}
		return methods;

	}

	public LinkedList<String> getParsedFileStats() {
		return realTest;
	}

	private void writeOutClassMetrics(String className, int classLOC, int NOM_class,
			List<CallableDeclaration> constructorsAndMethods) {

		System.out.println("------------------------------");
		int complexitySum = 0;

		List<Integer> pos = new ArrayList<>();

		if (constructorsAndMethods.isEmpty()) {
			metaDataStats.incrementMethodCountID();
			realTest.add(String.valueOf(metaDataStats.getMethodCountID()));
			if (pack == null)
				realTest.add("default");
			else
				realTest.add(pack.getNameAsString());

			realTest.add(className);
			// String [] splitedMethodDeclaration=m.getDeclarationAsString(true, false,
			// true).split(" ");
			// realTest.add(splitedMethodDeclaration[splitedMethodDeclaration.length-1]);
			realTest.add(className + "()".replaceAll(" ", ""));
			realTest.add("0");
			realTest.add(String.valueOf(classLOC));
			realTest.add("1");
			realTest.add("");// isgodclass
			realTest.add("0");
			realTest.add("0");
			realTest.add("");// islongmethod
			countMethod++;
			return;

		}
		for (CallableDeclaration m : constructorsAndMethods) {
			LexicalPreservingPrinter.setup(m);
			int method_LOC = getLOC(LexicalPreservingPrinter.print(m));
			int method_CYCLO = getMethodCYCLO(m);
			int methodComplexity = getMethodCYCLO(m);
			complexitySum += methodComplexity;

			System.out.println("class name: " + className + " " + "classLOC: " + classLOC + " " + "NOM_class: "
					+ NOM_class + " method name: " + " " + m.getName() + " " + "method LOC: " + method_LOC
					+ "  method CYCLO: " + method_CYCLO);
			// realTest.add(String.valueOf(countMethod));
			metaDataStats.incrementMethodCountID();
			realTest.add(String.valueOf(metaDataStats.getMethodCountID()));
			if (pack == null)
				realTest.add("default");

			else
				realTest.add(pack.getNameAsString());
			realTest.add(className);
			// String [] splitedMethodDeclaration=m.getDeclarationAsString(true, false,
			// true).split(" ");
			// realTest.add(splitedMethodDeclaration[splitedMethodDeclaration.length-1]);
			String fullMethodName = null;
			if (m instanceof ConstructorDeclaration)
				fullMethodName = "void " + m.getDeclarationAsString(false, false, false);
			else
				fullMethodName = m.getDeclarationAsString(false, false, false);

			String[] splitedMethodDeclaration = fullMethodName.split(" ", 2);
			realTest.add(splitedMethodDeclaration[1].replaceAll(" ", ""));
			realTest.add(String.valueOf(NOM_class));
			realTest.add(String.valueOf(classLOC));
			realTest.add("placeholder wmc_class");
			pos.add(realTest.size() - 1);
			realTest.add("");// isgodclass
			realTest.add(String.valueOf(method_LOC));
			realTest.add(Integer.toString(method_CYCLO));
			realTest.add("");// islongmethod
			countMethod++;

		}
		for (int i : pos) {
			if (methods.size() == 0)
				realTest.set(i, String.valueOf(1));
			else
				realTest.set(i, String.valueOf(complexitySum / methods.size()));
		}

		// writeFile(WRITEPATH,this);
		System.out.println("Class complexity: " + complexitySum);
		System.out.println("------------------------------");

	}

	// Extrai linhas de código das classes e dos métodos
	private int getLOC(String NodeString) {

		String curr_class = NodeString.replaceAll("(?m)^[ \t]*\r?\n", ""); // retira todas as linhas vazias dentro da
																			// String
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
	private int getMethodCYCLO(CallableDeclaration m) {

		VoidVisitor<methodComplexityInfo> method_cyclo_info = new ConditionalStatementExplorer();
		JavaParser javaParser = new JavaParser();
		methodComplexityInfo cyclo_info = new methodComplexityInfo(m);
		m.accept(method_cyclo_info, cyclo_info);
		// System.out.println(" nodes: " + cyclo_info.getNodes()+1);

		return cyclo_info.getNodes() + 1;

	}



	/*
	 * public static String[][] readyFileForGUI(Path path, String excelDir) {
	 * List<String> paths; LinkedList<String> list=new LinkedList<String>(); try {
	 * paths = listFiles(path);
	 * 
	 * System.out.println("------------------------------"); paths.forEach(x ->
	 * System.out.println(x)); System.out.println("------------------------------");
	 * ParserConfiguration configuration = new ParserConfiguration();
	 * configuration.setLexicalPreservationEnabled(true); JavaParser javaParser =
	 * new JavaParser(configuration); ArrayList<App>appList=new ArrayList<App>();
	 * for (String s : paths) { CompilationUnit compunit = javaParser.parse(new
	 * File(s)).getResult().get(); App app = new App(compunit); appList.add(app);
	 * app.getMetrics();
	 * 
	 * }
	 * 
	 * for(App p: appList) { list.addAll(p.getParsedFileStats()); } for(String
	 * s:list) { System.out.println(s); }
	 * 
	 * writeFile(null, list, excelDir); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * String[][] rows=dataFormater(list);
	 * 
	 * return rows; }
	 */

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
				App app = new App(compunit, new projectParserMediator());
				app.getMetrics();

			}

			boolean long_method = false;
			boolean long_class = false;
			int LOC_method = 60;
			int CYCLO_method = 40;
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine engine = scriptEngineManager.getEngineByName("graal.js");
			Bindings bindings = engine.createBindings();
			bindings.put("long_method", long_method);
			bindings.put("long_class", long_class);
			bindings.put("LOC_method", LOC_method);
			bindings.put("CYCLO_method", CYCLO_method);
			String script = "if(LOC_method>50 && CYCLO_method>10)long_method=true; else long_method=false";
			Object obj = engine.eval(script, bindings);
			codeSmellRuleInterpreter interpreter = new codeSmellRuleInterpreter(
					"if(LOC_method>50 && CYCLO_method>10){long_method=true;god_class=true;} else"
							+ " long_method=false;");
			HashMap<String, Boolean> testing = interpreter.getCodeSmellFlags(3, 100, 10, 52, 16);

			System.out.println("and now: " + testing.get("long_method"));
			System.out.println("and now: " + testing.get("god_class"));
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

					.map(p -> p.toString()).filter(f -> f.endsWith("java")).collect(Collectors.toList());
		}

		return result;
	}



	public static String getFileExtension(String fullName) {
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	private static String getFileNameWithoutExtension(File file) {
		String fileName = "";

		try {
			if (file != null && file.exists()) {
				String name = file.getName();
				fileName = name.replaceFirst("[.][^.]+$", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fileName = "";
		}

		return fileName;

	}

	private static String getNewFileName(String filename) throws IOException {

		File aFile = new File(filename);
		int fileNo = 0;
		String newFileName = "";
		String extension = getFileExtension(filename);
		String nameWithoutExtension = getFileNameWithoutExtension(aFile);

		if (aFile.exists() && !aFile.isDirectory()) {

			// newFileName = filename.replaceAll(getFileExtension(filename), "(" + fileNo +
			// ")" + getFileExtension(filename));

			while (aFile.exists()) {
				fileNo++;
				aFile = new File(nameWithoutExtension + "(" + fileNo + ")." + extension);
				newFileName = nameWithoutExtension + "(" + fileNo + ")." + extension;
			}

		} else if (!aFile.exists()) {

			newFileName = filename;
		}
		return newFileName;
	}



}
