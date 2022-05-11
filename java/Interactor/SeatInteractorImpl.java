package Interactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import Model.AllotedSeat;
import Model.BerthType;
import Model.CoachType;
import Model.SeatArrangement;
import Model.TicketStatus;

public class SeatInteractorImpl implements SeatInteractor {
	DatabaseHandler db = null;
	MongoDatabase seatsdb = null;
	SearchInteractor trainSearch = null;

	public SeatInteractorImpl() {
		db = DatabaseHandler.getInstance();
		seatsdb = db.getSeatsdb();
		trainSearch = new SearchInteractorImpl();
	}

//------------------ Create Seats ------------------------------------------------//
	public void CreateSeat(String date, String trainId, SeatArrangement seat) {
		Integer routeLength = trainSearch.getRoute(trainId).size();

		MongoCollection<Document> train = seatsdb.getCollection(trainId);

		// Changing Seat Structure to Document Type to save it in Db

		Document Ac1 = seatToDoc(seat.getAc1(), routeLength);

		Document Ac2 = seatToDoc(seat.getAc2(), routeLength);

		Document Ac3 = seatToDoc(seat.getAc3(), routeLength);

		Document Sleeper = seatToDoc(seat.getSleeper(), routeLength);

		Document SS = seatToDoc(seat.getSS(), routeLength);

		Document doc = new Document();
		doc.append("AC1", Ac1);
		doc.append("AC2", Ac2);
		doc.append("AC3", Ac3);
		doc.append("Sleeper", Sleeper);
		doc.append("SS", SS);

		train.insertOne(new Document(date, doc));
	}

	public Document seatToDoc(Map<String, Map<String, List<String>>> secondClassSeat, Integer routeLength) {

		Document seatDocument = new Document();

		// getting Compartment and Seat Type
		for (Entry<String, Map<String, List<String>>> entry : secondClassSeat.entrySet()) {

			Document coachSeat = new Document();

			// getting Seat Type and seats
			for (Entry<String, List<String>> coach : entry.getValue().entrySet()) {

				Document seatWithRoute = new Document();

				// adding route to indicate that they are available in these routes
				for (String seatNum : coach.getValue()) {
					seatWithRoute.put(seatNum, Arrays.asList("0-" + (routeLength - 1)));

				}

				coachSeat.put(coach.getKey(), seatWithRoute);
			}

			seatDocument.put(entry.getKey(), coachSeat);

		}

		return seatDocument;
	}

// -----------------------------------------get Available Seats ---------------------------------------------
	public SeatArrangement getAvailableSeats(String trainId, String deptDate, String src, String dest) {
		List<String> route = trainSearch.getRoute(trainId);

		FindIterable<Document> seatsIter = seatsdb.getCollection(trainId).find(Filters.exists(deptDate));

		Map<String, Map<String, List<String>>> Ac1 = null;
		Map<String, Map<String, List<String>>> Ac2 = null;
		Map<String, Map<String, List<String>>> Ac3 = null;
		Map<String, Map<String, List<String>>> Sleeper = null;
		Map<String, Map<String, List<String>>> SS = null;

		for (Document d : seatsIter) {

			Document Ac1doc = (Document) ((Document) d.get(deptDate)).get("AC1");
			Document Ac2doc = (Document) ((Document) d.get(deptDate)).get("AC2");
			Document Ac3doc = (Document) ((Document) d.get(deptDate)).get("AC3");
			Document Sleeperdoc = (Document) ((Document) d.get(deptDate)).get("Sleeper");
			Document SSdoc = (Document) ((Document) d.get(deptDate)).get("SS");

			Integer srcIndex = route.indexOf(src);
			Integer destIndex = route.indexOf(dest);

			Ac1 = docToSeat(Ac1doc, srcIndex, destIndex);
			Ac2 = docToSeat(Ac2doc, srcIndex, destIndex);
			Ac3 = docToSeat(Ac3doc, srcIndex, destIndex);
			Sleeper = docToSeat(Sleeperdoc, srcIndex, destIndex);
			SS = docToSeat(SSdoc, srcIndex, destIndex);
		}

		if (Ac1 == null && Ac2 == null && Ac3 == null && Sleeper == null && SS == null) {
			return null;
		}

		return new SeatArrangement(Ac1, Ac2, Ac3, Sleeper, SS);
	}

	// logic for checking whether the seat is available in this route
	private boolean isSeatAvailable(List<String> Availroutes, Integer src, Integer dest) {
		boolean isAvailable = false;

		for (String routes : Availroutes) {
			Integer start = Integer.parseInt(routes.split("-")[0]);
			Integer end = Integer.parseInt(routes.split("-")[1]);

			if (src >= start && dest <= end) {
				isAvailable = true;
				break;
			}
		}
		return isAvailable;
	}

	// gives List of All the seats
	private List<String> getAvailSeats(Document seatsdoc, Integer src, Integer dest) {

		List<String> AvailSeats = new ArrayList<String>();
		for (Entry<String, Object> seat : seatsdoc.entrySet()) {
			String seatName = seat.getKey();

			List<String> Availroutes = (List<String>) seat.getValue();

			if (isSeatAvailable(Availroutes, src, dest)) {
				AvailSeats.add(seatName);
			}
		}

		return AvailSeats;
	}

