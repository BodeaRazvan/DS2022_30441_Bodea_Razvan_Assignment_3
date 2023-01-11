package com.energy.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLogger {
    private static Logger instance = null;

    private MyLogger(){

    }

    public static Logger getInstance(){
        if(instance==null){
            instance = LoggerFactory.getLogger(MyLogger.class);
        }
        instance.info("MyLogger instance created");
        return instance;
    }
}
