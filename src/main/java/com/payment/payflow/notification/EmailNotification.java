package com.payment.payflow.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotification {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String senderEmail;

    public EmailNotification(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPaymentReceipt(String userEmail, String txId, Double amount, String method) {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(userEmail.trim());
            message.setSubject("Payment Receipt - " + txId);
            message.setText("Dear Customer,\n\n"
                    + "Your payment of INR " + amount + " via " + method + " was successful.\n"
                    + "Transaction ID: " + txId + "\n\n"
                    + "Thank you for using Payflow.");

            mailSender.send(message);
            System.out.println(">> [EMAIL SENT] Confirmation receipt dispatched to: " + userEmail);
        } catch (Exception e) {
            System.err.println(">> [EMAIL FAILED CAUSE]:");
            e.printStackTrace(); // Prints the exact SMTP server rejection line
        }
    }
}