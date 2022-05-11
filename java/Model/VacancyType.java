package Model;

public enum VacancyType {
	Cancelled("RT CLS DELETED"),
	Available("AVL"),
	RAC("RAC"),
	WL("WL");
	
	private String label = "";
	
	private VacancyType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
