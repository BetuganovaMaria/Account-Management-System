package ru.betuganova.Service.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
    private String transactionType;
    private double amount;
    private Long accountId;
}