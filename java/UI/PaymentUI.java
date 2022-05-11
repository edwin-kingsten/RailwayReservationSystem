package UI;

import Controller.PaymentController;
import Controller.PaymentControllerImpl;
import Model.Payment;
import Model.PaymentStatus;

public class PaymentUI {
	PaymentController paymentController = null;
	
	public PaymentUI() {
		paymentController = new PaymentControllerImpl();
	}
	
	public Payment makePayment(Double Amount) {
		Payment payment = paymentController.makePayment(Input.getBankDetail(), Amount);
		
		if(payment == null || payment.getPaymentStatus() != PaymentStatus.Confirmed) {
			Output.printTransactionFailed();
		}
		
		else {
			Output.printTransactionSuccess();
		}
		
		return payment;
	}
	
}
