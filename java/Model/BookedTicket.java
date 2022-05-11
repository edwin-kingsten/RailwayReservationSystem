package Model;

import java.util.List;

public class BookedTicket {
	private String Pnr;
	private String trainId;
    private String srcId;
    private String destId;
    private String boardingStationId;
    private String deptDate;
    private String bookedOn;
    private Quota quota;
    private CoachType coachType;
    private Payment payment;
    private PriceDetail priceDetail;
    private List<BookedPassenger> bookedPassengers;
    
    
	public BookedTicket(String pnr, String trainId, String srcId, String destId, String boardinStationId , String deptDate, String bookedOn,
			Quota quota, CoachType coachType, Payment payment, PriceDetail priceDetail,
			List<BookedPassenger> bookedPassengers) {
		super();
		Pnr = pnr;
		this.trainId = trainId;
		this.srcId = srcId;
		this.destId = destId;
		this.boardingStationId = boardinStationId;
		this.deptDate = deptDate;
		this.bookedOn = bookedOn;
		this.quota = quota;
		this.coachType = coachType;
		this.payment = payment;
		this.priceDetail = priceDetail;
		this.bookedPassengers = bookedPassengers;
	}


	public String getPnr() {
		return Pnr;
	}


	public String getTrainId() {
		return trainId;
	}


	public String getSrcId() {
		return srcId;
	}
 

	public String getBoardingStationId() {
		return boardingStationId;
	}

	public String getDestId() {
		return destId;
	}


	public String getDeptDate() {
		return deptDate;
	}


	public String getBookedOn() {
		return bookedOn;
	}


	public Quota getQuota() {
		return quota;
	}


	public CoachType getCoachType() {
		return coachType;
	}


	public Payment getPayment() {
		return payment;
	}


	public PriceDetail getPriceDetail() {
		return priceDetail;
	}


	public List<BookedPassenger> getBookedPassengers() {
		return bookedPassengers;
	}


	@Override
	public String toString() {
		return "BookedTicket [Pnr=" + Pnr + ", trainId=" + trainId + ", srcId=" + srcId + ", destId=" + destId
				+ ", boardingStationId=" + boardingStationId + ", deptDate=" + deptDate + ", bookedOn=" + bookedOn
				+ ", quota=" + quota + ", coachType=" + coachType + ", payment=" + payment + ", priceDetail="
				+ priceDetail + ", bookedPassengers=" + bookedPassengers + "]";
	}
    
}
