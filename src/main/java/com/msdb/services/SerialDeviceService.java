package com.msdb.services;

import com.msdb.services.settings.SettingsService;
import com.pi4j.io.serial.*;

import java.io.IOException;

public class SerialDeviceService {
    private final Serial serialPort;

    public SerialDeviceService() throws IOException {
        SettingsService.Settings settings = SettingsService.getSettings();
        serialPort = SerialFactory.createInstance();
        SerialConfig config = new SerialConfig();
        config.device(settings.getSerialDevice().getPortName())
                .baud(Baud.valueOf("_" + settings.getSerialDevice().getBaudRate()))
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        serialPort.open(config);
    }

    public void write(String data) throws IOException {
        if (serialPort != null)
            serialPort.write(data);
    }

    public void addEventListener(SerialDataEventListener serialDataEventListener) throws SerialPortException {
        if (serialPort != null)
            serialPort.addListener(serialDataEventListener);
    }

    public void close() throws IOException {
        serialPort.close();
    }

    public Serial getSerialPort() {
        return serialPort;
    }
}
