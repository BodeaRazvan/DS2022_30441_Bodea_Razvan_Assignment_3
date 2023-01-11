package com.energy.repositories;

import com.energy.entities.Device;
import com.energy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findDevicesByUser(User user);
}
