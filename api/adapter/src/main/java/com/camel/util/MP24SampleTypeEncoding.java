/**
 * 
 */
package com.camel.util;

public enum MP24SampleTypeEncoding {
    STANDARD("STANDARD"),EXPRESS("EXPRESS"),DUO("DUO"),FLEX("FLEX"),ADVANCED("ADVANCED");
    
    private MP24SampleTypeEncoding(String value) {
        this.value = value;
    }
    
    private String value;
    
    public static MP24SampleTypeEncoding findByValue(String value) {
        MP24SampleTypeEncoding values[] = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding.value.equalsIgnoreCase(value))
                return encoding;
        }
        return null;
    }
    
    public static MP24SampleTypeEncoding fromValue(String value) {
        MP24SampleTypeEncoding values[] = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding.value.equalsIgnoreCase(value))
                return encoding;
        }
        return null;
    }
    
    public static String getEncodingValue(String value) {
        MP24SampleTypeEncoding values[] = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding.value.equalsIgnoreCase(value))
                return encoding.value;
        }
        return "UnKnown";
    }
    
    public static String findByKey(MP24SampleTypeEncoding key) {
        MP24SampleTypeEncoding values[] = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding == key)
                return encoding.value;
        }
        return "UnKnown";
    }
}
