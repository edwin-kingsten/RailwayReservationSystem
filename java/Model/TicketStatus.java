package Model;

public enum TicketStatus {
	Confirmed,
	Cancelled,
	RAC,
	WL;
	
	public static TicketStatus getEnumByName(String name) {
		TicketStatus status = null;
		for(TicketStatus value : values()) {
			if(value.name().equals(name)) {
				status = value;
				break;
			}
		}
		return status;
	}
}
