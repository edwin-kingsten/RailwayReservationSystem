package Model;

public class BookingPassenger {
	private String name;
	private Integer age;
	private Gender gender;
	private BerthType berthType;
	
	
	public BookingPassenger(String name, Integer age, Gender gender, BerthType berthType) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.berthType = berthType;
	}


	@Override
	public String toString() {
		return "BookingPassenger [name=" + name + ", age=" + age + ", gender=" + gender + ", berthType=" + berthType
				+ "]";
	}


	public String getName() {
		return name;
	}


	public Integer getAge() {
		return age;
	}


	public Gender getGender() {
		return gender;
	}


	public BerthType getBerthType() {
		return berthType;
	}
		
	
}
