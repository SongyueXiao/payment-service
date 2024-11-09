package com.example.paymentservice.service;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import com.example.paymentservice.messaging.PaymentProducer;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    // Constructor Injection
    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentProducer paymentProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
    }

    @Override
    public Payment submitPayment(Payment payment) {
        // Check if payment with the same paymentId already exists
        Optional<Payment> existingPayment = paymentRepository.findByPaymentId(payment.getPaymentId());
        if (existingPayment.isPresent()) {
            // Return existing payment to ensure idempotency
            return existingPayment.get();
        }

        // Process payment logic (e.g., integrate with payment gateway)
        payment.setStatus("COMPLETED"); // or "FAILED" based on actual processing
        payment.setCreatedAt(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);

        // Publish payment result (will be implemented later)
         paymentProducer.sendPaymentEvent(savedPayment);

        return savedPayment;
    }

    @Override
    public Payment updatePayment(String paymentId, Payment payment) {
        Payment existingPayment = getPaymentByPaymentId(paymentId);
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(existingPayment);
    }

    @Override
    public Payment reversePayment(String paymentId) {
        Payment existingPayment = getPaymentByPaymentId(paymentId);

        // Check if payment is eligible for refund
        if (!"COMPLETED".equals(existingPayment.getStatus())) {
            throw new RuntimeException("Payment cannot be refunded");
        }

        // Process refund logic
        existingPayment.setStatus("REFUNDED");
        existingPayment.setUpdatedAt(LocalDateTime.now());
        Payment refundedPayment = paymentRepository.save(existingPayment);

        // Publish refund event (will be implemented later)
         paymentProducer.sendPaymentEvent(refundedPayment);

        return refundedPayment;
    }

    @Override
    public Payment getPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
