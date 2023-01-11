package com.energy.services;

import com.energy.entities.Device;
import com.energy.entities.User;
import com.energy.logger.MyLogger;
import com.energy.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeviceService {
    private static final Logger LOGGER = MyLogger.getInstance();
    private final DeviceRepository deviceRepository;
    private final UserService userService;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserService userService) {
        this.deviceRepository = deviceRepository;
        this.userService = userService;
    }

    public Device findById(Long id) {
        LOGGER.info("Finding device with id {}", id);
        return deviceRepository.findById(id).orElse(null);
    }

    public void save(Device device){
        LOGGER.info("Saving device");
        deviceRepository.save(device);
    }

    public List<Device> findAll(){
        LOGGER.info("Finding all devices");
        return deviceRepository.findAll();
    }

    public String linkDeviceToUser(Long deviceId, Long userId){
        Device device = findById(deviceId);
        User user = userService.findById(userId);
        if(device ==null || user == null)
            return "Device or user not found";
        user.getDevices().add(device);
        device.setUser(user);
        save(device);
        userService.save(user);
        return "Success";
    }

    public void delete(Long id) {
        LOGGER.info("Deleting device");
        deviceRepository.deleteById(id);
    }

    public String unlinkDeviceFromUser(Long deviceId){
        Device device = findById(deviceId);
        if(device ==null)
            return "Device not found";
        User user = userService.findById(device.getUser().getId());
        if(user == null)
            return "User not found";
        user.getDevices().remove(device);
        device.setUser(null);
        device.getMeasurements().clear();
        save(device);
        userService.save(user);
        return "Success";
    }

    public List<Device> getUserDevices(User user){
        return deviceRepository.findDevicesByUser(user);
    }
}
