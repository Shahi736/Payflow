package com.payment.payflow.Paymentgateway;

import org.springframework.stereotype.Component;

@Component
public class CreditcardPaymen implements PaymentGateway {

    @Override
    public boolean processPayment(double amount) {

        System.out.println("Connecting to Credit Card...");
        System.out.println("Processing payment of ₹" + amount);
        System.out.println("Payment Successful using Credit Card");

        return true;
    }
}