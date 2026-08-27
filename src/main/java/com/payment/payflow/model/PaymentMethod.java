package com.payment.payflow.model;

import com.payment.payflow.Enums.PaymentType;
import jakarta.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType type;

    @Column(name = "masked_details")
    private String maskedDetails;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public PaymentMethod() {
    }

    public PaymentMethod(PaymentType paymentType, Customer customer, String maskedDetails) {
        this.type = paymentType;
        this.customer = customer;
        this.maskedDetails = maskedDetails;
        this.active = true;
    }

    // Standard Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public PaymentType getType() { return type; }
    public void setType(PaymentType type) { this.type = type; }

    public String getMaskedDetails() { return maskedDetails; }
    public void setMaskedDetails(String maskedDetails) { this.maskedDetails = maskedDetails; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}