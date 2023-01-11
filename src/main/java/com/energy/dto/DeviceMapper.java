package com.energy.dto;

import com.energy.entities.Device;

public class DeviceMapper {
    private static volatile DeviceMapper deviceMapper = null;

    private DeviceMapper() {}

    public static DeviceMapper getInstance() {
        if(deviceMapper == null) {
            deviceMapper = new DeviceMapper();
        }
        return deviceMapper;
    }

    public Device convertFromDTO(CreateDeviceDTO createDeviceDTO){
        return Device.builder()
                .name(createDeviceDTO.getName())
                .description(createDeviceDTO.getDescription())
                .address(createDeviceDTO.getAddress())
                .user(null)
                .measurements(null)
                .maxConsumption(createDeviceDTO.getMaxConsumption())
                .build();
    }
}
