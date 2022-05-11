package Exceptions;

public class UserNameAlreadyExists extends Exception{
	public UserNameAlreadyExists() {
		super("The Given User Name Already Exists");
	}
}
