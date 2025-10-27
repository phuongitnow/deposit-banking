package com.banking.deposit.presentation.controller;

import com.banking.deposit.application.dto.DepositRequest;
import com.banking.deposit.application.dto.DepositResponse;
import com.banking.deposit.application.service.DepositService;
import com.banking.deposit.domain.model.DepositStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deposits")
@RequiredArgsConstructor
public class DepositController {
    
    private final DepositService depositService;
    
    @PostMapping
    public ResponseEntity<DepositResponse> createDeposit(@Valid @RequestBody DepositRequest request) {
        DepositResponse response = depositService.createDeposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DepositResponse> getDepositById(@PathVariable Long id) {
        DepositResponse response = depositService.getDepositById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<DepositResponse>> getAllDeposits(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DepositResponse> response = depositService.getAllDeposits(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Page<DepositResponse>> getDepositsByAccountNumber(
            @PathVariable String accountNumber,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DepositResponse> response = depositService.getDepositsByAccountNumber(accountNumber, pageable);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<DepositResponse> updateDepositStatus(
            @PathVariable Long id,
            @RequestParam DepositStatus status) {
        DepositResponse response = depositService.updateDepositStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return ResponseEntity.noContent().build();
    }
}