	// gives List of seats for different seat Types (Lower , Middle , Upper ,
	// sideLower , SideUpper)
	private Map<String, List<String>> getAvailSeatTypes(Document seatTypesDoc, Integer src, Integer dest) {
		Map<String, List<String>> AvailSeatTypes = new HashMap<String, List<String>>();

		for (Entry<String, Object> seatType : seatTypesDoc.entrySet()) {
			String seatTypeName = seatType.getKey();

			Document seatsdoc = (Document) seatType.getValue();

			List<String> AvailSeats = getAvailSeats(seatsdoc, src, dest);

			AvailSeatTypes.put(seatTypeName, AvailSeats);
		}
		return AvailSeatTypes;

	}

	// converts the Document type seat from db to Map<Compartment , Map<SeatType ,
	// List<Seat>>>
	public Map<String, Map<String, List<String>>> docToSeat(Document seatdocs, Integer src, Integer dest) {

		Map<String, Map<String, List<String>>> AvailComp = new HashMap<>();

		for (Entry<String, Object> compartment : seatdocs.entrySet()) {
			String compartmentName = compartment.getKey();

			Document seatTypesDoc = (Document) compartment.getValue();

			Map<String, List<String>> AvailSeatTypes = getAvailSeatTypes(seatTypesDoc, src, dest);

			AvailComp.put(compartmentName, AvailSeatTypes);
		}
		return (AvailComp);
	}

//get Available route for the Seat
	public List<String> getAvailableRoute(String trainId, String deptDate, CoachType coachType, String Coach,
			TicketStatus ticketStatus, BerthType berthType, String seatNumber) {
		List<String> route = trainSearch.getRoute(trainId);

		Document trainSeats = seatsdb.getCollection(trainId).find(Filters.exists(deptDate)).first();
		String seatType = "";

		if (berthType == null) {
			seatType = ticketStatus.name();
		} else {
			seatType = berthType.name();
		}

		// getting src - dest in Index Form
//		System.out.println(((Document)((Document)((Document)((Document)trainSeats.get(deptDate)).get(coachType.getLabel())).get(Coach)).get(seatType)).getList(seatNumber , new String().getClass()));
		List<? extends String> availRoutesIndex = ((Document) ((Document) ((Document) ((Document) trainSeats
				.get(deptDate)).get(coachType.getLabel())).get(Coach)).get(seatType)).getList(seatNumber,
						new String().getClass());

		List<String> availRoutes = new ArrayList();

		// Converting Index Form to SrcId - destId
		for (String routeIndex : availRoutesIndex) {
			Integer srcInd = Integer.parseInt(routeIndex.split("-")[0]);
			Integer destInd = Integer.parseInt(routeIndex.split("-")[1]);
			String srcId = route.get(srcInd);
			String destId = route.get(destInd);
			availRoutes.add(srcId + "-" + destId);
		}
		return availRoutes;
	}

// Getting Total Rac Seat alloted Count
	public Integer getTotalRacCount(String trainId, String deptDate, CoachType coachType) {
		Integer total = 0;

		Document seats = (Document) seatsdb.getCollection(trainId).find(Filters.exists(deptDate)).first().get(deptDate);

		Document doc = (Document) seats.get(coachType.getLabel());

		for (Entry<String, Object> compartment : doc.entrySet()) {
			Document seatTypesDoc = (Document) compartment.getValue();

			for (Entry<String, Object> seatType : seatTypesDoc.entrySet()) {

				if ("RAC".equals(seatType.getKey())) {
					total += ((Map<String, List<String>>) seatType.getValue()).keySet().size();
				}
			}
		}

		return total;
	}

