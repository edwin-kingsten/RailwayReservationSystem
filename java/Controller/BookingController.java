package Controller;

import java.util.List;

import Exceptions.InvalidCredential;
import Exceptions.InvalidStation;
import Exceptions.InvalidUserName;
import Exceptions.NoSeatException;
import Model.BookingTicket;
import Model.Credential;
import Model.Payment;
import Model.PriceDetail;
import Model.TicketDisplayer;

public interface BookingController {
	public void makeBooking(BookingTicket bookingTicket ,Payment payment , PriceDetail price , Credential credential) throws InvalidCredential, NoSeatException, InvalidUserName;
	public List<TicketDisplayer> getBookings(Credential credential) throws InvalidCredential, InvalidUserName;
}
