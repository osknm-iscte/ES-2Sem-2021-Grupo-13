package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.graalvm.polyglot.PolyglotException;

/**
 * @author iscte-iul grupo 13
 *
 */
public class codeSmellRuleInterpreter {

	private static String SCRIPT = "if(LOC_method>10)long_method=true; else" + " long_method=false;";
	private String script = "if(LOC_method>10)long_method=true; else" + " long_method=false;";

	public codeSmellRuleInterpreter(String script) {
		if (script != null)
			this.script = script;

	}

	/**
	 * Recebe como argumentos valores que representam as métricas que podem ser usadas dentro do script das regras.
	 * Retorna um HashMap que indica a detecção de code smells. chaves do hash a usar são nomes dos code smells. Neste caso
	 * long_method e god_class
	 * @param script script na forma de String a ser usado para calcular code smells
	 * @param NOM_class Numero de metodos da classe. Parametro  de input que pode ser usada no script
	 * @param LOC_class Numero de linhas de código da classe. Parametro  de input que pode ser usada no script
	 * @param WMC_class Complexidade ciclomática da classe. Parametro  de input que pode ser usada no script
	 * @param LOC_method Numero de linhas de código do método. Parametro  de input que pode ser usada no script
	 * @param CYCLO_method Complexidade ciclomática do método. Parametro  de input que pode ser usada no script
	 * @return HashMap com code smells flags 
	 * @throws PolyglotException pode gerar PolyglotException
	 * @throws ScriptException pode gerar ScriptException
	 */
	public static HashMap<String, Boolean> getCodeSmellFlags(String script, int NOM_class, int LOC_class, int WMC_class,
			int LOC_method, int CYCLO_method) throws PolyglotException, ScriptException {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine engine = scriptEngineManager.getEngineByName("graal.js");
		Bindings bindings = engine.createBindings();
		boolean long_method = false;
		boolean god_class = false;
		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		bindings.put("long_method", long_method);
		bindings.put("god_class", god_class);
		bindings.put("NOM_class", NOM_class);
		bindings.put("LOC_class", LOC_class);
		bindings.put("WMC_class", WMC_class);
		bindings.put("LOC_method", LOC_method);
		bindings.put("CYCLO_method", CYCLO_method);

		Object obj = engine.eval(script, bindings);
		flags.put("long_method", (Boolean) bindings.get("long_method"));
		flags.put("god_class", (Boolean) bindings.get("god_class"));
		bindings.clear();
		return flags;
	}

	/**
	 * Verifica se a definição do script tem uma sintaxe válida.
	 * Retorna o booleano a dizer se o script é valido ou não.
	 * 
	 * @param script representa o script a testar
	 * @return booleano que indica se script é valido ou não
	 */
	public static boolean checkIfRuns(String script) {
		int NOM_class = 10;
		int LOC_class = 10;
		int WMC_class = 10;
		int LOC_method = 10;
		int CYCLO_method = 10;
		boolean long_method = false;
		boolean god_class = false;
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine engine = scriptEngineManager.getEngineByName("graal.js");
		Bindings bindings = engine.createBindings();
		bindings.put("long_method", long_method);
		bindings.put("god_class", god_class);
		bindings.put("NOM_class", NOM_class);
		bindings.put("LOC_class", LOC_class);
		bindings.put("WMC_class", WMC_class);
		bindings.put("LOC_method", LOC_method);
		bindings.put("CYCLO_method", CYCLO_method);

		try {
			Object obj = engine.eval(script, bindings);
			return true;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			return false;
		}

	}

