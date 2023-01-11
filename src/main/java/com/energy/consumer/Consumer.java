package com.energy.consumer;

import com.energy.controllers.WebSocketController;
import com.energy.entities.Device;
import com.energy.entities.Measurement;
import com.energy.logger.MyLogger;
import com.energy.repositories.MeasurementRepository;
import com.energy.services.MeasurementService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static String QUEUE = "MyFirstQueue";
    private static final Logger LOGGER = MyLogger.getInstance();

    public void start(WebSocketController webSocketController, MeasurementService measurementService) throws IOException, TimeoutException {

        ConnectionFactory factory = getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE, false, false, false, null);
        LOGGER.info("Consumer started");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            LOGGER.info("Received '" + message + "'");
            JSONObject jsonObject = new JSONObject(message);
            measurementService.createAndSaveMeasurementFromJson(jsonObject);

            if(checkLimitExceeded(measurementService,jsonObject.getLong("deviceId"))){
                LOGGER.info("Limit exceeded");
                Device device = measurementService.getDeviceById(jsonObject.getLong("deviceId"));
                webSocketController.send(device);
            }
        };
        channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> { });
    }


    public static ConnectionFactory getConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("goose.rmq2.cloudamqp.com");
        factory.setUsername("zokgyxjs");
        factory.setVirtualHost("zokgyxjs");
        factory.setPassword("VPPKIuX_Y3lsgjLp6-zb_omtbBp77OJ9");
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(30000);

        return factory;
    }

    public boolean checkLimitExceeded(MeasurementService measurementService, Long deviceId){
        List<Measurement> measurements = measurementService.getLastSixMeasurements(deviceId);
        Device device = measurementService.getDeviceById(deviceId);
        Measurement lastMeasurement = measurements.get(measurements.size()-1);
       /*
        for(Measurement measurement : measurements){
            System.out.println(measurement.getEnergy());
        }*/
        return (measurements.get(0).getEnergy() - lastMeasurement.getEnergy()) > device.getMaxConsumption();
    }
}
