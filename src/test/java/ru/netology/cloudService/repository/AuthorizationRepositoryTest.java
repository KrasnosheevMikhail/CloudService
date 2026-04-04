package ru.netology.cloudService.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationRepositoryTest {

    private AuthorizationRepository authorizationRepository;
    private final Map<String, String> tokenAndUserNames = new ConcurrentHashMap<>();
    public static final String TOKEN_1 = "token1";
    public static final String TOKEN_2 = "token2";
    public static final String LOGIN_1 = "login1";
    public static final String LOGIN_2 = "login2";


    @BeforeEach
    void setUp() {
        authorizationRepository = new AuthorizationRepository();
        authorizationRepository.putTokenAndUserName(TOKEN_1, LOGIN_1);
        tokenAndUserNames.clear();
        tokenAndUserNames.put(TOKEN_1, LOGIN_1);
    }

    @Test
    void putTokenAndUsername() {
        String beforePut = authorizationRepository.getUserNameByToken(TOKEN_2);
        assertNull(beforePut);
        authorizationRepository.putTokenAndUserName(TOKEN_2, LOGIN_2);
        String afterPut = authorizationRepository.getUserNameByToken(TOKEN_2);
        assertEquals(LOGIN_2, afterPut);
    }

    @Test
    void getUserNameByToken() {
        assertEquals(tokenAndUserNames.get(TOKEN_1), authorizationRepository.getUserNameByToken(TOKEN_1));
    }

    @Test
    void removeTokenAndUsernameByToken() {
        String beforeRemove = authorizationRepository.getUserNameByToken(TOKEN_1);
        assertNotNull(beforeRemove);
        authorizationRepository.removeTokenAndUserNameByToken(TOKEN_1);
        String afterRemove = authorizationRepository.getUserNameByToken(TOKEN_1);
        assertNull(afterRemove);
    }
}

