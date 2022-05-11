package Model;

public class PriceDetail {
	private Double baseFare;
	private Double convienceFee;
	
	
	
	public PriceDetail(Double baseFare, Double convienceFee) {
		super();
		this.baseFare = baseFare;
		this.convienceFee = convienceFee;
	}
	
	
	public Double getBaseFare() {
		return baseFare;
	}
	public Double getConvienceFee() {
		return convienceFee;
	}
	
	public Double getTotal() {
		return baseFare + convienceFee;
	}
	
}
