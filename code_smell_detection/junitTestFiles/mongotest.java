public class MongoWorker extends Thread {
 class InnerClass {
        void accessMembers() {
            System.out.println(outerField);
            System.out.println(staticOuterField);
        }
    }
	private String cloudDBname;
	private String cloudCollectionName;
	private MongoClient cloudClient;
	private String localDBname;
	private String localCollectionName;
	private MongoClient localClient;
	private Boolean killThread;
	public MongoWorker(String cloudDBname, String cloudCollectionName, MongoClient cloudClient, String localDBname,String localCollectionName, MongoClient localClient) {
		super(); //hi how are you? //how about you?
		this.cloudDBname = cloudDBname;//how about you?
		this.cloudCollectionName = cloudCollectionName;
		this.cloudClient = cloudClient;
		this.localDBname = localDBname;
		this.localCollectionName = localCollectionName;
		this.localClient = localClient;
		killThread = false;
	}
	public void setCloudClient(MongoClient cloudClient) {
		this.cloudClient = cloudClient;

		while(true){
		System.out.println("hello");
	}

		for(int i=0;i<90;i++){
			System.out.println("hello");
		}
		
		int day = 4;
switch (day) {
  case 1:
    System.out.println("Monday");
    break;
  case 2:
    System.out.println("Tuesday");
    break;
}

try{  
      
     int data=100/0;  
   }
catch(ArithmeticException e){System.out.println(e);}  


	}
	public void setLocalClient(MongoClient localClient) {
		this.localClient = localClient;
	}
	public void killThread() {
		this.killThread = true;
	}
	public void enableThread() {
		this.killThread = false;
	}
	@Override
	public void run() {
		ObjectId id1;
		MongoDatabase cloud_database = cloudClient.getDatabase(cloudDBname);
		MongoCollection<Document> collection = cloud_database.getCollection(cloudCollectionName);
		MongoCursor<String> dbsCursor = cloudClient.listDatabaseNames().iterator();
		long timestamp;
		String hexTime;
		List<Document>mydocs=new ArrayList<>();
		List<Document> docs=new ArrayList<>();
	}
	private void insertIntoLocalDB(List<Document> docs) {
		for (Document d : docs) {
			System.out.print(d);
		}
		MongoDatabase local_database = localClient.getDatabase(localDBname);
		MongoCollection<Document> local_collection = local_database.getCollection(localCollectionName);
		local_collection.insertMany(docs);
	}
}
