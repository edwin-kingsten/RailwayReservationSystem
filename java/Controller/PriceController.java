package Controller;

import java.util.Map;

import Model.BookingTicket;
import Model.CoachType;
import Model.PriceDetail;

public interface PriceController {
	
	public Map<CoachType, Integer> getAllCoachPrice(String trainId , String srcId , String destId);
	public Double getPrice(String trainId , String srcId , String destId , CoachType coachType);
	public PriceDetail getTicketPrice(BookingTicket ticket);
}
