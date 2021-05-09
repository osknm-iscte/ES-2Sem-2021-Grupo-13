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

/**
 * Classe que representa o parsing do projeto java. Permite explorar todos os ficheiros java
 * que existem dentro de uma diretoria e dentro das subdiretorias e faz o parsing de todos os projetos.
 * Possui métodos que permites extrair estatisticas gerais do projeto.
 * @author iscte-iul grupo 13
 *
 */
/**
 * @author iscte-iul grupo 13
 *
 */

public class projectParser {

	private Path path;
	private List<App> parsedFilesUnits = new ArrayList<>();
	private LinkedList<String> parsedFilesStatsList = new LinkedList<String>();
	private codeSmellRuleInterpreter rulesInterpreter;
	private int methodID = 0;
	private projectParserMediator metaDataStats = new projectParserMediator();

	/**
	 * Construtor leva como argumento caminho em String da diretoria onde está o projeto java.
	 * @param path representa a diretoria do projeto onde estão os ficheiros java
	 */
	public projectParser(Path path) {
		super();
		this.path = path;
		// tem primeiro de verificar se existe ficheiro com regras
		rulesInterpreter = new codeSmellRuleInterpreter(
				"if(LOC_method>10)long_method=true; else" + " long_method=false;");
	}

	/**
	 * Método que executa o parsing e cálculo de métricas de todos os ficheiros java que estão dentro de uma
	 * diretoria. 
	 * 
	 */
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

	/**
	 * Invoca o método da classe de XLSX_read_write para escrever os dados das métricas cácluladas para um ficheiro excel.
	 */
	public void writeParsedFilesToExcel() {
		
		XLSX_read_write.writeFile(null, parsedFilesStatsList,path.getFileName().toString());

	}

	/**
	 * Devolve a representação dos dados das métricas num double Array.
	 * @return dados na forma de double array
	 */
	public String[][] getParsedFilesTabularData() {
		return XLSX_read_write.dataFormater(parsedFilesStatsList);
		
	}
	
	
	
	/**
	 * Devolve a estatistica geral do projeto num HashMap.
	 * @return HashMap com dados estatisticos
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
