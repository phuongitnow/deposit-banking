package com.banking.deposit.application.service;

import com.banking.deposit.application.dto.DepositRequest;
import com.banking.deposit.application.dto.DepositResponse;
import com.banking.deposit.application.exception.ResourceNotFoundException;
import com.banking.deposit.domain.model.Deposit;
import com.banking.deposit.domain.model.DepositStatus;
import com.banking.deposit.infrastructure.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepositService {
    
    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;
    
    public DepositResponse createDeposit(DepositRequest request) {
        log.info("Creating deposit for account: {}", request.getAccountNumber());
        
        Deposit deposit = depositMapper.toEntity(request);
        deposit.setStatus(DepositStatus.PENDING);
        
        Deposit savedDeposit = depositRepository.save(deposit);
        log.info("Deposit created with ID: {}", savedDeposit.getId());
        
        return depositMapper.toResponse(savedDeposit);
    }
    
    @Transactional(readOnly = true)
    public DepositResponse getDepositById(Long id) {
        log.info("Fetching deposit with ID: {}", id);
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit not found with id: " + id));
        return depositMapper.toResponse(deposit);
    }
    
    @Transactional(readOnly = true)
    public Page<DepositResponse> getAllDeposits(Pageable pageable) {
        log.info("Fetching all deposits with pagination");
        return depositRepository.findAll(pageable)
                .map(depositMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<DepositResponse> getDepositsByAccountNumber(String accountNumber, Pageable pageable) {
        log.info("Fetching deposits for account: {}", accountNumber);
        return depositRepository.findByAccountNumber(accountNumber, pageable)
                .map(depositMapper::toResponse);
    }
    
    public DepositResponse updateDepositStatus(Long id, DepositStatus status) {
        log.info("Updating deposit status for ID: {} to {}", id, status);
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit not found with id: " + id));
        
        deposit.setStatus(status);
        Deposit updatedDeposit = depositRepository.save(deposit);
        log.info("Deposit status updated for ID: {}", id);
        
        return depositMapper.toResponse(updatedDeposit);
    }
    
    public void deleteDeposit(Long id) {
        log.info("Deleting deposit with ID: {}", id);
        if (!depositRepository.existsById(id)) {
            throw new ResourceNotFoundException("Deposit not found with id: " + id);
        }
        depositRepository.deleteById(id);
        log.info("Deposit deleted with ID: {}", id);
    }
}

