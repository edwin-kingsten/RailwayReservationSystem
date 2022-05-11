package Model;

public class Seat implements Comparable<Seat>{
	private String passengerId;
	private String pnr;
	private String srcId;
	private String destId;
	private Integer waitingNumber;
	private AllotedSeat allotedSeat;
	
	
	public Seat(String passengerId, String pnr, String srcId, String destId, Integer waitingNumber , AllotedSeat allotedSeat) {
		super();
		this.passengerId = passengerId;
		this.pnr = pnr;
		this.srcId = srcId;
		this.destId = destId;
		this.waitingNumber = waitingNumber;
		this.allotedSeat = allotedSeat;
	}
	
	public String getPassengerId() {
		return passengerId;
	}
	public String getPnr() {
		return pnr;
	}
	public String getSrcId() {
		return srcId;
	}
	public String getDestId() {
		return destId;
	}
	public Integer getWaitingNumber() {
		return waitingNumber;
	}
	public AllotedSeat getAllotedSeat() {
		return allotedSeat;
	}

	@Override
	public String toString() {
		return "Seat [passengerId=" + passengerId + ", pnr=" + pnr + ", srcId=" + srcId + ", destId=" + destId
				+ ", Number=" + waitingNumber + "AllotedSeat" + allotedSeat +"]";
	}

	@Override
	public int compareTo(Seat s) {
		return this.waitingNumber - s.waitingNumber;
	}
 
	
}
