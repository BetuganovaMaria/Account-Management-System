package ru.betuganova.Service.AccountService;

import ru.betuganova.Exception.NegativeBalanceException;
import ru.betuganova.Model.BankAccount;
import ru.betuganova.Model.Transaction;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service interface for managing bank accounts.
 */
public interface BankAccountService {
    /**
     * Creates a new bank account for the current user.
     *
     * @param balance Initial balance of the bank account.
     * @return The ID of the created bank account.
     * @throws NoSuchElementException If the current user is not found.
     */
    long createBankAccount(double balance);

    /**
     * Retrieves the balance of a specified bank account.
     *
     * @param accountId The ID of the bank account.
     * @return The current balance of the account.
     * @throws NoSuchElementException If the bank account is not found.
     */
    double getBalance(long accountId);

    /**
     * Retrieves a list of bank accounts associated with a specific user ID.
     *
     * @param id the ID of the user whose bank accounts are to be retrieved
     * @return a list of {@link BankAccount} objects belonging to the specified user
     */
    List<BankAccount> getAccountsByUserId(long id);

    /**
     * Retrieves a list of all bank accounts in the system.
     *
     * @return a list of {@link BankAccount} objects representing all accounts
     */
    List<BankAccount> getAccounts();

    /**
     * Withdraws an amount from a bank account.
     *
     * @param accountId The ID of the bank account.
     * @param amount    The amount to withdraw.
     * @return The updated balance after withdrawal.
     * @throws NoSuchElementException   If the bank account is not found.
     * @throws NegativeBalanceException If the sender's account balance is insufficient.
     */
    double withdraw(long accountId, double amount);

    /**
     * Deposits an amount into a bank account.
     *
     * @param accountId The ID of the bank account.
     * @param amount    The amount to deposit.
     * @return The updated balance after the deposit.
     * @throws NoSuchElementException If the bank account is not found.
     */
    double replenish(long accountId, double amount);

    /**
     * Transfers an amount from one bank account to another.
     *
     * @param accountIdFrom The ID of the sender's bank account.
     * @param accountIdTo   The ID of the recipient's bank account.
     * @param amount        The amount to transfer.
     * @throws NoSuchElementException   If either bank account is not found.
     * @throws NegativeBalanceException If the sender's account balance is insufficient.
     */
    void transferTo(long accountIdFrom, long accountIdTo, double amount);

    /**
     * Retrieves a list of transactions filtered by transaction type and account ID.
     *
     * @param type      the type of the transaction (e.g., "WITHDRAWAL", "REPLENISHMENT", "TRANSFER_TO")
     * @param accountId the ID of the bank account
     * @return a list of {@link Transaction} objects matching the specified criteria
     */
    List<Transaction> getTransactionsByTypeAndAccountId(String type, long accountId);
}
