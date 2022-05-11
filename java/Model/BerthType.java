package Model;

public enum BerthType {
	NoPreference,
	Lower,
	Middle,
	Upper,
	SideLower,
	SideUpper,
	Normal,
	Window;
	
	
	public static BerthType getEnumByName(String name) {
		BerthType type = null;
		for(BerthType value : values()) {
			if(value.name().equals(name)) {
				type = value;
				break;
			}
		}
		return type;
	}
	
}