	// Getting Total Waiting List alloted Count
	public Integer getTotalWlCount(String trainId, String deptDate, CoachType coachType) {
		Integer total = 0;

		Document seats = (Document) seatsdb.getCollection(trainId).find(Filters.exists(deptDate)).first().get(deptDate);

		Document doc = (Document) seats.get(coachType.getLabel());

		Map<String, Map<String, List<String>>> AvailComp = new HashMap<>();

		for (Entry<String, Object> compartment : doc.entrySet()) {
			Document seatTypesDoc = (Document) compartment.getValue();

			for (Entry<String, Object> seatType : seatTypesDoc.entrySet()) {

				if ("WL".equals(seatType.getKey())) {
					total += ((Map<String, List<String>>) seatType.getValue()).keySet().size();
				}

			}
		}

		return total;
	}

// Booking Seats for given source and destination
	public List<String> LockSeat(String trainId, String date, String source, String destination,
			AllotedSeat allotedSeat) {

		List<String> newPathsIndex = new ArrayList();
		List<String> newPaths = new ArrayList();

		List<String> route = trainSearch.getRoute(trainId);
		String coach = allotedSeat.getCoachType().getLabel();
		String compartment = allotedSeat.getCompartmentNumber();
		BerthType berthType = allotedSeat.getBerthType();
		String seatType = "";

		if (berthType == null) {
			if (allotedSeat.getRAC() != null) {
				seatType = "RAC";
			} else if (allotedSeat.getWl() != null) {
				seatType = "WL";
			}
		} else {
			seatType = berthType.name();
		}

		String seatNumber = allotedSeat.getSeatNumber();
		Integer src = route.indexOf(source);
		Integer dest = route.indexOf(destination);

		MongoCollection<Document> seatCollection = seatsdb.getCollection(trainId);

		Document seatdoc = seatCollection.find(Filters.exists(date)).first();

		List<String> AvailPath = (List<String>) ((Document) ((Document) ((Document) ((Document) seatdoc.get(date))
				.get(coach)).get(compartment)).get(seatType)).get(seatNumber);
        
		String fieldPath = date + "." + coach + "." + compartment + "." + seatType + "." + seatNumber;
        
		for (String path : AvailPath) {
			Integer start = Integer.parseInt(path.split("-")[0]);
			Integer end = Integer.parseInt(path.split("-")[1]);

			if (src > start && dest < end) {
				seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, path));

				String newPath1 = start + "-" + src;
				String newPath2 = dest + "-" + end;
				
				newPathsIndex = Arrays.asList(newPath1 , newPath2);
				
				seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath1));
				seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath2));
				break;
			}

			else if (src > start && dest.equals(end)) {
				seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, path));

				String newPath1 = start + "-" + src;
				
				newPathsIndex = Arrays.asList(newPath1);
				
				seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath1));
				break;
			}

			else if (src.equals(start) && dest < end) {
				seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, path));

				String newPath1 = dest + "-" + end;
				
				newPathsIndex = Arrays.asList(newPath1);
				seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath1));

				break;
			}

			else if (src.equals(start) && dest.equals(end)) {
				seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, path));
				break;
			}
		}
     
		if (newPathsIndex != null) {
			for (String newPathInd : newPathsIndex) {
				src = Integer.parseInt(newPathInd.split("-")[0]);
				dest = Integer.parseInt(newPathInd.split("-")[1]);
				String newPath = route.get(src) + "-" + route.get(dest);
				newPaths.add(newPath);
			}
		}

		return newPaths;
	}

// Cancelling seat for given src and dest 

	public String UnLockSeat(String trainId, String date, String source, String destination, AllotedSeat allotedSeat) {
		List<String> route = trainSearch.getRoute(trainId);
		String coach = allotedSeat.getCoachType().getLabel();
		String compartment = allotedSeat.getCompartmentNumber();
		BerthType berthType = allotedSeat.getBerthType();
        String seatType = "";
        
		if (berthType == null) {
			if (allotedSeat.getRAC() != null) {
				seatType = "RAC";
			} else if (allotedSeat.getWl() != null) {
				seatType = "WL";
			}
		}
		else {
			seatType = berthType.name();
		}
		
		String seatNumber = allotedSeat.getSeatNumber();
		Integer src = route.indexOf(source);
		Integer dest = route.indexOf(destination);

		MongoCollection<Document> seatCollection = seatsdb.getCollection(trainId);

		Document seatdoc = seatCollection.find(Filters.exists(date)).first();

		List<String> AvailPath = (List<String>) ((Document) ((Document) ((Document) ((Document) seatdoc.get(date))
				.get(coach)).get(compartment)).get(seatType)).get(seatNumber);

		String fieldPath = date + "." + coach + "." + compartment + "." + seatType + "." + seatNumber;

		List<String> starts = new ArrayList();
		List<String> ends = new ArrayList();

		for (String path : AvailPath) {
			starts.add(path.split("-")[0]);
			ends.add(path.split("-")[1]);
		}

		String s = src.toString();
		String d = dest.toString();

		String newPath = null;

		if (starts.contains(d) && ends.contains(s)) {
			String newSrc = starts.get(ends.indexOf(s));
			String newDest = ends.get(starts.indexOf(d));

			String oldPath1 = d + "-" + newDest;
			String oldPath2 = newSrc + "-" + s;

			seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, oldPath1));
			seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, oldPath2));

			newPath = newSrc + "-" + newDest;

			seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath));
		}

		else if (starts.contains(d) && (!ends.contains(s))) {
			String newDest = ends.get(starts.indexOf(d));

			String oldPath = d + "-" + newDest;

			seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, oldPath));

			newPath = s + "-" + newDest;

			seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath));
		}

		else if ((!starts.contains(d)) && ends.contains(s)) {
			String newSrc = starts.get(ends.indexOf(s));

			String oldPath = newSrc + "-" + s;

			seatCollection.updateOne(Filters.exists(date), Updates.pull(fieldPath, oldPath));

			newPath = newSrc + "-" + d;

			seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath));
		}

		else if ((!starts.contains(d)) && (!ends.contains(s))) {

			newPath = s + "-" + d;

			seatCollection.updateOne(Filters.exists(date), Updates.push(fieldPath, newPath));
		}

		if (newPath != null) {
			src = Integer.parseInt(newPath.split("-")[0]);
			dest = Integer.parseInt(newPath.split("-")[1]);
			newPath = route.get(src) + "-" + route.get(dest);
		}

		return newPath;
	}

}