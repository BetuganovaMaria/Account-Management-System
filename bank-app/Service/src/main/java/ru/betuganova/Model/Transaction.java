package ru.betuganova.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a financial transaction associated with a bank account.
 */
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    private TransactionType transactionType;
    private double amount;
    private Long accountId;

    /**
     * Creates a new transaction with the specified type and amount.
     *
     * @param transactionType The type of transaction (e.g., withdrawal, deposit, transfer).
     * @param amount The amount of money involved in the transaction.
     */
    public Transaction(TransactionType transactionType, long accountId, double amount) {
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.amount = amount;
    }
}