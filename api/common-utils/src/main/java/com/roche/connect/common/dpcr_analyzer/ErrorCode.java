package com.roche.connect.common.dpcr_analyzer;

public class ErrorCode {

	public static final String PLATE_INVALID_CODE = "200";
	public static final String PLATE_MULTIPLE_SAMPLES_CODE = "201";
	public static final String PLATE_NO_ORDER_CODE = "202";
	public static final String PLATE_INCOMPLETE_CODE = "203";
	
	public static final String SAMPLE_TYPE_NOT_RECOGNIZED_CODE = "102";
	
	public static final String PLATE_INVALID_DESC = "Plate invalid";
	public static final String PLATE_MULTIPLE_SAMPLES_DESC = "Plate belong to multiple samples";
	public static final String PLATE_NO_ORDER_DESC = "No order";
	public static final String PLATE_INCOMPLETE_DESC = "Plate incomplete";
	public static final String SAMPLE_TYPE_NOT_RECOGNIZED_DESC = "Specified sample type is not recognized";
	
	
	private ErrorCode() {
		super();
	}
	
}
