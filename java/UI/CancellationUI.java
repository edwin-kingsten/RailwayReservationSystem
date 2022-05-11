package UI;

import java.util.ArrayList;
import java.util.List;

import Controller.BookingController;
import Controller.BookingControllerImpl;
import Controller.CancellationController;
import Controller.CancellationControllerImpl;
import Exceptions.InvalidCredential;
import Exceptions.InvalidUserName;
import Manager.CancellationManager;
import Model.BookedPassenger;
import Model.BookedTicket;
import Model.Credential;
import Model.TicketDisplayer;
import Model.TicketStatus;

public class CancellationUI {

	CancellationController cancellationController = null;
	BookingController bookingController = null;

	public CancellationUI() {
		cancellationController = new CancellationControllerImpl();
		bookingController = new BookingControllerImpl();
	}

	public void cancelPassengers() {
		Credential credential = UserUI.LoginUser();
		List<TicketDisplayer> ticketDisplayers = null;

		try {
			ticketDisplayers = bookingController.getBookings(credential);
			
		} catch (InvalidCredential | InvalidUserName e) {
			e.printStackTrace();
		}

		if (ticketDisplayers.size() > 0) {
			Output.listTickets(ticketDisplayers);

			if (Input.isUserWantDetailTicket()) {
				Integer ticketIndex = Input.getTicketIndexOption(ticketDisplayers.size()) - 1;
				
				BookedTicket bookedTicket = ticketDisplayers.get(ticketIndex).getTicket();
				List<BookedPassenger> bookedPassengers = new ArrayList(bookedTicket.getBookedPassengers());
				
				for(BookedPassenger bookedPassenger : bookedPassengers) {
					if(bookedPassenger.getTicketStatus() == TicketStatus.Cancelled) {
						bookedTicket.getBookedPassengers().remove(bookedPassenger);
					}
				}
				
				Output.printTicket(ticketDisplayers.get(ticketIndex) , false);
				
				BookedTicket ticket = ticketDisplayers.get(ticketIndex).getTicket();
				List<BookedPassenger> passengers = ticket.getBookedPassengers();
				List<String> passengerIds = new ArrayList();
				
				while(Input.isUserWantToCancelPassenger()) {
					Integer passengerIndex = Input.getPassengerIndexOption(passengers.size()) - 1;
					passengerIds.add(passengers.get(passengerIndex).getId());
				}
				
				if(passengerIds.size() > 0) {
					cancellationController.cancelPassengers(ticket.getPnr(), passengerIds);
					Output.printCancellationSuccess();
				}
				else {
					Output.printNoPassengerSelected();
				}
			}
		} else {
			Output.printNoAvailableBooking();
		}
	}
}
