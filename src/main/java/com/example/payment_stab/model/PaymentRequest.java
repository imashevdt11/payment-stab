package com.example.payment_stab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record PaymentRequest (
        @NotBlank
        @JsonProperty("card_number")
        String cardNumber,

        @NotBlank
        @JsonProperty("card_holder")
        String cardHolder,

        @NotBlank
        @JsonProperty("expiry_date")
        String expiryDate,

        @NotBlank
        @JsonProperty("cvv")
        String cvv,

        @NotBlank
        @JsonProperty("amount")
        Double amount
) {}