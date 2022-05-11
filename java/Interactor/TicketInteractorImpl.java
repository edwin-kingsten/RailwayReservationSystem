package Interactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

import Exceptions.InvalidStation;
import Model.AllotedSeat;
import Model.BerthType;
import Model.BookedPassenger;
import Model.BookedTicket;
import Model.CoachType;
import Model.Gender;
import Model.Payment;
import Model.PriceDetail;
import Model.Quota;
import Model.Seat;
import Model.TicketStatus;

public class TicketInteractorImpl implements TicketInteractor {
	DatabaseHandler db = null;
	MongoCollection<Document> ticketsCollection = null;
	MongoCollection<Document> passengersCollection = null;
	SearchInteractor searchInteractor = null;

	public TicketInteractorImpl() {
		db = DatabaseHandler.getInstance();
		ticketsCollection = db.getTicketdb().getCollection("Tickets");
		passengersCollection = db.getTicketdb().getCollection("Passengers");
		searchInteractor = new SearchInteractorImpl();
	}

	@Override
	public void addTicket(BookedTicket ticket) {
		String pnr = ticket.getPnr();
		String trainId = ticket.getTrainId();

		List<String> route = searchInteractor.getRoute(trainId);

		String srcId = ticket.getSrcId();
		Integer srcIndex = route.indexOf(srcId);

		String destId = ticket.getDestId();
		Integer destIndex = route.indexOf(destId);

		String deptDate = ticket.getDeptDate();
		String boardingStationId = ticket.getBoardingStationId();
		String bookedOn = ticket.getBookedOn();
		CoachType coachType = ticket.getCoachType();
		Quota quota = ticket.getQuota();
		Payment payment = ticket.getPayment();
		String paymentId = payment.getId();
		PriceDetail priceDetail = ticket.getPriceDetail();
		Double baseFare = priceDetail.getBaseFare();
		Double convinienceFee = priceDetail.getConvienceFee();
		Double totalFare = priceDetail.getTotal();

		List<BookedPassenger> passengers = ticket.getBookedPassengers();
		List<String> passengerIds = new ArrayList();

		for (BookedPassenger passenger : passengers) {
			passengerIds.add(passenger.getId());
			addPassenger(passenger);
		}

		Document ticketDoc = new Document();

		ticketDoc.put("PNR", pnr);
		ticketDoc.put("TrainId", trainId);
		ticketDoc.put("SrcId", srcId);
		ticketDoc.put("SrcIndex", srcIndex);
		ticketDoc.put("DestId", destId);
		ticketDoc.put("DestIndex", destIndex);
		ticketDoc.put("DeptDate", deptDate);
		ticketDoc.put("BoardingPoint", boardingStationId);
		ticketDoc.put("BookedOn", bookedOn);
		ticketDoc.put("CoachType", coachType.name());
		ticketDoc.put("Quota", quota.name());
		ticketDoc.put("PaymentId", paymentId);
		ticketDoc.put("BaseFare", baseFare);
		ticketDoc.put("ConvinienceFee", convinienceFee);
		ticketDoc.put("TotalFare", totalFare);
		ticketDoc.put("PassengerIds", passengerIds);

		ticketsCollection.insertOne(ticketDoc);
	}

	public void addPassenger(BookedPassenger passenger) {
		Document passengerDoc = new Document();

		passengerDoc.put("id", passenger.getId());
		passengerDoc.put("Name", passenger.getName());
		passengerDoc.put("Age", passenger.getAge());
		passengerDoc.put("Gender", passenger.getGender().name());
		passengerDoc.put("TicketStatus", passenger.getTicketStatus().name());
		passengerDoc.put("Coach", passenger.getAllotedSeat().getCompartmentNumber());
		BerthType berthType = passenger.getAllotedSeat().getBerthType();
		String type = "";
		if (berthType != null) {
			type = berthType.name();
		} else {
			type = null;
		}
		passengerDoc.put("BerthType", type);
		passengerDoc.put("SeatNumber", passenger.getAllotedSeat().getSeatNumber());

		Integer rac = null;
		Integer wl = null;

		if (passenger.getAllotedSeat().getRAC() != null) {
			rac = Integer.parseInt(passenger.getAllotedSeat().getRAC());
		}

		if (passenger.getAllotedSeat().getWl() != null) {
			wl = Integer.parseInt(passenger.getAllotedSeat().getWl());
		}

		passengerDoc.put("RAC", rac);
		passengerDoc.put("WL", wl);

		passengersCollection.insertOne(passengerDoc);
	}

