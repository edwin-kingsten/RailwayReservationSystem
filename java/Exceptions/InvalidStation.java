package Exceptions;

public class InvalidStation extends Exception{
	public InvalidStation() {
		super("Station Id doesnot exist");
	}
}
