package Manager;

import java.util.List;

import Model.AllotedSeat;
import Model.BerthType;
import Model.CoachType;

public interface SeatAllocManager {
	public AllotedSeat AllocateSeat(String trainId , String srcId , String destId , String deptDate ,CoachType coachType , BerthType preferredBerthType);
	public List<String> lockSeat(String trainId , String deptDate , String src ,String dest , AllotedSeat allotedSeat);
	public String unLockSeat(String trainId , String deptDate , String src ,String dest , AllotedSeat allotedSeat);
}
