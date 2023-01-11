package com.tutorialspoint;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String QUEUE = "MyFirstQueue";


    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println("Device with id:" + properties.getProperty("deviceId") + " Started");
        System.out.println("Device with id:" +  Long.valueOf(args[0]) + " Started");
        ConnectionFactory factory = getConnectionFactory();
        try(
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){
            channel.queueDeclare(QUEUE, false, false, false, null);

            CSVReader reader = new CSVReader(new FileReader("./src/main/resources/sensor.csv"));
            String[] nextLine;
            while(true){
                Date dateNow = new Date(System.currentTimeMillis());
                Time timeNow = new Time(System.currentTimeMillis());
                nextLine = reader.readNext();
                Double value = Double.parseDouble(nextLine[0]);
                String message = "{";
                //message += "\"deviceId\":\"" + properties.getProperty("deviceId") + "\",";
                message += "\"deviceId\":\"" + Long.valueOf(args[0]) + "\",";
                message += "\"energy\":" + value + ",";
                message += "\"date\":\"" + dateNow + "\",";
                message += "\"time\":\"" + timeNow + "\"";
                message += "}";
                System.out.println("Sending message: ");
                System.out.println(message);
                channel.basicPublish("", QUEUE, null, message.getBytes());
                //Wait 10 minutes then read again
                Thread.sleep(2000);
            }
        } catch (IOException | TimeoutException | InterruptedException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
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
}