package Exceptions;

public class InvalidUserName extends Exception{
	public InvalidUserName() {
		super("User Doesnot Exist");
	}
}
