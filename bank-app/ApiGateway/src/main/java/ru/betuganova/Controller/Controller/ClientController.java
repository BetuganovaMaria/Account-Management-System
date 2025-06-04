package ru.betuganova.Controller.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.betuganova.Controller.Dto.BankAccountDto;
import ru.betuganova.Controller.Dto.UserDto;
import ru.betuganova.Controller.Mapper.BankAccountDtoMapper;
import ru.betuganova.Controller.Mapper.TransactionDtoMapper;
import ru.betuganova.Controller.Mapper.UserDtoMapper;
import ru.betuganova.Service.Service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final UserDtoMapper userDtoMapper;
    private final BankAccountDtoMapper bankAccountDtoMapper;
    private final TransactionDtoMapper transactionDtoMapper;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
        this.userDtoMapper = new UserDtoMapper();
        this.bankAccountDtoMapper = new BankAccountDtoMapper();
        this.transactionDtoMapper = new TransactionDtoMapper();
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo(@RequestHeader("Authorization") String authHeader) {
        UserDto user = userDtoMapper.toDto(clientService.getInfo(authHeader));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/bank-accounts")
    public ResponseEntity<List<BankAccountDto>> getBankAccounts(@RequestHeader("Authorization") String authHeader) {
        String login = getInfo(authHeader).getBody().getLogin();
        List<BankAccountDto> bankAccounts = clientService.getBankAccounts(authHeader, login)
                .stream()
                .map(bankAccountDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bankAccounts, HttpStatus.OK);
    }

    @GetMapping("/bank-accounts/{id}")
    public ResponseEntity<BankAccountDto> getBankAccountById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long id) {

        String login = getInfo(authHeader).getBody().getLogin();
        BankAccountDto bankAccountDto = bankAccountDtoMapper.toDto(clientService.getBankAccountById(authHeader, login, id));
        return new ResponseEntity<>(bankAccountDto, HttpStatus.OK);
    }

    @PostMapping("/add-friend")
    public ResponseEntity<?> addFriend(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String friendLogin) {

        clientService.addFriend(authHeader, friendLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-friend")
    public ResponseEntity<?> deleteFriend(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String friendLogin) {

        clientService.deleteFriend(authHeader, friendLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{accountId}/replenishment")
    public ResponseEntity<Double> replenish(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam double amount) {

        double newBalance = clientService.replenish(authHeader, id, amount);
        return new ResponseEntity<>(newBalance, HttpStatus.OK);
    }

    @PostMapping("/{accountId}/withdrawal")
    public ResponseEntity<Double> withdraw(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam double amount) {

        double newBalance = clientService.withdraw(authHeader, id, amount);
        return new ResponseEntity<>(newBalance, HttpStatus.OK);
    }

    @PostMapping("/{accountIdFrom}/transfer/{accountIdTo}")
    public ResponseEntity<?> transfer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long accountIdFrom,
            @PathVariable Long accountIdTo,
            @RequestParam Double amount) {

        clientService.transfer(authHeader, accountIdFrom, accountIdTo, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        clientService.logout(authHeader);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
