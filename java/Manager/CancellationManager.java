package Manager;

import java.util.List;

import Model.AllotedSeat;
import Model.CoachType;
import Model.TicketStatus;

public interface CancellationManager {

	public void cancelPassengers(String pnr, List<String> passengerIds);

	public void releaseSeat(String trainId, String deptDate, String srcId, String destId, CoachType coachType,
			TicketStatus ticketStatus, AllotedSeat allotedSeat);

}