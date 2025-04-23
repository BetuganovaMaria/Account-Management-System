package ru.betuganova.Service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.betuganova.Entity.UserEntity;
import ru.betuganova.Exception.NegativeBalanceException;
import ru.betuganova.Mapper.BankAccountMapper;
import ru.betuganova.Mapper.TransactionMapper;
import ru.betuganova.Mapper.UserMapper;
import ru.betuganova.Entity.BankAccountEntity;
import ru.betuganova.Model.BankAccount;
import ru.betuganova.Model.Transaction;
import ru.betuganova.Model.TransactionType;
import ru.betuganova.Model.User;
import ru.betuganova.Repository.BankAccountRepository;
import ru.betuganova.Repository.UserRepository;
import ru.betuganova.Service.CurrentUserManager.CurrentUserManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link BankAccountService} interface.
 * Provides methods for managing bank accounts, including creation, balance retrieval,
 * withdrawals, deposits, and transfers.
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CurrentUserManager currentUserManager;
    private final UserMapper userMapper;
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;

    /**
     * Constructs a BankAccountServiceImpl with the required repositories and user manager.
     *
     * @param currentUserManager    Manages the currently authenticated user.
     */
    @Autowired
    public BankAccountServiceImpl(CurrentUserManager currentUserManager,
                                  UserRepository userRepository,
                                  BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.currentUserManager = currentUserManager;
        this.userMapper = new UserMapper(userRepository);
        this.bankAccountMapper = new BankAccountMapper();
        this.transactionMapper = new TransactionMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public long createBankAccount(double balance) throws NoSuchElementException {
        String currentUserLogin = currentUserManager.getCurrentUser().getLogin();
        UserEntity user = userRepository.findByLogin(currentUserLogin);

        if (user == null) {
            throw new NoSuchElementException("User with login: '" + currentUserLogin + "' wasn't found");
        }

        BankAccountEntity bankAccount = new BankAccountEntity(balance, user.getId());
        bankAccountRepository.save(bankAccount);

        return bankAccount.getId();
    }

    /**
     * {@inheritDoc}
     */
    public double getBalance(long accountId) throws NoSuchElementException {
        Long userId = userRepository.findByLogin(currentUserManager.getCurrentUser().getLogin()).getId();
        BankAccountEntity bankAccount = bankAccountRepository.findByIdAndUserId(accountId, userId);

        if (bankAccount == null) {
            throw new NoSuchElementException("Bank account with id: '" + accountId + "' wasn't found");
        }

        return bankAccount.getBalance();
    }

    /**
     * {@inheritDoc}
     */
    public List<BankAccount> getAccountsByUserId(long id) throws NoSuchElementException {
        return userRepository.findById(id)
                .map(user -> bankAccountRepository.findByUserId(user.getId()))
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"))
                .stream()
                .map(bankAccountMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public List<BankAccount> getAccounts() {
        return bankAccountRepository.findAll()
                .stream()
                .map(bankAccountMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public double withdraw(long accountId, double amount) throws NoSuchElementException, NegativeBalanceException {
        Long userId = userRepository.findByLogin(currentUserManager.getCurrentUser().getLogin()).getId();
        BankAccountEntity bankAccount = bankAccountRepository.findByIdAndUserId(accountId, userId);

        if (bankAccount == null) {
            throw new NoSuchElementException("Bank account with id: '" + accountId + "' wasn't found");
        }

        double newBalance = bankAccount.getBalance() - amount;
        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount);

        if (newBalance < 0) {
            throw new NegativeBalanceException("Amount is more than current balance");
        }

        Transaction transactionDTO = new Transaction(TransactionType.WITHDRAWAL, accountId, amount);
        bankAccount.addTransaction(transactionMapper.toEntity(transactionDTO));
        bankAccountRepository.save(bankAccount);

        return newBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public double replenish(long accountId, double amount) throws NoSuchElementException {
        Long userId = userRepository.findByLogin(currentUserManager.getCurrentUser().getLogin()).getId();
        BankAccountEntity bankAccount = bankAccountRepository.findByIdAndUserId(accountId, userId);

        if (bankAccount == null) {
            throw new NoSuchElementException("Bank account with id: '" + accountId + "' wasn't found");
        }

        double newBalance = bankAccount.getBalance() + amount;
        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount);

        Transaction transactionDTO = new Transaction(TransactionType.REPLENISHMENT, accountId, amount);
        bankAccount.addTransaction(transactionMapper.toEntity(transactionDTO));
        bankAccountRepository.save(bankAccount);

        return newBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void transferTo(long accountIdFrom, long accountIdTo, double amount)
            throws NoSuchElementException, NegativeBalanceException {

        User currentUser = currentUserManager.getCurrentUser();

        BankAccountEntity bankAccountTo = bankAccountRepository.findByAccountId(accountIdTo);
        Long userId = userRepository.findByLogin(currentUserManager.getCurrentUser().getLogin()).getId();
        BankAccountEntity bankAccountFrom = bankAccountRepository.findByIdAndUserId(accountIdFrom, userId);

        if (bankAccountFrom == null) {
            throw new NoSuchElementException("Bank account with id: '" + accountIdFrom + "' wasn't found");
        }
        if (bankAccountTo == null) {
            throw new NoSuchElementException("Bank account with id: '" + accountIdTo + "' wasn't found");
        }

        List<UserEntity> friends = userRepository.findFriendsByUserLogin(userMapper.toEntity(currentUser).getLogin());
        UserEntity friend = friends
                .stream()
                .filter(f -> f.getId().equals(bankAccountTo.getUserId()))
                .findFirst()
                .orElse(null);

        double commissionFreePercent;

        if (Objects.equals(bankAccountFrom.getId(), bankAccountTo.getUserId())) {
            commissionFreePercent = 1;

        } else if (friend != null) {
            commissionFreePercent = 0.97;

        } else {
            commissionFreePercent = 0.9;
        }

        double balanceFrom = bankAccountFrom.getBalance() - amount;
        double balanceTo = bankAccountTo.getBalance() + amount * commissionFreePercent;

        if (balanceFrom < 0) {
            throw new NegativeBalanceException("Amount for transfer is more than current balance");
        }

        Transaction transactionFrom = new Transaction(TransactionType.TRANSFER_FROM, accountIdFrom, amount);
        Transaction transactionTo = new Transaction(TransactionType.TRANSFER_TO, accountIdTo, commissionFreePercent * amount);

        bankAccountFrom.setBalance(balanceFrom);
        bankAccountFrom.addTransaction(transactionMapper.toEntity(transactionFrom));
        bankAccountRepository.save(bankAccountFrom);

        bankAccountTo.setBalance(balanceTo);
        bankAccountTo.addTransaction(transactionMapper.toEntity(transactionTo));
        bankAccountRepository.save(bankAccountTo);
    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> getTransactionsByTypeAndAccountId(String type, long accountId) throws NoSuchElementException {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found"))
                .getTransactionHistory().stream()
                .filter(t -> type == null || t.getTransactionType().equalsIgnoreCase(type))
                .map(transactionMapper::toModel)
                .collect(Collectors.toList());
    }
}
