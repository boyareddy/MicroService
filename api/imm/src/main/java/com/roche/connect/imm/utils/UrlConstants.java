/*******************************************************************************
 * UrlConstants.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
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
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.utils;

/**
 * The Class UrlConstants.
 */
public class UrlConstants {

	/** The Constant BASE_URL_V1. */
	public static final String BASE_URL_V1 = "/rest/api/v1";

	/** The Constant MP_STATUS. */
	public static final String MP_STATUS = "/MPStatus";

	/** The Constant LP_QUERY. */
	public static final String LP_QUERY = "/LPQuery";

	/** The Constant LP_STATUS. */
	public static final String LP_STATUS = "/LPStatus";

	/** The Constant HTP_STATUS. */
	public static final String HTP_STATUS = "/htpstatus";

	public static final String MP96_WFM_WORK_ORDER_REQ_URL = "/json/rest/api/v1/NIPTdPCR/startwfmprocess";
	public static final String MP96_WFM_ACK_UPDATE = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/ACKMP96";
	public static final String MP96_WFM_OUL_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/MP96Status";

	public static final String MP96_ADAPTOR_WORK_ORDER_RSP_URL = "/json/rest/api/v1/sendrsptomp96";
	public static final String MP96_ADAPTOR_OUL_ACK_URL = "/json/rest/api/v1/sendacktomp96";

	public static final String MP24_ADAPTOR_RSP_URL = "/json/rest/api/v1/sendrsptomp24";
	public static final String MP24_ADAPTOR_ACK_URL = "/json/rest/api/v1/sendacktomp24";
	
	public static final String LP24_ADAPTOR_RSP_URL = "/json/rest/api/v1/responseMessage";
	public static final String LP24_ADAPTOR_OUL_ACK_URL = "/json/rest/api/v1/acknowledgementMessage";
	
	public static final String DPCR_ANALYZER_ADAPTOR_ACK_URL = "/json/rest/api/v1/sendacktodpcr";
	public static final String DPCR_ANALYZER_ADAPTOR_OML_URL = "/json/rest/api/v1/sendomltodpcr";
	
	public static final String OMM_CONTAINER_SAMPLES_URL = "/json/rest/api/v1/containersamples";
	public static final String OMM_ORDER_DETAILS_URL = "/json/rest/api/v1/orders?accessioningID=";
	public static final String OMM_ORDER_URL = "/json/rest/api/v1/order/";
	public static final String OMM_ORDER_BY_ACCESSIONING_ID_URL = "/json/rest/api/v1/orders";
	public static final String OMM_CONTAINER_SAMPLES_VALIDATE ="/json/rest/api/v1/containersamples/validateStatus";
	

	public static final String FORTE_WFM_STATUS_UPDATE = "/json/rest/api/v1/updatewfmprocess/FORETEStatus";

	public static final String RMM_RUN_RESULT_URL = "/json/rest/api/v1/runresults";
	public static final String RMM_SAMPLE_RESULTS_URL = "/json/rest/api/v1/runresults/sampleresults";
	public static final String RMM_RUN_RESULTS_AND_SAMPLE_URL = "/json/rest/api/v1/runresults/runresultsByDeviceId";
	public static final String RMM_RUN_RESULTS_BY_DEVICE_RUNID_URL = "/json/rest/api/v1/runresults/devicerunid";

	public static final String WFM_MP24_RUN_RESULT_UPDATE_URL = "/json/rest/api/v1/updatewfmprocess/MP24UpdateRunResult";
	public static final String WFM_LP24_RUN_RESULT_UPDATE_URL = "/json/rest/api/v1//NIPTdPCR/updatewfmprocess/LP24UpdateRunResult";
	public static final String WFM_DPCR_LP24_QUERY_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/LPQuery";
	public static final String WFM_DPCR_LP24_SSU_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/LPStatus";
	public static final String WFM_DPCR_ANALYZER_QUERY_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/AnalyzerQuery";
	public static final String WFM_DPCR_ANALYZER_ACK_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/ACKAnalyzer";
	public static final String WFM_DPCR_ANALYZER_ESU_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/AnalyzerStatus";
	public static final String WFM_DPCR_ANALYZER_ORU_URL = "/json/rest/api/v1/NIPTdPCR/updatewfmprocess/AnalyzerORU";
	
	public static final String ADM_NOTIFICATION_URL = "/json/rest/api/v1/notification";
	
	
	public static final String DEVICE_MGMT_URL = "/json/device/fetch/expr?filterExpression=";

	/**
	 * Instantiates a new url constants.
	 */
	private UrlConstants() {

	}

}
