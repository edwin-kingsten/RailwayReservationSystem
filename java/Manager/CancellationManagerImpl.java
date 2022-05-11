package Manager;

import java.util.Collections;
import java.util.List;

import Interactor.SeatInteractor;
import Interactor.SeatInteractorImpl;
import Interactor.TicketInteractor;
import Interactor.TicketInteractorImpl;
import Model.AllotedSeat;
import Model.BerthType;
import Model.BookedPassenger;
import Model.BookedTicket;
import Model.CoachType;
import Model.Seat;
import Model.TicketStatus;

public class CancellationManagerImpl implements CancellationManager {
	TicketInteractor ticketInteractor = null;
	SeatInteractor seatInteractor = null;
	SeatAllocManager seatAllocManager = null;
	Integer totalRacCount = 0;

	public CancellationManagerImpl() {
		ticketInteractor = new TicketInteractorImpl();
		seatInteractor = new SeatInteractorImpl();
		seatAllocManager = new SeatAllocManagerImpl();
	}

	@Override
	public void cancelPassengers(String pnr, List<String> passengerIds) {
		BookedTicket ticket = ticketInteractor.getTicket(pnr);
		if (ticket != null && passengerIds != null) {
			String trainId = ticket.getTrainId();
			String deptDate = ticket.getDeptDate();
			String srcId = ticket.getSrcId();
			String destId = ticket.getDestId();
			CoachType coachType = ticket.getCoachType();

			for (BookedPassenger passenger : ticket.getBookedPassengers()) {
				if (passengerIds.contains(passenger.getId())) {
					releaseSeat(trainId, deptDate, srcId, destId, coachType, passenger.getTicketStatus(),
							passenger.getAllotedSeat());
					ticketInteractor.changeTicketStatus(passenger.getId(), TicketStatus.Cancelled);
					resetSeatDetails(passenger.getId());
				}
			}
		}
	}

	private void resetSeatDetails(String passengerId) {
		ticketInteractor.changeBerthType(passengerId, null);
		ticketInteractor.changeCoach(passengerId, null);
		ticketInteractor.changeSeatNumber(passengerId, null);
		ticketInteractor.changeRacNumber(passengerId, null);
		ticketInteractor.changeWlNumber(passengerId, null);
	}

	@Override
	public void releaseSeat(String trainId, String deptDate, String srcId, String destId, CoachType coachType,
			TicketStatus ticketStatus, AllotedSeat allotedSeat) {

		// initializing total Rac Count for the first Time
		totalRacCount = seatInteractor.getTotalRacCount(trainId, deptDate, coachType);
        
		String unLockedRoute = seatAllocManager.unLockSeat(trainId, deptDate, srcId, destId, allotedSeat);
        
		String unLockedSrcId = unLockedRoute.split("-")[0];
		String unLockedDestId = unLockedRoute.split("-")[1];
        
		System.out.println(unLockedRoute);
		
		switch (ticketStatus) {

		// If berth is Released then Allocate That seat By Rac passenger or Wl passenger
		case Confirmed:
			allocateRac(trainId, deptDate, unLockedSrcId, unLockedDestId, coachType);
			break;

		// If Rac is Cancelled then Allocate that seat By Wl
		case RAC:
			ticketInteractor.decreaseWaitingNumber(ticketStatus, trainId, deptDate, unLockedSrcId, unLockedDestId, coachType, Integer.parseInt(allotedSeat.getRAC()));
			allocateWL(trainId, deptDate, unLockedSrcId, unLockedDestId, coachType);
			break;
			
		case WL:
			ticketInteractor.decreaseWaitingNumber(ticketStatus, trainId, deptDate, unLockedSrcId, unLockedDestId, coachType, Integer.parseInt(allotedSeat.getWl()));
			break;
		}
	}

