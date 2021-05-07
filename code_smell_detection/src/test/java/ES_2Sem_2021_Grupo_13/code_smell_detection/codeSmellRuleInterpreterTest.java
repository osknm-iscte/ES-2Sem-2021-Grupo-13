package ES_2Sem_2021_Grupo_13.code_smell_detection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import javax.script.ScriptException;

import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Test;

class codeSmellRuleInterpreterTest {

	@Test
	void testGetCodeSmellFlagsExpectedValues() {
		try {
			codeSmellRuleInterpreter ruleTest = new codeSmellRuleInterpreter(
					"if(LOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;");
			HashMap<String, Boolean> mock = new HashMap<String, Boolean>();
			mock.put("long_method", true);
			mock.put("god_class", false);

			HashMap<String, Boolean> testing = ruleTest.getCodeSmellFlags("if(LOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;",3, 100, 10, 52, 16);
			assertTrue(mock.equals(testing));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testGetCodeSmellFlagsException() {

		ScriptException exception = assertThrows(ScriptException.class, () -> {
			codeSmellRuleInterpreter ruleTest = new codeSmellRuleInterpreter(
					// sintaxe das regras mal escrito
					"ifLOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;");

			HashMap<String, Boolean> testing = ruleTest.getCodeSmellFlags("ifLOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;",3, 100, 10, 52, 16);

		});
	}

	@Test
	void testDetactWrongSyntax() {

		codeSmellRuleInterpreter ruleTest = new codeSmellRuleInterpreter(
				// sintaxe das regras mal escrito
				"ifLOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;");

		boolean testReturn = ruleTest.checkIfRuns("ifLOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;");
		assertEquals(false, testReturn);

	}

	@Test
	void testRightSyntax() {

		codeSmellRuleInterpreter ruleTest = new codeSmellRuleInterpreter(

				"ifLOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;");
		boolean testReturn=ruleTest.checkIfRuns("if(LOC_method>50 && CYCLO_method>10)long_method=true; else" + " long_method=false;");
		assertEquals(true, testReturn);
	}

}
