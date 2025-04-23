package ru.betuganova.Mapper;

import org.modelmapper.ModelMapper;
import ru.betuganova.Entity.TransactionEntity;
import ru.betuganova.Model.Transaction;

public class TransactionMapper {
    private final ModelMapper modelMapper;

    public TransactionMapper() {
        this.modelMapper = new ModelMapper();
    }

    public Transaction toModel(TransactionEntity transaction) {
        return transaction == null ? null : modelMapper.map(transaction, Transaction.class);
    }

    public TransactionEntity toEntity(Transaction transaction) {
        if (transaction == null) return null;

        TransactionEntity transactionEntity = modelMapper.map(transaction, TransactionEntity.class);
        transactionEntity.setId(null);

        return transactionEntity;
    }
}
