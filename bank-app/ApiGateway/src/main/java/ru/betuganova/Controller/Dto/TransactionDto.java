package ru.betuganova.Controller.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDto {
    private String transactionType;
    private double amount;
    private Long accountId;
}
