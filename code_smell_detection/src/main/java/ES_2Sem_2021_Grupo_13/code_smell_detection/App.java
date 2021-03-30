package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Hello world!
 *
 */
public class App {
	private static class MethodNameCollector extends VoidVisitorAdapter<List<Comment>> {

		public void visit(CompilationUnit md, List<Comment> collector) {
			super.visit(md, collector);
			collector.addAll(md.getAllComments());
		}
	}

	private static final String FILE_PATH = "C:\\Users\\maria\\Desktop\\App.java";
	private static final String WRITEPATH = "C:\\Users\\maria\\Documents\\Code_Smells.xlsx";// will vary between
																							// computers
//	private static final String TESTREAD = "C:\\Users\\maria\\Documents\\TESTREAD.xlsx";

//	private static final LinkedList<String> DATA = dataSimulator();
	private static LinkedList<String> realTest = new LinkedList<String>();

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

			int classLOC = getLOC(LexicalPreservingPrinter.print(c));
			List<MethodDeclaration> classMethods = c.findAll(MethodDeclaration.class);
			writeOutClassMetrics(c.getName().toString(), classLOC, classMethods.size(), classMethods);

		}
	}

	private void writeOutClassMetrics(String className, int classLOC, int NOM_class, List<MethodDeclaration> methods) {

		int count = 1;
		for (MethodDeclaration m : methods) {
			LexicalPreservingPrinter.setup(m);
			int method_LOC = getLOC(LexicalPreservingPrinter.print(m));
			System.out.println("class name: " + className + "   " + "classLOC:   " + classLOC + "   " + "NOM_class: "
					+ NOM_class + " method name: " + "   " + m.getName() + "   " + "  method LOC: " + method_LOC);

//			"NOM_class", "LOC_class", "WMC_class", "is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method"
			realTest.add(String.valueOf(count));
			realTest.add("placeholder package");
			realTest.add(className);
			realTest.add(String.valueOf(m.getName()));

			realTest.add(String.valueOf(NOM_class));
			realTest.add(String.valueOf(classLOC));
			realTest.add("placeholder wmc_class");
			realTest.add("");// isgodclass
			realTest.add(String.valueOf(method_LOC));
			realTest.add("placeholder cyclo_method");
			realTest.add("");// islongmethod

			count++;

		}

	}

	private int getLOC(String NodeString) {
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

	private static LinkedList<String> readFile() throws IOException { //reads .xlsx file and writes to LinkedList<String>

		LinkedList<String> data = new LinkedList<String>();

		String excelFilePath = WRITEPATH;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
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

//	private static LinkedList<String> dataSimulator() { // will be replaced by the actual data generator in the final
//														// version, just for testing
//		LinkedList<String> data = new LinkedList<String>();
//
//		int numberLines = 2;
//		int numberColumns = 7;
//		int count = 0;
//
//		for (int i = 0; i < numberLines; i++) {
//			for (int j = 0; j < numberColumns; j++) {
//
//				data.add(count, String.valueOf(count + 1));
//				System.out.println("\n " + data);
//				count++;
//			}
//		}
//		return data;
//	}

	private static void writeFile(String path) { // to use you can't have the file opened anywhere else

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Code Smells");

		Object[][] datatypes = dataFormater(realTest); // gets the formated data, will probably be passed as an argument
														// in the final version

		int rowNum = 0; // creates the .xlsx file
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
			FileOutputStream outputStream = new FileOutputStream(path);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}

	private static String[][] dataFormater(LinkedList<String> data) { // formats the data so it can be put in a .xlsx,
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

	public static void main(String[] args) throws IOException {

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

			writeFile(WRITEPATH);
			readFile();

//			writeFile(TESTREAD);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}