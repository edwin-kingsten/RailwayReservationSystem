package Exceptions;

public class NoSeatException extends Exception{
	public NoSeatException() {
		super("No Seats are Available for this Coach");
	}

}
