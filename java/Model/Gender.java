package Model;

public enum Gender {
	Male,
	Female,
	TransGender;
	
	public static Gender getEnumByName(String name) {
		Gender gender = null;
		for(Gender value : values()) {
			if(value.name().equals(name)) {
				gender = value;
				break;
			}
		}
		return gender;
	}
	
}
