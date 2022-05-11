package Manager;

import Controller.SeatControllerImpl;
import Exceptions.InvalidCredential;
import Exceptions.InvalidUserName;
import Exceptions.NoSeatException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import Controller.SeatController;
import Interactor.TicketInteractor;
import Interactor.TicketInteractorImpl;
import Interactor.UserInteractor;
import Interactor.UserInteractorImpl;
import Model.AllotedSeat;
import Model.BerthType;
import Model.BookedPassenger;
import Model.BookedTicket;
import Model.BookingPassenger;
import Model.BookingTicket;
import Model.CoachType;
import Model.Credential;
import Model.Payment;
import Model.PriceDetail;
import Model.TicketStatus;

public class BookingManagerImpl implements BookingManager {
	UserInteractor userInteractor = null;
	TicketInteractor ticketInteractor = null;
	SeatAllocManager seatAllocManager = null;

	public BookingManagerImpl() {
		userInteractor = new UserInteractorImpl();
		ticketInteractor = new TicketInteractorImpl();
		seatAllocManager = new SeatAllocManagerImpl();
	}

	public void makeBooking(BookingTicket bookingTicket, Payment payment, PriceDetail price, Credential credential)
			throws InvalidCredential, NoSeatException, InvalidUserName {
		if (!userInteractor.isValidUser(credential)) {
			throw new InvalidCredential();
		}

		else {
			BookedTicket ticket = generateTicket(bookingTicket, payment, price);
			userInteractor.addTicketId(credential.getName(), ticket.getPnr());
			ticketInteractor.addTicket(ticket);
		}
	}

	public BookedTicket generateTicket(BookingTicket bookingTicket, Payment payment, PriceDetail price)
			throws NoSeatException {
		BookedTicket ticket = null;
		List<BookedPassenger> bookedPassengers = new ArrayList();

		try {

			for (BookingPassenger bookingPassenger : bookingTicket.getBookingPassengers()) {

				AllotedSeat allotedSeat = getAllotedSeat(bookingTicket.getTrainId(), bookingTicket.getSrcId(),
						bookingTicket.getDestId(), bookingTicket.getDeptDate(), bookingTicket.getCoachType(),
						bookingPassenger.getBerthType());

				TicketStatus ticketStatus = getTicketStatus(allotedSeat);

				bookedPassengers.add(new BookedPassenger(generateRandomNum(), allotedSeat, bookingPassenger.getName(),
						bookingPassenger.getAge().toString(), bookingPassenger.getGender(), ticketStatus));
			}
		} catch (NoSeatException e) {
			for (BookedPassenger passenger : bookedPassengers) {
				seatAllocManager.unLockSeat(bookingTicket.getTrainId(), bookingTicket.getDeptDate(),
						bookingTicket.getSrcId(), bookingTicket.getDestId(), passenger.getAllotedSeat());
			}
			throw new NoSeatException();
		}

		ticket = new BookedTicket(generateRandomNum(), bookingTicket.getTrainId(), bookingTicket.getSrcId(),
				bookingTicket.getDestId(), bookingTicket.getBoardingStationId(), bookingTicket.getDeptDate(),
				payment.getPaidOn(), bookingTicket.getQuota(), bookingTicket.getCoachType(), payment, price,
				bookedPassengers);

		return ticket;
	}

	private AllotedSeat getAllotedSeat(String trainId, String srcId, String destId, String deptDate,
			CoachType coachType, BerthType preferredBerthType) throws NoSeatException {

		// getting the Seat Allocation
		AllotedSeat allotedSeat = seatAllocManager.AllocateSeat(trainId, srcId, destId, deptDate, coachType,
				preferredBerthType);

		if (allotedSeat == null) {
			throw new NoSeatException();
		}

		// Locking the alloted Seat
		seatAllocManager.lockSeat(trainId, deptDate, srcId, destId, allotedSeat);

		return allotedSeat;
	}

	private String generateRandomNum() {
		Random random = new Random();
		String randomNum = "";

		for (int i = 0; i < 10; i++) {
			randomNum += random.nextInt(9);
		}

		return randomNum;
	}

	private TicketStatus getTicketStatus(AllotedSeat allotedSeat) {
		TicketStatus ticketStatus = null;

		if (allotedSeat.getRAC() == null && allotedSeat.getWl() == null && allotedSeat.getSeatNumber() != null) {
			ticketStatus = TicketStatus.Confirmed;
		}

		else if (allotedSeat.getRAC() != null && allotedSeat.getWl() == null) {
			ticketStatus = TicketStatus.RAC;
		}

		else if (allotedSeat.getWl() != null && allotedSeat.getRAC() == null) {
			ticketStatus = TicketStatus.WL;
		}
		return ticketStatus;
	}

	public List<BookedTicket> getTickets(Credential credential) throws InvalidCredential, InvalidUserName {
		List<BookedTicket> tickets = new ArrayList();

		if (!userInteractor.isValidUser(credential)) {
			throw new InvalidCredential();
		} else {
			List<String> ticketIds = userInteractor.getTicketIds(credential.getName());
			for (String ticketId : ticketIds) {
				tickets.add(ticketInteractor.getTicket(ticketId));
			}
		}
		return tickets;
	}
}