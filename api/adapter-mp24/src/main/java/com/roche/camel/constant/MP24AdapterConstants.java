/*******************************************************************************
 * MP24AdapterConstants.java
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
package com.roche.camel.constant;

/**
 * The Class MP24AdapterConstants.
 */
public class MP24AdapterConstants {

	/** The Constant BASE_URL. */
	public static final String BASE_URL = "/rest/api/v1";

	/** The Constant VALUE. */
	public static final String VALUE = "MP adapter";

	/** The Constant SEND_RSP_TO_MP24. */
	public static final String SEND_RSP_TO_MP24="/sendrsptomp24";

	/** The Constant SEND_ACK_TO_MP24. */
	public static final String SEND_ACK_TO_MP24="/sendacktomp24";

	/** The Constant CAMEL_HL7_MESSAGE_TYPE. */
	public static final String CAMEL_HL7_MESSAGE_TYPE = "CamelHL7MessageType";

	/** The Constant QBP. */
	public static final String QBP = "QBP";

	/** The Constant MSH. */
	public static final String MSH = "MSH";

	/** The Constant QPD. */
	public static final String QPD = "QPD";

	/** The Constant SSU. */
	public static final String SSU = "SSU";

	/** The Constant MSA. */
	public static final String MSA = "MSA";
	
	/** The Constant ESU. */
	public static final String ESU = "ESU";

	/** The Constant CLASSPATH_HL7_ACK_U03. */
	public static final String CLASSPATH_HL7_ACK_U03 = "classpath:hl7/ACK_U03.txt";
	
	/** The Constant CLASSPATH_HL7_ACK_U01. */
	public static final String CLASSPATH_HL7_ACK_U01 = "classpath:hl7/ACK_U01.txt";


	/** The Constant SIMPLE_DATE_FORMAT. */
	public static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";

	/** The Constant APPLICATION_ACCEPT. */
	public static final String APPLICATION_ACCEPT = "AA";

	/** The Constant NA_EXTRACTION. */
	public static final String NA_EXTRACTION = "NA-Extraction";

	/** The Constant QAK. */
	public static final String QAK = "QAK";

	/** The Constant SPM. */
	public static final String SPM = "SPM";

	/** The Constant ORC. */
	public static final String ORC = "ORC";

	/** The Constant OBR. */
	public static final String OBR = "OBR";

	/** The Constant SAC. */
	public static final String SAC = "SAC";

	/** The Constant OK. */
	public static final String OK = "OK";

	/** The Constant NF. */
	public static final String NF = "NF";

	/** The Constant SET_ID_SPM. */
	public static final String SET_ID_SPM = "1";

	/** The Constant SPECIMEN_ROLE_P. */
	public static final String SPECIMEN_ROLE_P = "P";

	/** The Constant SPECIMEN_ROLE_Q. */
	public static final String SPECIMEN_ROLE_Q = "Q";

	/** The Constant SPECIMEN_ROLE_PATIENT. */
	public static final String SPECIMEN_ROLE_PATIENT = "Patient";

	/** The Constant SPECIMEN_ROLE_CONTROL. */
	public static final String SPECIMEN_ROLE_CONTROL = "Control";

	/** The Constant VOLUME_UNITS. */
	public static final String VOLUME_UNITS = "ul";

	/** The Constant UNIVERSAL_IDENTIFIER. */
	public static final String UNIVERSAL_IDENTIFIER = "99ROC";

	/** The Constant RESULT_STATUS. */
	public static final String RESULT_STATUS = "S";

	/** The Constant MSG_ACK_CODE. */
	public static final String MSG_ACK_CODE = "AE";

	/** The Constant QUERY_RSP_STATUS. */
	public static final String QUERY_RSP_STATUS = "AE";

	/** The Constant EQU. */
	public static final String EQU = "EQU";

	/** The Constant NTE. */
	public static final String NTE = "NTE";

	/** The Constant MSG_TYPE. */
	public static final String MSG_TYPE = "StatusUpdate";

	/** The Constant CONTAINER_STATUS_COMPLETED. */
	public static final String CONTAINER_STATUS_COMPLETED = "R";

