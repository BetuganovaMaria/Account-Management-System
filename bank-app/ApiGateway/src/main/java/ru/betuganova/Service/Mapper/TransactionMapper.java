package ru.betuganova.Service.Mapper;

import ru.betuganova.Service.Model.Transaction;

import java.util.Map;

public class TransactionMapper {
    public Transaction mapToTransaction(Map<String, Object> map) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType((String) map.get("transactionType"));
        transaction.setAmount(((Number) map.get("amount")).doubleValue());
        transaction.setAccountId(((Number) map.get("accountId")).longValue());

        return transaction;
    }
}
