/*******************************************************************************
 * AppUrlEntry.java                  
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
 * The Enum AppUrlEntry.
 */
public enum AppUrlEntry {
    
    /** The root element. */
    ROOT_ELEMENT("appURLs"),
/** The send request. */
SEND_REQUEST("sendRequest"),
    
    /** The no entry. */
    NO_ENTRY("Unknown");
    
    /** The value. */
    private String value;
    
    /**
     * Instantiates a new app url entry.
     *
     * @param value the value
     */
    private AppUrlEntry(String value) {
        this.value = value;
    }
    
    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Find by value.
     *
     * @param value the value
     * @return the app url entry
     */
    public static AppUrlEntry findByValue(String value) {
        AppUrlEntry[] values = AppUrlEntry.values();
        for (AppUrlEntry url : values) {
            if (url.value.equalsIgnoreCase(value))
                return url;
        }
        return NO_ENTRY;
    }
    
    /**
     * Find by key.
     *
     * @param value the value
     * @return the string
     */
    public static String findByKey(AppUrlEntry value) {
        AppUrlEntry[] values = AppUrlEntry.values();
        for (AppUrlEntry url : values) {
            if (url == value)
                return url.value;
        }
        return "Unknown";
    }
}
