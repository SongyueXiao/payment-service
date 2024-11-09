package com.example.paymentservice.controller;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    // Constructor Injection
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Submit a payment")
    public ResponseEntity<Payment> submitPayment(@RequestBody Payment payment) {
        Payment processedPayment = paymentService.submitPayment(payment);
        return ResponseEntity.ok(processedPayment);
    }

    @PutMapping("/{paymentId}")
    @Operation(summary = "Update a payment")
    public ResponseEntity<Payment> updatePayment(@PathVariable String paymentId, @RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(paymentId, payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Reverse a payment (refund)")
    public ResponseEntity<Payment> reversePayment(@PathVariable String paymentId) {
        Payment refundedPayment = paymentService.reversePayment(paymentId);
        return ResponseEntity.ok(refundedPayment);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment details by payment ID")
    public ResponseEntity<Payment> getPaymentByPaymentId(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentByPaymentId(paymentId);
        return ResponseEntity.ok(payment);
    }
}
