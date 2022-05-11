package Manager;

import java.util.List;

import Exceptions.InvalidBoardingStation;
import Exceptions.InvalidStation;
import Interactor.SearchInteractor;
import Interactor.SearchInteractorImpl;
import Interactor.TicketInteractor;
import Interactor.TicketInteractorImpl;
import Model.BookedTicket;

public class TicketChangesManagerImpl implements TicketChangesManager{
	TicketInteractor ticketInteractor = null;
	SearchInteractor searchInteractor = null;
	
	public TicketChangesManagerImpl() {
		ticketInteractor = new TicketInteractorImpl();
		searchInteractor = new SearchInteractorImpl();
	}
   
	
	@Override
	public void ChangeBoardingPoint(BookedTicket ticket , String boardingStationId) throws InvalidStation, InvalidBoardingStation {
		String srcId = ticket.getSrcId();
		String destId = ticket.getDestId();
		String trainId = ticket.getTrainId();
		List<String> route = searchInteractor.getRoute(trainId);
		
		Integer srcInd = route.indexOf(srcId);
		Integer destInd = route.indexOf(destId);
		
		if(!route.contains(boardingStationId)) {
			throw new InvalidStation();
		}
		
		Integer boardingStationInd = route.indexOf(boardingStationId);
		
		if(boardingStationInd < srcInd || boardingStationInd > destInd) {
			throw new InvalidBoardingStation();
		}
		
		ticketInteractor.changeBoardingPoint(ticket.getPnr(), boardingStationId);
	}
}