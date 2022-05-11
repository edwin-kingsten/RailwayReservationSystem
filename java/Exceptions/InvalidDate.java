package Exceptions;

public class InvalidDate extends Exception {
	public InvalidDate() {
		super("The Given Date is Not present in db");
	}
}
