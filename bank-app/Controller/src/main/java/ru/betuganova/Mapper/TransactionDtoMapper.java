package ru.betuganova.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.betuganova.Dto.TransactionDto;
import ru.betuganova.Model.Transaction;

@Component
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
