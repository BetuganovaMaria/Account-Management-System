package ru.betuganova.Service.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class BankAccount {
    private Long id;
    private Long userId;
    private ArrayList<Long> transactionIdHistory;
    private double balance;
}
