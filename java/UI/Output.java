package UI;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Model.BookedPassenger;
import Model.BookedTicket;
import Model.CoachType;
import Model.PriceDetail;
import Model.SeatVacancy;
import Model.StoppingInfo;
import Model.TicketDisplayer;
import Model.TicketStatus;
import Model.Train;
import Model.TrainDisplayer;
import Model.Vacancy;
import Model.VacancyType;

public class Output {
	public static void showTrain(TrainDisplayer train) {
		System.out.format("                        \t%s (%s)\t                  \n", train.getName() , train.getId());
		System.out.format("|---\t%s\t-----------\t%s\t----------------\t%s\t---|\n", train.getStartTime(),
				train.getTravelTime(), train.getEndTime());
		System.out.format("|---\t%s ( %s )---\t%s\t       --------------- %s ( %s ) ---|\n", train.getSrcName(), train.getSrc(),
				getAvailableDays(train.getAvailableDays()), train.getDestName(), train.getDest());
		System.out.format("|----------------------------------------------------------------------------------|\n");
	}

	private static String getAvailableDays(List<String> availableDays) {
		String availDays = "";
		List<String> days = Arrays.asList("M", "T", "W", "T", "F", "S", "S");
		for (int i = 0; i < days.size(); i++) {
			if (availableDays.get(i).equals("1")) {
				availDays += days.get(i);
			} else {
				availDays += "*";
			}
		}
		return availDays;
	}

	public static void printAvailability(SeatVacancy vacancy, Map<CoachType, Integer> price) {
		Vacancy ac1Vacancy = vacancy.getAC1Vacancy();
		Vacancy ac2Vacancy = vacancy.getAC2Vacancy();
		Vacancy ac3Vacancy = vacancy.getAC3Vacancy();
		Vacancy sleeperVacancy = vacancy.getSleeperVacancy();
		Vacancy sSVacancy = vacancy.getSSVacancy();

		System.out.format("| %s ( Rs %s )| %s ( Rs %s ) | %s ( Rs %s ) | %s ( Rs %s ) | %s ( Rs %s ) |\n",
				"AC One Tier", price.get(CoachType.FirstClassAc), "AC Two Tier", price.get(CoachType.TwoTierAc),
				"AC Three Tier", price.get(CoachType.ThreeTierAc), "Sleeper", price.get(CoachType.Sleeper),
				"Second Sitting", price.get(CoachType.SecondSitting));
            
		System.out.format(
				"|---------%s %s---------|---------%s %s --------|--------- %s %s --------|------ %s %s -----|---------- %s %s----------|\n",
				ac1Vacancy.getVacancyType().getLabel(), ac1Vacancy.getAvailability(),
				ac2Vacancy.getVacancyType().getLabel(), ac2Vacancy.getAvailability(),
				ac3Vacancy.getVacancyType().getLabel(), ac3Vacancy.getAvailability(),
				sleeperVacancy.getVacancyType().getLabel(), sleeperVacancy.getAvailability(),
				sSVacancy.getVacancyType().getLabel(), sSVacancy.getAvailability());
		System.out.println(
				"|------------------------------------------------------------------------------------------------------------------------------|\n");
	}
     
	public static void listTickets(List<TicketDisplayer> ticketDisplayers) {
		Integer ticketCount = 0;
		for (TicketDisplayer ticketDisplayer : ticketDisplayers) {
			System.out.println();
			System.out.println("Ticket - " + ++ticketCount);
			printPnrAndDate(ticketDisplayer.getTicket().getPnr(), ticketDisplayer.getTicket().getDeptDate());
			showTrain(ticketDisplayer.getTrainDisplayer());
			printBookedOnStatus(ticketDisplayer.getTicket().getBookedOn());
		}
		System.out.println();
	}
    
