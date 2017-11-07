package com.msdb.services;

import com.msdb.MqttCallBack;
import com.msdb.services.settings.SettingsService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(MqttCallBack.class.getName());

    public static MqttClientService create() {
        return new MqttClientService();
    }

    public MqttClientService() {
        start();
    }

    @Override
    public void run() {
        try {
            // Options
            SettingsService.Settings settings = SettingsService.getSettings();
            MqttConnectOptions connOpt = new MqttConnectOptions();
            connOpt.setCleanSession(settings.getOptions().getCleanSession());
            connOpt.setKeepAliveInterval(settings.getOptions().getKeepAliveInterval());
            connOpt.setUserName(settings.getOptions().getUsername());
            connOpt.setPassword(settings.getOptions().getPassword().toCharArray());

            // Client
            MqttClient client = new MqttClient("tcp://" + settings.getBroker().getHost() +
                    ":" + settings.getBroker().getPort(),
                    settings.getBroker().getClientId() != null ? settings.getBroker().getClientId() : MqttClient.generateClientId());
            client.setCallback(new MqttCallBack(client));
            client.connect(connOpt);
            client.subscribe(settings.getSubscriptions().getRequest());

            logger.info("Connected to " + client.getServerURI());
        } catch (Exception e) {
            logger.error("MqttService error", e);
            logger.info("Restarting service in 5 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                logger.error("Restart error", e1);
                System.exit(0);
            }
            create();
        }
    }
}
