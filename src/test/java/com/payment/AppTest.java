package com.payment;

import com.payment.payflow.Paymentgateway.PaymentGateway;
import com.payment.payflow.Service.PaymentProcessor;
import com.payment.payflow.DTO.PaymentRequestDto;
import com.payment.payflow.DTO.PaymentResponseDto;
import com.payment.payflow.entity.TransactionRecord;
import com.payment.payflow.notification.NotificationService;
import com.payment.payflow.repository.TransactionRepository; // Use com.payment.payflow.respository.TransactionRepository if not renamed
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    private PaymentProcessor processor;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        PaymentGateway dummyUpi = amount -> true;
        NotificationService dummyEmail = message -> {};

        Map<String, PaymentGateway> gateways = Map.of("upiPayment", dummyUpi);
        Map<String, NotificationService> notifiers = Map.of("emailNotification", dummyEmail);

        // Pass all 3 dependencies: gateways, notifiers, and the mocked repository
        processor = new PaymentProcessor(gateways, notifiers, transactionRepository);
    }

    @Test
    void testProcessPaymentSuccess() {
        PaymentRequestDto request = new PaymentRequestDto();
        request.setCustomerId(1L);
        request.setUsername("testUser");
        request.setPaymentMethod("upiPayment");
        request.setNotificationType("emailNotification");
        request.setAmount(500.0);

        // Mock saving behavior so it doesn't fail
        when(transactionRepository.save(any(TransactionRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponseDto response = processor.processPayment(request);

        assertNotNull(response.getTransactionId());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(500.0, response.getAmount());
    }

    @Test
    void testProcessPaymentInvalidGateway() {
        PaymentRequestDto request = new PaymentRequestDto();
        request.setPaymentMethod("invalidGateway");
        request.setAmount(100.0);

        assertThrows(IllegalArgumentException.class, () -> processor.processPayment(request));
    }
}