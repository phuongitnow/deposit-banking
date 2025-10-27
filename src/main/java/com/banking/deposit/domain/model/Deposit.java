package com.banking.deposit.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_seq")
    @SequenceGenerator(name = "deposit_seq", sequenceName = "deposit_sequence", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String accountNumber;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositStatus status;
    
    @Column(nullable = false)
    private String currency;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

