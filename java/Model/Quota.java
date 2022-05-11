package Model;

public enum Quota {
	General;
	
	public static Quota getEnumByName(String name) {
		Quota quota = null;
		for(Quota value : values()) {
			if(value.name().equals(name)) {
				quota = value;
				break;
			}
		}
		return quota;
	}
}
