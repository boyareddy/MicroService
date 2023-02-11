package com.roche.nipt.dpcr.constant;

public class CamelAdapterConstants {

	private CamelAdapterConstants() {
		super();
	}

	public static final String CAMEL_HL7_MESSAGE_TYPE = "CamelHL7MessageType";
	public static final String CAMEL_HL7_TRIGGER_EVENT = "CamelHL7TriggerEvent";
	public static final String CAMEL_HL7_PROCESSING_ID = "CamelHL7ProcessingId";
	public static final String CAMEL_HL7_SERIALNO = "CamelHL7MessageControl";
	public static final String CAMEL_VERSION = "CamelHL7VersionId";
	public static final String QBP = "QBP";
	
	public static final String Q11 = "Q11";

	public static final String MSH = "MSH";

	public static final String QPD = "QPD";

	public static final String OUL = "OUL";
	
	public static final String R21 = "R21";

	public static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";

	public static final String ACK = "ACK";

	public static final String MSA = "MSA";
	public static final String P = "P";
	public static final String T = "T";
	public static final String VERSION="2.4";
	
	public static final String UNREGISTERED_DEVICE_MP96="The MP96 device is not registered and is sending messages to Connect. Please register the device to proceed further";
	public static final String INVALID_HL7_VER_MP96="The HL7 version used by the MP96 device is invalid. Please rectify to proceed further";
	public static final String INVALID_DEVICE_MODEL_MP96="The device model is invalid for MP96. Please rectify to proceed further";
	public static final String DEVICE_OFFLINE_MP96="The MP96 device is offline. Please establish connectivity to receive messages";
}