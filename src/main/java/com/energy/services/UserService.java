package com.energy.services;

import com.energy.dto.EditUserDTO;
import com.energy.entities.Device;
import com.energy.entities.User;
import com.energy.logger.MyLogger;
import com.energy.repositories.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private static final Logger LOGGER = MyLogger.getInstance();
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        LOGGER.info("Finding all users");
        return userRepository.findAll();
    }

    public User save(User user) {
        LOGGER.info("Saving user");
        userRepository.save(user);
        return user;
    }

    public User findByUsername(String username) {
        LOGGER.info("Finding user with username {}", username);
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) {
        LOGGER.info("Finding user with id {}", id);
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        LOGGER.info("Finding user with email {}", email);
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with id {}", id);
        User user = findById(id);
        List<Device> devices = user.getDevices();
        for (Device device : devices) {
            device.setUser(null);
            device.getMeasurements().clear();
        }
        userRepository.deleteById(id);
    }

    public void deleteUserByUsername(String username) {
        LOGGER.info("Deleting user with username {}", username);
        userRepository.deleteByUsername(username);
    }

    public void editUser(EditUserDTO editUserDTO){
        User user = findById(editUserDTO.getId());
        user.setUsername(editUserDTO.getUsername());
        user.setEmail(editUserDTO.getEmail());
        user.setAddress(editUserDTO.getAddress());
        user.setRole(editUserDTO.getRole());
        save(user);
    }

}
