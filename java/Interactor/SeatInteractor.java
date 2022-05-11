package Interactor;

import java.util.List;

import Model.AllotedSeat;
import Model.BerthType;
import Model.CoachType;
import Model.SeatArrangement;
import Model.TicketStatus;

public interface SeatInteractor {
	public SeatArrangement getAvailableSeats(String trainId, String deptDate, String src, String dest);
	public Integer getTotalWlCount(String trainId, String deptDate, CoachType coachType);
	public Integer getTotalRacCount(String trainId, String deptDate, CoachType coachType);
	public List<String> getAvailableRoute(String trainId, String deptDate, CoachType coachType, String Coach,
			TicketStatus ticketStatus, BerthType berthType, String seatNumber);
	public List<String> LockSeat(String trainId , String date , String source , String destination , AllotedSeat allotedSeat);
	public String UnLockSeat(String trainId , String date , String source , String destination , AllotedSeat allotedSeat);
}
