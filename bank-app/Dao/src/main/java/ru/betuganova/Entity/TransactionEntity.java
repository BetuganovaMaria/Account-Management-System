package ru.betuganova.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a financial transaction entity in the system.
 * This entity is used to store transaction details related to a specific bank account.
 */
@Getter
@Setter
@Entity
@Table(name = "transactions")
@NoArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "transaction_type")
    private String transactionType;

    @NotNull
    @Column(name = "amount")
    private double amount;

    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    /**
     * Represents a financial transaction entity in the system.
     * This entity is used to store transaction details related to a specific bank account.
     */
    public TransactionEntity(String transactionType, long accountId, double amount) {
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.amount = amount;
    }
}