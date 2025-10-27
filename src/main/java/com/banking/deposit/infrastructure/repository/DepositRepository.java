package com.banking.deposit.infrastructure.repository;

import com.banking.deposit.domain.model.Deposit;
import com.banking.deposit.domain.model.DepositStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    
    Page<Deposit> findByAccountNumber(String accountNumber, Pageable pageable);
    
    List<Deposit> findByStatus(DepositStatus status);
    
    Optional<Deposit> findByIdAndAccountNumber(Long id, String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
    
    long countByAccountNumber(String accountNumber);
}

