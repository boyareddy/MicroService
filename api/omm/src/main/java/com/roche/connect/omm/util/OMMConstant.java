/*******************************************************************************
 * File Name: OMMConstant.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 * 
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.util;

public enum OMMConstant {

	ACTIVE_FLAG_YES("Y"), ACTIVE_FLAG_NO("N"), RETEST_SAMPLE_YES("Y"), RETEST_SAMPLE_TRUE(true), RETEST_SAMPLE_FALSE(
			false), TRUE_FLAG(true), FALSE_FLAG(false), FLAG_TRUE("true"), FLAG_FALSE(
					"false"), ORDER_STATUS_UNASSIGNED("unassigned"), HIGH_PRIORITY("high"), 
						HARMONY("harmony"), SCAP("scap"), FATAL_SEX("fetalSex"), MX("mx"),

	FIRST_SAMPLE(0), SECOND_SAMPLE(1),WARNING_CONTAINER_ID("WARNING_CONTAINER_ID");

	private String strVal;
	private Integer intVal;
	private boolean boolVal;
	
public enum Validation {
        
        NIPT("NIPT"),
        STATUS_TRUE("Yes"),
        STATUS_FALSE("No"),
        STATUS_MISSING("Missing"),
        SELF("Self"),
        NON_SELF("Non-self"),
        MISSING("Missing"),  
        FETUS_ONE("1"),
        FETUS_SECOND("2"),
        FETUS_GREATERTHAN2(">2"),
        MATERNAL_AGE("Maternal Age"),
        GESTATIONAL_AGE_DAYS("Gestational Age Days"),
        GESTATIONAL_AGE("Gestational Age Weeks"),
        IVF_STATUS("ivf status"),
        EGG_DONOR("egg donor"),
        EGG_DONOR_AGE("Egg Donor Age"),
        FETUS("Number of Fetus");
    
    
        
        
        private final String text;
        Validation(final String text){
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
}
  
public enum ErrorMessages {
    EGG_DONOR_AGE_ERR_MESSAGE("Egg Donor Age "),
    EGGDONORAGE_METERNALAGE_ERR_MESSAGE("Egg donor age should be less than or equal to the Maternal age."),
	ORDER_ID_NULL("OrderId is null"),
	ORDER_STATUS_NULL("Order Status is null"),
	ORDER_NOT_FOUND("Order Not found"),
	WORKFLOW_ORDERS_NOT_FOUND("In-workflow orders not found"),
	UNASSIGNED_ORDERS_NOT_FOUND("unassigned orders not found"),
	ORDERCOMMENTS_MAX_LENGTH_ERR_MESSAGE("Allowed maximum field length is 150 characters"),
	VALIDATION_IN_VALID_ERR_MESSAGE("is not Valid"),
	VALIDATION_IN_VALID_ERR("should be empty"),
	VALIDATION_FUTURE_DATE_ERR_MESSAGE("Sample Received Date should not be Future date"),
	VALIDATION_MAX30LENGTH_ERR_MESSAGE("allowed Maximum field length is 30 characters"),
	VALIDATION_FUTURE_PATIENT_DOB_DATE_ERR_MESSAGE("PatientDOB should not be Future date"),
	VALIDATION_PATIENT_DOB_YEAR_ERR_MESSAGE("PatientDOB should not be >99 years"),
	VALIDATION_PATIENT_DOB_INVALID_ERR_MESSAGE("PatientDOB is not Valid"),
	VALIDATION_NUMERICS_ERR_MESSAGE("is Not Valid , Allowed Maximum Field length is 20 and should be Numeric"),
	ACCESSIONING_ID_NULL("accessioningID is null"),
	OWNER_NOT_VALID("Owner is not valid"),
	CONTAINERIDS_NOT_FOUND("Container Not found"),
	STATUS_NULL("Status is null"),
	ACCESSIONING_ID_DUPLICATE("Accessioning ID already exists. Please enter unique value"),
	ACCOUNT_NUMBER_DUPLICATE("Account Number already exists. Please enter unique value"),
	ERROR_WHILE_ENCRYPTION("Error while encryption"),
	VALIDATION_IN_RETEST_ERR_MESSAGE("Invalid data type value"),
	VALIDATION_REFERRALREASON_LENGTH_ERR_MESSAGE("allowed maximum field length is 40 characters"),
	VALIDATION_ACCOUNTNUMBER_MINLENGTH_ERR_MESSAGE("allowed minimum field length is 3 characters"),
	VALIDATION_ACCOUNTNUMBER_MAXLENGTH_ERR_MESSAGE("allowed maximum field length is 20 characters"),
	VALIDATION_ACCOUNTNUMBER_ERR_MESSAGE("allowed characters are alphabets and numbers"),
	VALIDATION_LABID_ERR_MESSAGE("Allowed characters are alphabets, numbers and hyphen."),
	VALIDATION_CLINICNAME_ERR_MESSAGE("allowed characters are alphabets, hyphen, apostrophe and one space"),
	VALIDATION_ONESPACE_ERR_MESSAGE("repeated spaces are not allowed in this field"),
	
	//CSV ERROR MESSAGES
	INVALID_DELETION_OF_CONTAINER_SAMPLES_MAPPING("Delete is not possible as the MP96 run results for the XXXX  have already been sent"),
	INCORRECT_HEADER_LENGTH("Header Length is incorrect"),	
	CSV_HEADER_VALIDATION_FAILURE("CSV header validation failed"),
	CONTAINER_ID_NOT_FOUND("ContainerId Not found"),
    CONTAINER_ID_NULL("ContainerId is null"),	
	ORDER_NOT_PRESENT("Order does not exist for Accessioning ID %s in connect."),
	DATA_MISSING("Data is missing in row"),
	VALIDATION_ERROR("Error while validating CSV"),
	NO_DATA_PRESENT("doesn't have data, please fill the data and upload"),
	CONTAINER_ID_FORMAT_INCORRECT("Incorrect data format. Accepted values for 'Container ID' column is 'WP 101-999'."),
	//CONTAINER_LOCATION_FORMAT_INCORRECT("Incorrect data format. Accepted values for 'Location' column is A1-H1, A2-H2......A12-H12."),
	CONTAINER_LOCATION_FORMAT_INCORRECT("Incorrect data format. Accepted values for the 'Position' column is A1-H1, A2-H2......A12-H12."),
	CONTAINER_TYPE_DATA_INCORRECT("Incorrect data format. Accepted value for the 'Container Type' column is '96 wellplate'."),
	//LOCATION_EMPTY("Missing 'Location'."),
	LOCATION_EMPTY("Missing 'Position'."),
	CONTAINER_TYPE_EMPTY("Missing 'Container Type'."),
	CONTAINER_ID_EMPTY("Missing 'Container ID'."),
	ACCESSIONING_ID_EMPTY("Missing 'Accessioning ID'."),
	ACCESSIONING_ID_DUPLICATE_IN_CSV("'Accessioning ID' already exists in row number %s."),
	//INCORRECT_HEADER("Incorrect header format. Valid format of csv file header is 'Container Type, Container ID, Location, Accessioning ID'"),
	INCORRECT_HEADER("Incorrect header format. Valid format of csv file header is 'Container Type, Container ID, Position, Accessioning ID'"),
	//ACCESSIONING_ID_DUPLICATE_IN_DB("Accessioning ID is already mapped to different Container ID %s + Location %s in Connect."),
	ACCESSIONING_ID_DUPLICATE_IN_DB("Accessioning ID is already mapped to different Container ID %s + Position %s in Connect."),
	//CONTAINERID_POSITION_DUPLICATE_CSV("'Location' already exists in row number %s."),
	CONTAINERID_POSITION_DUPLICATE_CSV("'Position' already exists in row number %s."),
	//CONTAINERID_POSITION_DUPLICATE_DB("Container ID + Location is already mapped to another Accessioning ID %s in Connect."),
	CONTAINERID_POSITION_DUPLICATE_DB("Container ID + Position is already mapped to another Accessioning ID %s in Connect."),
	ASSAY_TYPE_INVALID("Invalid Assay Type"),
	INVALID_COLLECTION_DATE("Received date should be same as collection date or greater than collection date"),
	INVALID_FIRST_NAME_FORMAT("Firstname is invalid"),
	INVALID_LAST_NAME_FORMAT("Lastname is invalid"),
	INVALID_OTHER_CLINICIAN_NAME_FORMAT("Other clinician name is invalid"),
	INVALID_REF_CLINICIAN_CLINIC_NAME_FORMAT("Ref clinician clinic name is invalid"),
	INVALID_REF_CLINICIAN_NAME_FORMAT("Ref clinician name is invalid"),
	INVALID_TREATING_DOCTOR_NAME_FORMAT("Treating doctor name is invalid"),
	ACCESSIONING_ID_ERROR_MSG1("Allowed maximum field length for Accessioning ID is 20 characters"),
	ACCESSIONING_ID_ERROR_MSG2("Accessioning ID must have at least one alpha numeric character."),
	ACCESSIONING_ID_ERROR_MSG3("Allowed special characters for Accessioning ID are hyphen and underscore"),
	FILE_EMPTY("Records are missing in CSV file."),
	FILE_DOWNLOAD_FAILED("Failed to dowload template."),
	BULK_ORDER_VALIDATION_FAILED("File exceeds allowed size of 1MB."),
	FILE_ROWS_EXCEEDED("Maximum allowed orders count is "),
	BULK_ORDER_ERROR_OCCURED("Error occured. Kindly re-try upload."),
	INVALID_CSV_TEMPLATE("Invalid CSV format.");
	
	private final String text;
	 ErrorMessages(final String text){
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
}

	private OMMConstant(boolean boolValue) {
		boolVal = boolValue;
	}

	private OMMConstant(String strValue) {
		strVal = strValue;
	}

	private OMMConstant(Integer intValue) {
		intVal = intValue;
	}

	private OMMConstant() {
	}

	public boolean isBoolVal() {
		return boolVal;
	}

	@Override
	public String toString() {
		if (strVal != null) {
			return strVal;
		}
		return super.toString();
	}

	public Integer toInteger() {
		if (intVal != null) {
			return intVal;
		}
		return 0;
	}

	public enum CSV{		
		CONTAINER_TYPE("Container Type"),
		CONTAINER_ID("Container ID"),
		//LOCATION("Location"),
		POSITION("Position"),
		ACCESSIONING_ID("Accessioning ID"),
		//DEVICE_ID("DeviceId"),
		HEADER_SIZE(4),		
		CONTAINER_TYPE_INDEX(0),
		CONTAINER_ID_INDEX(1),
		//LOCATION_INDEX(2),
		POSITION_INDEX(2),
		ACCESSIONING_ID_INDEX(3),
		//DEVICE_ID_INDEX(4),
		STATUS_OPEN("open"),
		STATUS_SENT_TO_DEVICE("senttodevice"),
		STATUS_PROCESSED("processed"),
		NIPTDPCR("NIPTDPCR"),
		NIPTHTP("NIPTHTP"),
		FEMALEGENDER("FEMALE"),
		ACTIVE_FLAG("Y"),
	    INACTIVE_FLAG("N"),
		DELIMETER(","),
		FIRST_HEADER_ROW(0),
		//REGEX_FOR_CONTAINER_ID("WP [1-9][0-9][1-9]"),
		//REGEX_FOR_CONTAINER_ID("^[^-]{1}?[^\"\']*$"),
		REGEX_FOR_CONTAINER_ID("^[a-zA-Z0-9-_]+$"),
		REGEX_FOR_LOCATION_1("[A-H][1-9]"),
		REGEX_FOR_LOCATION_2("[A-H]1[0-2]"),
		CONTAINER_TYPE_NAME("96 wellplate"),
		CSV_FILE_SIZE(1048576),
		ACCESSIONINGID("Accessioning ID*"),
		ASSAY_TYPE("Assay type*"),
		SAMPLE_TYPE("Sample type*"),
		RETEST_SAMPLE("Retest sample available* (Yes /No)"),
		COMMENTS("Comments"),
		FIRST_NAME("First name"),
		LAST_NAME("Last name"),
		MEDICAL_REORD_NUMBER("Medical record number"),
		DOB("Date of birth (MM/DD/YYYY)"),
		REF_CLINICIAN("Referring clinician"),
		CLINIC_NAME("Clinic name"),
		REASON_FOR_REFERRAL("Reason for referral"),
		OTHER_CLINICIAN("Other clinician"),
		MATERNAL_AGE("Maternal age (0 to 100)"),
		GESTATIONAL_AGE("Gestational age (weeks) (10 to 40)"),
		GESTATIONAL_AGE_DAYS("Gestational age (days) (0 to 6)"),
		IVF_STATUS("IVF Status (Yes/No/Missing)"),
		EGG_DONAR("Egg donor (Self/Non-self/Missing)"),
		EGG_DONAR_AGE("Egg donor age (12 to 76)"),
		TEST_OPTIONS("Test option: T13 T18 T21* (Yes/ No)"),
		TEST_OPTION_FETAL_SEX("Test options: Fetal sex* (Yes/ No)"),
		NO_OF_FETUS("Number of fetus (1/2)"),
		COLLECTION_DATE("Collection date* (MM/DD/YYYY)"),
		RECEIVED_DATE("Received date* (MM/DD/YYYY)"),
		NIPT_DPCR("NIPT-dPCR"),
		ONE_SPACE("([A-Za-z-']+ )+[A-Za-z-']+$|^[A-Za-z-']+$"),
		SPECIAL_CHAR_RES("^[a-zA-Z\\s-']+$"),
		ROW("Row - "),
		VALIDATE_ONY_NUMERIC("\\+?\\d+"),
		LAB_ID("Lab ID"),
		ACCOUNT("Account #"),
		ISO_DATE_PATTERN("MM/dd/yyyy"),
		INPUT_DATA_VALIDATION_LIST("inputDataValidationslist"),
		ASSAY_API_PATH("/json/rest/api/v1/assay/");
		
		private CSV(String strValue){
			strVal = strValue;
		}
		private CSV(int  intvalue){
			intVal = intvalue;
		}
		private String strVal;
		private Integer intVal;
		
		@Override
		public String toString() {
			if (strVal != null) {
				return strVal;
			}
			return super.toString();
		}
	
		public Integer toInteger() {
			if (intVal != null) {
				return intVal;
			}
			return 0;
		}
	}
	
	public enum RequiredFieldValidation {		
		COMMENTS("Comments"),
		MATERNAL_AGE("Maternal age"),
		GESTATIONAL_AGE("Gestational age"),
		IVF_STATUS("IVF status"),
		NUMBER_OF_FETUS("Number of fetus"),
		FIRST_NAME("First name"),
		LAST_NAME("Last name"),
		MEDICAL_RECORD_NUMBER("Medical record number"),
		DOB("DOB"),
		REFERRING_CLINICIAN("Referring clinician"),
		LABORATORY_ID("Laboratory ID"),
		OTHER_CLINICIAN("Other clinician"),
		CLINIC_NAME("Clinic name"),
		REASON_FOR_REFERRAL("Reason for Referral"),
		ACCOUNT_NUMBER("Account #"),
		MANDATORY_FLAG("mandatory flag");
		
		private RequiredFieldValidation(String strValue){
			strVal = strValue;
		}

		private String strVal;
		private Integer intVal;
		private boolean boolVal;
		
		@Override
		public String toString() {
			if (strVal != null) {
				return strVal;
			}
			return super.toString();
		}
	
		public Integer toInteger() {
			if (intVal != null) {
				return intVal;
			}
			return 0;
		}
		public boolean isBoolVal() {
			return boolVal;
		}
	}
}