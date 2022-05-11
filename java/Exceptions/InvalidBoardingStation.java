package Exceptions;

public class InvalidBoardingStation extends Exception{

	public InvalidBoardingStation() {
		super("Boarding station should be between Source and Destination");
	}
}
