package com.energy;

import com.energy.consumer.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class EnergyApplication {

    public static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(EnergyApplication.class, args);
    }

}
