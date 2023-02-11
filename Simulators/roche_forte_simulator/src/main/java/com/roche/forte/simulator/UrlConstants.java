package com.roche.forte.simulator;

public enum UrlConstants {

    POST("post"), PUT("put"), GET("get"),PING("Ping") ,GETJOB("GetJob");

    private final String text;

    UrlConstants(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
