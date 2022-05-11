package Controller;

import java.util.List;

public interface CancellationController {

	void cancelPassengers(String pnr, List<String> passengerIds);

}