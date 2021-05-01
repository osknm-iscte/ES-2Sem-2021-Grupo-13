
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
	private static final String WRITEPATH = null;
	private  LinkedList<String> realTest = new LinkedList<String>();
	private CompilationUnit compunit;
	private List<MethodDeclaration> methods;
	private List<ClassOrInterfaceDeclaration> classes;
	private PackageDeclaration pack;
	
	
	
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

	public App(CompilationUnit compunit) {

		this.compunit = compunit;
		methods = compunit.findAll(MethodDeclaration.class);
		classes = compunit.findAll(ClassOrInterfaceDeclaration.class);
		Optional<PackageDeclaration> packteste =compunit.getPackageDeclaration();
		packteste.ifPresentOrElse(
                (value)
                    -> { pack=value; },
                ()
                    -> { pack=null; });
		
		
		
		codeSmellRuleInterpreter ruleInterpreter=new codeSmellRuleInterpreter("if(LOC_method>50 && CYCLO_method>10)long_method=true; else long_method=false");

	}

	public void getMetrics() {

		LexicalPreservingPrinter.setup(compunit);

		for (ClassOrInterfaceDeclaration c : classes) {

			int classLOC = getLOC(LexicalPreservingPrinter.print(c));

			List<MethodDeclaration> classMethods = c.findAll(MethodDeclaration.class);
			writeOutClassMetrics(c.getName().toString(), classLOC, classMethods.size(), classMethods);

		}
	}
public LinkedList<String> getParsedFileStats() {
	return realTest;
}
	private void writeOutClassMetrics(String className, int classLOC, int NOM_class, List<MethodDeclaration> methods) {

		System.out.println("------------------------------");
		int complexitySum = 0;
		List<Integer>pos=new ArrayList<>();
		
		for (MethodDeclaration m : methods) {
			LexicalPreservingPrinter.setup(m);
			int method_LOC = getLOC(LexicalPreservingPrinter.print(m));
			int method_CYCLO = getMethodCYCLO(m);
			int methodComplexity = getMethodCYCLO(m);
			complexitySum += methodComplexity;

			//System.out.println("class package: " + pack.getNameAsString() + "class name: " + className + " "
				//	+ "classLOC: " + classLOC + " " + "NOM_class: " + NOM_class + " method name: " + " " + m.getName()
					//+ " " + "method LOC: " + method_LOC + "  method CYCLO: " + method_CYCLO);
			realTest.add(String.valueOf(countMethod));
			if(pack==null)realTest.add("");
			else realTest.add(pack.toString());
			realTest.add(className);
			realTest.add(String.valueOf(m.getName()));
			realTest.add(String.valueOf(NOM_class));
			realTest.add(String.valueOf(classLOC));
			realTest.add("placeholder wmc_class");
			pos.add(realTest.size()-1);
			realTest.add("");// isgodclass
			realTest.add(String.valueOf(method_LOC));
			realTest.add(Integer.toString(method_CYCLO));
			realTest.add("");// islongmethod
			countMethod++;
			
		}
		for(int i:pos) {
			realTest.set(i, String.valueOf(complexitySum/methods.size()));
		}
		
		//writeFile(WRITEPATH,this);
		System.out.println("Class complexity: " + complexitySum);
		System.out.println("------------------------------");

	}

	// Extrai linhas de código das classes e dos métodos
	private int getLOC(String NodeString) {
		
		String curr_class = NodeString.replaceAll("(?m)^[ \t]*\r?\n", ""); // retira todas as linhas vazias dentro da String
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
			codeSmellRuleInterpreter interpreter=new codeSmellRuleInterpreter("if(LOC_method>50 && CYCLO_method>10){long_method=true;god_class=true;} else"
			+ " long_method=false;");
			HashMap<String,Boolean>testing=interpreter.getCodeSmellFlags(3, 100, 10, 52, 16);
			
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

					.map(p -> p.toString().toLowerCase()).filter(f -> f.endsWith("java")).collect(Collectors.toList());
		}

		return result;
	}

	private static LinkedList<String> readFile() throws IOException { // implemented, but needs adjustments - what will
		// it print to??

		LinkedList<String> data = new LinkedList<String>();

		String excelFilePath = WRITEPATH;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = (Sheet) workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				System.out.print(cell.getStringCellValue());
				System.out.print(" - ");

				data.add(cell.getStringCellValue());

			}
			System.out.println();
		}

		workbook.close();
		inputStream.close();

		return data;

	}

	public static void writeFile(String path, LinkedList<String>data) { // to use you can't have the file opened anywhere else
		

		XSSFWorkbook workbook =  new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Code Smells");
		
		Object[][] datatypes = dataFormater(data); // gets the formated data, will probably be passed as an argument
// in the final version

		int rowNum = 0; // creates the .xlsx file
		if(sheet.getLastRowNum()!=-1)rowNum=sheet.getLastRowNum();
		System.out.println("Creating excel");

		for (Object[] datatype : datatypes) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (Object field : datatype) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}
		}

		try {
			FileOutputStream outputStream=null;
			if(path==null) {
				 outputStream = new FileOutputStream(System.getProperty("user.dir")+"/"+"Code_Smells.xlsx");
			}
			else  outputStream = new FileOutputStream(path); 
			
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}

public static String[][] dataFormater(LinkedList<String> data) { // formats the data so it can be put in a .xlsx,
		// receives a LinkedList<String>
	int numberOfParameters = 11;

		String[][] formatedData = new String[data.size() / numberOfParameters + numberOfParameters][numberOfParameters
		                                                                                            + 1]; // creates the array
		String[] predefinido = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
				"is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method" }; // 1st line

		int nrLines = data.size() / numberOfParameters; // number of lines the final table will have (does not account
		// for the 1st)
		int count = 0; // iterates the linkedList

		for (int i = 1; i < nrLines + 2; i++) { // lines
			for (int j = 1; j < numberOfParameters + 1; j++) { // columns

				if (i == 1) {
				formatedData[i - 1][j - 1] = predefinido[i * j - 1]; // writes 1st line
				} else {
					formatedData[i - 1][j - 1] = data.get(count); // writes the rest
					count++;
				}
			}
}
return formatedData;
}

}

