package com.example.payment_stab.controller;

import com.example.payment_stab.model.AddCardRequest;
import com.example.payment_stab.model.Card;
import com.example.payment_stab.model.PaymentRequest;
import com.example.payment_stab.model.TopUpRequest;
import com.example.payment_stab.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @Operation(summary = "Оплата")
    @PostMapping("/process")
    public String processPayment(@RequestBody @Valid PaymentRequest request) {
        return service.processPayment(request);
    }

    @Operation(summary = "Добавление карты")
    @PostMapping("/cards/add")
    public String addCard(@RequestBody @Valid AddCardRequest request) {
        return service.addCard(request);
    }

    @Operation(summary = "Пополнение баланса")
    @PostMapping("/top-up")
    public String topUpBalance(@RequestBody @Valid TopUpRequest request) {
        return service.topUpBalance(request);
    }

    @Operation(summary = "Получение информации о всех картах")
    @GetMapping("/cards")
    public List<Card> getAllCards() {
        return service.getAllCards();
    }
}
