/**
 * 
 */
package com.camel.util;

public enum AppUrlEntry {
    ROOT_ELEMENT("appURLs"),SEND_REQUEST("sendRequest"),
    NO_ENTRY("Unknown");
    
    private String value;
    
    private AppUrlEntry(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static AppUrlEntry findByValue(String value) {
        AppUrlEntry[] values = AppUrlEntry.values();
        for (AppUrlEntry url : values) {
            if (url.value.equalsIgnoreCase(value))
                return url;
        }
        return NO_ENTRY;
    }
    
    public static String findByKey(AppUrlEntry value) {
        AppUrlEntry[] values = AppUrlEntry.values();
        for (AppUrlEntry url : values) {
            if (url == value)
                return url.value;
        }
        return "Unknown";
    }
}
