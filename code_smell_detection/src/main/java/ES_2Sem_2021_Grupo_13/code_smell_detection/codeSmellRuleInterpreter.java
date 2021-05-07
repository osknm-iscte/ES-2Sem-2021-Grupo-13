package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.util.HashMap;

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
