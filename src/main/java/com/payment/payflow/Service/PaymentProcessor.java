package com.payment.payflow.Service;

import com.payment.payflow.DTO.PaymentRequestDto;
import com.payment.payflow.DTO.PaymentResponseDto;
import com.payment.payflow.Enums.PaymentType;
import com.payment.payflow.Enums.TransactionsStatus;
import com.payment.payflow.model.Customer;
import com.payment.payflow.model.PaymentMethod;
import com.payment.payflow.model.Transaction;
import com.payment.payflow.notification.EmailNotification;
import com.payment.payflow.repository.CustomerRepository;
import com.payment.payflow.repository.PaymentMethodRepository;
import com.payment.payflow.repository.TransacRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentProcessor {

    private final TransacRepository transacRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final EmailNotification emailNotification;

    public PaymentProcessor(TransacRepository transacRepository,
                            CustomerRepository customerRepository,
                            PaymentMethodRepository paymentMethodRepository,
                            EmailNotification emailNotification) {
        this.transacRepository = transacRepository;
        this.customerRepository = customerRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.emailNotification = emailNotification;
    }

    @Transactional
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        String methodInput = request.getPaymentMethod().trim();
        PaymentType paymentType;
        String message;
        String maskedDetails;

        // 1. Map input string to PaymentType enum
        if (methodInput.equalsIgnoreCase("upipayment") || methodInput.equalsIgnoreCase("upi")) {
            paymentType = PaymentType.UPI;
            message = "UPI payment processed instantly via NPCI / VPA network.";
            maskedDetails = request.getUsername() + "@payflow";
        } else if (methodInput.equalsIgnoreCase("creditcardpayment") || methodInput.equalsIgnoreCase("credit_card")) {
            paymentType = PaymentType.Credit_card;
            message = "Credit card transaction authorized and 3DS verified.";
            maskedDetails = "4532-XXXX-XXXX-3341";
        } else if (methodInput.equalsIgnoreCase("walletpayment") || methodInput.equalsIgnoreCase("wallet")) {
            paymentType = PaymentType.Wallet;
            message = "Wallet balance debited and tokenized transfer complete.";
            maskedDetails = "Payflow Wallet Balance";
        } else {
            throw new IllegalArgumentException("Unsupported payment type: " + request.getPaymentMethod());
        }

        // 2. Fetch Customer from DB
        Customer customer = customerRepository.findByUsername(request.getUsername())
                .orElseGet(() -> customerRepository.findById(request.getCustomerId())
                        .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + request.getUsername())));

        // 3. Find existing PaymentMethod entity or create a new row for this customer
        PaymentMethod paymentMethod = paymentMethodRepository
                .findByCustomer_CustIdAndType(customer.getCustId(), paymentType)
                .orElseGet(() -> paymentMethodRepository.save(new PaymentMethod(paymentType, customer, maskedDetails)));

        // 4. Save Transaction
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setPaymentMethod(paymentMethod); // Populates the not-null payment_method_id
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionsStatus.SUCCESS);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction savedTx = transacRepository.save(transaction);

        String displayTxId = "TX-" + savedTx.getId() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        // 5. Trigger real email notification
        if ("emailNotification".equalsIgnoreCase(request.getNotificationType())
                && request.getRecipientEmail() != null
                && !request.getRecipientEmail().isBlank()) {
            emailNotification.sendPaymentReceipt(
                    request.getRecipientEmail(),
                    displayTxId,
                    savedTx.getAmount(),
                    paymentType.name()
            );
        }

        return new PaymentResponseDto(
                displayTxId,
                savedTx.getStatus().name(),
                message,
                savedTx.getAmount()
        );
    }

    public List<Transaction> getAllTransactions() {
        return transacRepository.findAll();
    }

    public List<Transaction> getCustomerTransactions(Long customerId) {
        return transacRepository.findByCustomer_CustId(customerId);
    }
}