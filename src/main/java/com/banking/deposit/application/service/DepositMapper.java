package com.banking.deposit.application.service;

import com.banking.deposit.application.dto.DepositRequest;
import com.banking.deposit.application.dto.DepositResponse;
import com.banking.deposit.domain.model.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    
    DepositMapper INSTANCE = Mappers.getMapper(DepositMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Deposit toEntity(DepositRequest request);
    
    DepositResponse toResponse(Deposit deposit);
}

