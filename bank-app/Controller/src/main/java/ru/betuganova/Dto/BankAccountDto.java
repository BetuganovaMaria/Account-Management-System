package ru.betuganova.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Represents a bank account dto
 */
@Getter
@Setter
@NoArgsConstructor
public class BankAccountDto {
    private Long id;
    private Long userId;
    private ArrayList<Long> transactionIdHistory;
    private double balance;

    /**
     * Constructs a new bank account dto.
     *
     * @param id The unique ID of the bank account.
     * @param balance The initial balance of the account.
     * @param userId The ID of the user who owns the account.
     */
    public BankAccountDto(long id, double balance, Long userId) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
        this.transactionIdHistory = new ArrayList<>();
    }
}
