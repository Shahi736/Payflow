package com.payment.payflow.repository;

import com.payment.payflow.Enums.PaymentType;
import com.payment.payflow.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    // Matches the 'type' field and customer
    Optional<PaymentMethod> findByCustomer_CustIdAndType(Long customerId, PaymentType type);

    // List all methods for a given customer
    List<PaymentMethod> findByCustomer_CustId(Long customerId);
}