package ru.betuganova.Service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.betuganova.Dao.Entity.UserEntity;
import ru.betuganova.Dao.Repository.UserRepository;
import ru.betuganova.Service.Mapper.BankAccountMapper;
import ru.betuganova.Service.Mapper.TransactionMapper;
import ru.betuganova.Service.Mapper.UserMapper;
import ru.betuganova.Service.Model.BankAccount;
import ru.betuganova.Service.Model.Transaction;
import ru.betuganova.Service.Model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final UserMapper userMapper;
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;

    @Value("${main-app.url}")
    private String serviceUrl;

    @Autowired
    public AdminService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.userMapper = new UserMapper();
        this.bankAccountMapper = new BankAccountMapper();
        this.transactionMapper = new TransactionMapper();
    }

    @Transactional
    public void registerClient(String header, User user, String password) {
        String url = serviceUrl + "/users";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("login", user.getLogin())
                .queryParam("name", user.getName())
                .queryParam("age", user.getAge())
                .queryParam("gender", user.getGender())
                .queryParam("hairColor", user.getHairColor());


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                Void.class
        );

        UserEntity userEntity = new UserEntity(user.getLogin(), password, "CLIENT");
        userRepository.save(userEntity);
    }

    @Transactional
    public void registerAdmin(String header, User user, String password) {
        String url = serviceUrl + "/users";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("login", user.getLogin())
                .queryParam("name", user.getName())
                .queryParam("age", user.getAge())
                .queryParam("gender", user.getGender())
                .queryParam("hairColor", user.getHairColor());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                Void.class
        );

        UserEntity userEntity = new UserEntity(user.getLogin(), password, "ADMIN");
        userRepository.save(userEntity);
    }

    @Transactional
    public List<User> getUsersByHairColorAndGender(String header, String hairColor, Integer gender) {
        String url = serviceUrl + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(extractTokenFromHeader(header));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (hairColor != null) {
            builder.queryParam("hairColor", hairColor);
        }
        if (gender != null) {
            builder.queryParam("gender", gender);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> body = response.getBody();
        if (body == null) return Collections.emptyList();

        return body.stream()
                .map(userMapper::mapToUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public User getUserInfoById(String header, Long id) throws NoSuchElementException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> users = response.getBody();
        if (users == null) return null;

        User user = users.stream()
                .filter(userMap -> {
                    Object idObj = userMap.get("id");
                    return idObj instanceof Number && ((Number) idObj).longValue() == id;
                })
                .map(userMapper::mapToUser)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        return user;
    }

    @Transactional
    public List<BankAccount> getBankAccounts(String header) {
        String url = serviceUrl + "/bank-accounts/info/";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> body = response.getBody();
        if (body == null) return Collections.emptyList();

        return body.stream()
                .map(bankAccountMapper::mapToBankAccount)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BankAccount> getBankAccountsById(String header, Long id) {
        String url = serviceUrl + "/bank-accounts/info/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> body = response.getBody();
        if (body == null) return Collections.emptyList();

        return body.stream()
                .map(bankAccountMapper::mapToBankAccount)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Transaction> getTransactionsByAccountId(String header, Long accountId) {
        String url = serviceUrl + "/bank-accounts/transactions";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(extractTokenFromHeader(header));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accountId", accountId);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().stream()
                .map(transactionMapper::mapToTransaction)
                .collect(Collectors.toList());
    }

    @Transactional
    public void logout(String header) {
        String url = serviceUrl + "/users/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.postForEntity(url, request, Void.class);
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("No token found");
    }
}
