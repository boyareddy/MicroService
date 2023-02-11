package com.roche.connect.adm.util;


public enum ADMConstants {

	NOTIFICATION_ADD_FAILED("Failed to add notification"),
	NOTIFICATION_UPDATE_FAILED("Failed to update notification"),
	NOTIFICATION_FETCH_FAILED("Error occured while fetching notifications"),
	NOTIFICATION_TEMPLATE_NOT_FOUND("Notification template is not available"),
	NOTIFICATION_NOT_AVAILABLE("Notification is not available"),
	YES("Y"),
	NOTIFICATION_BUILD_FAILED("Sending email notification failed"),
	NOTIFICATION_GROUP_NOT_VALID("Notification Group is invalid"),
	VERSION_FETCH_FAILED("Error occured while fetching version info"),
	DEVICE_MGMT_URL("/json/devicetype/fetch"),
	ASSAY_URL("/json/rest/api/v1/assay"),
	SUPPORTED_PROTOCOLS("supportedProtocols"),
	ATTRIBUTES("attributes"),
	HL7("HL7"),
	REST("REST"),
	DEVICE_TYPE_NAME("name"),
	SUPPORTED_PROTOCOL_VERSION("supportedProtocolVersion"),
	AUDIT_FILE_NAME("AuditDetails_"),
	DEVICE_FILE_NAME("DeviceSummaryLogs_"),
	RUNTIME_FILE_NAME("RuntimeLogs_"),
	MESSAGE(" Full system backup has failed to start on "),
	SEVERITY("Warning"),
	TITLE("Connect system backup"),
	TOPIC("Backup"),
	MODULE("ADM"),
	SYSTEMLANGUAGE("systemLanguage"),
	BACKUPINTERVAL("backup_interval"),
	BACKUPLOCATION("backup_location"),
	DATEFORMAT("dateFormat"),
	TIMEFORMATE("timeFormat"),
	EMAIL_TEMPLATE_NOTAVAILABLE("Email Template is not available for this locale :"),
	SYS_SETTINGS_NOTAVAILABLE("System settings are not available"),
	VALID_ADDRESS_FIELD_LENGTH(100),
	DATEANDTIME("DateAndTime"),
	RSPMESSAGE("Message"),
	BACKUP_STARTED("Backup started successfully"),
	BACKUP_IN_PROGRESS("Backup is in progress"),
	DESTINATION_DIRECTORY_IS_NOT_AVILABLE("Destination directory is not available"),
	WRITE_ACCESS_RESTRICTED("Write access is restricted in the selected folder"),
	SYSTEM_SETTINGS_NOT_AVAILABLE_FOR_BACKUP("System settings is not avaliable for backup"),
	BASIC_SIZE_MEASURE(1024),
	NOT_ENOUGH_STORAGE("Insufficient storage"),
	JOB_DETAILS_URL("/json/security/tokenDetails"),
	GET_ROLES_URL("/json/roles"),
	X_AUTH_TOKEN("X-AUTH-TOKEN");
	
	private String value;
	private Integer intValue;
	
	private ADMConstants(String value) {
		this.value = value;
	}
	private ADMConstants(Integer intValue) {
		this.intValue = intValue;
	}
	
    @Override public String toString() {
        return value;
    }
    public Integer toInteger() {
        return intValue;
    }
	
	public enum SystemSettings {
		ACTIVE_FLAG("Y"), LAB_LOGO_ATTR_NAME("labLogo"), REP_SETTING_ATTR_TYPE("reportsettings"), PHONE_NUM_ATTR_NAME("phoneNumber"),
		LAB_ADDRESS_FIELD1("labAddress1"),LAB_ADDRESS_FIELD2("labAddress2"),LAB_ADDRESS_FIELD3("labAddress1");

		private SystemSettings(String strValue) {
			strVal = strValue;
		}
		
		private String strVal;

		@Override
		public String toString() {
			if (strVal != null) {
				return strVal;
			}
			return super.toString();
		}
	}
	
	public enum ErrorMessages {
		INVALID_PHONE_NUM_LENGTH("Allowed length for phone number is 20."),
		INVALID_PHONE_NUM_FORMAT("Allowed special characters for Phone Number are hyphen, plus and brackets."),
		INVALID_LAB_LOGO_FORMAT("Allowed format for lab logo are jpg,png and jpeg."),
		INVALID_LAB_LOGO_SIZE("Allowed size for lab logo is 1 MB."),
		SYSTEM_SETTINGS_SAVE_SUCCESS("System settings saved successfully."),
		LAB_LOGO_SAVE_SUCCESS("Lab logo saved successfully."),
		ADDRESS_LENGTH_INVALID("Allowed maximum field length is 100 characters");
		
		private final String text;
		 ErrorMessages(final String text){
	            this.text = text;
	        }
	        @Override
	        public String toString() {
	            return text;
	        }
	}
}
