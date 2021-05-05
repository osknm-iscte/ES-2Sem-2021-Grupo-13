package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;



/**
 * Hello world!
 *
 */
public class XLSX_read_write {
	



	private static final String FILE_PATH = "C:\\Users\\maria\\Desktop\\App.java"; // file to analyse, debug
	private static final String WRITEPATH = "C:\\Users\\maria\\Documents\\Code_Smells.xlsx";// will vary between
																							// computers, code_smells.xlsx destination, debug
	
	private static final String XMLWRITEPATH = "C:\\Users\\maria\\Documents\\xml.xml";
	
	
	private int countMethod = 1; //will count the number of methods, did not work inside the method writeOutClassMetrics 

	private static LinkedList<String> writedata = new LinkedList<String>(); //will be used in writeOutClassMetrics to store the data to write to the .xlsx file
	private static final int NUMBERPARAMETERS = 9;
	private CompilationUnit compunit;
	private List<MethodDeclaration> methods;
	private List<ClassOrInterfaceDeclaration> classes;

	public XLSX_read_write(CompilationUnit compunit) { //?

		this.compunit = compunit;
		methods = compunit.findAll(MethodDeclaration.class);
		classes = compunit.findAll(ClassOrInterfaceDeclaration.class);

	}








	private static LinkedList<String> readFile() throws IOException { //reads the .xlsx file and puts its content on a linkedList
		
		//if you want a String [][] use the dataFormater(returnOfThisMethod) 

		LinkedList<String> data = new LinkedList<String>();


		String excelFilePath = WRITEPATH;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0); //reads only the first sheet
		Iterator<Row> iterator = firstSheet.iterator(); //creates an iterator to read the sheet

		
		while (iterator.hasNext()) {
			Row nextRow = iterator.next(); //reads next row
			Iterator<Cell> cellIterator = nextRow.cellIterator(); //will read the cells in each row

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next(); //goes to the next cell
				if (cell.getCellType() != CellType.BLANK) { //if the cell is empty don't create a new position on the linkedList
					System.out.print(cell.getStringCellValue()); //test
					System.out.print(" - "); //test
					
					if (nextRow.getRowNum() != 0) { //if its not the columns names then print
						data.add(cell.getStringCellValue()); // adds the value, if not empty, to the linkedList
					}
				}

			}
			System.out.println();
		}

		
		workbook.close();
		inputStream.close(); //closes reading
		
//		testeToLinkedList(data);	//debug
		

		return data; //returns linkedList
		
		

	}
	
	
	
	
	
//	private static void testeToLinkedList(LinkedList<String> data) { //prints all the values of the linkedList data, for debug
//		for(int i = 0; i < data.size(); i++)
//			
//			System.out.println("\n" + data.get(i));
//			
//		}
		




	static void writeFile(String path, LinkedList<String> dataset) { // to use you can't have the file opened anywhere else or else it will give errors on the console

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Code Smells"); // creates the .xlsx file

		Object[][] datatypes = dataFormater(dataset); // gets the formated data

		int rowNum = 0; //will count the rows
		System.out.println("Creating excel");

		for (Object[] datatype : datatypes) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			for (Object field : datatype) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) { //writes the string, i dont think the next condition will ever be used since the linkedList has Strings only
					cell.setCellValue((String) field);
//				} else if (field instanceof Integer) {
//					cell.setCellValue((Integer) field);
				}
			}
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(path);
			workbook.write(outputStream);//writes file
			workbook.close();//closes file
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done"); //debug
	}
	
	public static String[][] readyExcelForGUI(File excelFile) throws IOException {

		return dataFormater(readFile());

	}

	static String[][] dataFormater(LinkedList<String> data) { // formats the data so it can be put in a .xlsx,
																		// receives a LinkedList<String> and transforms into String [][]


		String[][] formatedData = new String[data.size() / NUMBERPARAMETERS + NUMBERPARAMETERS][NUMBERPARAMETERS
				+ 1]; // creates the array with the size required
		String[] predefinido = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
				"LOC_method", "CYCLO_method" }; // 1st line, titles

		int nrLines = data.size() / NUMBERPARAMETERS; // number of lines the final table will have (does not account
														// for the 1st)
		int count = 0; // iterates the linkedList

		for (int i = 1; i < nrLines + 2; i++) { // lines
			for (int j = 1; j < NUMBERPARAMETERS + 1; j++) { // columns

				if (i == 1) {
					formatedData[i - 1][j - 1] = predefinido[i * j - 1]; // writes the 1st line
				} else {
					formatedData[i - 1][j - 1] = data.get(count); // writes the rest of the content
					count++;
				}
			}
		}
		return formatedData; // returns the data, now as String [][]
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

			

			writeFile(WRITEPATH, writedata);
			LinkedList <String> dataset = readFile();
			System.out.println("readfile  - " + readFile());
			writeFile("C:\\Users\\maria\\Desktop\\after_read.xlsx", dataset);
			
			



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}