package UI;

import java.util.Scanner;

import Exceptions.InvalidCredential;
import Exceptions.InvalidStation;
import Exceptions.InvalidUserName;
import Model.DashBoardOptions;

public class Dashboard {
	
	BookingUI bookingUI = new BookingUI();
	MyBookingsUI myBookingsUI = new MyBookingsUI();
	CancellationUI cancellationUI = new CancellationUI();
	
	public void ChooseAction(){
		DashBoardOptions option = Input.getDashBoardOption();
		
		switch(option) {
		case BookTicket:
			bookingUI.makeBooking();
			break;
			
		case MyBookings:
			myBookingsUI.viewBooking();
			break;
			
		case CancelTicket:
			cancellationUI.cancelPassengers();
			break;
			}
	}
	
	public void DashBoardBuilder(){
		ChooseAction();
	}
}
