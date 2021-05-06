package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.util.HashMap;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.graalvm.polyglot.PolyglotException;

public class codeSmellRuleInterpreter {

	private boolean long_method=false;
	private boolean god_class=false;
	private String script="if(LOC_method>10)long_method=true; else"
			+ " long_method=false;";
	private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	private ScriptEngine engine = scriptEngineManager.getEngineByName("graal.js");
	private Bindings bindings = engine.createBindings();
	

	public codeSmellRuleInterpreter(String script) {
		if(script!=null)
		this.script=script;
		
		
	}
	
	public HashMap<String,Boolean> getCodeSmellFlags(int NOM_class,int LOC_class,int WMC_class,int LOC_method,int CYCLO_method) throws PolyglotException, ScriptException {
	
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
	
	

}
