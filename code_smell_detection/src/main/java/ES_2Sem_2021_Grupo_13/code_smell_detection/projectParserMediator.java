package ES_2Sem_2021_Grupo_13.code_smell_detection;

/**
 * POJO class que armazena as estatisticas gerais do projeto. Este POJO é partilhado pelo projectParser 
 * e pelos objetos da class App que representam os ficheiros de java sobre os quais foi feito parsing. Cada objeto da classe App,
 * a medida que está efetuar cálculos de métricas, incrementa os valores do POJO. Após processamento de todos os 
 * ficheiros por projectParser é possível obter estatisticas gerais do projeto.
 *
 * 
 *  
 * @author iscte-iul grupo 13
 *
 */
public class projectParserMediator {

	private static int methodCountID = 0;
	private static int classCounter = 0;
	private static int totalLOC = 0;
	private static int numberOfPackages = 0;

	public static int getMethodCountID() {
		return methodCountID;
	}

	public static void incrementMethodCountID() {
		methodCountID = methodCountID + 1;
	}

	public static int getClassCounter() {
		return classCounter;
	}

	public static void incrementClassCounter() {
		classCounter = classCounter + 1;
	}

	public static int getTotalLOC() {
		return totalLOC;
	}

	public static void addTotalLOC(int newTotalLOC) {
		totalLOC = totalLOC + newTotalLOC;
	}

	public static int getNumberOfPackages() {
		return numberOfPackages;
	}

	public static void incrementNumberOfPackages() {
		numberOfPackages = numberOfPackages + 1;
	}

}
