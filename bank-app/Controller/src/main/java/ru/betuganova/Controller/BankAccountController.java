package ru.betuganova.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.betuganova.Dto.BankAccountDto;
import ru.betuganova.Dto.TransactionDto;
import ru.betuganova.Mapper.BankAccountDtoMapper;
import ru.betuganova.Mapper.TransactionDtoMapper;
import ru.betuganova.Service.AccountService.BankAccountService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank-accounts")
@Tag(name = "Bank Account Controller", description = "Operations related to bank accounts and transactions")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankAccountDtoMapper bankAccountDtoMapper;
    private final TransactionDtoMapper transactionDtoMapper;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, BankAccountDtoMapper bankAccountDtoMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountDtoMapper = new BankAccountDtoMapper();
        this.transactionDtoMapper = new TransactionDtoMapper();
    }

    @Operation(
            summary = "Create a bank account",
            description = "Creates a new bank account with the specified initial balance"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank account successfully created"),
            @ApiResponse(responseCode = "404", description = "User not found when creating bank account")
    })
    @PostMapping
    public ResponseEntity<?> createBankAccount(@RequestParam Double balance) {
        bankAccountService.createBankAccount(balance);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get balance",
            description = "Returns the balance of a specific bank account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Bank account not found")
    })
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long accountId) {
        double balance = bankAccountService.getBalance(accountId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @Operation(
            summary = "Replenish balance",
            description = "Replenishes the balance of a specific bank account by the given amount"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Replenishment successful"),
            @ApiResponse(responseCode = "404", description = "Bank account not found"),
    })
    @PostMapping("/{accountId}/replenishment")
    public ResponseEntity<Double> replenish(
            @PathVariable Long accountId,
            @RequestParam double amount
    ) {
        double newBalance = bankAccountService.replenish(accountId, amount);
        return new ResponseEntity<>(newBalance, HttpStatus.OK);
    }

    @Operation(
            summary = "Withdraw money",
            description = "Withdraws the given amount from the specified bank account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "404", description = "Bank account not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient funds")
    })
    @PostMapping("/{accountId}/withdrawal")
    public ResponseEntity<Double> withdraw(
            @PathVariable Long accountId,
            @RequestParam Double amount
    ) {
        double newBalance = bankAccountService.withdraw(accountId, amount);
        return new ResponseEntity<>(newBalance, HttpStatus.OK);
    }

    @Operation(
            summary = "Transfer money",
            description = "Transfers money from one bank account to another"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "404", description = "One or both bank accounts not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient funds or conflict during transfer")
    })
    @PostMapping("/{accountIdFrom}/transfer/{accountIdTo}")
    public ResponseEntity<Void> transfer(
            @PathVariable Long accountIdFrom,
            @PathVariable Long accountIdTo,
            @RequestParam Double amount
    ) {
        bankAccountService.transferTo(accountIdFrom, accountIdTo, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get accounts by user ID", description = "Returns all bank accounts belonging to a specific user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/info/{id}")
    public ResponseEntity<List<BankAccountDto>> getAccountsByUserId(@PathVariable Long id) {
        List<BankAccountDto> accounts = bankAccountService.getAccountsByUserId(id)
                .stream()
                .map(bankAccountDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @Operation(summary = "Get all accounts", description = "Returns all bank accounts in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All accounts retrieved successfully")
    })
    @GetMapping("/info")
    public ResponseEntity<List<BankAccountDto>> getAccounts() {
        List<BankAccountDto> accounts = bankAccountService.getAccounts()
                .stream()
                .map(bankAccountDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @Operation(summary = "Get transactions", description = "Returns all transactions filtered by type and/or account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No transactions found")
    })
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "accountId", required = false) Long accountId
    ) {
        List<TransactionDto> transactions = bankAccountService.getTransactionsByTypeAndAccountId(type, (accountId != null) ? accountId : 0)
                .stream()
                .map(transactionDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
