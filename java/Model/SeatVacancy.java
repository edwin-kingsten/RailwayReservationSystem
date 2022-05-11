package Model;

public class SeatVacancy {
	private Vacancy AC1Vacancy;
	private Vacancy AC2Vacancy;
	private Vacancy AC3Vacancy;
	private Vacancy SleeperVacancy;
	private Vacancy SSVacancy;
	
	public SeatVacancy(Vacancy aC1Vacancy, Vacancy aC2Vacancy, Vacancy aC3Vacancy, Vacancy sleeperVacancy , Vacancy ssVacancy) {
		super();
		AC1Vacancy = aC1Vacancy;
		AC2Vacancy = aC2Vacancy;
		AC3Vacancy = aC3Vacancy;
		SleeperVacancy = sleeperVacancy;
		SSVacancy = ssVacancy;
	}


	public Vacancy getSleeperVacancy() {
		return SleeperVacancy;
	}


	public Vacancy getSSVacancy() {
		return SSVacancy;
	}


	public Vacancy getAC1Vacancy() {
		return AC1Vacancy;
	}


	public Vacancy getAC2Vacancy() {
		return AC2Vacancy;
	}


	public Vacancy getAC3Vacancy() {
		return AC3Vacancy;
	}
	
}
