package ru.betuganova.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import ru.betuganova.Entity.BankAccountEntity;
import ru.betuganova.Entity.TransactionEntity;
import ru.betuganova.Model.BankAccount;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BankAccountMapper {
    private final ModelMapper modelMapper;

    public BankAccountMapper() {
        this.modelMapper = new ModelMapper();

        Converter<Set<TransactionEntity>, ArrayList<Long>> transactionsToIds = ctx ->
                ctx.getSource() == null ? new java.util.ArrayList<>() :
                        ctx.getSource().stream()
                                .map(TransactionEntity::getId)
                                .collect(Collectors.toCollection(java.util.ArrayList::new));

        Converter<BankAccount, Set<TransactionEntity>> idsToTransactions = ctx -> {
            BankAccount source = ctx.getSource();
            if (source.getTransactionIdHistory() == null || source.getTransactionIdHistory().isEmpty()) {
                return new HashSet<>();
            }

            BankAccountEntity destination = (BankAccountEntity) ctx.getParent().getDestination();
            if (destination.getTransactionHistory() == null) {
                return new HashSet<>();
            }

            Set<Long> ids = new HashSet<>(source.getTransactionIdHistory());
            return destination.getTransactionHistory().stream()
                    .filter(tx -> ids.contains(tx.getId()))
                    .collect(Collectors.toSet());
        };


        modelMapper.createTypeMap(BankAccountEntity.class, BankAccount.class)
                .addMappings(mapper -> mapper.using(transactionsToIds)
                        .map(BankAccountEntity::getTransactionHistory, BankAccount::setTransactionIdHistory));

        modelMapper.createTypeMap(BankAccount.class, BankAccountEntity.class);
    }

    public BankAccount toModel(BankAccountEntity bankAccount) {
        return modelMapper.map(bankAccount, BankAccount.class);
    }

    public BankAccountEntity toEntity(BankAccount bankAccount) {
        return modelMapper.map(bankAccount, BankAccountEntity.class);
    }
}
