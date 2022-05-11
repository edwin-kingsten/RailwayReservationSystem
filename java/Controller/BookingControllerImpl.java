package Controller;

import java.util.ArrayList;
import java.util.List;

import Exceptions.InvalidCredential;
import Exceptions.InvalidStation;
import Exceptions.InvalidUserName;
import Exceptions.NoSeatException;
import Interactor.SearchInteractor;
import Interactor.SearchInteractorImpl;
import Interactor.TicketInteractor;
import Interactor.TicketInteractorImpl;
import Interactor.UserInteractor;
import Interactor.UserInteractorImpl;
import Manager.BookingManager;
import Manager.BookingManagerImpl;
import Model.BookedTicket;
import Model.BookingTicket;
import Model.Credential;
import Model.Payment;
import Model.PriceDetail;
import Model.TicketDisplayer;
import Model.Train;
import Model.TrainDisplayer;

public class BookingControllerImpl  implements BookingController {
	
	BookingManager bookingManager = null;
	SearchInteractor searchInteractor = null;
	SearchControllerImpl searchController = null;
	
	public BookingControllerImpl() {
		bookingManager = new BookingManagerImpl();
		searchInteractor = new SearchInteractorImpl();
		searchController = new SearchControllerImpl();
	}

	@Override
	public void makeBooking(BookingTicket bookingTicket, Payment payment, PriceDetail price, Credential credential)
			throws InvalidCredential, NoSeatException, InvalidUserName {
		bookingManager.makeBooking(bookingTicket, payment, price, credential);
	}
	
	public List<TicketDisplayer> getBookings(Credential credential) throws InvalidCredential, InvalidUserName{
		List<BookedTicket> tickets = bookingManager.getTickets(credential);
	    List<TicketDisplayer> ticketDisplayers = new ArrayList();
		
		for(BookedTicket ticket : tickets) {
			Train train = searchInteractor.getTrain(ticket.getTrainId());
			TrainDisplayer trainDisplayer = null;
			try {
				trainDisplayer = searchController.trainToDisplayer(ticket.getSrcId(), ticket.getDestId(), train);
			} catch (InvalidStation e) {
				e.printStackTrace();
			}
			ticketDisplayers.add(new TicketDisplayer(trainDisplayer, ticket));
		}
		return ticketDisplayers;
	}
}
