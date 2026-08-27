package com.payment.payflow.Paymentgateway;

import org.springframework.stereotype.Component;

@Component
public class UpiPayment implements PaymentGateway {

    @Override
    public boolean processPayment(double amount) {

        System.out.println("Connecting to UPI...");
        System.out.println("Processing payment of ₹" + amount);
        System.out.println("Payment Successful using UPI");

        return true;
    }
}