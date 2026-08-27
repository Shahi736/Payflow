package com.payment.payflow.DTO;

import java.time.LocalDateTime;

public class PaymentResponseDto {
    private String transactionId;
    private String status;
    private String message;
    private double amount;
    private LocalDateTime timestamp;

    public PaymentResponseDto(String transactionId, String status, String message, double amount) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}