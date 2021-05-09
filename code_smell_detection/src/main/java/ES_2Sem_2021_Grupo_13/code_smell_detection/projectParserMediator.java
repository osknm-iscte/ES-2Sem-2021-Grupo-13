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

	private int methodCountID = 0;
	private int classCounter = 0;
	private int totalLOC = 0;
	private int numberOfPackages = 0;

	public int getMethodCountID() {
		return methodCountID;
	}

	public void incrementMethodCountID() {
		this.methodCountID = this.methodCountID + 1;
	}

	public int getClassCounter() {
		return classCounter;
	}

	public void incrementClassCounter() {
		this.classCounter = this.classCounter + 1;
	}

	public int getTotalLOC() {
		return totalLOC;
	}

	public void addTotalLOC(int totalLOC) {
		this.totalLOC = this.totalLOC + totalLOC;
	}

	public int getNumberOfPackages() {
		return numberOfPackages;
	}

	public void incrementNumberOfPackages() {
		this.numberOfPackages = this.numberOfPackages + 1;
	}

}
