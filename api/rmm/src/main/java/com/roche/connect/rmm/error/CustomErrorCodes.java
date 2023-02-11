package com.roche.connect.rmm.error;

import com.hcl.hmtp.common.client.exceptions.ErrorCodes;

public enum CustomErrorCodes implements ErrorCodes{

	/**
     * The general errors.
     */
	 /** The argument null. */
    RUNRESULT_ID_NULL(400),
   
    /** The argument null. */
    PROCESS_STEP_NAME_NULL(400),
    DEVICE_RUN_ID_NULL(400),
    ACCESSIONING_ID_NULL(400),
    INVALID_ACCESSIONING_ID(400),
    SAMPLERESULTS_NOT_FOUND(400),
    RUNSTATUS_NULL(400),
    /** The argument not found . */
    RUNRESULT_NOT_FOUND(400),
    ASSAYTYPE_RUNSTATUS_PROCESS_STEP_NAME_NULL(400),  
	ASSAYTYPE_NOT_FOUND(400),
	PROCESS_STEP_NAME_NOT_FOUND(400),
	INVALID_DEVICE_RUN_ID(400);  
	  
	/** The value. */
     private int value;
    /**
     * Instantiates a new error codes.
     * @param intValue the int value
     */
    private CustomErrorCodes(final int intValue) {
        value = intValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public String toString() {
        return name() + " (Code : " + value + ")";
    }
    /**
     * {@inheritDoc}
     */
    @Override public int getErrorCode() {
        return value;
    }
}
