package com.msdb.services.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.serial.Serial;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsService {
    private static Settings settings;

    public static SettingsService create() throws IOException {
        return new SettingsService();
    }

    public SettingsService() throws IOException {
        // settings.json file
        Path settingsPath;
        try {
            settingsPath = Paths.get("settings.json");
        } catch (NullPointerException e) {
            throw new SettingsException("File settings.json not found");
        }

        // Binding to Settings model
        ObjectMapper objectMapper = new ObjectMapper();
        settings = objectMapper.readValue(new StringReader(new String(Files.readAllBytes(settingsPath), StandardCharsets.UTF_8)), Settings.class);
    }

    /**
     * @return MQTT client settings in {@link Settings}
     */
    public static Settings getSettings() {
        return settings;
    }

    public static class Settings {
        private Boolean debug;

        private Broker broker;
        private Options options;
        private Subscriptions subscriptions;
        @JsonProperty("serial_device")
        private SerialDevice serialDevice;

        public boolean isDebug() {
            return Boolean.TRUE.equals(debug);
        }

        public Boolean getDebug() {
            return debug;
        }

        public void setDebug(Boolean debug) {
            this.debug = debug;
        }

        public Broker getBroker() {
            return broker;
        }

        public void setBroker(Broker broker) {
            this.broker = broker;
        }

        public Options getOptions() {
            return options;
        }

        public void setOptions(Options options) {
            this.options = options;
        }

        public Subscriptions getSubscriptions() {
            return subscriptions;
        }

        public void setSubscriptions(Subscriptions subscriptions) {
            this.subscriptions = subscriptions;
        }

        public SerialDevice getSerialDevice() {
            return serialDevice;
        }

        public void setSerialDevice(SerialDevice serialDevice) {
            this.serialDevice = serialDevice;
        }

        public class Broker {
            private String host;
            private String port;
            @JsonProperty("client_id")
            private String clientId;

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public String getPort() {
                return port;
            }

            public void setPort(String port) {
                this.port = port;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }
        }

        public class Options {
            @JsonProperty("clean_session")
            private Boolean cleanSession;
            @JsonProperty("keep_alive_interval")
            private Integer keepAliveInterval;
            private String username;
            private String password;

            public Boolean getCleanSession() {
                return cleanSession;
            }

            public void setCleanSession(Boolean cleanSession) {
                this.cleanSession = cleanSession;
            }

            public Integer getKeepAliveInterval() {
                return keepAliveInterval;
            }

            public void setKeepAliveInterval(Integer keepAliveInterval) {
                this.keepAliveInterval = keepAliveInterval;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

        public class Subscriptions {
            private String request;
            private String response;

            public String getRequest() {
                return request;
            }

            public void setRequest(String request) {
                this.request = request;
            }

            public String getResponse() {
                return response;
            }

            public void setResponse(String response) {
                this.response = response;
            }
        }

        public class SerialDevice {
            @JsonProperty("port_name")
            private String portName;
            @JsonProperty("baud_rate")
            private Integer baudRate;

            public String getPortName() {
                if (portName == null)
                    return Serial.DEFAULT_COM_PORT;
                return portName;
            }

            public void setPortName(String portName) {
                this.portName = portName;
            }

            public Integer getBaudRate() {
                if (baudRate == null)
                    return 9600;
                return baudRate;
            }

            public void setBaudRate(Integer baudRate) {
                this.baudRate = baudRate;
            }
        }
    }
}
