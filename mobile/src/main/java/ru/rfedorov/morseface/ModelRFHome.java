package ru.rfedorov.morseface;

import java.util.AbstractMap;
import java.util.TreeMap;

public class ModelRFHome {
    private String message = "sos";
    private Boolean enabled = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}