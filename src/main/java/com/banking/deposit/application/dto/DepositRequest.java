package com.banking.deposit.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {
    
    @NotBlank(message = "Account number is required")
    @Size(min = 8, max = 20, message = "Account number must be between 8 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Account number must contain only uppercase letters and numbers")
    private String accountNumber;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Amount must have at most 2 decimal places")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be uppercase ISO currency code")
    private String currency;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}

