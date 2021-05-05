package ES_2Sem_2021_Grupo_13.code_smell_detection;

public class projectParserMediator {

	private int methodCountID=0;
	private int classCounter=0;
	private int totalLOC=0;
	private int numberOfPackages=0;
	public int getMethodCountID() {
		return methodCountID;
	}
	public void incrementMethodCountID() {
		this.methodCountID =this.methodCountID+ 1;
	}
	public int getClassCounter() {
		return classCounter;
	}
	public void incrementClassCounter() {
		this.classCounter = this.classCounter+1;
	}
	public int getTotalLOC() {
		return totalLOC;
	}
	public void setTotalLOC(int totalLOC) {
		this.totalLOC = this.totalLOC+totalLOC;
	}
	public int getNumberOfPackages() {
		return numberOfPackages;
	}
	public void incrementNumberOfPackages() {
		this.numberOfPackages = this.numberOfPackages+1;
	}
	
}