	public static void printTicket(TicketDisplayer ticketDisplayer , boolean printPrice) {
		System.out.println("|---------------------------------------------------------------------------------|");
		printPnrAndDate(ticketDisplayer.getTicket().getPnr(), ticketDisplayer.getTicket().getDeptDate());
		showTrain(ticketDisplayer.getTrainDisplayer());
		printTicketSpecificDetail(ticketDisplayer.getTicket());
		System.out.println("----------------------------Passenger Information----------------------------------");
		for(BookedPassenger passenger : ticketDisplayer.getTicket().getBookedPassengers()) {
			printPassenger(passenger, ticketDisplayer.getTicket().getCoachType());
		}
		
		if(printPrice) {
		printPriceDetail(ticketDisplayer.getTicket().getPriceDetail());}
	}
	
	
	public static void printTicketSpecificDetail(BookedTicket ticket) {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.format("Total Passengers - %s |CoachType - %s  | Quota - %s | Boarding Point - %s |\n",
				ticket.getBookedPassengers().size(), ticket.getCoachType().name(), ticket.getQuota(),
				ticket.getBoardingStationId());
		System.out.println("---------------------------------------------------------------------------------");
	}

	public static void printPassenger(BookedPassenger passenger, CoachType coachType) {
		System.out.format("Name   - %s\n", passenger.getName());
		System.out.format("Age    - %s yrs\n", passenger.getAge());
		System.out.format("Gender - %s\n", passenger.getGender().name());
		System.out.format("Ticket Status - %s\n", passenger.getTicketStatus().name());
		if (passenger.getTicketStatus() == TicketStatus.Confirmed) {
			if (coachType == CoachType.SecondSitting) {
				System.out.format("|Coach - %s |  Seat Number - %s |\n",
						passenger.getAllotedSeat().getCompartmentNumber(), passenger.getAllotedSeat().getSeatNumber());
			} else {
				System.out.format("|Coach - %s | Berth - %s | BerthType - %s |\n",
						passenger.getAllotedSeat().getCompartmentNumber(), passenger.getAllotedSeat().getSeatNumber(),
						passenger.getAllotedSeat().getBerthType());

			}
		} else {
			if (passenger.getTicketStatus() == TicketStatus.RAC) {
				System.out.format(" %s\n", passenger.getAllotedSeat().getRAC());
			} else if (passenger.getTicketStatus() == TicketStatus.WL) {
				System.out.format(" %s\n", passenger.getAllotedSeat().getWl());
			}
		}

		System.out.println("------------------------------------------------------------------------------");
	}

	public static void printPriceDetail(PriceDetail price) {
		System.out.println("-----------------------------Payment Detail-----------------------------------");
		System.out.format("Base Fare                   Rs %s\n", price.getBaseFare());
		System.out.format("Convenience Fee             Rs %s\n", price.getConvienceFee());
		System.out.println("------------------------------------");
		System.out.format("Total Fare                  Rs %s\n", price.getTotal());
	}

	public static void printPnrAndDate(String pnr, String deptDate) {
//		System.out.format("|--------------------------------------------------------------------------|\n");
		System.out.format("|--Pnr - %s ---------------------------------Departure Date - %s --|\n", pnr, deptDate);
//		System.out.format("|--------------------------------------------------------------------------|\n");
	}

	public static void printBookedOnStatus(String bookedOn) {
//		System.out.format("|--------------------------------------------------------------------------|\n");
		System.out.format("|---BookedOn - %s ---------------------------------------Status - %s---|\n", bookedOn, "Booked");
//		System.out.format("|--------------------------------------------------------------------------|\n");
	}

	public static void printTrainRoute(List<StoppingInfo> stops) {
		for (StoppingInfo stop : stops) {
			System.out.println(stop.toString());
		}
	}

	public static void showNoAvailTrains() {
		System.out.println("No Trains Available for this source and destination");
	}

	public static void printInvalidSrcdest() {
		System.out.println("Please Enter Valid Source and destination");
	}

	public static void printInvalidDate() {
		System.out.println("Please Enter Valid Date");
	}

	public static void printTransactionFailed() {
		System.out.println("Oops , Your Transaction Has been Failed");
	}

	public static void printTransactionSuccess() {
		System.out.println("Transaction is Successfully Completed");
	}

	public static void printBookingSucess() {
		System.out.println("Booking is Success");
	}

	public static void printNoSeatsAvailable() {
		System.out.println("No Seats are Available for this Coach");
	}
	
	public static void printNoAvailableBooking() {
		System.out.println("No Bookings are Available");
	}
	
	public static void printNoPassengerSelected() {
		System.out.println("No Passengers are Selected");
	}
	
	public static void printCancellationSuccess() {
		System.out.println("Cancellation is Sucess");
	}
}
