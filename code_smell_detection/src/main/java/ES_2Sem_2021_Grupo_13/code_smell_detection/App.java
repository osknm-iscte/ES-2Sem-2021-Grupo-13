
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
/**
 * @author grupo 13 iscte-iul
 * @version 1.0
 *
 */
public class App {

	private LinkedList<String> realTest = new LinkedList<String>();
	private CompilationUnit compunit;
	private List<MethodDeclaration> methods;
	private List<ConstructorDeclaration> constructors;
	private List<ClassOrInterfaceDeclaration> classes;
	private PackageDeclaration pack;
	private projectParserMediator metaDataStats;


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

	/**
	 * @param compunit unidade de compilação
	 * @param metaDataStats POJO que guarda info de uma inidade de compilação
	 */
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

		
		this.metaDataStats = metaDataStats;

	}

	
	/**
	 * Ponto de entrada para começar a extrair métricas de um ficheiro java
	 */
	public void getMetrics() {

		// LexicalPreservingPrinter.setup(compunit);

		for (ClassOrInterfaceDeclaration c : classes) {
			if (c.isInterface()) {
				continue;
			}
			List<MethodDeclaration> methodList = new ArrayList<MethodDeclaration>();
			int sumOfLOCtoSubtract = getSumLOCnestedClasses(c);
			int classLOC = getLOC(c.toString());
			int classFinalLOC = classLOC - sumOfLOCtoSubtract;
			String classFullName = getFullCLassName(c);
			List<CallableDeclaration> classMethods = filterClassMethods(c);
			metaDataStats.incrementClassCounter();
			metaDataStats.addTotalLOC(classFinalLOC);

			writeOutClassMetrics(classFullName, classFinalLOC, classMethods.size(), classMethods);

		}
		
	}

	/**
	 * Este método vai ver se a classe possui classes internas. Se tiver, vai somar LOC das classes internas para
	 * que depois seja possível calcular  LOC da classe que é LOC da classe externa-LOC da soma das LOC das classes internas 
	 * @param c Node que representa uma classe
	 * @return inteiro que indica a soma das LOCS das classes internas, se estas existirem.
	 */
	private int getSumLOCnestedClasses(ClassOrInterfaceDeclaration c) {
		List<ClassOrInterfaceDeclaration> innerClasses = new ArrayList<ClassOrInterfaceDeclaration>();
		List<Node> classChildren = c.getChildNodes();
		int LOCcounter = 0;
		for (Node n : classChildren) {
			if (n instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) n).isInterface())
				continue;
			else if (n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) n).isInterface()) {
				LOCcounter += getLOC(n.toString());
			}
		}

		return LOCcounter;
	}

	/**
	 * Obtém nome completo da classe em causa. Se a classe for definida dentro de outra classe,
	 * então o nome vai ter um sufixo com nome da classe externa, seguido de um ponto mais o nome da classe interna
	 * Ex: OutterClass.InnerClass
	 * @param c Node que representa a classe
	 * @return Nome da classe completa
	 */
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

	/**
	 * Este metodo vai filtrar todos os metodos que pertencem diretamente a classe, i.e,
	 * só são considerados os métodos que forem definidos dentro da classe. Se a classe tiver classes internas,
	 * os métodos das classes internas não são adicionados a lista a devolver. 
	 * @param c Node que representa a classe em questão
	 * @return Lista com métodos filtrados
	 */
	private List<CallableDeclaration> filterClassMethods(ClassOrInterfaceDeclaration c) {

		List<CallableDeclaration> methods = new ArrayList<CallableDeclaration>();
		for (Node d : c.getChildNodes()) {
			if (d instanceof MethodDeclaration || d instanceof ConstructorDeclaration) {
				methods.add(((CallableDeclaration) d));

			}

		}
		return methods;

	}

	/**
	 * Retorna a estatistica das métricas da unidade de compilação analisada.
	 * 
	 * @return Retorna a estatistica das métricas da unidade de compilação analisada.
	 */
	public LinkedList<String> getParsedFileStats() {
		return realTest;
		
	}
	

	/** Popula estrutura de dados que contém os dados de extração das métricas.
	 * @param className nome da classe 
	 * @param classLOC Linhas de código da classe
	 * @param NOM_class Número de métodos da classe
	 * @param constructorsAndMethods Estrutura de dados que contém os métodos e os consturtores da classe
	 */
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
			realTest.add(className + "()".replaceAll(" ", ""));
			realTest.add("0");
			realTest.add(String.valueOf(classLOC));
			realTest.add("1");
			realTest.add("");// isgodclass
			realTest.add("0");
			realTest.add("1");
			realTest.add("");// islongmethod
			
			return;

		}
		for (CallableDeclaration m : constructorsAndMethods) {
			
			int method_LOC = 0;
			if (m instanceof MethodDeclaration)
				method_LOC = getLOC(((MethodDeclaration) m).toString());
			if (m instanceof ConstructorDeclaration)
				method_LOC = getLOC(((ConstructorDeclaration) m).toString());

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
			

		}
		for (int i : pos) {
			if (methods.size() == 0)
				realTest.set(i, String.valueOf(1));
			else
				realTest.set(i, String.valueOf(complexitySum));
		}

		// writeFile(WRITEPATH,this);
		System.out.println("Class complexity: " + complexitySum);
		System.out.println("------------------------------");

	}

	// Extrai linhas de código das classes e dos métodos
	/**
	 * Obtém linhas de código das classes e dos métodos.
	 * Recebe como um  argumento um Node que representa  um método ou uma classe na forma de
	 * uma String. Ignora as linhas vazias no cálculo de LOC.
	 * 
	 * @param NodeString
	 * @return Número de linhas de código
	 */
	private int getLOC(String NodeString) {

		return NodeString.replaceAll("(?m)^[ \t]*\r?\n", "").split("\n").length;

	}

	
	  
	/**
	 *Inicializa a extração da complexidade ciclomática do método. É instanciado o
	 VoidVIsitor que vai percorrer todos os ifs,whiles,swtich statement cases,
	 TryCatch clauses, dentro do método e extrair a complexidade. Neste caso,
	 no cálculo da  extração de complexidade são somadas as ocorrências
	 dos statements referidos em cima +1.
	 
	 * @param m Node que representa o método cuja complexidade cilcomática pretende-se calcular
	 * @return complexidade ciclomática
	 */
	private int getMethodCYCLO(CallableDeclaration m) {

		VoidVisitor<methodComplexityInfo> method_cyclo_info = new ConditionalStatementExplorer();
		JavaParser javaParser = new JavaParser();
		methodComplexityInfo cyclo_info = new methodComplexityInfo(m);
		m.accept(method_cyclo_info, cyclo_info);
		// System.out.println(" nodes: " + cyclo_info.getNodes()+1);

		return cyclo_info.getNodes() + 1;

	}

	/**
	 * Obtém os caminhos para todos os ficheiros java dentro de uma diretoria e dentro
	 * das subdiretorias
	 * @param path representa o caminho da diretoria  onde estão os ficheiros java 
	 * @return Lista com os caminhos para os ficheiros
	 * @throws IOException pode mandar IOException
	 */
	public static List<String> listFiles(Path path) throws IOException {

		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}

		List<String> result=new ArrayList<String>();
		

		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(p -> !Files.isDirectory(p))

					.map(p -> p.toString()).filter(f -> f.endsWith("java")).collect(Collectors.toList());
		}

		return result;

	}
	
	
	}


