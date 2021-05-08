package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;

public class XLSX_read_writeTest {
	@Test
	public void testXLSX_read_write() { //?????
		
		
	}
	
	@Test
	public void testXLSX_readFile() { 
		
		String path = "C:\\Users\\maria\\Downloads\\Code_Smells (2).xlsx";
		
		try {
			XLSX_read_write.toTestreadFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testwriteFile() { 
		
		String path = "C:\\Users\\maria\\Documents\\Smells.xlsx";
		
		LinkedList<String> data = new LinkedList<String>();
		
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		data.add("rule_name");
		data.add("");
		data.add("rule_x");
		
		XLSX_read_write.writeFile(path, data);
	
	}
	
	@Test
	public void testreadyExcelForGUI() { 
		
		String path = "C:\\Users\\maria\\Documents\\Smells.xlsx";
		
		try {
			XLSX_read_write.readyExcelForGUI(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
