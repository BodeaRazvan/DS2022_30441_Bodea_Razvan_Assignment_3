package com.energy.controllers;

import com.energy.entities.Device;
import com.energy.entities.Measurement;
import com.energy.entities.User;
import com.energy.logger.MyLogger;
import com.energy.services.DeviceService;
import com.energy.services.MeasurementService;
import com.energy.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final DeviceService deviceService;
    private final MeasurementService measurementService;
    private static final Logger LOGGER = MyLogger.getInstance();

    @Autowired
    public UserController(UserService userService, DeviceService deviceService, MeasurementService measurementService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.measurementService = measurementService;
    }

    @GetMapping("/getAdmins")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.findAll();
        List<User> admins = users.stream()
                .filter(u -> u.getRole().equals("ADMIN")).toList();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/getMyDevices")
    public ResponseEntity<List<Device>> getMyDevices(){
        User user = LoginController.getCurrentUser();
        return ResponseEntity.ok(deviceService.getUserDevices(user));
    }

    @GetMapping("/getMeasurements/{deviceId}/{date}")
    public ResponseEntity<List<Measurement>> getMeasurements(@PathVariable Long deviceId, @PathVariable Date date){
        LOGGER.info("Getting measurements for device with id: " + deviceId + " and date: " + date);
        return ResponseEntity.ok(measurementService.getMeasurements(deviceId, date));
    }

    @GetMapping("/findCurrentUser/{userId}")
    public ResponseEntity<Boolean> findCurrentUser(@PathVariable Long userId){
        if(LoginController.getCurrentUser().getId().equals(userId)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
