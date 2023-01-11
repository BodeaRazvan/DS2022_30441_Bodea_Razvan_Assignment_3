package com.energy.controllers;

import com.energy.dto.RegisterDTO;
import com.energy.dto.UserMapper;
import com.energy.entities.User;
import com.energy.logger.MyLogger;
import com.energy.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@CrossOrigin
public class RegisterController {

    private static final Logger LOGGER = MyLogger.getInstance();
    @Autowired
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.getInstance();

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register user.
     * Method: POST
     * Body: RegisterDTO(JSON) : {username, password1, password2, email, address}
     * Responses: 200 - OK, 400 - Bad Request
     *
     * @param registerDTO the register dto(JSON)
     * @return the response entity
     */
    @RequestMapping(value ="/register",consumes = "application/json")
    public ResponseEntity<User> registerUser(@RequestBody RegisterDTO registerDTO){
        LOGGER.info("Request for registering user");
        if(registerDTO.getAddress().equals("")||
                registerDTO.getEmail().equals("")||
                registerDTO.getPassword1().equals("")||
                registerDTO.getPassword2().equals("")||
                registerDTO.getUsername().equals("")){
            LOGGER.info("Empty fields");
            return ResponseEntity.badRequest().body(null);
        }
        if(!registerDTO.getPassword1().equals(registerDTO.getPassword2())){
            LOGGER.info("Passwords do not match");
            return ResponseEntity.badRequest().body(null);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword= bCryptPasswordEncoder.encode(registerDTO.getPassword1());
        User user = userMapper.convertFromRegisterDTO(registerDTO);
        user.setPassword(encodedPassword);
        //set the list of user devices to null
        try{
            userService.save(user);
            LOGGER.info("User registered");
            return ResponseEntity.ok().body(user);
        }catch (Exception e){
            LOGGER.info("Register failed - User already exists");
            return ResponseEntity.badRequest().body(null);
        }
    }

}
