package Interactor;

import java.util.List;

import Exceptions.InvalidStation;
import Model.BerthType;
import Model.BookedTicket;
import Model.CoachType;
import Model.Seat;
import Model.TicketStatus;

public interface TicketInteractor {
	public void addTicket(BookedTicket ticket);
	public BookedTicket getTicket(String pnr);
	public Seat getAllocationBetweenSrcAndDest(TicketStatus ticketStatus, String trainId, String deptDate,
			String srcId, String destId, CoachType coachType);
	public void decreaseWaitingNumber(TicketStatus ticketStatus, String trainId, String deptDate, String srcId,
			String destId, CoachType coachType, Integer refWaitingNum);
	public void changeBoardingPoint(String pnr , String stationId) throws InvalidStation;
	public void changeCoach(String passengerId , String coach);
	public void changeTicketStatus(String passengerId , TicketStatus ticketStatus);
	public void changeBerthType(String passengerId , BerthType berthType);
	public void changeSeatNumber(String passengerId , String seatNumber);
	public void changeRacNumber(String passengerId , Integer racNumber);
	public void changeWlNumber(String passengerId , Integer wlNumber);
}
