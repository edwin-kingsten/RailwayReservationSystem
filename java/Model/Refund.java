package Model;

public class Refund {
	private String id;
	private Ticket ticket;
	private RefundStatus refundStaus;
	private String refundedOn;
	
	
	public Refund(String id, Ticket ticket, RefundStatus refundStaus, String refundedOn) {
		super();
		this.id = id;
		this.ticket = ticket;
		this.refundStaus = refundStaus;
		this.refundedOn = refundedOn;
	}


	public String getId() {
		return id;
	}


	public Ticket getTicket() {
		return ticket;
	}


	public RefundStatus getRefundStaus() {
		return refundStaus;
	}


	public String getRefundedOn() {
		return refundedOn;
	}
	
}
