package Model;

public enum CoachType {
	FirstClassAc("AC1"),
	TwoTierAc("AC2"),
	ThreeTierAc("AC3"),
	Sleeper("Sleeper"),
	SecondSitting("SS");
	
	private final String label;
	
	private CoachType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static CoachType getEnumByLabel(String label) {
		CoachType enumType = null;
		
		for(CoachType val : CoachType.values()) {
			if(val.getLabel().contentEquals(label)) {
				enumType = val;
				break;
			}
		}
		
		return enumType;
	}
	
	public static CoachType getEnumByName(String name) {
		CoachType type = null;
		for(CoachType value : values()) {
			if(value.name().equals(name)) {
				type = value;
				break;
			}
		}
		return type;
	}
}
