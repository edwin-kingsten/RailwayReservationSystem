package UI;

import java.util.List;

import Controller.BookingController;
import Controller.BookingControllerImpl;
import Exceptions.InvalidCredential;
import Exceptions.InvalidStation;
import Exceptions.InvalidUserName;
import Model.Credential;
import Model.TicketDisplayer;

public class MyBookingsUI {
	BookingController bookingController = null;

	public MyBookingsUI() {
		bookingController = new BookingControllerImpl();
	}

	public void viewBooking() {
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
				Output.printTicket(ticketDisplayers.get(ticketIndex) , true);
			}
		} else {
			Output.printNoAvailableBooking();
		}
	}

}
