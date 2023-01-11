package com.energy.controllers;

import com.energy.dto.LoginDTO;
import com.energy.dto.TokenDTO;
import com.energy.entities.User;
import com.energy.logger.MyLogger;
import com.energy.security.jwt.AuthenticationService;
import com.energy.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@Controller
@RestController
@CrossOrigin
public class LoginController {
    private final AuthenticationService authenticationService;
    private static final Logger LOGGER = MyLogger.getInstance();
    private final UserService userService;

    @Autowired
    public LoginController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    public static User getCurrentUser() {
        return AuthenticationService.getAuthed();
    }

    /**
     * Login controller.
     * Method: POST
     * Responses: 200 - OK, 401 - Unauthorized, 403 - Forbidden, 404 - Not Found, 500 - Internal Server Error
     *
     * @param loginDTO the login dto(JSON) : { "username" : "username", "password" : "password" }
     * @return the response entity
     */
    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO){
        LOGGER.info("Login controller request login user");
        String token = authenticationService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if(token == null || token.isEmpty()){
            LOGGER.info("Login controller login user failed");
            return ResponseEntity.badRequest().build();
        }
        User user = userService.findByUsername(loginDTO.getUsername());

        return  new ResponseEntity<>(new TokenDTO(user, token), HttpStatus.OK);
    }
}
