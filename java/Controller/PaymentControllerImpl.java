package Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import Model.BankDetail;
import Model.Payment;
import Model.PaymentStatus;

public class PaymentControllerImpl implements PaymentController {

	@Override
	public Payment makePayment(BankDetail bankDetail, Double Amount) {
		String id = generateId();
		String date = getDate();
		PaymentStatus paymentStatus = PaymentStatus.Confirmed;
		return new Payment(id , paymentStatus , date , Amount);
	}

	private String generateId() {
		Random random = new Random();
		String id = "";
		for (int i = 0; i < 10; i++) {
			id += random.nextInt(10);
		}
		return id;
	}
	
	private String getDate() {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date.getTime());
	}

}
