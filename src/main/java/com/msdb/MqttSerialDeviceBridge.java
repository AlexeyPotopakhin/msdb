package com.msdb;

import com.msdb.services.MqttClientService;
import com.msdb.services.settings.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class MqttSerialDeviceBridge {
    private static final Logger logger = LoggerFactory.getLogger(MqttSerialDeviceBridge.class.getName());

    public static void main(String[] args) {
        // Logs initialization
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        logger.info("MSDB application started");
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                logger.info("MSDB application stopped"), "shutdown"));

        // Application initialization
        try {
            SettingsService.create();
            MqttClientService.create();
        } catch (Exception e) {
            logger.error("Common application exception", e);
        }
    }
}
