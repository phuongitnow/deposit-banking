package com.banking.deposit.application.service;

import com.banking.deposit.application.dto.DepositRequest;
import com.banking.deposit.application.dto.DepositResponse;
import com.banking.deposit.application.exception.ResourceNotFoundException;
import com.banking.deposit.domain.model.Deposit;
import com.banking.deposit.domain.model.DepositStatus;
import com.banking.deposit.infrastructure.repository.DepositRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {
    
    @Mock
    private DepositRepository depositRepository;
    
    @Mock
    private DepositMapper depositMapper;
    
    @InjectMocks
    private DepositService depositService;
    
    private DepositRequest depositRequest;
    private Deposit deposit;
    private DepositResponse depositResponse;
    
    @BeforeEach
    void setUp() {
        depositRequest = DepositRequest.builder()
                .accountNumber("ACC123456789")
                .amount(new BigDecimal("1000.00"))
                .currency("USD")
                .description("Test deposit")
                .build();
        
        deposit = Deposit.builder()
                .id(1L)
                .accountNumber("ACC123456789")
                .amount(new BigDecimal("1000.00"))
                .status(DepositStatus.PENDING)
                .currency("USD")
                .description("Test deposit")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        depositResponse = DepositResponse.builder()
                .id(1L)
                .accountNumber("ACC123456789")
                .amount(new BigDecimal("1000.00"))
                .status(DepositStatus.PENDING)
                .currency("USD")
                .description("Test deposit")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    @Test
    void testCreateDeposit() {
        when(depositMapper.toEntity(depositRequest)).thenReturn(deposit);
        when(depositRepository.save(any(Deposit.class))).thenReturn(deposit);
        when(depositMapper.toResponse(deposit)).thenReturn(depositResponse);
        
        DepositResponse response = depositService.createDeposit(depositRequest);
        
        assertNotNull(response);
        assertEquals("ACC123456789", response.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), response.getAmount());
        assertEquals(DepositStatus.PENDING, response.getStatus());
        
        verify(depositRepository, times(1)).save(any(Deposit.class));
    }
    
    @Test
    void testGetDepositById() {
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));
        when(depositMapper.toResponse(deposit)).thenReturn(depositResponse);
        
        DepositResponse response = depositService.getDepositById(1L);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(depositRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetDepositById_NotFound() {
        when(depositRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> depositService.getDepositById(1L));
        verify(depositRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetAllDeposits() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Deposit> deposits = Arrays.asList(deposit);
        Page<Deposit> depositPage = new PageImpl<>(deposits);
        
        when(depositRepository.findAll(pageable)).thenReturn(depositPage);
        when(depositMapper.toResponse(deposit)).thenReturn(depositResponse);
        
        Page<DepositResponse> responsePage = depositService.getAllDeposits(pageable);
        
        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        verify(depositRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void testUpdateDepositStatus() {
        Deposit updatedDeposit = Deposit.builder()
                .id(deposit.getId())
                .accountNumber(deposit.getAccountNumber())
                .amount(deposit.getAmount())
                .status(DepositStatus.COMPLETED)
                .currency(deposit.getCurrency())
                .description(deposit.getDescription())
                .createdAt(deposit.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        DepositResponse updatedResponse = DepositResponse.builder()
                .id(depositResponse.getId())
                .accountNumber(depositResponse.getAccountNumber())
                .amount(depositResponse.getAmount())
                .status(DepositStatus.COMPLETED)
                .currency(depositResponse.getCurrency())
                .description(depositResponse.getDescription())
                .createdAt(depositResponse.getCreatedAt())
                .updatedAt(depositResponse.getUpdatedAt())
                .build();
        
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));
        when(depositRepository.save(any(Deposit.class))).thenReturn(updatedDeposit);
        when(depositMapper.toResponse(updatedDeposit)).thenReturn(updatedResponse);
        
        DepositResponse response = depositService.updateDepositStatus(1L, DepositStatus.COMPLETED);
        
        assertNotNull(response);
        assertEquals(DepositStatus.COMPLETED, response.getStatus());
        verify(depositRepository, times(1)).save(any(Deposit.class));
    }
    
    @Test
    void testDeleteDeposit() {
        when(depositRepository.existsById(1L)).thenReturn(true);
        doNothing().when(depositRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> depositService.deleteDeposit(1L));
        verify(depositRepository, times(1)).existsById(1L);
        verify(depositRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeleteDeposit_NotFound() {
        when(depositRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> depositService.deleteDeposit(1L));
        verify(depositRepository, times(1)).existsById(1L);
        verify(depositRepository, never()).deleteById(1L);
    }
}

