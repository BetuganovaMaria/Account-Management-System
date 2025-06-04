package ru.betuganova.Controller.Mapper;

import org.modelmapper.ModelMapper;
import ru.betuganova.Controller.Dto.TransactionDto;
import ru.betuganova.Service.Model.Transaction;

public class TransactionDtoMapper {
    private final ModelMapper modelMapper;

    public TransactionDtoMapper() {
        this.modelMapper = new ModelMapper();
    }

    public TransactionDto toDto(Transaction transaction) {
        return transaction == null ? null : modelMapper.map(transaction, TransactionDto.class);
    }

    public Transaction toModel(TransactionDto transaction) {
        return transaction == null ? null : modelMapper.map(transaction, Transaction.class);
    }
}
