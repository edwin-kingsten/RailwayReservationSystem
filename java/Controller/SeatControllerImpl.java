package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Exceptions.InvalidDate;
import Interactor.SeatInteractor;
import Interactor.SeatInteractorImpl;
import Model.AllotedSeat;
import Model.BerthRacWlSeats;
import Model.BerthType;
import Model.BookingPassenger;
import Model.CoachType;
import Model.SeatArrangement;
import Model.SeatVacancy;
import Model.Vacancy;
import Model.VacancyType;

public class SeatControllerImpl implements SeatController {

	private SeatInteractor seatInteractor = null;

	public SeatControllerImpl() {

		seatInteractor = new SeatInteractorImpl();
	}

// Getting vacant Seats count for different Coaches
	public SeatVacancy getTrainVacancy(String trainId, String deptDate, String src, String dest) throws InvalidDate {
		SeatVacancy seatVacancy = null;
        
		SeatArrangement seatArrangement = seatInteractor.getAvailableSeats(trainId, deptDate, src, dest);

		if (seatArrangement == null) {
			throw new InvalidDate();
		}
        
		
		Vacancy ac1Vacancy = getCoachVacancy(seatArrangement.getAc1() , trainId , deptDate , CoachType.FirstClassAc);
		Vacancy ac2Vacancy = getCoachVacancy(seatArrangement.getAc2() , trainId , deptDate , CoachType.TwoTierAc);
		Vacancy ac3Vacancy = getCoachVacancy(seatArrangement.getAc3() , trainId , deptDate , CoachType.ThreeTierAc);
		Vacancy sleeperVacancy = getCoachVacancy(seatArrangement.getSleeper() , trainId , deptDate , CoachType.Sleeper);
        Vacancy sSVacancy = getCoachVacancy(seatArrangement.getSS() , trainId , deptDate , CoachType.SecondSitting);		

		return new SeatVacancy(ac1Vacancy, ac2Vacancy, ac3Vacancy, sleeperVacancy , sSVacancy);
	}

	// Calculating Available seat Count , Rac vacancy , Waiting List vacancy
	public Vacancy getCoachVacancy(Map<String, Map<String, List<String>>> Compartments , String trainId , String deptDate , CoachType coachType) {
        
		Vacancy vacancy = null;
		
		// vacancy count
		Integer SeatCount = 0;
		Integer RacCount = 0;
		Integer WLCount = 0;

		for (Entry<String, Map<String, List<String>>> comp : Compartments.entrySet()) {

			for (Entry<String, List<String>> seatType : comp.getValue().entrySet()) {
				if ("RAC".equals(seatType.getKey())) {
					RacCount += seatType.getValue().size();
				}

				else if ("WL".equals(seatType.getKey())) {
					WLCount += seatType.getValue().size();
				}

				else {
					SeatCount += seatType.getValue().size();
				}
			}
		}
        
		// Total Alloted Seats for Rac and Wl
		Integer totalRac = seatInteractor.getTotalRacCount(trainId, deptDate, coachType);
		Integer totalWl = seatInteractor.getTotalWlCount(trainId, deptDate, coachType);
		
	    // Total Number of people Booked in Rac and Wl
		Integer RacBooked = totalRac - RacCount;
		Integer WlBooked = totalWl - WLCount;
		
		
		if (SeatCount != 0) {
			vacancy = new Vacancy(VacancyType.Available, SeatCount);
		}

		else if (RacCount != 0) {
			vacancy = new Vacancy(VacancyType.RAC, RacBooked);
		}
		
		else if(WLCount != 0) {
			vacancy = new Vacancy(VacancyType.WL, WlBooked);
		}

		else {
			vacancy = new Vacancy(VacancyType.Cancelled, null);
		}
		
		return vacancy;
	}
}
