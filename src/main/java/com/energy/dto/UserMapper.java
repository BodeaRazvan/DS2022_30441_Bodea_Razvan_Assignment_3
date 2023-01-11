package com.energy.dto;

import com.energy.entities.User;

public class UserMapper {
    private static volatile UserMapper userMapper = null;

    private UserMapper() {}

    public static UserMapper getInstance() {
        if(userMapper == null) {
            userMapper = new UserMapper();
        }
        return userMapper;
    }

    public User convertFromRegisterDTO(RegisterDTO registerDTO){
        return User.builder()
                .address(registerDTO.getAddress())
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword1())
                .username(registerDTO.getUsername())
                .role("USER")
                .build();
    }

    public RegisterDTO convertToRegisterDTO(User user){
        return RegisterDTO.builder()
                .address(user.getAddress())
                .email(user.getEmail())
                .password1(user.getPassword())
                .password2(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    public UserDTO convUserToDTO(User user){
        return UserDTO.builder()
                .address(user.getAddress())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public User convertFromDTO(CreateUserDTO createUserDTO){
        return User.builder()
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword1())
                .address(createUserDTO.getAddress())
                .role(createUserDTO.getRole())
                .build();
    }
}