	public BookedTicket getTicket(String pnr) {
		BookedTicket ticket = null;
		Document ticketDoc = ticketsCollection.find(Filters.eq("PNR", pnr)).first();
		String trainId = ticketDoc.getString("TrainId");
		String srcId = ticketDoc.getString("SrcId");
		String destId = ticketDoc.getString("DestId");
		String deptDate = ticketDoc.getString("DeptDate");
		String boardingStationId = ticketDoc.getString("BoardingPoint");
		String bookedOn = ticketDoc.getString("BookedOn");
		CoachType coachType = CoachType.getEnumByName(ticketDoc.getString("CoachType"));
		Quota quota = Quota.getEnumByName(ticketDoc.getString("Quota"));

		String paymentId = ticketDoc.getString("PaymentId");
		Payment payment = new Payment(paymentId, null, null, null);

		Double baseFare = ticketDoc.getDouble("BaseFare");
		Double convinienceFee = ticketDoc.getDouble("ConvinienceFee");
		PriceDetail price = new PriceDetail(baseFare, convinienceFee);

		List<? extends String> passengerIds = ticketDoc.getList("PassengerIds", trainId.getClass());

		List<BookedPassenger> passengers = new ArrayList<BookedPassenger>();

		for (String passengerId : passengerIds) {
			passengers.add(getPassenger(passengerId , coachType));
		}

		ticket = new BookedTicket(pnr, trainId, srcId, destId, boardingStationId, deptDate, bookedOn, quota, coachType,
				payment, price, passengers);

		return ticket;
	}

	public BookedPassenger getPassenger(String passengerId , CoachType coachType) {
		BookedPassenger passenger = null;
		Document passengerDoc = passengersCollection.find(Filters.eq("id", passengerId)).first();

		String name = passengerDoc.getString("Name");
		String age = passengerDoc.getString("Age");
		Gender gender = Gender.getEnumByName(passengerDoc.getString("Gender"));
		TicketStatus ticketStatus = TicketStatus.getEnumByName(passengerDoc.getString("TicketStatus"));
		String coach = passengerDoc.getString("Coach");
		BerthType berthType = BerthType.getEnumByName(passengerDoc.getString("BerthType"));
		String seatNumber = passengerDoc.getString("SeatNumber");
		Integer rac = passengerDoc.getInteger("RAC");
		Integer wl = passengerDoc.getInteger("WL");

		String Rac = null;
		String Wl = null;

		if (rac != null) {
			Rac = rac.toString();
		}
		if (wl != null) {
			Wl = wl.toString();
		}

		passenger = new BookedPassenger(passengerId, new AllotedSeat(coachType, coach, berthType, seatNumber, Rac, Wl), name,
				age, gender, ticketStatus);

		return passenger;
	}

	public Seat getAllocationBetweenSrcAndDest(TicketStatus ticketStatus, String trainId, String deptDate, String srcId,
			String destId, CoachType coachType) {
		Seat seat = null;
		List<String> route = searchInteractor.getRoute(trainId);
		Integer srcIndex = route.indexOf(srcId);
		Integer destIndex = route.indexOf(destId);

		// filtering the tickets which Matches trainId , deptDate ,CoachType and places
		// between srcId destId
		FindIterable<Document> ticketsDoc = ticketsCollection.find(Filters.and(Filters.eq("TrainId", trainId),
				Filters.eq("DeptDate", deptDate), Filters.eq("CoachType", coachType.name()),
				Filters.gte("SrcIndex", srcIndex), Filters.lte("DestIndex", destIndex)));

		List<String> passengerIds = new ArrayList();
        
		//iterating to find all the passengers between the src and dest
		for (Document ticketDoc : ticketsDoc) {
			for (String passengerId : ticketDoc.getList("PassengerIds", new String().getClass())) {
				passengerIds.add(passengerId);
			}
		}
        
		// finding the first seat between the given source and Dest
		Document passengerDoc = passengersCollection
				.find(Filters.and(Filters.in("id", passengerIds), Filters.eq("TicketStatus", ticketStatus.name())))
				.sort(Sorts.ascending(ticketStatus.name())).first();

		if (passengerDoc != null) {
			String passengerId = passengerDoc.getString("id");

			Document doc = ticketsCollection.find(Filters.all("PassengerIds", passengerDoc.getString("id"))).first();

			String pnr = doc.getString("PNR");
			String passengerSrcId = doc.getString("SrcId");
			String passengerDestId = doc.getString("DestId");
			Integer waitingNumber = passengerDoc.getInteger(ticketStatus.name());
			String coach = passengerDoc.getString("Coach");
			BerthType berthType = BerthType.getEnumByName(passengerDoc.getString("BerthType"));
			String seatNumber = passengerDoc.getString("SeatNumber");

			String rac = null;
			String wl = null;

			if (ticketStatus == TicketStatus.RAC) {
				rac = waitingNumber.toString();
			}

			else if (ticketStatus == TicketStatus.WL) {
				wl = waitingNumber.toString();
			}

			AllotedSeat allotedSeat = new AllotedSeat(coachType, passengerDoc.getString("Coach"), null, seatNumber, rac,
					wl);
			seat = new Seat(passengerId, pnr, passengerSrcId, passengerDestId, waitingNumber, allotedSeat);
		}
		return seat;
	}

