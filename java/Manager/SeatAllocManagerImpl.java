package Manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Interactor.SeatInteractor;
import Interactor.SeatInteractorImpl;
import Model.AllotedSeat;
import Model.BerthRacWlSeats;
import Model.BerthType;
import Model.CoachType;
import Model.SeatArrangement;

public class SeatAllocManagerImpl implements SeatAllocManager {

	private SeatInteractor seatInteractor = null;

	public SeatAllocManagerImpl() {
		seatInteractor = new SeatInteractorImpl();
	}

	// Allocating the seating for the Passenger
	public AllotedSeat AllocateSeat(String trainId , String srcId , String destId , String deptDate ,CoachType coachType , BerthType preferredBerthType) {

		SeatArrangement seatArrangement = seatInteractor.getAvailableSeats(trainId , deptDate , srcId , destId);

		// getting Compartments for the passenger selected Coach
		Map<String, Map<String, List<String>>> compartments = getCompartments(seatArrangement, coachType);

		// Splitting the seat Arrangement to Berth Types , RAC , WL
		BerthRacWlSeats berthRacWlSeats = splitSeatsToBerthRacWl(compartments);

		Integer berthCount = berthRacWlSeats.getBerthCount();
		Map<BerthType, List<String>> allBerths = berthRacWlSeats.getAllBerths();
		List<String> RacSeats = berthRacWlSeats.getRacSeats();
		List<String> WL = berthRacWlSeats.getWL();

		// allocating Seat for the Customer based on the availability of seats
		// Logic -
		// first preference for user preferred seat
		// If the User preferred seat is unavailable , allocate the berths with highest
		// Number of seats
		// If there is no seats available - try to allocate Rac Seats else allocate them
		// as Waiting List
		// If both Rac and Wl are exceeded , return null

		String compartmentNumber = null;
		BerthType berthType = null;
		String seatNumber = null;

		if (berthCount > 0) {
			// checking if the User Preferred Seat is Available
			if (allBerths.get(preferredBerthType) != null && allBerths.get(preferredBerthType).size() > 0) {
				String seat = allBerths.get(preferredBerthType).get(0);
				compartmentNumber = seat.split("-")[0];
				seatNumber = seat.split("-")[1];
				berthType = preferredBerthType;
			}

			// allocating to the next Available highest berthTypes
			else {
				List<Entry<BerthType, List<String>>> entryList = new ArrayList(allBerths.entrySet());

				// sorting the Berths List in descending Order based on Number of Available
				// Seats
				Collections.sort(entryList, new Comparator<Entry<BerthType, List<String>>>() {
					@Override
					public int compare(Entry<BerthType, List<String>> o1, Entry<BerthType, List<String>> o2) {
						return o2.getValue().size() - o1.getValue().size();
					}
				});

				Entry<BerthType, List<String>> nextAvailBerth = entryList.get(0);

				berthType = nextAvailBerth.getKey();
				String seat = nextAvailBerth.getValue().get(0);
				compartmentNumber = seat.split("-")[0];
				seatNumber = seat.split("-")[1];
			}

			return new AllotedSeat(coachType, compartmentNumber, berthType, seatNumber, null, null);
		}

		else {

			// Checking if there is an Available RAC Seats
			if (RacSeats.size() > 0) {
				Collections.sort(RacSeats);
				String seat = RacSeats.get(0);
				compartmentNumber = seat.split("-")[0];
				seatNumber = seat.split("-")[1];
                
				Integer totalRacCount = seatInteractor.getTotalRacCount(trainId, deptDate ,coachType);
				Integer RacAllotedNumber = totalRacCount - RacSeats.size() + 1;
				return new AllotedSeat(coachType, compartmentNumber, berthType, seatNumber,RacAllotedNumber.toString(), null);
			}

			// checking if there is an Available RAC Seats
			else if (WL.size() > 0) {
				Collections.sort(WL);
				String seat = WL.get(0);
				compartmentNumber = seat.split("-")[0];
				seatNumber = seat.split("-")[1];

				Integer totalWlCount = seatInteractor.getTotalWlCount(trainId, deptDate, coachType);
				Integer WlAllotedNumber = totalWlCount - WL.size() + 1;

				return new AllotedSeat(coachType , compartmentNumber, berthType, seatNumber, null,
						WlAllotedNumber.toString());
			}
		}
		return null;
	}