	/**
	 * Calcula os code smells usando as metricas recebidas de input e devolve 
	 * as metricas com cálculo de code smells.
	 * @param tabularData Representa as métricas em forma de um double array.
	 * @param script Script a usar para detetar os code smells
	 * @return String[][] que representa o array recebido de input com a deteccao de code smells.
	 * @throws NumberFormatException pode gerar NumberFormatException 
	 * @throws PolyglotException pode gerar PolyglotException
	 * @throws ScriptException pode gerar ScriptException
	 */
	public static String[][] getProjectCodeSmells(String[][] tabularData, String script)
			throws NumberFormatException, PolyglotException, ScriptException {

		for (int i = 1; i < tabularData.length - 1; i++) {
			if (tabularData[i][0] == null)
				return tabularData;
			HashMap<String, Boolean> singleRowFlags = getCodeSmellFlags(script, Integer.parseInt(tabularData[i][4]),
					Integer.parseInt(tabularData[i][5]), Integer.parseInt(tabularData[i][6]),
					Integer.parseInt(tabularData[i][8]), Integer.parseInt(tabularData[i][9]));
			if (singleRowFlags.get("long_method"))
				tabularData[i][10] = "VERDADEIRO";
			else
				tabularData[i][10] = "FALSO";
			if (singleRowFlags.get("god_class"))
				tabularData[i][7] = "VERDADEIRO";
			else
				tabularData[i][7] = "FALSO";
		}
		return tabularData;

	}

	/**
	 * @return retorna o script definido por default
	 */
	public static String getDefaultRule() {
		return SCRIPT;
	}

	/**
	 * Testa a acurácia da regra usada pelo utilizador na detecção dos code smells.
	 * Recebe como argumento script com regras na forma de string a testar e o id do
	 * code smell que pretendemos analisar. É devolvido um HashMap com 4 valores que guarda
	 * verdadeiros positivos, verdadeiros negativos, falsos positivos, falsos negativos.
	 * 
	 * @param scriptToTest script a testar
	 * @param code_smell_id id do code smell que identifica code smell usado no teste.
	 * @return HashMap que representa os valores da matriz de confusão.
	 * @throws IOException pode gerar IOException
	 * @throws NumberFormatException pode gerar NumberFormatException
	 * @throws PolyglotException pode gerar PolyglotException
	 * @throws ScriptException pode gerar ScriptException
	 */
	public static HashMap<String, Integer> testRuleAccuracy(String scriptToTest, int code_smell_id)
			throws IOException, NumberFormatException, PolyglotException, ScriptException {

		projectParser testingProject = new projectParser(
				Paths.get(System.getProperty("user.dir") + "/rulesTesting/jasml/"));
		testingProject.parseJavaFiles();
		String[][] testingRuleCodeSmells = getProjectCodeSmells(testingProject.getParsedFilesTabularData(),
				scriptToTest);
		testingRuleCodeSmells = preprocessData(testingRuleCodeSmells);
		String[][] testingData = XLSX_read_write.readyExcelForGUI(System.getProperty("user.dir") + "/rulesTesting/excelForTesting/Code_Smells.xlsx");
		testingData=preprocessData(testingData);
		HashMap<String, Boolean> testingRulesCodeSmellFlags = getMetricsInHashMap(testingRuleCodeSmells, code_smell_id);
		HashMap<String, Boolean> testAgainstModel = getMetricsInHashMap(testingData, code_smell_id);
		return compareHashMapsAndGetResults(testingRulesCodeSmellFlags, testAgainstModel);
	}

	/**
	 * Transforma os valores booleanos presentes no parametro de input na representação de String.
	 * Serve para tratar o probelma de ler o excel com APOI que transforma depois Strings em booleanos. 
	 * @param testingRuleCodeSmells
	 * @return dados processados
	 */
	private static String[][] preprocessData(String[][] testingRuleCodeSmells) {
		for (int i = 0; i < testingRuleCodeSmells.length; i++) {
			if (testingRuleCodeSmells[i][1] != null) {
				if (testingRuleCodeSmells[i][7] == "false")
					testingRuleCodeSmells[i][7] = "FALSO";
				if (testingRuleCodeSmells[i][7] == "true")
					testingRuleCodeSmells[i][7] = "VERDADEIRO";
				if (testingRuleCodeSmells[i][10] == "false")
					testingRuleCodeSmells[i][10] = "FALSO";
				if (testingRuleCodeSmells[i][10] == "true")
					testingRuleCodeSmells[i][10] = "VERDADEIRO";
			}
		}
		return testingRuleCodeSmells;

	}

