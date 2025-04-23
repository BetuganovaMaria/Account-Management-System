package ru.betuganova.Model;

/**
 * Represents different types of transactions that can be performed on a bank account.
 */
public enum TransactionType {
    /** Withdrawal of funds from an account. */
    WITHDRAWAL,

    /** Replenishment (deposit) of funds into an account. */
    REPLENISHMENT,

    /** Transfer of funds from an account to another account. */
    TRANSFER_FROM,

    /** Transfer of funds to an account from another account. */
    TRANSFER_TO
}