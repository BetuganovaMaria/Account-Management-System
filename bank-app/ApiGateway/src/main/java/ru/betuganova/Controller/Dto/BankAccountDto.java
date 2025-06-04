package ru.betuganova.Controller.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class BankAccountDto {
    private Long id;
    private Long userId;
    private ArrayList<Long> transactionIdHistory;
    private double balance;
}