	/** The Constant CONTAINER_STATUS_INPROGRESS. */
	public static final String CONTAINER_STATUS_INPROGRESS = "O";

	/** The Constant OBSERVE_IDENTIFIER_PROTOCOL_NAME. */
	public static final String OBSERVE_IDENTIFIER_PROTOCOL_NAME = "P";

	/** The Constant OBSERVE_IDENTIFIER_PROTOCOL_VERSION. */
	public static final String OBSERVE_IDENTIFIER_PROTOCOL_VERSION = "PV";

	/** The Constant OBSERVE_IDENTIFIER_RUN_RESULT. */
	public static final String OBSERVE_IDENTIFIER_RUN_RESULT = "RES";

	/** The Constant OBSERVE_IDENTIFIER_RUNTIME_RANGE. */
	public static final String OBSERVE_IDENTIFIER_RUNTIME_RANGE = "RuntimeRange";

	/** The Constant OBSERVE_IDENTIFIER_ORDER_NAME. */
	public static final String OBSERVE_IDENTIFIER_ORDER_NAME="OrderName";

	/** The Constant OBSERVE_IDENTIFIER_ORDER_RESULT. */
	public static final String OBSERVE_IDENTIFIER_ORDER_RESULT="OrderResult";

	/** The Constant OBSERVE_IDENTIFIER_INTERNAL_CONTROLS. */
	public static final String OBSERVE_IDENTIFIER_INTERNAL_CONTROLS="IC";

	/** The Constant OBSERVE_IDENTIFIER_SC_PROCESSING_CARTRIDGE. */
	public static final String OBSERVE_IDENTIFIER_SC_PROCESSING_CARTRIDGE="SCProcessingCartridge";

	/** The Constant OBSERVE_IDENTIFIER_SC_TIP_RACK. */
	public static final String OBSERVE_IDENTIFIER_SC_TIP_RACK="SCTipRack";

	/** The Constant OBSERVE_IDENTIFIER_MRREAGENT_CASSETTE. */
	public static final String OBSERVE_IDENTIFIER_MRREAGENT_CASSETTE="MRReagentCassette";

	/** The Constant OBSERVE_IDENTIFIER_SR2ML_REAGENT_TUBE. */
	public static final String OBSERVE_IDENTIFIER_SR2ML_REAGENT_TUBE="SR2mlReagentTube";

	/** The Constant OBSERVE_IDENTIFIER_REAGENT2ML_TUBE. */
	public static final String OBSERVE_IDENTIFIER_REAGENT2ML_TUBE="reagent2mlTube";

	/** The Constant OBSERVE_IDENTIFIER_SR25ML_REAGENT_TUBE. */
	public static final String OBSERVE_IDENTIFIER_SR25ML_REAGENT_TUBE="SR25mlReagentTube";

	/** The Constant OBSERVE_IDENTIFIER_REAGENT25ML_TUBE. */
	public static final String OBSERVE_IDENTIFIER_REAGENT25ML_TUBE="reagent25mlTube";

	/** The Constant UPDATED_BY. */
	public static final String UPDATED_BY="System";
	
	/** The Constant TEMPLATE_TYPE. */
	public static final String TEMPLATE_TYPE="MP24-DATAMISSING";
	
	/** The Constant NOTIFICATION_USER_ID. */
	public static final String NOTIFICATION_USER_ID="ADMIN";
	
	public static final String MISSING_DATA="Missing";
	
	public static final String UNREGISTERED_DEVICE_MP24="The MP24 device is not registered and is sending messages to Connect. Please register the device to proceed further";

	public static final String INVALID_HL7_VER_MP24="The HL7 version used by the MP24 device is invalid. Please rectify to proceed further";
	
	public static final String INVALID_DEVICE_MODEL_MP24="The device model is invalid for MP24. Please rectify to proceed further";
	
	public static final String DEVICE_OFFLINE_MP24="The MP24 device is offline. Please establish connectivity to receive messages";
	
	public static final String CAMEL_VERSION = "CamelHL7VersionId";
	
	public static final String HL7_VERSION = "2.5.1";
	
    private MP24AdapterConstants() {
        super();
    }
	
}
