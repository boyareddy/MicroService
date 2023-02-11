/*******************************************************************************
 *  RMMConstant.java                  
 *  Version:  1.0
 * 
 *  Authors:  Varun Kumar
 * 
 *  ==================================
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
 *  ==================================
 *  ChangeLog:
 ******************************************************************************/
package com.roche.connect.rmm.util;

public class RMMConstant {
	
	public static final String ONGOING = "ongoing";
	public static final String PENDING = "pending";
	public static final String PARTIALLYMOVED = "partiallymoved";
	public static final String ARCHIVED = "archived";
	public static final String ASSAYTYPENIPTHTP = "NIPTHTP";
	public static final String ASSAYTYPENIPTDPCR = "NIPTDPCR";
	public static final String ASSAYTYPE = "assayType";
	public static final String RUNSTATUS = "runStatus";
	public static final String PROCESSSTEPNAME = "processStepName";
	public static final String UTF_8 = "UTF-8";
	public static final int SEARCH_KEY_MIN_LENGTH = 3;
	public static final String SEARCH_ORDER_CONTENT_QUERY_PARAM = "order";
	public static final String SEARCH_RUN_CONTENT_QUERY_PARAM = "run";
	public static final String SEARCH_BOTH_CONTENT_QUERY_PARAM = "both";
	
	public enum RunStatus {
		OPEN("Open"),
		CLOSE("Close"),
		COMPLETED("Completed"),
		COMPLETEDWITHFLAGS("Completed with flags"),
		ABORTED("Aborted"),
		FAILED("Failed"),
		INPROGRESS("InProgress");
		

		private final String text;
		RunStatus(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	
	
	public enum ProcessStepValues {
	    
	    NAEXTRACTION("NA Extraction"),
	    LIBRARYPREPARATION("Library Preparation"),
	    DPCR("dPCR"),
	    LPPREPCR("LP Pre PCR"),
	    LPPOSTPCR("LP Post PCR/Pooling"),
	    LPSEQUENCING("LP Sequencing Prep"),
	    SEQUENCING("Sequencing"),
	    ANALYSIS("Analysis");
	    
	    private final String text;
	    ProcessStepValues(final String text){
            this.text = text;
        }
        
        @Override
        public String toString() {
            return text;
        }
	
	}
public enum ErrorMessages {
	RUNRESULT_ID_NULL("Runresult is null"),
	RUNRESULT_STATUS_NULL("RunResult Status is null"),
	RUNRESULT_NOT_FOUND("Runresult Not found"),
	WORKFLOW_ORDERS_NOT_FOUND("In-workflow orders not found"),
	INVALID_ACCESSIONING_ID("In-valid accessioningID"),
	ACCESSIONING_ID_NULL("accessioningID is null"),
	PROCESS_STEP_NAME_NULL("ProcessStepName is null"),
	SAMPLERESULTS_NOT_FOUND("SampleResults are Not found"),
	DEVICE_RUN_ID_NULL("DeviceRunId is null"),
	ASSAYTYPE_RUNSTATUS_PROCESS_STEP_NAME_NULL("assayType or runstatus or  processStep name not found"),
	ASSAYTYPE_NOT_FOUND("assayType not found"),
	PROCESS_STEP_NAME_NOT_FOUND("ProcessStepName is invalid as per AMM configuration"),
	INVALID_ACCESSIONING_ID_JASPER_REPORTS("AccessioningId is invalid."),
	INVALID_DEVICE_RUN_ID("Run id is invalid.");
	
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
