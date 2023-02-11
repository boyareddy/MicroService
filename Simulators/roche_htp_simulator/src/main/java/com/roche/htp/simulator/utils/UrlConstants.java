package com.roche.htp.simulator.utils;

public enum UrlConstants {

    POST("post"), PUT("put"), GET("get") ;

    private final String text;

    UrlConstants(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
