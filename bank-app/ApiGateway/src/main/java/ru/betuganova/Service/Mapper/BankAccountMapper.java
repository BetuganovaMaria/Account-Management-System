package ru.betuganova.Service.Mapper;

import ru.betuganova.Service.Model.BankAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankAccountMapper {
    public BankAccount mapToBankAccount(Map<String, Object> map) {
        BankAccount account = new BankAccount();
        account.setId(((Number) map.get("id")).longValue());
        account.setUserId(((Number) map.get("userId")).longValue());
        account.setBalance(((Number) map.get("balance")).doubleValue());

        List<Number> transactionIds = (List<Number>) map.getOrDefault("transactionIdHistory", new ArrayList<>());
        ArrayList<Long> transactionIdHistory = new ArrayList<>();
        for (Number number : transactionIds) {
            transactionIdHistory.add(number.longValue());
        }
        account.setTransactionIdHistory(transactionIdHistory);

        return account;
    }
}
