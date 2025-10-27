package com.banking.deposit.application.dto;

import com.banking.deposit.domain.model.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse {
    
    private Long id;
    private String accountNumber;
    private BigDecimal amount;
    private DepositStatus status;
    private String currency;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

