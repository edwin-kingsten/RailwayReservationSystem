package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mongodb.MongoClient;

import Exceptions.InvalidCredential;
import Exceptions.InvalidStation;
import Exceptions.InvalidUserName;
import Exceptions.NoSeatException;
import Exceptions.UserNameAlreadyExists;
import Interactor.DatabaseHandler;
import Interactor.SeatInteractor;
import Interactor.SeatInteractorImpl;
import Interactor.TicketInteractorImpl;
import Interactor.UserInteractor;
import Interactor.UserInteractorImpl;
import Manager.BookingManagerImpl;
import Manager.CancellationManagerImpl;
import Manager.SeatAllocManagerImpl;
import Interactor.SearchInteractor;
import Interactor.SearchInteractorImpl;
import Model.AllotedSeat;
import Model.BerthType;
import Model.BookedPassenger;
import Model.BookedTicket;
import Model.BookingPassenger;
import Model.BookingTicket;
import Model.CoachType;
import Model.Credential;
import Model.Gender;
import Model.PNRDetail;
import Model.Passenger;
import Model.Payment;
import Model.PriceDetail;
import Model.Quota;
import Model.Seat;
import Model.SeatArrangement;
import Model.TickectSatus;
import Model.Ticket;
import Model.TicketStatus;
import Model.Train;
import Model.User;

public class TrainServices extends SeatControllerImpl {
	SearchInteractor trainSearch = new SearchInteractorImpl();
	
