package com.example.payment_stab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TopUpRequest (
        @NotBlank
        @JsonProperty("card_number")
        String cardNumber,
        @NotBlank
        double amount
) {}