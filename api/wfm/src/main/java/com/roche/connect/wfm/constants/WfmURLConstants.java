package com.roche.connect.wfm.constants;


/**
 * The Class WfmURLConstants.
 */
public class WfmURLConstants {

    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String RUN_RESULT_API_PATH= "/json/rest/api/v1/runresults";
    
    /** The Constant QUESTION_MARK. */
    public  static final String QUESTION_MARK="?";
    
    /** The Constant DEVICE_ID. */
    public  static final String DEVICE_ID="deviceid=";
    
    /** The Constant ORDER_ID. */
    public  static final String ORDER_ID="orderId=";
    
    /** The Constant CONTAINER_ID. */
    public static final  String CONTAINER_ID="containerId=";
    
    /** The Constant UTF_8. */
    public  static final   String UTF_8 = "UTF-8";
    
    /** The Constant APPLICATION_JSON */
    public static final String APPLICATION_JSON="application/json";
    
    /** The Constant PROCESS_STEP_NAME. */
    public static final String PROCESS_STEP_NAME="&processstepname=";
    
    
    /** The Constant PROCESS_STEP_NAME. */
    public static final String DEVICE_RUN_ID="&deviceRunId=";
    
    /** The Constant PROCESS_STEP_NAME. */
    public  static final String PROCESS_STEP_NAME_ONE="&processStepname=";
    
    /** The Constant SAMPLE_RESULTS. */
    public static final String SAMPLE_RESULTS ="/sampleresults";

    /** The Constant OUTPUT_CONTAINER_ID. */
    public static final String OUTPUT_CONTAINER_ID ="outputContainerId=";
    
    /** The Constant OUTPUT_CONTAINER_POSITION. */
    public  static final String OUTPUT_CONTAINER_POSITION ="&outputContainerPosition=";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String ORDERS_API_PATH= "/json/rest/api/v1/orders?";
    
    /** The Constant ACCESSIONING_ID. */
    public  static final String ACCESSIONING_ID ="accessioningID=";
    
    /** The Constant ORDER_STATUS_IN_WORKFLOW. */
    public  static final String ORDER_STATUS_IN_WORKFLOW = "&orderStatus=Inworkflow";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String IMM_LP_RSP_API_PATH= "/rest/api/v1/messages?source=Connect&devicetype=LP24&messagetype=";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String IMM_LP_ACK_API_PATH= "/rest/api/v1/messages?source=Connect&devicetype=LP24&messagetype=";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String IMM_MP_ACK_API_PATH= "/rest/api/v1/messages?source=Connect&devicetype=MP24&messagetype=";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String IMM_DPCR_LP24_API_URL= "/rest/api/v1/dpcr/messages";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String IMM_HTP_STATUS_API_PATH= "/rest/api/v1/updatewfmprocess/{htpstatus}";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String IMM_FORTE_STATUS_API_PATH= "/rest/api/v1/updatewfmprocess/{fortestatus}";
    
    /** The Constant RUN_RESULT_API_PATH. */
    public  static final String AMM_ASSAY_API_PATH= "/json/rest/api/v1/assay/";
    
    /** The Constant AMM_MOLICULAR_ID_ASSAY. */
    public static final String AMM_MOLECULAR_ID_ASSAY = "molecularIdDisplay?assayType=";
    
    /** The Constant AMM_MOLICULAR_ID_PLATETYPE. */
    public static final String AMM_MOLECULAR_ID_PLATETYPE = "plateType=";
    
    /** The Constant AMM_MOLICULAR_ID_PLATELOCATION. */
    public static final String AMM_MOLECULAR_ID_PLATELOCATION = "plateLocation=";
    
    /** The Constant PROCESS_STEP_ACTION. */
    public  static final String PROCESS_STEP_ACTION= "/processstepaction";
   
    /** The Constant AMM_ASSAY_TYPE. */
    public  static final String AMM_ASSAY_TYPE="/json/rest/api/v1/assay?assayType=";
    
    /** The Constant AMM_DEVICE_TEST_OPTIONS. */          
    public  static final String AMM_DEVICE_TEST_OPTIONS="/devicetestoptions?deviceType=";
    
    /** The parameter passed during query formation */  
    public  static final String AMM_PROCESSSTEP_PARAM="processStepName=";
    
    /** The parameter passed during query formation */  
    public  static final String DPCR_PROCESSSTEP_NAME="&processStepName=";
    
    /** Ampersand constant */ 
    public  static final String AMM_AMPERSAND="&";
    
    /** MESSAGE TYPE RSP constant */ 
    public  static final String MESSAGE_TYPE_RESPONSE="messagetype=RSP";
    
    /** Device Type LP24 constant */ 
    public  static final String DEVICE_TYPE_LP24="&devicetype=LP24";
    
    /** Source LP24 constant */ 
    public  static final String SOURCE="&source=Connect";
    
    /** The Constant DEVICE_ID. */
    public  static final String DEVICE_ID_AMPERSAND="&deviceid=";
    
    public static final String CONTAINER_SAMPLE = "/json/rest/api/v1/containersamples?accessioningID=";
   
    public static final String CONTAINER_SAMPLE_PARAMETER_STATUS = "&status=";
    
    /** The Constant DEVICE_ID. */
    public static final String PROCESSED = "processed";
    
    /** The NOTIFICATION VALUE */
    public static final String ADM_DPCR_LP24_SSU_URL = "/rest/api/v1/notification";
    
    /** ADM NOTIFICATION URL */
    public static final String ADM_NOTIFICATION_URL = "/json/rest/api/v1/notification";
    
    public static final String ADM_DEVICE_URL = "deviceType=";
    
    public static final String RMM_RUN_RESULT_BY_ID_URL = "/runresultsbyid/";
    
    /**                                             
     * Instantiates a new url constants.
     */
    private WfmURLConstants() {

    }
}