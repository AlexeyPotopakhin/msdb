package com.msdb.services.settings;

import java.io.IOException;

public class SettingsException extends IOException {
    public SettingsException(String message) {
        super(message);
    }
}
