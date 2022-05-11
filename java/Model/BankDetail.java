package Model;

public class BankDetail {
	private String name;
	private String cardNumber;
	private String cvv;

	public BankDetail(String name, String cardNumber, String cvv) {
		super();
		this.name = name;
		this.cardNumber = cardNumber;
		this.cvv = cvv;
	}
	
	public String getName() {
		return name;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public String getCvv() {
		return cvv;
	}
	
	
}
