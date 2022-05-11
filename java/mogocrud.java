import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;




public class mogocrud {
	
	public static void addTrainToDb(MongoClient client, String[] trains1) throws IOException {
		MongoDatabase db = client.getDatabase("Trains");
		
		File file;
		BufferedReader br;
		String st;

		for (String train : trains1) {
			
			file = new File("E:\\eclipse\\Edwin Learning\\src\\learning\\" + train + ".txt");
			br = new BufferedReader(new FileReader(file));
            
			
			MongoCollection<Document> trains = db.getCollection(train);
			
			int index = 0;
			
			List<String> route = new ArrayList();
			
			while ((st = br.readLine()) != null) {
				// Print the string
				String[] arr = st.split(",");
				System.out.format("%s   %s  %s  %s\n", arr[1], arr[5], arr[6], arr[10]);
                
				route.add(arr[1]);
				
				Document info = new Document();
               
				info.append("Arrives", arr[5]);
				info.append("Departs", arr[6]);
				info.append("Halt" , arr[7]);
				info.append("Dist" , arr[9]);
				info.append("Day", arr[10]);
                info.append("index" , index);
                
                Document trainInfo = new Document();
                
                trainInfo.put(arr[1], info);
                
                trains.insertOne(trainInfo);
                index += 1;
			}
			
			trains.insertOne(new Document("route" , route));
		}
	}
	
	public static void AddStoppingFromtrains(List<String> trains , MongoClient client)
			throws InterruptedException, ExecutionException, IOException {
        
		MongoDatabase db = client.getDatabase("Stations");
		
		File file;
		BufferedReader br;
		String st;

		Map<String, List<String>> TrainStopMap = new HashMap<String, List<String>>();
        Map<String , String> TrainNameMap = new HashMap();
		
		for (String train : trains) {
			file = new File("E:\\eclipse\\Edwin Learning\\src\\learning\\" + train + ".txt");
			br = new BufferedReader(new FileReader(file));

			while ((st = br.readLine()) != null) {
				String[] arr = st.split(",");

				if (TrainStopMap.containsKey(arr[1])) {
					TrainStopMap.get(arr[1]).add(train);
				}

				else {
					ArrayList<String> trainList = new ArrayList();
					trainList.add(train);
					TrainStopMap.put(arr[1] , trainList);
					TrainNameMap.put(arr[1] , arr[2]);
				}

			}
		}
        
		System.out.println(TrainStopMap);
		System.out.println(TrainNameMap);
		System.out.println(TrainNameMap.size());
		System.out.println(TrainStopMap.size());
		
		for (Entry<String, List<String>> entry : TrainStopMap.entrySet()) {
            
			MongoCollection<Document> stations = db.getCollection(entry.getKey());
			
			stations.insertOne(new Document("Trains" , entry.getValue()));
			stations.insertOne(new Document("Name" , TrainNameMap.get(entry.getKey())));
		}
	}
	

	
	public static void main(String [] args) throws IOException, InterruptedException, ExecutionException {
		MongoClient client = new MongoClient("localhost" , 27017);
		
		

		String [] trains1 = {"12027" , "12028" , "12661" ,"12662", "16101" , "16102" , "22351" , "22352"};
		
		
		AddStoppingFromtrains(Arrays.asList(trains1), client);
		
		
		MongoDatabase Train = client.getDatabase("Trains");
		MongoCollection<Document> pothigai = Train.getCollection("16101");
		
		 FindIterable<Document> docs= pothigai.find();
		
		 List<String> route;
		 
		 Trains train = new Trains();
		 train.id = 16101;
		 
		 System.out.println(train);
		 
 		 for(Document d : docs) {
               
 			 for(Entry entry : d.entrySet()) {
 				 if(!entry.getKey().equals("_id")) {
 					 
 					 if(entry.getKey().equals("route")) {
 						 train.route = (List<String>) entry.getValue();
 					 }
 					 
 				 else {
 					Map<String , Object> map = (Map<String, Object>) entry.getValue();
 					StoppingInfo info = new StoppingInfo();
 					info.id = (String) entry.getKey();
 					info.Arrives = (String) map.get("Arrives");
 					info.Departs = (String) map.get("Departs");
 					info.Halt = (String) map.get("Halt");
 					info.Distance = (String) map.get("Dist");
 					info.Day = (String) map.get("Day");
 					info.index = (int) map.get("index");
 					
// 					System.out.println(info);
 					train.stops.add(info);
 				 }
 				 }
 			 }
 		 }
 		 System.out.println(train.stops);
	}
		
	}

class StoppingInfo{
	public String id;
	public String Arrives;
	public String Departs;
	public String Halt;
	public String Distance;
	public String Day;
	public Integer index;
	
	
	public String toString() {
		return (  "id - " + id 
				+ " Arr - " + Arrives 
				+ " Dept - " + Departs
				+ " index - " + index
				+ "\n");
	}
}


class Trains{
	public int id;
	public List<StoppingInfo> stops = new ArrayList();
	public List<String> route = new ArrayList();
	
	public String toString() {
		return "id - "+ id +  "\n stops - " + stops + "\n route - " + route; 
	}
}