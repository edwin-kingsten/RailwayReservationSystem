package Model;

public class BookedPassenger {
	private String id;
	private AllotedSeat allotedSeat;
	private String name;
	private String age;
	private Gender gender;
	private TicketStatus ticketStatus;

	public BookedPassenger(String id , AllotedSeat allotedSeat, String name, String age, Gender gender, TicketStatus ticketStatus) {
		super();
		this.id = id;
		this.allotedSeat = allotedSeat;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.ticketStatus = ticketStatus;
	}
	
	public String getId() {
		return id;
	}

	public AllotedSeat getAllotedSeat() {
		return allotedSeat;
	}

	public String getName() {
		return name;
	}

	public String getAge() {
		return age;
	}

	public Gender getGender() {
		return gender;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	@Override
	public String toString() {
		return "\nBookedPassenger [id=" + id + ", allotedSeat=" + allotedSeat + ", name=" + name + ", age=" + age
				+ ", gender=" + gender + ", ticketStatus=" + ticketStatus + "]";
	}

}
