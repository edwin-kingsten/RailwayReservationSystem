package UI;

import Controller.UserController;
import Controller.UserControllerImpl;
import Model.Credential;

public class UserUI {
	private static UserController userController = new UserControllerImpl();
	
	public static Credential LoginUser() {
		boolean isValid = false;
		Credential credential = null;
		do {
			credential = Input.getUserCredential();
			if(userController.isVerifiedUser(credential)) {
				isValid = true;
			}
			else {
				System.out.println("Please Enter Correct User Name and Password");
			}
		}while(!isValid);
		
		return credential;
	}
	
}
