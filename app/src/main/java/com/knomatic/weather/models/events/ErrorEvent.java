package com.knomatic.weather.models.events;

/**
 * Created by stephany.berrio on 28/02/17.
 */

public class ErrorEvent {

    private String error;

    public ErrorEvent(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
