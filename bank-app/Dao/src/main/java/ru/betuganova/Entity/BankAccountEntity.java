package ru.betuganova.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a bank account entity with transaction history and balance.
 * This class stores the details of a user's bank account, including transactions, balance, and associated user login.
 */
@Getter
@Setter
@Entity
@Table(name = "bank_accounts")
@NoArgsConstructor
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private long userId;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountId", fetch = FetchType.EAGER)
    private Set<TransactionEntity> transactionHistory;

    @NotNull
    @Column(name = "balance")
    private double balance;

    /**
     * Constructs a new BankAccount with the specified id, balance, and user login.
     *
     *
     * @param balance The balance of the bank account.
     * @param userId The ID of the user associated with the bank account.
     */
    public BankAccountEntity(double balance, long userId) {
        this.balance = balance;
        this.userId = userId;
        this.transactionHistory = new HashSet<>();
    }

    /**
     * Adds a new transaction entry to the bank account.
     *
     * @param transaction The transaction to be recorded, containing type, amount, and related account information.
     */
    public void addTransaction(TransactionEntity transaction) {
        transactionHistory.add(transaction);
    }
}
