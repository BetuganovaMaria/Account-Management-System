package ru.betuganova.Service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.betuganova.Controller.Dto.AuthenticationRequest;
import ru.betuganova.Controller.Dto.AuthenticationResponse;
import ru.betuganova.Security.JwtProvider;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    @Value("${main-app.url}")
    private String userServiceUrl;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtProvider jwtProvider,
                                 RestTemplate restTemplate) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        String url = userServiceUrl + "/users/login?login=" + authenticationRequest.getLogin();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> form = new HashMap<>();
        form.put("login", authenticationRequest.getLogin());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(form, headers);
        restTemplate.postForEntity(url, request, Void.class);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getLogin(),
                        authenticationRequest.getPassword()
                )
        );
        return new AuthenticationResponse(jwtProvider.generateToken(authentication));
    }
}
