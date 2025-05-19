package com.example.payment_stab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopUpRequest (
        @NotBlank
        @JsonProperty("card_number")
        String cardNumber,
        @NotNull
        double amount
) {}