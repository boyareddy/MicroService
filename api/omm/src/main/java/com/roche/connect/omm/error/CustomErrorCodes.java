package com.roche.connect.omm.error;

import com.hcl.hmtp.common.client.exceptions.ErrorCodes;

public enum CustomErrorCodes implements ErrorCodes{

	/**
     * The general errors.
     */
	 /** The argument null. */
    ORDER_ID_NULL(400),
    /** The argument null. */
    ORDER_STATUS_NULL(400),
    /** The argument null. */
    ACCESSIONING_ID_NULL(400),
    /** The argument null. */
    VALIDATION_GEN_STATUS_CODE(400),
    /** The argument not found . */
    ORDER_NOT_FOUND(404),
    /** Owner is not valid to update the order details */
	OWNER_NOT_VALID(404),
	STATUS_NULL(400),
    ACCESSIONING_ID_DUPLICATE(400),
	CONTAINER_NOT_FOUND(400),
	/** The argument not found . */
    CONTAINER_ID_NOT_FOUND(404),
    /** The argument null. */
    CONTAINER_ID_NULL(400),
    
    INVALID_COLLECTION_DATE(400),
    
    INVALID_FIRST_NAME_FORMAT(400),
    
    INVALID_LAST_NAME_FORMAT(400),
    
    ERROR_ENCRIPTION(400),
    
    INVALID_OTHER_CLINICIAN_NAME_FORMAT(400),
    
    INVALID_REF_CLINICIAN_CLINIC_NAME_FORMAT(400),
    
    INVALID_REF_CLINICIAN_NAME_FORMAT(400),
    
    INVALID_JSON_FORMAT(400),
    
    INVALID_TREATING_DOCTOR_NAME_FORMAT(400),
	
	FILE_EMPTY(500),
	
	FILE_ROWS_EXCEEDED(500),
	
	FILE_DOWNLOAD_FAILED(500),
	
	BULK_ORDER_VALIDATION_FAILED(500),
	
	BULK_ORDER_ERROR_OCCURED(500),
	
	INVALID_CSV_TEMPLATE(500),

	INVALID_CONTAINER_ID(400),

	ACCOUNT_NUMBER_DUPLICATE(400);

	
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
