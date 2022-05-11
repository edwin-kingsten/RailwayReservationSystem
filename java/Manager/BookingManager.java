package Manager;

import java.util.List;

import Exceptions.InvalidCredential;
import Exceptions.InvalidUserName;
import Exceptions.NoSeatException;
import Model.BookedTicket;
import Model.BookingTicket;
import Model.Credential;
import Model.Payment;
import Model.PriceDetail;

public interface BookingManager {
	public void makeBooking(BookingTicket bookingTicket , Payment payment , PriceDetail price , Credential credential) throws InvalidCredential, NoSeatException, InvalidUserName;
	public List<BookedTicket> getTickets(Credential credential) throws InvalidCredential, InvalidUserName;
}
