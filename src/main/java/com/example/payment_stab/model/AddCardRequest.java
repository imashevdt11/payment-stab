package com.example.payment_stab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddCardRequest (
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
        String cvv,
        @Positive
        @JsonProperty("balance")
        double balance
) {}