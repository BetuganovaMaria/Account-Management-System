package ru.betuganova.Controller.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.betuganova.Controller.Dto.BankAccountDto;
import ru.betuganova.Controller.Dto.RegisterUserRequest;
import ru.betuganova.Controller.Dto.TransactionDto;
import ru.betuganova.Controller.Dto.UserDto;
import ru.betuganova.Controller.Mapper.BankAccountDtoMapper;
import ru.betuganova.Controller.Mapper.TransactionDtoMapper;
import ru.betuganova.Controller.Mapper.UserDtoMapper;
import ru.betuganova.Service.Service.AdminService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;
    private final BankAccountDtoMapper bankAccountDtoMapper;
    private final TransactionDtoMapper transactionDtoMapper;

    @Autowired
    public AdminController(AdminService adminService,
                           PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = new UserDtoMapper();
        this.bankAccountDtoMapper = new BankAccountDtoMapper();
        this.transactionDtoMapper = new TransactionDtoMapper();
    }

    @PostMapping("/register-client")
    public ResponseEntity<String> registerClient(@RequestBody RegisterUserRequest registerUserRequest,
                                                 @RequestHeader("Authorization") String authHeader)
            throws IllegalArgumentException {

        adminService.registerClient(
                authHeader,
                userDtoMapper.toModel(registerUserRequest.getUser()),
                passwordEncoder.encode(registerUserRequest.getPassword()));

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterUserRequest registerUserRequest,
                                                @RequestHeader("Authorization") String authHeader)
            throws IllegalArgumentException {

        adminService.registerAdmin(
                authHeader,
                userDtoMapper.toModel(registerUserRequest.getUser()),
                passwordEncoder.encode(registerUserRequest.getPassword()));


        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "hairColor", required = false) String hairColor,
            @RequestParam(name = "gender", required = false) Integer gender) {

        List<UserDto> users = adminService.getUsersByHairColorAndGender(authHeader, hairColor, gender)
                .stream()
                .map(userDtoMapper::toDto)
                .toList();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UserDto> getUserInfoById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        UserDto userDto = userDtoMapper.toDto(adminService.getUserInfoById(authHeader, id));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/bank-accounts")
    public ResponseEntity<List<BankAccountDto>> getBankAccounts(
            @RequestHeader("Authorization") String authHeader) {

        List<BankAccountDto> bankAccounts = adminService.getBankAccounts(authHeader)
                .stream()
                .map(bankAccountDtoMapper::toDto)
                .toList();

        return new ResponseEntity<>(bankAccounts, HttpStatus.OK);
    }

    @GetMapping("/bank-accounts/{id}")
    public ResponseEntity<List<BankAccountDto>> getAccountsByUserId(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        List<BankAccountDto> bankAccounts = adminService.getBankAccountsById(authHeader, id)
                .stream()
                .map(bankAccountDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bankAccounts, HttpStatus.OK);
    }

    @GetMapping("/bank-accounts/transactions")
    public ResponseEntity<List<TransactionDto>> getBankAccountTransactions(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "accountId") Long accountId) {

        List<TransactionDto> transactions = adminService.getTransactionsByAccountId(authHeader, accountId)
                .stream()
                .map(transactionDtoMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        adminService.logout(authHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
