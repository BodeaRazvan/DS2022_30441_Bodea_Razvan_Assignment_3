package com.energy.security.jwt;

import com.energy.entities.User;

public interface AuthenticationInterface {
    String login(String username, String password);
    User findByToken(String token);
    void logout(User user);
}
