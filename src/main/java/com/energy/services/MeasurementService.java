package com.energy.services;

import com.energy.consumer.Consumer;
import com.energy.dto.MeasurementDTO;
import com.energy.entities.Device;
import com.energy.entities.Measurement;
import com.energy.logger.MyLogger;
import com.energy.repositories.MeasurementRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
@Transactional
public class MeasurementService {
    private static final Logger LOGGER = MyLogger.getInstance();
    private final MeasurementRepository measurementRepository;
    private final DeviceService deviceService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, DeviceService deviceService) throws IOException, TimeoutException {
        this.measurementRepository = measurementRepository;
        this.deviceService = deviceService;
    }

    public void delete(Measurement measurement) {
        LOGGER.info("Deleting measurement");
        measurementRepository.delete(measurement);
    }

    public void addMeasurement(Long deviceId, Measurement measurement) {
        Device device = deviceService.findById(deviceId);
        if(measurementRepository.findByDeviceAndTimeAndDate(device, measurement.getTime(), measurement.getDate()) != null) {
            LOGGER.info("Measurement already exists");
            return;
        }
        device.getMeasurements().add(measurement);
        measurement.setDevice(device);
        measurementRepository.save(measurement);
        deviceService.save(device);
    }

    public List<Measurement> getMeasurements(Long deviceId, Date date) {
        Device device = deviceService.findById(deviceId);
        return measurementRepository.findByDeviceAndDate(device, date);
    }

    public void createAndSaveMeasurementFromJson(JSONObject measurementJson){
        Device device = deviceService.findById(measurementJson.getLong("deviceId"));
        Measurement measurement = Measurement.builder()
                .device(device)
                .time(LocalTime.parse(measurementJson.getString("time")))
                .date(Date.valueOf(measurementJson.getString("date")))
                .energy(measurementJson.getDouble("energy"))
                .build();
        device.getMeasurements().add(measurement);
        LOGGER.info("Saving measurement");
        measurementRepository.save(measurement);
    }

    public List<Measurement> getLastSixMeasurements(Long deviceId) {
        Device device = deviceService.findById(deviceId);
        return measurementRepository.findTop6ByDeviceOrderByDateDescTimeDesc(device);
    }

    public Device getDeviceById(Long id){
        return deviceService.findById(id);
    }
}
