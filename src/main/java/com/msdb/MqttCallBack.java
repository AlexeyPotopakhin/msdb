package com.msdb;

import com.msdb.services.SerialDeviceService;
import com.msdb.services.MqttClientService;
import com.msdb.services.settings.SettingsService;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MqttCallBack implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MqttCallBack.class.getName());

    private MqttClient mqttClient;
    private SerialDeviceService serialDeviceService;
    private SettingsService.Settings settings = SettingsService.getSettings();

    public MqttCallBack(MqttClient mqttClient) throws IOException {
        this.mqttClient = mqttClient;
        serialDeviceService = new SerialDeviceService();
        serialDeviceService.addEventListener(serialDataEvent -> {
            try {
                MqttMessage message = new MqttMessage(serialDataEvent.getBytes());
                mqttClient.publish(settings.getSubscriptions().getResponse(), message);

                if (settings.isDebug())
                    logger.debug("[Serial device message] = " + new String(message.getPayload()));
            } catch (IOException | MqttException e) {
                logger.error("Serial device receive message error", e);
            }
        });
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Connection lost");
        try {
            mqttClient.close(true);
            serialDeviceService.close();
        } catch (MqttException | IOException e) {
            logger.error("Close error", e);
            System.exit(0);
        }
        MqttClientService.create();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        if (!s.equals(settings.getSubscriptions().getRequest()))
            return;
        serialDeviceService.write(new String(mqttMessage.getPayload()));

        if (settings.isDebug())
            logger.debug("[MQTT message] = " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
}