	// Checking and Allocating RAC seat to Berth
	public void allocateRac(String trainId, String deptDate, String unLockedSrcId, String unLockedDestId,
			CoachType coachType) {
        
		System.out.println("rac");
		
		Seat racSeat = ticketInteractor.getAllocationBetweenSrcAndDest(TicketStatus.RAC, trainId, deptDate,
				unLockedSrcId, unLockedDestId, coachType);

		AllotedSeat newAllotedSeat = null;
		
		if (racSeat != null) {
			newAllotedSeat = seatAllocManager.AllocateSeat(trainId, racSeat.getSrcId(), racSeat.getDestId(), deptDate,
					coachType, BerthType.NoPreference);
			
			System.out.println(racSeat + "\n" + newAllotedSeat);
			
			if (newAllotedSeat != null && newAllotedSeat.getRAC() == null && newAllotedSeat.getWl() == null
					&& newAllotedSeat.getBerthType() != null) {
				ticketInteractor.changeTicketStatus(racSeat.getPassengerId(), TicketStatus.Confirmed);
				ticketInteractor.changeBerthType(racSeat.getPassengerId(), newAllotedSeat.getBerthType());
				ticketInteractor.changeCoach(racSeat.getPassengerId(), newAllotedSeat.getCompartmentNumber());
				ticketInteractor.changeSeatNumber(racSeat.getPassengerId(), newAllotedSeat.getSeatNumber());
				ticketInteractor.changeRacNumber(racSeat.getPassengerId(), null);

				// new Paths release after seat Locking
				List<String> newPaths = seatAllocManager.lockSeat(trainId, deptDate, racSeat.getSrcId(),
						racSeat.getDestId(), newAllotedSeat);
				
				System.out.println("new Paths " + newPaths);
//				// decrease RAC Numbers
//				ticketInteractor.decreaseWaitingNumber(TicketStatus.RAC, trainId, deptDate, racSeat.getSrcId(),
//						racSeat.getDestId(), coachType, racSeat.getWaitingNumber());

				
				// Cancelling the Rac seat after berth Seat is allocated
				releaseSeat(trainId, deptDate, racSeat.getSrcId(), racSeat.getDestId(), coachType, TicketStatus.RAC,
						racSeat.getAllotedSeat());


				// Recursively allocating if there is any path Left
				for (String newPath : newPaths) {
					String newUnlockedSrcId = newPath.split("-")[0];
					String newUnlockedDestId = newPath.split("-")[1];
					allocateRac(trainId, deptDate, newUnlockedSrcId, newUnlockedDestId, coachType);
				}
			}
		}

		// If there is BerthSeats Left but No Rac Seats available with this route then
		// Allocate Wl seats to Berth
		else {
			allocateWL(trainId, deptDate, unLockedSrcId, unLockedDestId, coachType);
		}
	}

//	 Checking and Allocating WL to Rac or Berth
	public void allocateWL(String trainId, String deptDate, String unLockedSrcId, String unLockedDestId,
			CoachType coachType) {
        
		System.out.println("WL");
		
		Seat wlSeat = ticketInteractor.getAllocationBetweenSrcAndDest(TicketStatus.WL, trainId, deptDate, unLockedSrcId,
				unLockedDestId, coachType);

		AllotedSeat newAllotedSeat = null;

		// checking if there is a Wl seat for this route
		if (wlSeat != null) {
			newAllotedSeat = seatAllocManager.AllocateSeat(trainId, wlSeat.getSrcId(), wlSeat.getDestId(), deptDate,
					coachType, BerthType.NoPreference);
			
			System.out.println(wlSeat + "\n" + newAllotedSeat);
			
			if (newAllotedSeat != null && newAllotedSeat.getWl() == null) {

				ticketInteractor.changeBerthType(wlSeat.getPassengerId(), newAllotedSeat.getBerthType());
				ticketInteractor.changeCoach(wlSeat.getPassengerId(), newAllotedSeat.getCompartmentNumber());
				ticketInteractor.changeSeatNumber(wlSeat.getPassengerId(), newAllotedSeat.getSeatNumber());
				ticketInteractor.changeWlNumber(wlSeat.getPassengerId(), null);

				if (newAllotedSeat.getBerthType() == null) {
					ticketInteractor.changeRacNumber(wlSeat.getPassengerId(), totalRacCount);
					if (newAllotedSeat.getRAC() != null) {
						ticketInteractor.changeTicketStatus(wlSeat.getPassengerId(), TicketStatus.RAC);
					}
				} else {
					ticketInteractor.changeTicketStatus(wlSeat.getPassengerId(), TicketStatus.Confirmed);
				}

				// new Paths released After SeatLocking
				List<String> newPaths = seatAllocManager.lockSeat(trainId, deptDate, wlSeat.getSrcId(),
						wlSeat.getDestId(), newAllotedSeat);
				
				System.out.println(newPaths);
				
//				// decrease Wl Numbers
//				ticketInteractor.decreaseWaitingNumber(TicketStatus.WL, trainId, deptDate, wlSeat.getSrcId(),
//						wlSeat.getDestId(), coachType, wlSeat.getWaitingNumber());

				// Cancelling the WL Seat
				releaseSeat(trainId, deptDate, wlSeat.getSrcId(), wlSeat.getDestId(), coachType, TicketStatus.WL,
						wlSeat.getAllotedSeat());


				// Recursively allocating if there is any path Left
				for (String newPath : newPaths) {
					String newUnlockedSrcId = newPath.split("-")[0];
					String newUnlockedDestId = newPath.split("-")[1];
					allocateWL(trainId, deptDate, newUnlockedSrcId, newUnlockedDestId, coachType);
				}
			}
		}
	}
}
