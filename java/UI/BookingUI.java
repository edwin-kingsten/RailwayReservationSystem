package UI;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Controller.BookingController;
import Controller.BookingControllerImpl;
import Controller.PriceController;
import Controller.PriceControllerImpl;
import Controller.SearchController;
import Controller.SearchControllerImpl;
import Controller.SeatController;
import Controller.SeatControllerImpl;
import Exceptions.InvalidCredential;
import Exceptions.InvalidDate;
import Exceptions.InvalidStation;
import Exceptions.InvalidUserName;
import Exceptions.NoSeatException;
import Interactor.SeatInteractorImpl;
import Model.BookingOption;
import Model.BookingPassenger;
import Model.BookingTicket;
import Model.CoachType;
import Model.Credential;
import Model.Payment;
import Model.PaymentStatus;
import Model.PriceDetail;
import Model.Quota;
import Model.SeatVacancy;
import Model.Train;
import Model.TrainDisplayer;
import Model.Vacancy;

public class BookingUI {
	SearchController searchController = new SearchControllerImpl();
	SeatController seatController = new SeatControllerImpl();
	PriceController priceController = new PriceControllerImpl();
	PaymentUI paymentUI = new PaymentUI();
	BookingController bookingController = new BookingControllerImpl();

	public void makeBooking() {

		List<TrainDisplayer> trains = null;
		boolean isValid = false;
		String srcId = "";
		String destId = "";

		do {
			try {
				srcId = Input.getSource();
				destId = Input.getDestination();

				trains = searchController.findTrains(srcId, destId);

				for (TrainDisplayer train : trains) {
					Output.showTrain(train);
				}

				isValid = true;

			} catch (InvalidStation e) {
				Output.printInvalidSrcdest();
			}

		} while (!isValid);

		if (trains.size() == 0) {
			Output.showNoAvailTrains();
		}

		else {
			BookingOption bookingOption = Input.getBookingOption();
			Integer trainIndex = 0;
			String date = "";
			Quota quota = null;

			switch (bookingOption) {
			case SeeTrainRoute:
				trainIndex = Input.getTrainIndexOption(trains.size()) - 1;
				Output.printTrainRoute(searchController.getTrainRoute(trains.get(trainIndex).getId()));
				break;

			case ContinueToBooking:
				SeatVacancy vacancy = null;
				Map<CoachType, Integer> price = null;
				isValid = false;
				trainIndex = Input.getTrainIndexOption(trains.size()) - 1;
				TrainDisplayer train = trains.get(trainIndex);

				// Showing Availability for Each Coach
				do {
					try {
						quota = Input.getQuota();
						date = Input.getDate();
						vacancy = seatController.getTrainVacancy(train.getId(), date, srcId, destId);
						price = priceController.getAllCoachPrice(train.getId(), train.getSrc(), train.getDest());
						Output.printAvailability(vacancy, price);
						isValid = true;
					} catch (InvalidDate e) {
						Output.printInvalidDate();
					}
				} while (!isValid);

				// getting ticket Details
				if (Input.isUserBookingTicket()) {
					String trainId = trains.get(trainIndex).getId();
					bookTicket(trainId, srcId, destId, date, quota, vacancy);
				}
			}
		}
	}

	public void bookTicket(String trainId, String srcId, String destId, String date, Quota quota, SeatVacancy vacancy) {
		Credential credential = UserUI.LoginUser();
		CoachType coachType = Input.getCoachType(vacancy);
		List<BookingPassenger> passengers = Input.getPassengers(coachType);
		BookingTicket ticket = new BookingTicket(trainId, srcId, destId, date, srcId, quota, coachType, passengers);
		PriceDetail priceDetail = priceController.getTicketPrice(ticket);
		Output.printPriceDetail(priceDetail);

		if (Input.isUserProceedToPay()) {
			Payment payment = paymentUI.makePayment(priceDetail.getTotal());

			if (payment != null || payment.getPaymentStatus() == PaymentStatus.Confirmed) {
				try {
					bookingController.makeBooking(ticket, payment, priceDetail, credential);
					Output.printBookingSucess();
				} catch (InvalidCredential | InvalidUserName e) {
					e.printStackTrace();
				} catch (NoSeatException e) {
					Output.printNoSeatsAvailable();
				}
			}
		}
	}

}