	// Parsing the Compartments based on user Selected coach Type
	private Map<String, Map<String, List<String>>> getCompartments(SeatArrangement seatArrangement,
			CoachType coachType) {
		Map<String, Map<String, List<String>>> compartments = null;
		switch (coachType) {
		case FirstClassAc: {
			compartments = seatArrangement.getAc1();
			break;
		}

		case TwoTierAc: {
			compartments = seatArrangement.getAc2();
			break;
		}

		case ThreeTierAc: {
			compartments = seatArrangement.getAc3();
			break;
		}
		case Sleeper: {
			compartments = seatArrangement.getSleeper();
			break;
		}

		case SecondSitting: {
			compartments = seatArrangement.getSS();
			break;
		}
		}

		return compartments;
	}

	// Splitting the seat Arrangement to Berth Types , RAC , WL
	private BerthRacWlSeats splitSeatsToBerthRacWl(Map<String, Map<String, List<String>>> compartments) {
		Integer berthCount = 0;
		Map<BerthType, List<String>> allBerths = new HashMap();
		List<String> RacSeats = new ArrayList();
		List<String> WL = new ArrayList();

		for (Entry<String, Map<String, List<String>>> comp : compartments.entrySet()) {

			for (Entry<String, List<String>> seatType : comp.getValue().entrySet()) {

				// adding compartment Id to all the available seats
				seatType.getValue().replaceAll(s -> comp.getKey() + "-" + s);

//							System.out.println(seatType);
				switch (seatType.getKey()) {
				case "Lower": {
					BerthType berthType = BerthType.Lower;
					if (!allBerths.containsKey(berthType)) {
						allBerths.put(berthType, seatType.getValue());
					} else {
						allBerths.get(berthType).addAll(seatType.getValue());
					}
					berthCount += seatType.getValue().size();
					break;
				}

				case "Middle": {
					BerthType berthType = BerthType.Middle;
					if (!allBerths.containsKey(berthType)) {
						allBerths.put(berthType, seatType.getValue());
					} else {
						allBerths.get(berthType).addAll(seatType.getValue());
					}
					berthCount += seatType.getValue().size();
					break;
				}

				case "Upper": {
					BerthType berthType = BerthType.Upper;
					if (!allBerths.containsKey(berthType)) {
						allBerths.put(berthType, seatType.getValue());
					} else {
						allBerths.get(berthType).addAll(seatType.getValue());
					}
					berthCount += seatType.getValue().size();
					break;
				}

				case "SideUpper": {
					BerthType berthType = BerthType.SideUpper;
					if (!allBerths.containsKey(berthType)) {
						allBerths.put(berthType, seatType.getValue());
					} else {
						allBerths.get(berthType).addAll(seatType.getValue());
					}
					berthCount += seatType.getValue().size();
					break;
				}

				case "SideLower": {
					BerthType berthType = BerthType.SideUpper;
					if (!allBerths.containsKey(berthType)) {
						allBerths.put(berthType, seatType.getValue());
					} else {
						allBerths.get(berthType).addAll(seatType.getValue());
					}
					berthCount += seatType.getValue().size();
					break;
				}

				case "RAC": {
					RacSeats.addAll(seatType.getValue());
					break;
				}

				case "WL": {
					WL.addAll(seatType.getValue());
					break;
				}
				}
			}
		}
		return new BerthRacWlSeats(allBerths, berthCount, RacSeats, WL);
	}
   
	
	public List<String> lockSeat(String trainId , String deptDate , String src ,String dest , AllotedSeat allotedSeat) {
		return seatInteractor.LockSeat(trainId, deptDate, src, dest, allotedSeat);
	}
	
	public String unLockSeat(String trainId , String deptDate , String src , String dest , AllotedSeat allotedSeat) {
		return seatInteractor.UnLockSeat(trainId, deptDate, src, dest, allotedSeat);
	}
}
