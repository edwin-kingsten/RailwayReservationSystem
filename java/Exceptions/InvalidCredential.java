package Exceptions;

public class InvalidCredential extends Exception{
	
	public InvalidCredential() {
		super("The Given User Name or Password is Incorrect");
	}
}
