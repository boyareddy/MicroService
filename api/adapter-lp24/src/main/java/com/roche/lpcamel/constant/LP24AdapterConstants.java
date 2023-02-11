package com.roche.lpcamel.constant;

public class LP24AdapterConstants {
	private LP24AdapterConstants() {
		super();
	}
	
	public static final String UNREGISTERED_DEVICE_LP24="The LP24 device is not registered and is sending messages to Connect. Please register the device to proceed further";

	public static final String INVALID_HL7_VER_LP24="The HL7 version used by the LP24 device is invalid. Please rectify to proceed further";
	
	public static final String INVALID_DEVICE_MODEL_LP24="The device model is invalid for LP24. Please rectify to proceed further";
	
	public static final String DEVICE_OFFLINE_LP24="The LP24 device is offline. Please establish connectivity to receive messages";
	
	public static final String CAMEL_VERSION = "CamelHL7VersionId";
	
	public static final String HL7_VERSION = "2.5.1";
}
