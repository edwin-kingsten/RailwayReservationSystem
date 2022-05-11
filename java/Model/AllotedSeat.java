package Model;

public class AllotedSeat {
	private CoachType coachType;
	private String compartmentNumber;
	private BerthType berthType;
	private String seatNumber;
	private String RAC;
	private String Wl;
	
	public AllotedSeat(CoachType coachType, String compartmentNumber, BerthType berthType, String seatNumber,
			String rAC, String wl) {
		super();
		this.coachType = coachType;
		this.compartmentNumber = compartmentNumber;
		this.berthType = berthType;
		this.seatNumber = seatNumber;
		RAC = rAC;
		Wl = wl;
	}

	
	public String getRAC() {
		return RAC;
	}


	public String getWl() {
		return Wl;
	}


	public CoachType getCoachType() {
		return coachType;
	}

	public String getCompartmentNumber() {
		return compartmentNumber;
	}

	public BerthType getBerthType() {
		return berthType;
	}

	public String getSeatNumber() {
		return seatNumber;
	}


	@Override
	public String toString() {
		return "\nAllotedSeat [coachType=" + coachType + ", compartmentNumber=" + compartmentNumber + ", berthType="
				+ berthType + ", seatNumber=" + seatNumber + ", RAC=" + RAC + ", Wl=" + Wl + "]";
	}
	
}
