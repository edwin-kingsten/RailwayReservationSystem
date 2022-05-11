package Controller;

import java.util.List;

import Manager.CancellationManager;
import Manager.CancellationManagerImpl;

public class CancellationControllerImpl implements CancellationController {
	private CancellationManager cancellationManager = null;
	
	public CancellationControllerImpl() {
		cancellationManager = new CancellationManagerImpl();
	}
	
	@Override
	public void cancelPassengers(String pnr , List<String> passengerIds) {
		cancellationManager.cancelPassengers(pnr, passengerIds);
	}
}
