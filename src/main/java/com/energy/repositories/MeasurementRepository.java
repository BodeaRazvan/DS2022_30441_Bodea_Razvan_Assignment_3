package com.energy.repositories;

import com.energy.entities.Device;
import com.energy.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByDeviceAndDate(Device device, Date date);
    Measurement findByDeviceAndTimeAndDate(Device device, LocalTime time, Date date);

    List<Measurement> findTop6ByOrderByDateDescTimeDesc();

    List<Measurement> findTop6ByDeviceOrderByDateDescTimeDesc(Device device);
}
