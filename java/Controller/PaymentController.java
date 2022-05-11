package Controller;

import Model.BankDetail;
import Model.Payment;

public interface PaymentController {
	public Payment makePayment(BankDetail bankDetail , Double Amount);
}
