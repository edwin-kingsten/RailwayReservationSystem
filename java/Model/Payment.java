package Model;

public class Payment {
	private String id;
	private String paidOn;
	private PaymentStatus paymentStatus;
	private Double amount;
	
	public Payment(String id,PaymentStatus paymentStatus, String paidOn, Double amount) {
		super();
		this.id = id;
		this.paymentStatus = paymentStatus;
		this.paidOn = paidOn;
		this.amount = amount;
	}
 
	public String getId() {
		return id;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	
  	public String getPaidOn() {
		return paidOn;
	}

	public Double getAmount() {
		return amount;
	}

}
