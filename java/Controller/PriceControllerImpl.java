package Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Interactor.SearchInteractor;
import Interactor.SearchInteractorImpl;
import Manager.PriceManager;
import Manager.PriceManagerImpl;
import Model.BookingTicket;
import Model.CoachType;
import Model.PriceDetail;
import Model.StoppingInfo;

public class PriceControllerImpl implements PriceController {
	PriceManager priceManager = null;
	
	public PriceControllerImpl() {
		priceManager = new PriceManagerImpl();
	}

	@Override
	public Map<CoachType, Integer> getAllCoachPrice(String trainId, String srcId, String destId) {
		return priceManager.getAllCoachPrice(trainId, srcId, destId);
	}

	@Override
	public Double getPrice(String trainId, String srcId, String destId, CoachType coachType) {
		return priceManager.getPrice(trainId, srcId, destId, coachType);
	}

	@Override
	public PriceDetail getTicketPrice(BookingTicket ticket) {
		return priceManager.getTicketPrice(ticket);
	}
	
}
