package UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.naming.directory.InvalidAttributeValueException;

import Exceptions.NoSeatException;
import Model.BankDetail;
import Model.BerthType;
import Model.BookingOption;
import Model.BookingPassenger;
import Model.BookingTicket;
import Model.CoachType;
import Model.Credential;
import Model.DashBoardOptions;
import Model.Gender;
import Model.Quota;
import Model.SeatVacancy;

public class Input {

	static Scanner sc = new Scanner(System.in);

	public static DashBoardOptions getDashBoardOption() {

		System.out.println("Press 1 - Book Ticket");
		System.out.println("Press 2 - My Bookings");
		System.out.println("Press 3 - Cancellation");
		boolean isValid = false;
		Integer EnteredNum;
		DashBoardOptions option = null;

		do {
			try {
				System.out.print("Enter Your Option - ");
				EnteredNum = Integer.parseInt(sc.next());

				switch (EnteredNum) {
				case 1:
					option = DashBoardOptions.BookTicket;
					break;

				case 2:
					option = DashBoardOptions.MyBookings;
					break;
                
				case 3:
					option = DashBoardOptions.CancelTicket;
					break;
					
				default:
					throw new InvalidAttributeValueException();
				}

				isValid = true;

			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return option;
	}

	public static BookingOption getBookingOption() {
		BookingOption option = null;
		Integer EnteredNum = 0;
		boolean isValid = false;

		System.out.println("Press 1 - To See Train Route");
		System.out.println("Press 2 - To Continue Booking");

		do {
			try {
				System.out.print("Enter Your Option - ");
				EnteredNum = Integer.parseInt(sc.next());

				switch (EnteredNum) {
				case 1:
					option = BookingOption.SeeTrainRoute;
					break;

				case 2:
					option = BookingOption.ContinueToBooking;
					break;

				default:
					throw new InvalidAttributeValueException();
				}

				isValid = true;

			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return option;
	}

	public static Integer getTrainIndexOption(Integer totalTrains) {
		Integer EnteredNum = 0;
		boolean isValid = false;
		do {
			try {
				System.out.print("Enter Selected Train Index - ");
				EnteredNum = Integer.parseInt(sc.next());

				if (EnteredNum > totalTrains) {
					throw new IllegalArgumentException();
				}

				isValid = true;

			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return EnteredNum;
	}

	public static CoachType getCoachType(SeatVacancy vacancy) {

		List<CoachType> coachTypes = Arrays.asList(CoachType.FirstClassAc, CoachType.TwoTierAc, CoachType.ThreeTierAc,
				CoachType.Sleeper, CoachType.SecondSitting);

		List<Integer> availability = Arrays.asList(vacancy.getAC1Vacancy().getAvailability(),
				vacancy.getAC2Vacancy().getAvailability(), vacancy.getAC3Vacancy().getAvailability(),
				vacancy.getSleeperVacancy().getAvailability(), vacancy.getSSVacancy().getAvailability());

		CoachType coachType = null;

		for (int i = 0; i < coachTypes.size(); i++) {
			System.out.println((i + 1) + "-" + coachTypes.get(i).name());
		}

		boolean isValid = false;
		do {
			try {
				System.out.print("Select Coach type - ");
				Integer EnteredNum = Integer.parseInt(sc.next());

				if (availability.get(EnteredNum - 1) == null) {
					throw new NoSeatException();
				}

				coachType = coachTypes.get(EnteredNum - 1);
				isValid = true;
			} catch (NoSeatException e) {
				System.out.println("No Seats are Available");
				System.out.println("Please Enter Valid Option");
			}

			catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return coachType;
	}

	public static Quota getQuota() {
		List<Quota> quotas = Arrays.asList(Quota.General);
		Quota quota = null;

		for (int i = 0; i < quotas.size(); i++) {
			System.out.println((i + 1) + "-" + quotas.get(i).name());
		}

		boolean isValid = false;
		do {
			try {
				System.out.print("Select Quota type - ");
				Integer EnteredNum = Integer.parseInt(sc.next());
				quota = quotas.get(EnteredNum - 1);
				isValid = true;
			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return quota;
	}

	public static String getBoardingStation() {
		System.out.print("Enter BoardingStation Id - ");
		String src = sc.next();
		return src;
	}

	public static boolean isUserBookingTicket() {
		System.out.print("Do you Want to Book Ticket (y/n) - ");
		return "y".equalsIgnoreCase(sc.next());
	}

	public static List<BookingPassenger> getPassengers(CoachType coachType) {
		List<BookingPassenger> passengers = new ArrayList();
		boolean Continue = false;
		do {
			System.out.print("Do you want to Add a Passenger (y/n) - ");
			Continue = sc.next().equalsIgnoreCase("y");
			if (Continue) {
				String name = getName();
				Integer age = getAge();
				Gender gender = getGender();
				BerthType berthType = getBerthType(coachType);
				passengers.add(new BookingPassenger(name, age, gender, berthType));
			}
		} while (Continue);

		return passengers;
	}

	public static BerthType getBerthType(CoachType coachType) {
		List<BerthType> berthTypes = getBerthTypesForCoaches(coachType);
		BerthType berthType = null;

		for (int i = 0; i < berthTypes.size(); i++) {
			System.out.println((i + 1) + "-" + berthTypes.get(i));
		}

		boolean isValid = false;
		do {
			try {
				System.out.print("Select Preferred Berth type - ");
				Integer EnteredNum = Integer.parseInt(sc.next());
				berthType = berthTypes.get(EnteredNum - 1);
				isValid = true;
			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return berthType;
	}

	public static List<BerthType> getBerthTypesForCoaches(CoachType coachType) {
		List<BerthType> berthTypes = null;

		switch (coachType) {
		case FirstClassAc:
			berthTypes = Arrays.asList(BerthType.NoPreference, BerthType.Lower, BerthType.Upper);
			break;

		case SecondSitting:
			berthTypes = Arrays.asList(BerthType.NoPreference, BerthType.Window);
			break;

		case TwoTierAc:
			berthTypes = Arrays.asList(BerthType.NoPreference, BerthType.Lower, BerthType.Upper, BerthType.SideLower,
					BerthType.SideUpper);
			break;

		case ThreeTierAc:
			berthTypes = Arrays.asList(BerthType.NoPreference, BerthType.Lower, BerthType.Upper, BerthType.SideLower,
					BerthType.SideUpper);
			break;

		case Sleeper:
			berthTypes = Arrays.asList(BerthType.NoPreference, BerthType.Lower, BerthType.Upper, BerthType.SideLower,
					BerthType.SideUpper);
			break;

		}

		return berthTypes;
	}

	public static Gender getGender() {
		boolean isValid = false;
		Gender gender = null;
		System.out.println("Choose Passenger Gender");
		System.out.println("Press 1 - Male");
		System.out.println("Press 2 - Female");
		System.out.println("Press 3 - TransGender");

		do {
			try {
				System.out.print("Enter Option - ");
				Integer EnteredNum = Integer.parseInt(sc.next());
				switch (EnteredNum) {
				case 1:
					gender = Gender.Male;
					break;

				case 2:
					gender = Gender.Female;
					break;

				case 3:
					gender = Gender.TransGender;
					break;

				default:
					throw new IllegalArgumentException();
				}

				isValid = true;

			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return gender;

	}

	public static Integer getAge() {
		Integer age = 0;
		boolean isValid = false;
		do {
			try {
				System.out.print("Enter Age - ");
				age = Integer.parseInt(sc.next());
				isValid = true;
			} catch (Exception e) {
				System.out.println("Please Enter Valid Age");
			}
		} while (!isValid);

		return age;
	}

	public static String getName() {
		System.out.print("Please Enter Name - ");
		return sc.next();
	}

	public static String getSource() {
		System.out.print("Enter Source Id - ");
		String src = sc.next();
		return src;
	}

	public static String getDestination() {
		System.out.print("Enter Destination Id - ");
		String dest = sc.next();
		return dest;
	}

	public static String getDate() {
		System.out.print("Enter the Departure Date - ");
		String Date = sc.next();
		return Date;
	}

	public static Credential getUserCredential() {
		System.out.println("Enter Your Login Credential");
		System.out.print("Enter User Name - ");
		String name = sc.next();
		System.out.print("Enter Password - ");
		String password = sc.next();

		return new Credential(name, password);
	}

	public static BankDetail getBankDetail() {
		System.out.println("Enter Bank Details ");
		System.out.print("Enter Name - ");
		String name = sc.next();
		System.out.print("Enter Card Number - ");
		String cardNumber = sc.next();
		System.out.print("Enter CVV - ");
		String cvv = sc.next();

		return new BankDetail(name, cardNumber, cvv);
	}

	public static boolean isUserWantDetailTicket() {
		System.out.print("Do you Want to See the Ticket Detail (y/n) - ");
		return "y".equalsIgnoreCase(sc.next());
	}

	public static Integer getTicketIndexOption(Integer totalTickets) {
		Integer EnteredNum = 0;
		boolean isValid = false;
		do {
			try {
				System.out.print("Enter Selected Ticket Index - ");
				EnteredNum = Integer.parseInt(sc.next());

				if (EnteredNum > totalTickets) {
					throw new IllegalArgumentException();
				}

				isValid = true;

			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return EnteredNum;
	}
	
	public static Integer getPassengerIndexOption(Integer totalPassengers) {
		Integer EnteredNum = 0;
		boolean isValid = false;
		do {
			try {
				System.out.print("Enter Selected Passenger Index - ");
				EnteredNum = Integer.parseInt(sc.next());

				if (EnteredNum > totalPassengers) {
					throw new IllegalArgumentException();
				}

				isValid = true;

			} catch (Exception e) {
				System.out.println("Please Enter Valid Option");
			}
		} while (!isValid);

		return EnteredNum;
	}
	
	public static boolean isUserWantToCancelPassenger() {
		System.out.print("Do you want to Cancel Passenger (y/n) - ");
		return "y".equalsIgnoreCase(sc.next());
	}
	
	public static boolean isUserProceedToPay() {
		System.out.print("Do You Want to Proceed to Pay (y/n) - ");
		return "y".equalsIgnoreCase(sc.next());
	}
}
