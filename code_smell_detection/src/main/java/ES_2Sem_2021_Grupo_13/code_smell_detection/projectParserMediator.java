package ES_2Sem_2021_Grupo_13.code_smell_detection;

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
