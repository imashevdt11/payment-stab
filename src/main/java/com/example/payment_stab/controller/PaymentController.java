package com.example.payment_stab.controller;

import com.example.payment_stab.model.AddCardRequest;
import com.example.payment_stab.model.Card;
import com.example.payment_stab.model.PaymentRequest;
import com.example.payment_stab.model.TopUpRequest;
import com.example.payment_stab.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/process")
    public String processPayment(@RequestBody PaymentRequest request) {
        return service.processPayment(request);
    }

    @PostMapping("/cards/add")
    public String addCard(@RequestBody AddCardRequest request) {
        return service.addCard(request);
    }

    @PostMapping("/top-up")
    public String topUpBalance(@RequestBody TopUpRequest request) {
        return service.topUpBalance(request);
    }

    @GetMapping("/cards")
    public List<Card> getAllCards() {
        return service.getAllCards();
    }
}
