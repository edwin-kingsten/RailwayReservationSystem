package Controller;

import Exceptions.InvalidDate;
import Model.AllotedSeat;
import Model.BookingPassenger;
import Model.SeatVacancy;

public interface SeatController {
	public SeatVacancy getTrainVacancy(String trainId, String deptDate, String src, String dest) throws InvalidDate;
//	public AllotedSeat AllocateSeat(BookingPassenger passenger);	
}
