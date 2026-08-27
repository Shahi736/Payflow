package com.payment.payflow.repository;

import com.payment.payflow.Enums.TransactionsStatus;
import com.payment.payflow.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomer_CustId(Long customerId);

    List<Transaction> findByStatus(TransactionsStatus status);

    List<Transaction> findByCustomer_CustIdAndStatus(Long customerId, TransactionsStatus status);

    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findByPaymentMethod_Id(Long paymentMethodId);
}