package Controller;

import Interactor.UserInteractor;
import Interactor.UserInteractorImpl;
import Model.Credential;

public class UserControllerImpl implements UserController {
	private UserInteractor userInteractor = null;
	
	public UserControllerImpl() {
		userInteractor = new UserInteractorImpl();
	}
	
	public boolean isVerifiedUser(Credential credential) {
		return userInteractor.isValidUser(credential);
	}
}