	public static SeatArrangement createSeatArrangement() {
		
		Map<String  , Map<String , List<String>>> Ac1 = new HashMap<>();
		
		Map<String , List<String>> A1 = new HashMap();
		A1.put("Lower" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		A1.put("Upper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		A1.put("WL" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		
		Ac1.put("A1",A1);
		Ac1.put("A2", A1);
		Ac1.put("A3", A1);
		
		
		Map<String , Map<String , List<String>>> Ac2 = new HashMap<>();
		Map<String , List<String>> B1 = new HashMap();
		B1.put("Upper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		B1.put("Lower" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		B1.put("SideUpper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		B1.put("SideLower" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		B1.put("RAC" , Arrays.asList("6L" , "6R" , "7L" , "7R" , "8L" , "8R" , "9L" ,"9R" , "10L" , "10R"));
		B1.put("WL" , Arrays.asList("1" , "2" , "3" ,"4" , "5"));
		
		Ac2.put("B1", B1);
		Ac2.put("B2" , B1);
		Ac2.put("B3" , B1);
		
		Map<String , Map<String , List<String>>> Ac3 = new HashMap<>();
		Map<String , List<String>> C1 = new HashMap();
		C1.put("Upper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		C1.put("Middle" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		C1.put("Lower" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		C1.put("SideUpper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		C1.put("SideLower" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		C1.put("RAC" , Arrays.asList("6L" , "6R" , "7L" , "7R" , "8L" , "8R" , "9L" ,"9R" , "10L" , "10R"));
		C1.put("WL" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		
		Ac3.put("C1", C1);
		Ac3.put("C2" , C1);
		Ac3.put("C3" , C1);
		
		
		Map<String , Map<String , List<String>>> Sleeper = new HashMap<>();
		Map<String , List<String>> S1 = new HashMap();
		S1.put("Upper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		S1.put("Middle" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		S1.put("Lower" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		S1.put("SideUpper" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10"));
		S1.put("SideLower" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		S1.put("RAC" , Arrays.asList("6L" , "6R" , "7L" , "7R" , "8L" , "8R" , "9L" ,"9R" , "10L" , "10R"));
		S1.put("WL" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		
		Sleeper.put("S1", S1);
		Sleeper.put("S2" , S1);
		Sleeper.put("S3" , S1);
		
		Map<String  , Map<String , List<String>>> SS = new HashMap<>();
		
		Map<String , List<String>> D1 = new HashMap();
		D1.put("Lower" , Arrays.asList("1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10" , "11" , "12" ,"13" , "14" , "15" ,"16" , "17" , "18" , "19" , "20"));
		D1.put("WL" , Arrays.asList("1" , "2" , "3" , "4" , "5"));
		
		SS.put("D1",D1);
		SS.put("D2", D1);
		SS.put("D3", D1);
		return new SeatArrangement(Ac1, Ac2, Ac3, Sleeper , SS);
	}
	
	
	public static void main(String[] args) throws UserNameAlreadyExists, InvalidUserName, InvalidStation, InvalidCredential, NoSeatException {
//		TrainSearch trainSearch = new TrainSearch();
//		System.out.println(trainSearch.findTrains("SCT", "MS").size());
//		for(Train train : trainSearch.findTrains("MS", "SCT")) {
//			System.out.println("---------------------------------------------------------------");
//			System.out.println(train);
//		}	
//		
//		for(String trainId : DatabaseHandler.getInstance().getTrainsDb().listCollectionNames().into(new ArrayList<String>())) {
//			new SeatInteractorImpl().CreateSeat("09-05-2022", trainId, createSeatArrangement());
//			new SeatInteractorImpl().CreateSeat("10-05-2022", trainId, createSeatArrangement());
//			new SeatInteractorImpl().CreateSeat("11-05-2022", trainId, createSeatArrangement());
//			new SeatInteractorImpl().CreateSeat("12-05-2022", trainId, createSeatArrangement());
//			new SeatInteractorImpl().CreateSeat("13-05-2022", trainId, createSeatArrangement());
//			new SeatInteractorImpl().CreateSeat("14-05-2022", trainId, createSeatArrangement());
//		}
	    
//		System.out.println(new SeatInteractorImpl().getAvailableSeats("12662", "02-05-2022", "SCT", "MS"));
//		System.out.println(new SeatInteractorImpl().getTotalWlCount("12662", "07-05-2022", CoachType.SecondSitting));
//		System.out.println(new SearchInteractorImpl().findTrains("SCT","MS").get(0).getDays());
		
//		System.out.println(new SeatDao().getAvailableSeats("12662", "2-05-2022", "SCT", "TBM"));
		
//		new SeatDao().CancelSeat("12662", "2-05-2022", "AC1" , "A1" , "Lower" , "2" , 3 , 7);	
		
//		System.out.println(new SearchController().SearchByDate("MAS", "SBC", "02-05-2022").get(0).getDisplayString());
		
//		new SeatInteractorImpl().LockSeat("12662", "02-05-2022", "AC1", "A1", "Lower", "1", 0, 15);
		
//		System.out.println(new SeatInteractorImpl().getTotalWlCount("12662", "02-05-2022", CoachType.FirstClassAc));
		
//		System.out.println(new SeatControllerImpl().AllocateSeat(new Passenger("x"  , 12 , "y" , "SCT" , "MS" , "12662" , "02-05-2022" , CoachType.FirstClassAc , BerthType.Lower)));-
		
//		new UserInteractorImpl().addTicketId("King", "1234567890");
		
//		System.out.println(new UserInteractorImpl().addTicketId("King", "1234567890"));
		
//		new PnrInteractorImpl().addPnr(new Ticket(null , null , null , null , null , null , new PNRDetail(12345L,new  AllotedSeat(CoachType.FirstClassAc ,  "A1" , BerthType.Lower , "12" , "10" , null), "SCT", "MS", "12662", "03-05-2022", TickectSatus.RAC)));
		
//		System.out.println(new SearchInteractorImpl().getBasicPrice("12662").get("AC1") + 455);t
		
//		System.out.println(CoachType.getEnumByLabel("AC1").getLabel());
		
//		System.out.println(new PriceControllerImpl().getAllCoachPrice("12662", "RJPM", "MS"));
//		System.out.println(new SeatInteractorImpl().getAvailableSeats("12662", "", "SCT", "MS"));
		
//		AllotedSeat allotedSeat1 = new AllotedSeat(CoachType.SecondSitting , "D1" , BerthType.Lower , "1", null, null);
//		AllotedSeat allotedSeat2 = new AllotedSeat(CoachType.SecondSitting , "D1" , BerthType.Lower , "2", null, null);
//		AllotedSeat allotedSeat3 = new AllotedSeat(CoachType.SecondSitting , "D1" , BerthType.Lower , null , "10", null);
//		AllotedSeat allotedSeat4 = new AllotedSeat(CoachType.SecondSitting , "D1" , BerthType.Lower , null , null, "12");
//		
//		BookedPassenger passenger1 = new BookedPassenger("123456789", allotedSeat1, "King", "30", Gender.Male, TicketStatus.Confirmed);
//		BookedPassenger passenger2 = new BookedPassenger("123456780", allotedSeat2, "King1", "30", Gender.Female, TicketStatus.Confirmed);
//		BookedPassenger passenger3 = new BookedPassenger("123456781", allotedSeat3, "King2", "30", Gender.Male, TicketStatus.RAC);
//		BookedPassenger passenger4 = new BookedPassenger("123456783", allotedSeat4, "King3", "30", Gender.TransGender, TicketStatus.WL);
//		
//		PriceDetail price = new PriceDetail(200D, 17.3D);
//		Payment payment = new Payment("126738201", null, null);
//	
//		BookedTicket ticket = new BookedTicket("143516287", "12662", "SCT", "MAS", "KDNL", "10-05-2022", "06-05-2022", Quota.General, CoachType.SecondSitting, payment, price, Arrays.asList(passenger1 , passenger2 , passenger3 , passenger4));
//		new TicketInteractorImpl().addTicket(ticket);
		
//		System.out.println(new TicketInteractorImpl().getTicket("143516287"));
//		System.out.println(new SeatAllocManagerImpl().AllocateSeat("16102", "SCT", "MS", "09-05-2022", CoachType.Sleeper, BerthType.Upper));
//		
//		BookingPassenger passenger1 = new BookingPassenger("King", 30, Gender.Male , BerthType.SideUpper);
//		BookingPassenger passenger2 = new BookingPassenger("King1", 30, Gender.Female, BerthType.SideLower);
//		BookingPassenger passenger3 = new BookingPassenger("King2", 30 , Gender.Male, BerthType.Lower);
//		BookingPassenger passenger4 = new BookingPassenger("King3", 30, Gender.TransGender, BerthType.Middle);
//	
//		BookingTicket ticket = new BookingTicket("16102", "SCT", "MDU", "09-05-2022", "KDNL", Quota.General, CoachType.Sleeper , Arrays.asList(passenger1 , passenger2, passenger3 ,passenger4, passenger3));
//		new BookingManagerImpl().makeBooking(ticket,new Payment("1223" , null, "06-05-2022" , null), new PriceDetail(12556D, 17.5D) , new Credential("Kingsten" , "King"));
//	
//		System.out.println(new SeatAllocManagerImpl().AllocateSeat("16102", "SCT", "MDU", "09-05-2022", CoachType.Sleeper, BerthType.Middle));
//		System.out.println(new PaymentControllerImpl().makePayment(null, 1000D).getPaidOn());
		
//		System.out.println(new TicketInteractorImpl().getAllocationBetweenSrcAndDest(TicketStatus.Confirmed,"16102", "09-05-2022", "SCT", "MS", CoachType.SecondSitting));
//		new TicketInteractorImpl().decreaseWaitingNumber(TicketStatus.RAC, "16102", "09-05-2022","SCT", "MDU", CoachType.Sleeper, 0);
//		System.out.println(new SeatInteractorImpl().getAvailableRoute("16102", "09-05-2022", CoachType.Sleeper, "S1", TicketStatus.RAC, null , "10R"));
//		Seat seat1 = new Seat("92038", null , null , null , 2);
//		Seat seat2 = new Seat("92039", null , null , null , 1);
//		List<Seat> seats = Arrays.asList(seat1 , seat2);
//		Collections.sort(seats);
//		System.out.println(seats);
//		System.out.println(new UserInteractorImpl().getTicketIds("King"));
//		new BookingControllerImpl().getBookings(new Credential("King", "King"));
//		System.out.println(new TicketInteractorImpl().getAllocationBetweenSrcAndDest(TicketStatus.Cancelled, "16102", "09-05-2022", "SCT", "MS", CoachType.SecondSitting));
//		AllotedSeat allotedSeat = new AllotedSeat(CoachType.Sleeper, "S1", BerthType.Lower, "1", null, null);
//		new CancellationManagerImpl().releaseSeat("16102", "09-05-2022", "SCT", "MS", CoachType.Sleeper, TicketStatus.Confirmed, allotedSeat);
//		new CancellationManagerImpl().cancelPassengers("8031582586", Arrays.asList("4353782527" , "7138436552" ,"6452117161" , "8008803758" , "6362142726"));
//		System.out.println(new TicketInteractorImpl().getTicket("1382201813"));
//		BookedTicket ticket = new TicketInteractorImpl().getTicket("1382201813");
//		List<BookedPassenger> passengers = new ArrayList(ticket.getBookedPassengers());
// 		int i = 0;
//		for(BookedPassenger passenger : passengers) {
//			if(passenger.getTicketStatus() != TicketStatus.Cancelled) {
//				ticket.getBookedPassengers().remove(i);
//			}
//			i++;
//		}
//		System.out.println(ticket);
	}		
	
	
	public List<Train> findTrains(String src , String dest) {
		return trainSearch.findTrains(src, dest);
	}
}
