package ru.betuganova.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.betuganova.Entity.TransactionEntity;

import java.util.ArrayList;

/**
 * Represents a bank account in the system.
 */
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
    private Long id;
    private Long userId;
    private ArrayList<Long> transactionIdHistory;
    private double balance;

    /**
     * Constructs a new bank account.
     *
     * @param id The unique ID of the bank account.
     * @param balance The initial balance of the account.
     * @param userId The ID of the user who owns the account.
     */
    public BankAccount(long id, double balance, Long userId) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
        this.transactionIdHistory = new ArrayList<>();
    }
}
