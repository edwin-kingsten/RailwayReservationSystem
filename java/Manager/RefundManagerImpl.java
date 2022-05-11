package Manager;

import Model.CoachType;
import Model.TicketStatus;

public class RefundManagerImpl {
	
	
	public Double getCancelCharge(CoachType coachType , TicketStatus ticketStatus) {
		Double charge = 0D;
		
		if(ticketStatus == TicketStatus.RAC || ticketStatus == TicketStatus.WL) {
			charge = 60D;
		}
		
		else if(ticketStatus == TicketStatus.Confirmed) {
			switch(coachType) {
			case FirstClassAc:
				charge = 240D;
				break;
				
			case TwoTierAc:
				charge = 200D;
				break;
				
			case ThreeTierAc:
				charge = 180D;
				break;
				
			case Sleeper:
				charge = 120D;
				break;
				
			case SecondSitting:
				charge = 60D;
				break;
			}
		}
		
		return charge;
	}
	
}
