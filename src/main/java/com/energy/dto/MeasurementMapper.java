package com.energy.dto;

import com.energy.entities.Measurement;

public class MeasurementMapper {
    private static volatile MeasurementMapper measurementMapper = null;

    private MeasurementMapper() {}

    public static MeasurementMapper getInstance() {
        if(measurementMapper == null) {
            measurementMapper = new MeasurementMapper();
        }
        return measurementMapper;
    }

    public MeasurementDTO toDTO(Measurement measurement) {
        return MeasurementDTO.builder()
                .date(measurement.getDate())
                .time(measurement.getTime())
                .energy(measurement.getEnergy())
                .build();
    }

    public Measurement toEntity(MeasurementDTO measurementDTO) {
        return Measurement.builder()
                .date(measurementDTO.getDate())
                .time(measurementDTO.getTime())
                .energy(measurementDTO.getEnergy())
                .build();
    }
}
