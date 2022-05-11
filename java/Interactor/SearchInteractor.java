package Interactor;

import java.util.List;
import java.util.Map;

import Exceptions.InvalidStation;
import Model.Train;

public interface SearchInteractor {
	public List<String> getRoute(String trainId);
	public Train getTrain(String trainId);
	public List<Train> findTrains(String src, String dest);
	public boolean isValidStation(String station);
	public String getSationName(String station) throws InvalidStation;
	public Map<String , Double> getBasicPrice(String trainId);
}
