package com.example.paymentservice.messaging;

import com.example.paymentservice.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private static final String TOPIC = "payment_topic";

    private final KafkaTemplate<String, Payment> kafkaTemplate;

    @Autowired
    public PaymentProducer(KafkaTemplate<String, Payment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentEvent(Payment payment) {
        kafkaTemplate.send(TOPIC, payment);
    }
}
