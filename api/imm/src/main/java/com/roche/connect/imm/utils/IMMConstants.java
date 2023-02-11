package com.roche.connect.imm.utils;

public class IMMConstants {
    
    
    /** The Constant PROCESS_STEP_ACTION. */
    public  static final String PROCESS_STEP_ACTION= "/processstepaction";
    
    
    public static final String DEVICE_ID1 = "?deviceId=";
    public static final String DEVICE_ID2 = "&deviceId=";
    public static final String DEVICE_RUN_ID1 = "?deviceRunId=";
    public static final String DEVICE_RUN_ID2 = "&deviceRunId=";
    public static final String DEVICE_SERIAL_NUM1 = "?deviceSerialNumber=";
    public static final String DEVICE_SERIAL_NUM2 = "&deviceSerialNumber=";
    public static final String PROCESS_STEPNAME1 = "?processStepName=";
    public static final String PROCESS_STEPNAME2 = "&processStepName=";
    public static final String INPUT_CONTAINER_ID1 = "?inputContainerId=";
    public static final String INPUT_CONTAINER_ID2 = "&inputContainerId=";
    public static final String INPUT_CONTAINER_POSITION1 = "?inputContainerPosition=";
    public static final String INPUT_CONTAINER_POSITION2 = "&inputContainerPosition=";
    public static final String OUTPUT_CONTAINER_ID1 = "?outputContainerId=";    
    public static final String OUTPUT_CONTAINER_ID2 = "&outputContainerId=";
    public static final String OUTPUT_CONTAINER_POSITION1 = "?outputContainerPosition=";
    public static final String OUTPUT_CONTAINER_POSITION2 = "&outputContainerPosition=";
    
	public static final String SYSTEM_STR = "System";
	public static final String HTP_ASSAY_TYPE = "NIPTHTP";
	public static final String DPCR_ASSAY_TYPE = "NIPTDPCR";
	

	public static final String REAGENT_KIT = "reagentKitName";
	public static final String REAGENT_VERSION = "reagentVesion";
	public static final String LOT_NO = "lotNo";
	public static final String BARCODE = "barcode";
	public static final String OUTPUTPLATETYPE = "outputPlateType";
	public static final String SAMPLEVOLUME = "sampleVolume";
	public static final String VOLUME = "volume";
	public static final String SOFTWAREVERSION = "softwareVersion";
	public static final String SERIALNO = "serialNo";
	public static final String EXPDATE = "expDate";
	public static final String ELUATEVOLUME = "eluateVolume";
	
	public static final String NOORDER = "NOORDER";
	public static final String SUCCESS = "SUCCESS";
	public static final String DUPLICATE = "DUPLICATE";
	
	public static final String PROTOCOLNAMEOFASSAYMISMATCH = "";
	public static final long ORDERIDOFASSAYMISMATCH = 0;
	
	public static final String COMPLEX_ID_KEY_STR = "complexId";
	public static final String ADAPTOR_SIMPLE_DATE_FORMAT_STR = "yyyyMMddHHmmss";
	public static final String ADMIN_USER_STR = "admin";
	public static final String DPCR_ANALYZER_FILE_PATH_STR = "DPCR Analyzer FilePath";
	public static final String DPCR_ANALYZER_PLATE_ID_LIST_STR = "DPCR PlateId List";
	
	
    
    public enum API_URL {
        AMM_API_URL("pas.amm_api_url"),OMM_API_URL("pas.omm_api_url"),RMM_API_URL("pas.rmm_api_url"),IMM_API_URL("pas.imm_api_url");
        
        private final String text;

        API_URL(final String text) {
             this.text = text;
         }

         @Override
         public String toString() {
             return text;
         } 
     }
    
    
}
