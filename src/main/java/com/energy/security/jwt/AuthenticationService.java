package com.energy.security.jwt;

import com.energy.entities.User;
import com.energy.repositories.UserRepository;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements AuthenticationInterface {
    TokenService tokenService;
    UserRepository userRepository;

    @Autowired
    public AuthenticationService(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public String login(final String username, final String password) {
        User user = userRepository.findByUsername(username);
        if(user==null){
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(password, user.getPassword())){
            return null;
        }
        ImmutableMap<String, String> userData = ImmutableMap.of("username", username, "id",String.valueOf(user.getId()));
        return tokenService.encode(userData);
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findByUsername(tokenService.decode(token).get("username"));
    }

    @Override
    public void logout(User user) {
        //forget token on frontend
    }

    public static User getAuthed() {
        try{
           return (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        }catch (Throwable t){
            return null;
        }
    }

}
