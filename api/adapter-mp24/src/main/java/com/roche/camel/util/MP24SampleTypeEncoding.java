/*******************************************************************************
 * MP24SampleTypeEncoding.java                  
 *  Version:  1.0
 * 
 *  Authors:  Arun Paul Jacob
 * 
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * *********************
 *  ChangeLog:
 * 
 *   arunpaul.j@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
/**
 * 
 */
package com.roche.camel.util;

/**
 * The Enum MP24SampleTypeEncoding.
 */
public enum MP24SampleTypeEncoding {
    
    /** The standard. */
    STANDARD("STANDARD"),
/** The express. */
EXPRESS("EXPRESS"),
/** The duo. */
DUO("DUO"),
/** The flex. */
FLEX("FLEX"),
/** The advanced. */
ADVANCED("ADVANCED");
    
    /**
     * Instantiates a new MP 24 sample type encoding.
     *
     * @param value the value
     */
    private MP24SampleTypeEncoding(String value) {
        this.value = value;
    }
    
    /** The value. */
    private String value;
    
    /**
     * Find by value.
     *
     * @param value the value
     * @return the MP 24 sample type encoding
     */
    public static MP24SampleTypeEncoding findByValue(String value) {
        MP24SampleTypeEncoding[] values = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding.value.equalsIgnoreCase(value))
                return encoding;
        }
        return null;
    }
    
    /**
     * From value.
     *
     * @param value the value
     * @return the MP 24 sample type encoding
     */
    public static MP24SampleTypeEncoding fromValue(String value) {
        MP24SampleTypeEncoding[] values = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding.value.equalsIgnoreCase(value))
                return encoding;
        }
        return null;
    }
    
    /**
     * Gets the encoding value.
     *
     * @param value the value
     * @return the encoding value
     */
    public static String getEncodingValue(String value) {
        MP24SampleTypeEncoding[] values = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding.value.equalsIgnoreCase(value))
                return encoding.value;
        }
        return "UnKnown";
    }
    
    /**
     * Find by key.
     *
     * @param key the key
     * @return the string
     */
    public static String findByKey(MP24SampleTypeEncoding key) {
        MP24SampleTypeEncoding[] values = MP24SampleTypeEncoding.values();
        for (MP24SampleTypeEncoding encoding : values) {
            if (encoding == key)
                return encoding.value;
        }
        return "UnKnown";
    }
}
