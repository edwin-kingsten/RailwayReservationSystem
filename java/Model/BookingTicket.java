package Model;

import java.util.List;

public class BookingTicket {
	private String trainId;
	private String srcId;
	private String destId;
	private String deptDate;
	private String boardingStationId;
	private Quota quota;
	private CoachType coachType;
	private List<BookingPassenger> bookingPassengers;
	
	public BookingTicket(String trainId, String srcId, String destId, String deptDate, String boardingStationId,
			Quota quota, CoachType coachType,List<BookingPassenger> bookingPassengers) {
		super();
		this.trainId = trainId;
		this.srcId = srcId;
		this.destId = destId;
		this.deptDate = deptDate;
		this.boardingStationId = boardingStationId;
		this.quota = quota;
		this.coachType = coachType;
		this.bookingPassengers = bookingPassengers;
	}

	public String getBoardingStationId() {
		return boardingStationId;
	}

	public String getTrainId() {
		return trainId;
	}


	public String getSrcId() {
		return srcId;
	}


	public String getDestId() {
		return destId;
	}


	public String getDeptDate() {
		return deptDate;
	}


	public Quota getQuota() {
		return quota;
	}


	public CoachType getCoachType() {
		return coachType;
	}


	public List<BookingPassenger> getBookingPassengers() {
		return bookingPassengers;
	}

	@Override
	public String toString() {
		return "BookingTicket [trainId=" + trainId + ", srcId=" + srcId + ", destId=" + destId + ", deptDate="
				+ deptDate + ", boardingStationId=" + boardingStationId + ", quota=" + quota + ", coachType="
				+ coachType + ", bookingPassengers=" + bookingPassengers + "]";
	}
	
	
}
