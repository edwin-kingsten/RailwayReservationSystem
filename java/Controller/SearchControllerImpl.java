package Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import Exceptions.InvalidStation;
import Interactor.SeatInteractor;
import Interactor.SearchInteractor;
import Interactor.SearchInteractorImpl;
import Model.SeatArrangement;
import Model.SeatVacancy;
import Model.StoppingInfo;
import Model.Train;
import Model.TrainDisplayer;

public class SearchControllerImpl implements SearchController {

	private SearchInteractor trainSearchInteractor = null;
	private SeatController seatController = null;

	public SearchControllerImpl() {
		trainSearchInteractor = new SearchInteractorImpl();
		seatController = new SeatControllerImpl();
	}

	public List<TrainDisplayer> findTrains(String src, String dest) throws InvalidStation {
		// checking if it is a valid Station
		if (!(trainSearchInteractor.isValidStation(src) && trainSearchInteractor.isValidStation(dest))) {
			throw new InvalidStation();
		}
        
		List<Train> trains = trainSearchInteractor.findTrains(src, dest);
        
		List<TrainDisplayer> trainDisplayers = new ArrayList<TrainDisplayer>();
        
		//converting train to trainDisplayer
		for(Train train : trains) {
			trainDisplayers.add(trainToDisplayer(src, dest, train));
		}
		
		return trainDisplayers;
	}

	public TrainDisplayer trainToDisplayer(String src, String dest, Train train) throws InvalidStation {
		TrainDisplayer trainDisplayer = null;
		String trainId = train.getId();
		String trainName = train.getName();
		String srcName = trainSearchInteractor.getSationName(src);
		String destName = trainSearchInteractor.getSationName(dest);
		
		Map<String , String> srcTime = getdepartureTime(train.getStops(), src);
		Map<String , String> destTime = getArrivalTime(train.getStops(), dest);
		
        String startTime = srcTime.get("time");
        String endTime = destTime.get("time");
        
        String travelTime = getTravelTime(srcTime, destTime);
        
		List<String> availableDays = train.getDays();
        
		trainDisplayer = new TrainDisplayer(trainId, trainName, src, dest, srcName, destName, startTime, endTime, travelTime, availableDays);
		
		return trainDisplayer;
	}

	private Map<String , String> getArrivalTime(List<StoppingInfo> stoppingsInfo, String stationId) {
		String arrivalTime = "";
        String arrivalDay = "";
        
        Map<String , String> time = new HashMap();
        
		for (StoppingInfo info : stoppingsInfo) {
			if (info.getId().equals(stationId)) {
				arrivalDay = info.getDay().toString();
				arrivalTime = info.getArrives();
				break;
			}
		}
        
		time.put("day", arrivalDay);
		time.put("time", arrivalTime);
		
		return time;
	}
	
	private Map<String , String> getdepartureTime(List<StoppingInfo> stoppingsInfo, String stationId) {
		String deptTime = "";
        String deptDay = "";
		
		Map<String , String> time = new HashMap();
		for (StoppingInfo info : stoppingsInfo) {
			if (info.getId().equals(stationId)) {
				deptDay = info.getDay().toString();
				deptTime = info.getDeparts();
				break;
			}
		}
        
		time.put("day" , deptDay);
		time.put("time", deptTime);
		return time;
	}
	
	private String getTravelTime(Map<String , String> srcTime , Map<String , String> destTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.HH.mm");
		String travelTime = "";
		Date d1 = null;
		Date d2 = null;
		
		try {
			d1 = formatter.parse(srcTime.get("day")+ "." + srcTime.get("time"));
			d2 = formatter.parse(destTime.get("day")+ "." + destTime.get("time"));
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		
		Long timediff = d2.getTime() - d1.getTime();
 		
		long hours = TimeUnit.MILLISECONDS.toHours(timediff);
		long minutes = (timediff / (1000*60))%60;
		
		travelTime += hours + "H " + minutes + "Min ";
		return travelTime;
	}
	
	public List<StoppingInfo> getTrainRoute(String trainId){
		List<StoppingInfo> stops = null;
		Train train = trainSearchInteractor.getTrain(trainId);
		
		if(train != null) {
			stops = train.getStops();
		}
		
		return stops;
	}
}