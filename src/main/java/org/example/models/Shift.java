package org.example.models;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Shift implements Serializable {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening");

    private final String displayName;

    Shift(String displayName) {
        this.displayName = displayName;
    }

}
