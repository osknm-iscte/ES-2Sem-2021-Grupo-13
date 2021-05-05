package ES_2Sem_2021_Grupo_13.code_smell_detection;

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

	public projectParser(Path path) {
		super();
		this.path = path;
		//tem primeiro de verificar se existe ficheiro com regras
		rulesInterpreter=new codeSmellRuleInterpreter("if(LOC_method>10)long_method=true; else" + " long_method=false;");
	}

	public void parseJavaFiles() {

		List<String> paths;
		try {
			paths = App.listFiles(path);
			ParserConfiguration configuration = new ParserConfiguration();
			configuration.setLexicalPreservationEnabled(true);
			JavaParser javaParser = new JavaParser(configuration);
			for (String s : paths) {
				CompilationUnit compunit = javaParser.parse(new File(s)).getResult().get();
				App app = new App(compunit);
				parsedFilesUnits.add(app);
				app.getMetrics();
			}
			

		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	public void writeParsedFilesToExcel() {
		for (App p : parsedFilesUnits) {
			parsedFilesStatsList.addAll(p.getParsedFileStats());
		}
//		App.writeFile(null, parsedFilesStatsList);
	}
	
	private LinkedList<String> getParsedFilesStatsList(){
		return parsedFilesStatsList;
	}
	public String[][] getParsedFilesTabularData(){
		return App.dataFormater(getParsedFilesStatsList());
	}
	public String[][] getProjectCodeSmells() throws NumberFormatException, PolyglotException, ScriptException{
		assert !parsedFilesStatsList.isEmpty():"Não foi feito parsing do projeto";
		String[][] tabularData=getParsedFilesTabularData();
		System.out.println("tabularDataLength= "+tabularData.length);
		for(int i=1;i<tabularData.length-1;i++) {
			if(tabularData[i][0]==null)return tabularData;
			HashMap<String,Boolean> singleRowFlags=rulesInterpreter.getCodeSmellFlags(Integer.parseInt(tabularData[i][4]), Integer.parseInt(tabularData[i][5]),
										Integer.parseInt(tabularData[i][6]),
										Integer.parseInt( tabularData[i][8]), Integer.parseInt(tabularData[i][9]));
			if(singleRowFlags.get("long_method"))tabularData[i][10]="Verdadeiro";
			else tabularData[i][10]="Falso";
			if(singleRowFlags.get("god_class"))tabularData[i][7]="Verdadeiro";
			else tabularData[i][7]="Falso";
		}
		return tabularData;
		
	}

}