	/**
	 * Compara os dois HashMaps para calcular os valores da matriz de confusão. testingRulesCodeSmells
	 * representa os code smells calculados usando a regra dos testes sobre o projeto jasml. O segundo HashMap
	 * representa o excel de referência para o teste que representa os code smells do projeto jasml.
	 * A chave destes HashMaps consiste numa String que junta o nome do pacote +.+ nome da classe +.+nome do metodo.
	 * Devolve HashMap com o cálculo dos valores de matriz de confusão
	 * @param testingRulesCodeSmellFlags HashMap que representa os code smells calculados usando a regra dos testes sobre o projeto jasml
	 * @param testAgainstModel HashMap que representa os valores de referência para o teste.
	 * @return HashMap que representa os valores de  matriz de confusão
	 */
	private static HashMap<String, Integer> compareHashMapsAndGetResults(
			HashMap<String, Boolean> testingRulesCodeSmellFlags, HashMap<String, Boolean> testAgainstModel) {
		int truePositiveCounter = 0;
		int falsePositiveCounter = 0;
		int trueNegativeCounter = 0;
		int falseNegativeCounter = 0;
		HashMap<String, Integer> results = new HashMap<String, Integer>();

		for (String key : testingRulesCodeSmellFlags.keySet()) {

			if (testAgainstModel.containsKey(key)) {
				if (testingRulesCodeSmellFlags.get(key) == testAgainstModel.get(key)) {
					if (testingRulesCodeSmellFlags.get(key) == true)
						truePositiveCounter++;
					else
						trueNegativeCounter++;

				} else {
					if (testingRulesCodeSmellFlags.get(key) == true && testAgainstModel.get(key) == false)
						falsePositiveCounter++;
					else if (testingRulesCodeSmellFlags.get(key) == false && testAgainstModel.get(key) == true)
						falseNegativeCounter++;

				}
			}
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		for (String key : testingRulesCodeSmellFlags.keySet()) {

			if (testAgainstModel.containsKey(key)) {
				System.out.println(key);
			}
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");


		results.put("truePositive", truePositiveCounter);
		results.put("trueNegative", trueNegativeCounter);
		results.put("falsePositiveCounter", falsePositiveCounter);
		results.put("falseNegative", falseNegativeCounter);
		
		return results;

	}

	/**
	 * Extrai do array o nome do pacote, classe e metodo e o valor do code smell identificado pelo id.
	 * Devolve um HashMap que vai servir para testar acurácia da regra selecionada pelo utilizador
	 * @param dataToConvert double array que representa as metricas. 
	 * @param code_smell_id id que representa o code smells cujos valores se pretende extrair do array
	 * @return HashMap cuja chave consiste numa String que junta o nome do pacote +.+ nome da classe +.+nome do metodo. O valor é o booleano de code smell identificado pelo id
	 * (god_class ou long_method)
	 * @throws IOException pode gerar IOException
	 */
	public static HashMap<String, Boolean> getMetricsInHashMap(String[][] dataToConvert, int code_smell_id)
			throws IOException {
		HashMap<String, Boolean> excelInHash = new HashMap<String, Boolean>();
		for (int i = 1; i < dataToConvert.length; i++) {

			if (dataToConvert[i][1] != null) {

				if (!excelInHash
						.containsKey(dataToConvert[i][1] + "." + dataToConvert[i][2] + "." + dataToConvert[i][3])) {

					boolean code_smell;

					if (dataToConvert[i][code_smell_id] == "VERDADEIRO")
						code_smell = true;
					else
						code_smell = false;

					excelInHash.put(dataToConvert[i][1] + "." + dataToConvert[i][2] + "." + dataToConvert[i][3],
							code_smell);
				}

			} else
				break;
		}

		//Iterator hmIterator = excelInHash.entrySet().iterator();
		//while (hmIterator.hasNext()) {
		//	Map.Entry mapElement = (Map.Entry) hmIterator.next();

		//	System.out.println(mapElement.getKey() + " : " + mapElement.getValue());
		//}
		return excelInHash;
	}
	

}
