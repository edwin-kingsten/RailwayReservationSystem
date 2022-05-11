package Model;

public class Vacancy {
	private VacancyType vacancyType;
	private Integer availability;
	
	public Vacancy(VacancyType vacancyType, Integer availability) {
		super();
		this.vacancyType = vacancyType;
		this.availability = availability;
	}

	public VacancyType getVacancyType() {
		return vacancyType;
	}

	public Integer getAvailability() {
		return availability;
	}
}
