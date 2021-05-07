package ES_2Sem_2021_Grupo_13.code_smell_detection;

//new
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptException;

import org.graalvm.polyglot.PolyglotException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

public class projectParser {

	private Path path;
	private List<App> parsedFilesUnits = new ArrayList<>();
	private LinkedList<String> parsedFilesStatsList = new LinkedList<String>();
	private codeSmellRuleInterpreter rulesInterpreter;
	private int methodID = 0;
	private projectParserMediator metaDataStats = new projectParserMediator();

	public projectParser(Path path) {
		super();
		this.path = path;
		// tem primeiro de verificar se existe ficheiro com regras
		rulesInterpreter = new codeSmellRuleInterpreter(
				"if(LOC_method>10)long_method=true; else" + " long_method=false;");
	}

	public void parseJavaFiles() {

		List<String> paths;
		try {
			paths = App.listFiles(path);

			ParserConfiguration configuration = new ParserConfiguration();
			// configuration.setLexicalPreservationEnabled(true);
			configuration.setAttributeComments(false);
			JavaParser javaParser = new JavaParser(configuration);
			int i = 0;
			for (String s : paths) {
				i++;
				System.out.println("path id: " + i + "path: " + s);
				CompilationUnit compunit = javaParser.parse(new File(s)).getResult().get();
				App app = new App(compunit, metaDataStats);
				app.getMetrics();
				parsedFilesUnits.add(app);
				parsedFilesStatsList.addAll(app.getParsedFileStats());

			}
			System.out.println("size is: " + parsedFilesUnits.size());
			

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void writeParsedFilesToExcel() {
		//int i = 0;
		//for (App p : parsedFilesUnits) {
		//	i++;
		//	parsedFilesStatsList.addAll(p.getParsedFileStats());
			

		//}

		XLSX_read_write.writeFile(null, parsedFilesStatsList);

//		App.writeFile(null, parsedFilesStatsList);
	}

	private LinkedList<String> getParsedFilesStatsList() {
		return parsedFilesStatsList;
	}

	public String[][] getParsedFilesTabularData() {
		return XLSX_read_write.dataFormater(getParsedFilesStatsList());
	}
	/*
	 * public String[][] getProjectCodeSmells() throws NumberFormatException,
	 * PolyglotException, ScriptException{ assert
	 * !parsedFilesStatsList.isEmpty():"Não foi feito parsing do projeto";
	 * String[][] tabularData=getParsedFilesTabularData();
	 * System.out.println("tabularDataLength= "+tabularData.length); for(int
	 * i=1;i<tabularData.length-1;i++) { if(tabularData[i][0]==null)return
	 * tabularData; HashMap<String,Boolean>
	 * singleRowFlags=rulesInterpreter.getCodeSmellFlags(Integer.parseInt(
	 * tabularData[i][4]), Integer.parseInt(tabularData[i][5]),
	 * Integer.parseInt(tabularData[i][6]), Integer.parseInt( tabularData[i][8]),
	 * Integer.parseInt(tabularData[i][9]));
	 * if(singleRowFlags.get("long_method"))tabularData[i][10]="VERDADEIRO"; else
	 * tabularData[i][10]="FALSO";
	 * if(singleRowFlags.get("god_class"))tabularData[i][7]="VERDADEIRO"; else
	 * tabularData[i][7]="FALSO"; } return tabularData;
	 * 
	 * }
	 */

	public HashMap<String, String> getProjectData() {
		HashMap<String, String> projectStats = new HashMap<String, String>();
		projectStats.put("packages", String.valueOf(metaDataStats.getNumberOfPackages()));
		projectStats.put("classCounter", String.valueOf(metaDataStats.getClassCounter()));
		projectStats.put("totalLOC", String.valueOf(metaDataStats.getTotalLOC()));
		projectStats.put("methodCountID", String.valueOf(metaDataStats.getMethodCountID()));
		return projectStats;
	}

}
