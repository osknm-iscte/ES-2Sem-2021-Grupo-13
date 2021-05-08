package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.graalvm.polyglot.PolyglotException;

public class codeSmellRuleInterpreter {

	
	private  static String SCRIPT="if(LOC_method>10)long_method=true; else"
			+ " long_method=false;";
	private String script="if(LOC_method>10)long_method=true; else"
			+ " long_method=false;";
	

	public codeSmellRuleInterpreter(String script) {
		if(script!=null)
		this.script=script;
		
	}
	
	public static HashMap<String, Integer> testRuleAccuracy(String scriptToTest, int code_smell_id)
			throws IOException, NumberFormatException, PolyglotException, ScriptException {

		projectParser testingProject = new projectParser(
				Paths.get(System.getProperty("user.dir") + "/rulesTesting/jasml/"));
		testingProject.parseJavaFiles();
		String[][] testingRuleCodeSmells = getProjectCodeSmells(testingProject.getParsedFilesTabularData(),
				scriptToTest);
		testingRuleCodeSmells = preprocessData(testingRuleCodeSmells);
		String[][] testingData = XLSX_read_write
				.readyExcelForGUI(System.getProperty("user.dir") + "/rulesTesting/excelForTesting/Code_Smells.xlsx");
		testingData=preprocessData(testingData);
		HashMap<String, Boolean> testingRulesCodeSmellFlags = getMetricsInHashMap(testingRuleCodeSmells, code_smell_id);
		HashMap<String, Boolean> testAgainstModel = getMetricsInHashMap(testingData, code_smell_id);
		return compareHashMapsAndGetResults(testingRulesCodeSmellFlags, testAgainstModel);
	}

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

		results.put("truePositive", truePositiveCounter);
		results.put("trueNegative", trueNegativeCounter);
		results.put("falsePositiveCounter", falsePositiveCounter);
		results.put("falseNegative", falseNegativeCounter);
		
		return results;

	}

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

		Iterator hmIterator = excelInHash.entrySet().iterator();
		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry) hmIterator.next();

			System.out.println(mapElement.getKey() + " : " + mapElement.getValue());
		}
		return excelInHash;
	}

	
	public static HashMap<String,Boolean> getCodeSmellFlags(String script,int NOM_class,int LOC_class,int WMC_class,int LOC_method,int CYCLO_method) throws PolyglotException, ScriptException {
		 ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		 ScriptEngine engine = scriptEngineManager.getEngineByName("graal.js");
		 Bindings bindings = engine.createBindings();
		 boolean long_method=false;
		 boolean god_class=false;
		HashMap<String,Boolean>flags=new HashMap<String,Boolean>();
		bindings.put("long_method", long_method);
		bindings.put("god_class", god_class);
		bindings.put("NOM_class", NOM_class);
		bindings.put("LOC_class", LOC_class);
		bindings.put("WMC_class", WMC_class);
		bindings.put("LOC_method", LOC_method);
		bindings.put("CYCLO_method", CYCLO_method);
	
			Object obj=engine.eval(script,bindings);
			flags.put("long_method", (Boolean) bindings.get("long_method"));
			flags.put("god_class", (Boolean) bindings.get("god_class"));
			bindings.clear();
			return flags;
		}
	
	
	public static boolean checkIfRuns(String script) {
		int NOM_class=10;
		int LOC_class=10;
		int WMC_class=10;
		int LOC_method=10;
		int CYCLO_method=10;
		boolean long_method=false;
		boolean god_class=false;
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
				Object obj=engine.eval(script,bindings);
				return true;
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				return false;
			}
		
		
	}
	
	
	public static String[][] getProjectCodeSmells(String[][] tabularData,String script) throws NumberFormatException, PolyglotException, ScriptException{
		
		for(int i=1;i<tabularData.length-1;i++) {
			if(tabularData[i][0]==null)return tabularData;
			HashMap<String,Boolean> singleRowFlags=getCodeSmellFlags(script,Integer.parseInt(tabularData[i][4]), Integer.parseInt(tabularData[i][5]),
										Integer.parseInt(tabularData[i][6]),
										Integer.parseInt( tabularData[i][8]), Integer.parseInt(tabularData[i][9]));
			if(singleRowFlags.get("long_method"))tabularData[i][10]="VERDADEIRO";
			else tabularData[i][10]="FALSO";
			if(singleRowFlags.get("god_class"))tabularData[i][7]="VERDADEIRO";
			else tabularData[i][7]="FALSO";
		}
		return tabularData;
		
	}

	public static String getDefaultRule() {
		return SCRIPT;
	}
}
