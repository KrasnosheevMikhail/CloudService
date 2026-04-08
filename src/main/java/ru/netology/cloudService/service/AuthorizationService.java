package ru.netology.cloudService.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.netology.cloudService.config.TokenProvider;
import ru.netology.cloudService.model.AuthorizationRequest;
import ru.netology.cloudService.model.AuthorizationResponse;
import ru.netology.cloudService.repository.AuthorizationRepository;

@Service
@Slf4j
@AllArgsConstructor
public class AuthorizationService {
    private AuthorizationRepository authorizationRepository;
    private UserService userService;
    private TokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;

    public AuthorizationResponse login(AuthorizationRequest authorizationRequest) {
        final String username = authorizationRequest.getLogin();
        final String password = authorizationRequest.getPassword();
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = tokenProvider.generateToken(userDetails);
        authorizationRepository.putTokenAndUserName(token, username);
        log.info("User {} is authorized", username);
        return new AuthorizationResponse(token);
    }

    public void logout(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        final String username = authorizationRepository.getUserNameByToken(authToken);
        log.info("User {} logout", username);
        authorizationRepository.removeTokenAndUserNameByToken(authToken);

    }
}