package Interactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import Exceptions.InvalidStation;
import Model.StoppingInfo;
import Model.Train;

public class SearchInteractorImpl implements SearchInteractor{
	DatabaseHandler db = null;
	MongoDatabase trainsdb = null;
	MongoDatabase stationsdb = null;

	public SearchInteractorImpl() {
		db = DatabaseHandler.getInstance();
		trainsdb = db.getTrainsDb();
		stationsdb = db.getStationsDb();
	}
	
	
	public Map<String , Double> getBasicPrice(String trainId){
		Map<String , Double> price = new HashMap();
		
		if(trainId != null && !trainId.equals("")) {
			Document priceDoc = (Document) trainsdb.getCollection(trainId).find(Filters.exists("Price")).first().get("Price");
			
			for(Entry<String , Object> entry : priceDoc.entrySet()) {
				price.put(entry.getKey() , Double.parseDouble(entry.getValue().toString()));
			}
		}
		
		return price;
	}
	
	@Override
	public boolean isValidStation(String station) {
		boolean isValid = false;
		
		
		if(station != null && !station.equals("") && stationsdb.getCollection(station).count() > 0) {isValid = true;}
		
		return isValid;
	}
	
	public Train getTrain(String trainId) {
		Train train = null;
		
		if(trainId != null && !trainId.equals("")) {
			FindIterable<Document> trainDoc = trainsdb.getCollection(trainId.toString()).find();
			train = IterDocToTrain(trainId, trainDoc);
		}
		
		return train;
	}
	
	
	public String getSationName(String station) throws InvalidStation {
		if(! isValidStation(station)) {throw new InvalidStation();}
		String stationName = null;
		stationName = (String) stationsdb.getCollection(station).find(Filters.exists("Name")).first().get("Name");
		return stationName;
	}
	
	private List<String> getCommonTrains(String src, String dest) {
		Set<String> srcTrains = new HashSet<String>();
		Set<String> destTrains = new HashSet<String>();

		MongoCollection<Document> srcTrainCollection = stationsdb.getCollection(src);

		FindIterable<Document> srcTrainsDocument = srcTrainCollection.find();

		// get all trains that stop in source
		for (Document d : srcTrainsDocument) {
			if (d.containsKey("Trains")) {
				srcTrains.addAll((List<String>) d.get("Trains"));
			}
		}

		MongoCollection<Document> destTrainCollection = stationsdb.getCollection(dest);

		FindIterable<Document> destTrainsDocument = destTrainCollection.find();

		// get all trains that stop in destination
		for (Document d : destTrainsDocument) {
			if (d.containsKey("Trains")) {
				destTrains.addAll((List<String>) d.get("Trains" , List.class));
			}
		}

		// finding the Intersection to find trains that stop in both Location

		Set<String> commonTrains = new HashSet(srcTrains);
		commonTrains.retainAll(destTrains);

		// commonTrains will contain common trains that come from src - dest and dest - src
		// By using Train route find src - dest trains

		List<String> trains = new ArrayList();

		for (String trainId : commonTrains) {
			List<String> route = getRoute(trainId);
			if (route.indexOf(src) < route.indexOf(dest)) {
				trains.add(trainId.toString());
			}
		}
		return trains;
	}
    
	
	// returns the route of train by TrainId
	public List<String> getRoute(String trainId) {
		FindIterable<Document> trainDocument = trainsdb.getCollection(trainId.toString()).find(Filters.exists("route"));
		List<String> route = new ArrayList();

		for (Document d : trainDocument) {
			route.addAll((List<String>) d.get("route"));
		}

		return route;
	}
    
	// Getting List of Trains which Starts at src and Stops at destination
	public List<Train> findTrains(String src, String dest) {
		List<Train> trains = new ArrayList();

		List<String> trainIds = getCommonTrains(src, dest);

		for (String trainId : trainIds) {
			FindIterable<Document> trainsDoc = trainsdb.getCollection(trainId.toString()).find();
			trains.add(IterDocToTrain(trainId, trainsDoc));
		}

		return trains;
	}
	
	
	// converts the Entry Object to StoppingInfo Object
	public StoppingInfo EntryToStoppingInfo(Entry<String, Object> entry) {

		String id = (String) entry.getKey();

		// parse the info from document
		Map<String, String> info = (Map<String, String>) entry.getValue();
		String arrives = info.get("Arrives");
		String departs = info.get("Departs");
		String halt = info.get("Halt");
		Integer distance = Integer.parseInt(info.get("Dist").toString());
		Integer day = Integer.parseInt(info.get("Day").toString());
		Integer index = Integer.parseInt(((Object) info.get("index")).toString());

		// creating Stopping Info Object and Adding to ArrayList

		return new StoppingInfo(id, arrives, departs, halt, distance, day, index);

	}
	
	// Converts the IteratbleDocument to Train Object
	public Train IterDocToTrain(String trainId , FindIterable<Document> trainsDoc) {
			 List<String> route = new ArrayList();
			 List<StoppingInfo> stoppinginfo = new ArrayList(); 
			 List<String> days = new ArrayList();
			 String trainName = "" ;
			 
			 for(Document d : trainsDoc) {
				 
				 for(Entry<String, Object> entry : d.entrySet()) {
					 
					 if(!entry.getKey().equals("_id")) {
						 
						 // if key = route , store it in route List
						 if(entry.getKey().equals("route")) {
							 route.addAll((List<String>) entry.getValue());
						 }
						 
						 else if(entry.getKey().equals("Name")) {
							 trainName = entry.getValue().toString();
						 }
						 
						 else if(entry.getKey().equals("days")) {
							 for(Object day : (List<Object>)entry.getValue()) {days.add(day.toString());}
							
						 }
						 
						 else if(entry.getKey().equals("Price")) {}
						 
						 else {
							 // parsing info from EntrySet , creating StoppingInfo object , Appending to List
							 stoppinginfo.add(EntryToStoppingInfo(entry));
						 }
					 }	 
				 }
			 }
			 
		    return (new Train(trainId ,trainName , stoppinginfo , route , days));
	}
}
