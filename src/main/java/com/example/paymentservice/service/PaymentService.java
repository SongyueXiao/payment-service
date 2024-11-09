package com.example.paymentservice.service;

import com.example.paymentservice.model.Payment;

public interface PaymentService {
    Payment submitPayment(Payment payment);
    Payment updatePayment(String paymentId, Payment payment);
    Payment reversePayment(String paymentId); // Refund
    Payment getPaymentByPaymentId(String paymentId);
}
