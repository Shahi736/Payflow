package com.paymentApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.payment.payflow")
@EntityScan(basePackages = {"com.payment.payflow.model", "com.payment.payflow.entity"})
@EnableJpaRepositories(basePackages = "com.payment.payflow.repository")
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}