	public void decreaseWaitingNumber(TicketStatus ticketStatus, String trainId, String deptDate, String srcId,
			String destId, CoachType coachType, Integer refWaitingNum) {
		if (ticketStatus == TicketStatus.RAC || ticketStatus == TicketStatus.WL) {

			List<String> route = searchInteractor.getRoute(trainId);
			Integer srcIndex = route.indexOf(srcId);
			Integer destIndex = route.indexOf(destId);
			FindIterable<Document> ticketsDoc = ticketsCollection.find(Filters.and(Filters.eq("TrainId", trainId),
					Filters.eq("DeptDate", deptDate), Filters.eq("CoachType", coachType.name()),
					Filters.gte("SrcIndex", srcIndex), Filters.lte("DestIndex", destIndex)));

			for (Document ticketDoc : ticketsDoc) {
				for (String passengerId : ticketDoc.getList("PassengerIds", new String().getClass())) {
					Document passengerDoc = passengersCollection.find(Filters.eq("id", passengerId)).first();
					if (passengerDoc.get("TicketStatus").equals(ticketStatus.name())) {
						passengersCollection.updateMany(Filters.and(Filters.eq("id", passengerId),
								Filters.gt(ticketStatus.name(), refWaitingNum)), Updates.inc(ticketStatus.name(), -1));
					}
				}
			}
		}
	}
	
	public void changeCoach(String passengerId, String coach) {
		passengersCollection.updateOne(Filters.eq("id", passengerId), Updates.set("Coach", coach));
	}
    
	public void changeSrcId(String pnr , String srcId) throws InvalidStation {
		String trainId = ticketsCollection.find(Filters.eq("PNR", pnr)).first().getString("TrainId");
		List<String> route = searchInteractor.getRoute(trainId);
		
		if(!route.contains(srcId)) {
			throw new InvalidStation();
		}
		
		Integer srcInd = route.indexOf(srcId);
		ticketsCollection.updateOne(Filters.eq("PNR" , pnr), Updates.set("SrcId" , srcId));
		ticketsCollection.updateOne(Filters.eq("PNR" , pnr), Updates.set("SrcIndex" , srcInd));
	}
	
	public void changeDestId(String pnr , String destId) throws InvalidStation {
		String trainId = ticketsCollection.find(Filters.eq("PNR", pnr)).first().getString("TrainId");
		List<String> route = searchInteractor.getRoute(trainId);
		
		if(!route.contains(destId)) {
			throw new InvalidStation();
		}
		
		Integer destInd = route.indexOf(destId);
		ticketsCollection.updateOne(Filters.eq("PNR" , pnr), Updates.set("DestId" , destId));
		ticketsCollection.updateOne(Filters.eq("PNR" , pnr), Updates.set("Destndex" , destInd));
	}
	
	
	public void changeBoardingPoint(String pnr , String stationId) throws InvalidStation {
		String trainId = ticketsCollection.find(Filters.eq("PNR", pnr)).first().getString("TrainId");
		List<String> route = searchInteractor.getRoute(trainId);
		if(!route.contains(stationId)) {
			throw new InvalidStation();
		}	
		ticketsCollection.updateOne(Filters.eq("PNR" , pnr), Updates.set("BoardingPoint" , stationId));
	}
	
	
	public void changeTicketStatus(String passengerId, TicketStatus ticketStatus) {
		passengersCollection.updateOne(Filters.eq("id", passengerId), Updates.set("TicketStatus", ticketStatus.name()));
	}

	public void changeBerthType(String passengerId, BerthType berthType) {
		String type = null;
		if(berthType != null) {
			type = berthType.name();
		}
		passengersCollection.updateOne(Filters.eq("id", passengerId), Updates.set("BerthType", type));
	}

	public void changeSeatNumber(String passengerId, String seatNumber) {
		passengersCollection.updateOne(Filters.eq("id", passengerId), Updates.set("SeatNumber", seatNumber));
	}

	public void changeRacNumber(String passengerId, Integer racNumber) {
		passengersCollection.updateOne(Filters.eq("id", passengerId), Updates.set("RAC", racNumber));
	}

	public void changeWlNumber(String passengerId, Integer wlNumber) {
		passengersCollection.updateOne(Filters.eq("id", passengerId), Updates.set("WL", wlNumber));
	}
	
}
