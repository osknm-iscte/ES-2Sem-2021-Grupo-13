package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;

public class XLSX_read_writeTest {

	
//	static final String PATH = "C:\\Users\\maria\\Downloads\\Code_Smells (2).xlsx";
	
//	static final String PATH2 = "C:\\Users\\maria\\Documents\\Smells.xlsx";
	
	static final String PATH  = System.getProperty("user.dir") + "/" + "Code_Smells (2).xlsx";
	static final String PATH2  = System.getProperty("user.dir") + "/" + "Smells.xlsx";

	
	
	
//	@Test
//	public void testXLSX_readFile() { 
//		
//		
////		System.out.println(newPath);
//		
//		try {
//			XLSX_read_write.readFile2(PATH);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	
	@Test
	public void testwriteFile() { 
		
		
		
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
		
		XLSX_read_write.writeFile(PATH2, data, null);
	
	}
	
	@Test
	public void testreadyExcelForGUI() { 
		
		
		try {
			XLSX_read_write.readyExcelForGUI(PATH2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
