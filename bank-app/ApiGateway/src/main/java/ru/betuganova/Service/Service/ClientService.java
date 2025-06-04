package ru.betuganova.Service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.betuganova.Dao.Repository.UserRepository;
import ru.betuganova.Service.Mapper.BankAccountMapper;
import ru.betuganova.Service.Mapper.TransactionMapper;
import ru.betuganova.Service.Mapper.UserMapper;
import ru.betuganova.Service.Model.BankAccount;
import ru.betuganova.Service.Model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final UserMapper userMapper;
    private final BankAccountMapper bankAccountMapper;
    private final TransactionMapper transactionMapper;

    @Value("${main-app.url}")
    private String serviceUrl;

    @Autowired
    public ClientService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.userMapper = new UserMapper();
        this.bankAccountMapper = new BankAccountMapper();
        this.transactionMapper = new TransactionMapper();
    }

    @Transactional
    public User getInfo(String header) {
        String url = serviceUrl + "/users/info";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> body = response.getBody();
        if (body == null) return null;

        return userMapper.mapToUser(body);
    }

    @Transactional
    public List<BankAccount> getBankAccounts(String header, String login) {
        Long userId = userRepository.findByLogin(login).getId();
        String url = serviceUrl + "/bank-accounts/info/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Map<String, Object>> body = response.getBody();
        if (body == null) return null;

        return body.stream()
                .map(bankAccountMapper::mapToBankAccount)
                .collect(Collectors.toList());
    }

    @Transactional
    public BankAccount getBankAccountById(String header, String login, Long id) {
        Long userId = userRepository.findByLogin(login).getId();
        String url = serviceUrl + "/bank-accounts/info/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Map<String, Object>> body = response.getBody();
        if (body == null) return null;

        return body.stream()
                .filter(account -> {
                    Object userIdObj = account.get("id");
                    if (userIdObj instanceof Number) {
                        return ((Number) userIdObj).longValue() == id;
                    }
                    return false;
                })
                .map(bankAccountMapper::mapToBankAccount)
                .findFirst().orElse(null);
    }

    @Transactional
    public void addFriend(String header, String friendLogin) {
        String url = serviceUrl + "/users/add-friend";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("friendLogin", friendLogin);

        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                Void.class
        );
    }

    @Transactional
    public void deleteFriend(String header, String friendLogin) {
        String url = serviceUrl + "/users/delete-friend";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("friendLogin", friendLogin);

        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                request,
                Void.class
        );
    }

    @Transactional
    public double replenish(String header, Long id, double amount) {
        String url = serviceUrl + "/" + id + "/replenishment";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("amount", amount);

        ResponseEntity<Double> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                Double.class
        );

        return response.getBody();
    }

    @Transactional
    public double withdraw(String header, Long id, double amount) {
        String url = serviceUrl + "/" + id + "/withdrawal";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("amount", amount);

        ResponseEntity<Double> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                Double.class
        );

        return response.getBody();
    }

    @Transactional
    public void transfer(String header, Long idFrom, Long idTo, double amount) {
        String url = serviceUrl + "/" + idFrom + "/transfer/" + idTo;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(extractTokenFromHeader(header));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("amount", amount);

        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                Void.class
        );
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
