package ru.netology.cloudService.entity;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AutorizationRepository {
    private final Map<String, String> tokenAndUserNames = new ConcurrentHashMap<>();

    void putTokenAndUserName(String token, String username) {
        tokenAndUserNames.put(token, username);
    }

    public String getUserNameByToken(String token) {
        return tokenAndUserNames.get(token);
    }

    void removeTokenAndUserNameByToken(String token) {
        tokenAndUserNames.remove(token);
    }
}
