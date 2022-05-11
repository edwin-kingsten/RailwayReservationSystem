package Manager;

import Exceptions.InvalidBoardingStation;
import Exceptions.InvalidStation;
import Model.BookedTicket;

public interface TicketChangesManager {

	public void ChangeBoardingPoint(BookedTicket ticket, String boardingStationId)
			throws InvalidStation, InvalidBoardingStation;
}