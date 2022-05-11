package Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Interactor.SearchInteractor;
import Interactor.SearchInteractorImpl;
import Model.BookingTicket;
import Model.CoachType;
import Model.PriceDetail;
import Model.StoppingInfo;

public class PriceManagerImpl implements PriceManager{

	SearchInteractor searchInteractor = null;

	public PriceManagerImpl() {
		searchInteractor = new SearchInteractorImpl();
	}

	@Override
	public Map<CoachType, Integer> getAllCoachPrice(String trainId, String srcId, String destId) {
		Map<CoachType, Integer> price = new HashMap();

		Map<String, Double> basicPrices = searchInteractor.getBasicPrice(trainId);

		for (Entry<String, Double> basicPrice : basicPrices.entrySet()) {
			CoachType coachType = CoachType.getEnumByLabel(basicPrice.getKey());
			Double evalPrice = determinePrice(basicPrice.getValue(), getDistance(trainId, srcId, destId));

			price.put(coachType, evalPrice.intValue());
		}
		return price;
	}

	@Override
	public Double getPrice(String trainId, String srcId, String destId, CoachType coachType) {
		return (double)getAllCoachPrice(trainId, srcId, destId).get(coachType);
	}
    
	@Override
	public PriceDetail getTicketPrice(BookingTicket ticket) {
		PriceDetail priceDetail = null;
		if(ticket != null) {
		String trainId = ticket.getTrainId();
		String srcId = ticket.getSrcId();
		String destId = ticket.getDestId();
		CoachType coachType = ticket.getCoachType();
		Integer totalPassengers = 0;
		if(ticket.getBookingPassengers().size() > 0) {
			totalPassengers = ticket.getBookingPassengers().size();
		}

		Double baseFare = getPrice(trainId, srcId, destId, coachType) * totalPassengers;
		priceDetail = new PriceDetail(baseFare , getConvinienceFee());
		}
		return priceDetail;
	}
	
	
	private Double getDistance(String trainId, String srcId, String destId) {
		List<StoppingInfo> stops = null;

		if (trainId != null && !trainId.equals("")) {
			stops = searchInteractor.getTrain(trainId).getStops();
		}

		Double srcDist = 0D;
		Double destDist = 0D;

		for (StoppingInfo info : stops) {
			if (info.getId().equals(srcId)) {
				srcDist = info.getDistance().doubleValue();
			}

			else if (info.getId().equals(destId)) {
				destDist = info.getDistance().doubleValue();
			}
		}

		return destDist - srcDist;
	}

	// logic for determining price based on Distance
	private Double determinePrice(Double basicPrice, Double dist) {
		Double baseDistance = getBaseDistance();
		
		if(dist <= baseDistance) {
			return basicPrice;
		}

		Double evalPrice = (dist / baseDistance) * basicPrice;
		return (double) evalPrice.intValue();
	}
	
	// Service Charge
	private Double getConvinienceFee() {
		return 17.70D;
	}
	
	private Double getBaseDistance() {
		return 300D;
	}
}
