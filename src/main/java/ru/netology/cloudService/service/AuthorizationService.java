package ru.netology.cloudService.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.exception.UnauthorizedException;
import ru.netology.cloudService.repository.AuthorizationRepository;
import ru.netology.cloudService.repository.UsersRepository;


@Service
@AllArgsConstructor
public class AuthorizationService {

    private final AuthorizationRepository authorizationRepository;
    private final UsersRepository usersRepository;

    public Users getCurrentUser(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        String username = authorizationRepository.getUserNameByToken(authToken);
        if (username == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return usersRepository.findByLogin(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
