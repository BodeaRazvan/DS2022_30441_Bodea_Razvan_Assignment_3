package com.energy.controllers;

import com.energy.dto.*;
import com.energy.entities.Device;
import com.energy.entities.User;
import com.energy.logger.MyLogger;
import com.energy.services.DeviceService;
import com.energy.services.MeasurementService;
import com.energy.services.UserService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Controller
@RestController
@CrossOrigin
@RolesAllowed("ROLE_ADMIN")
public class AdminController {
    private final UserService userService;
    private final DeviceService deviceService;
    private final MeasurementService measurementService;

    private final UserMapper userMapper = UserMapper.getInstance();
    private final DeviceMapper deviceMapper = DeviceMapper.getInstance();
    private final MeasurementMapper measurementMapper = MeasurementMapper.getInstance();
    private static final Logger LOGGER = MyLogger.getInstance();

    @Autowired
    public AdminController(UserService userService, DeviceService deviceService, MeasurementService measurementService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.measurementService = measurementService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping(value="/createUser", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO createUserDTO){
        LOGGER.info("Request for creating user {}", createUserDTO.toString());
        if(!createUserDTO.getPassword1().equals(createUserDTO.getPassword2())){
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        userService.save(userMapper.convertFromDTO(createUserDTO));
        return ResponseEntity.ok("User created");
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable  Long id){
        LOGGER.info("Deleting user with username {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    @PostMapping(value="/createDevice",consumes = "application/json")
    public ResponseEntity<String> createDevice(@RequestBody CreateDeviceDTO createDeviceDTO){
        LOGGER.info("Request for creating device {}", createDeviceDTO.toString());
        if(createDeviceDTO.getName().equals("")||
                createDeviceDTO.getAddress().equals("")||
                createDeviceDTO.getDescription().equals("")||
                createDeviceDTO.getMaxConsumption()<0){
            LOGGER.info("Empty fields");
            return ResponseEntity.badRequest().body("Empty fields");
        }
        deviceService.save(deviceMapper.convertFromDTO(createDeviceDTO));
        return ResponseEntity.ok("Device created");
    }

    @GetMapping("/getDevices")
    public ResponseEntity<List<Device>> getDevices(){
        List<Device> devices = deviceService.findAll();
        return ResponseEntity.ok(devices);
    }

    @RequestMapping("/linkDevice/{deviceId}/{userId}")
    public ResponseEntity<String> linkDeviceToUser(@PathVariable Long deviceId, @PathVariable Long userId){
        LOGGER.info("Request to link device and user with ids {} {}",deviceId,userId);
        String response = deviceService.linkDeviceToUser(deviceId,userId);
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/unlinkDevice/{deviceId}")
    public ResponseEntity<String> unlinkDeviceFromUser(@PathVariable Long deviceId){
        LOGGER.info("Request to unlink device and user with ids {}",deviceId);
        String response = deviceService.unlinkDeviceFromUser(deviceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/editUser")
    public ResponseEntity<String> editUser(@RequestBody EditUserDTO editUserDTO){
        LOGGER.info("Request to edit user {}", editUserDTO.toString());
        userService.editUser(editUserDTO);
        return ResponseEntity.ok("User edited");
    }

    @DeleteMapping("/deleteDevice/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable Long id){
        LOGGER.info("Deleting device with id {}", id);
        deviceService.delete(id);
        return ResponseEntity.ok("Device deleted");
    }

    @PostMapping("/addMeasurement/{deviceId}")
    public ResponseEntity<String> addMeasurement(@RequestBody MeasurementDTO measurementDTO, @PathVariable Long deviceId){
        LOGGER.info("Request to add measurement {}", measurementDTO.toString());
        measurementService.addMeasurement(deviceId, measurementMapper.toEntity(measurementDTO));
        return ResponseEntity.ok("Measurement added");
    }
}
