package Controller;

import java.util.List;

import Exceptions.InvalidStation;
import Model.StoppingInfo;
import Model.Train;
import Model.TrainDisplayer;

public interface SearchController {
	public List<TrainDisplayer> findTrains(String src, String dest) throws InvalidStation;
	public List<StoppingInfo> getTrainRoute(String trainId);
}
