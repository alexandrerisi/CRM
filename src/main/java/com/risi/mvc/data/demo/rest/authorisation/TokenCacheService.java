package com.risi.mvc.data.demo.rest.authorisation;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Simulates the behaviour
 */
@Component
public class TokenCacheService {

    private Map<String, String> tokenUsername = new HashMap<>();
    private Map<String, String> usernameToken = new HashMap<>();

    synchronized void addToken(String jwtToken, String username) {
        tokenUsername.computeIfAbsent(jwtToken, token -> {
            usernameToken.putIfAbsent(username, token);
            return username;
        });
    }

    synchronized void deleteToken(String username) {
        if (usernameToken.containsKey(username)) {
            tokenUsername.remove(usernameToken.get(username));
            usernameToken.remove(username);
        }
    }

    boolean isValidToken(String token) {
        return tokenUsername.containsKey(token);
    }
}
