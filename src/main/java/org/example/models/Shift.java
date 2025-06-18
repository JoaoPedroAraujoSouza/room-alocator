package org.example.models;

import lombok.Getter;

@Getter
public enum Shift {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening");

    private final String displayName;

    Shift(String displayName) {
        this.displayName = displayName;
    }

}
