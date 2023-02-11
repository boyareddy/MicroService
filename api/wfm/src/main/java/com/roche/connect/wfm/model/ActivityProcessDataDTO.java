/*******************************************************************************
 * 
 * 
 *  ActivityProcessDataDTO.java                  
 *  Version:  1.0
 * 
 *  Authors:  somesh_r
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
 *   somesh_r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: ActivityProcessData Bean class.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.htp.HtpStatusMessage;
import com.roche.connect.common.lp24.AcknowledgementMessage;
import com.roche.connect.common.lp24.QueryMessage;
import com.roche.connect.common.lp24.ResponseMessage;
import com.roche.connect.common.lp24.SpecimenStatusUpdateMessage;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.common.mp24.message.AdaptorResponseMessage;
import com.roche.connect.common.mp96.WFMoULMessage;
import com.roche.connect.common.mp96.WFMQueryResponseMessage;
import com.roche.connect.common.dpcr_analyzer.WFMESUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMORUMessage;
import com.roche.connect.common.dpcr_analyzer.WFMQueryMessage;

/**
 * ActivityProcessData Bean class.
 *
 */
public class ActivityProcessDataDTO implements Serializable {
    
    /**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String accessioningId;
    private String deviceId;
    private String messageType;
    private String dateTimeMessageGenerated;
    private String messageControlId;
    private String batchId;
    private String containerId;   
    private String sendingApplicationName;
    private AdaptorResponseMessage qbpResponseMessage;
    private AdaptorResponseMessage u03ResponseMessage;
    private WFMQueryResponseMessage qbpResponseMsg;
    private AcknowledgementMessage sendLP24SeqPrepACKResponce;
    private QueryMessage queryMessage;
    private SpecimenStatusUpdateMessage specimenStatusUpdateMessage;
    private AdaptorRequestMessage adaptorRequestMessage;
    private HtpStatusMessage htpStatusMessage;
    private ForteStatusMessage forteStatusMessage;
    private ResponseMessage responseMessage;
    private WFMoULMessage u03RequestMessage;
    private WFMQueryMessage wfmQueryMessage;
    private WFMESUMessage wfmESUMessage;
	private WFMORUMessage oRUMessage;
    
    
	public WFMESUMessage getWfmESUMessage() {
        return wfmESUMessage;
    }
    public void setWfmESUMessage(WFMESUMessage wfmESUMessage) {
        this.wfmESUMessage = wfmESUMessage;
    }
    public WFMQueryMessage getWfmQueryMessage() {
        return wfmQueryMessage;
    }
    public void setWfmQueryMessage(WFMQueryMessage wfmQueryMessage) {
        this.wfmQueryMessage = wfmQueryMessage;
    }
    public WFMoULMessage getU03RequestMessage() {
		return u03RequestMessage;
	}
	public void setU03RequestMessage(WFMoULMessage u03RequestMessage) {
		this.u03RequestMessage = u03RequestMessage;
	}
	private List<WfmDTO> orderDetails = new ArrayList<>();
	public String getAccessioningId() {
		return accessioningId;
	}
	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}
	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getContainerId() {
		return containerId;
	}
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	
	public String getSendingApplicationName() {
		return sendingApplicationName;
	}
	public void setSendingApplicationName(String sendingApplicationName) {
		this.sendingApplicationName = sendingApplicationName;
	}
	public AdaptorResponseMessage getQbpResponseMessage() {
		return qbpResponseMessage;
	}
	public void setQbpResponseMessage(AdaptorResponseMessage qbpResponseMessage) {
		this.qbpResponseMessage = qbpResponseMessage;
	}
	public WFMQueryResponseMessage getQbpResponseMsg() {
		return qbpResponseMsg;
	}
	public void setQbpResponseMsg(WFMQueryResponseMessage qbpResponseMsg) {
		this.qbpResponseMsg = qbpResponseMsg;
	}
	public AdaptorResponseMessage getU03ResponseMessage() {
		return u03ResponseMessage;
	}
	public void setU03ResponseMessage(AdaptorResponseMessage u03ResponseMessage) {
		this.u03ResponseMessage = u03ResponseMessage;
	}
	public AcknowledgementMessage getSendLP24SeqPrepACKResponce() {
		return sendLP24SeqPrepACKResponce;
	}
	public void setSendLP24SeqPrepACKResponce(AcknowledgementMessage sendLP24SeqPrepACKResponce) {
		this.sendLP24SeqPrepACKResponce = sendLP24SeqPrepACKResponce;
	}
	public QueryMessage getQueryMessage() {
		return queryMessage;
	}
	public void setQueryMessage(QueryMessage queryMessage) {
		this.queryMessage = queryMessage;
	}
	public SpecimenStatusUpdateMessage getSpecimenStatusUpdateMessage() {
		return specimenStatusUpdateMessage;
	}
	public void setSpecimenStatusUpdateMessage(SpecimenStatusUpdateMessage specimenStatusUpdateMessage) {
		this.specimenStatusUpdateMessage = specimenStatusUpdateMessage;
	}
	public AdaptorRequestMessage getAdaptorRequestMessage() {
		return adaptorRequestMessage;
	}
	public void setAdaptorRequestMessage(AdaptorRequestMessage adaptorRequestMessage) {
		this.adaptorRequestMessage = adaptorRequestMessage;
	}
	public HtpStatusMessage getHtpStatusMessage() {
		return htpStatusMessage;
	}
	public void setHtpStatusMessage(HtpStatusMessage htpStatusMessage) {
		this.htpStatusMessage = htpStatusMessage;
	}
	public ForteStatusMessage getForteStatusMessage() {
		return forteStatusMessage;
	}
	public void setForteStatusMessage(ForteStatusMessage forteStatusMessage) {
		this.forteStatusMessage = forteStatusMessage;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<WfmDTO> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<WfmDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getMessageControlId() {
		return messageControlId;
	}
	public void setMessageControlId(String messageControlId) {
		this.messageControlId = messageControlId;
	}
    public WFMORUMessage getoRUMessage() {
		return oRUMessage;
	}
	public void setoRUMessage(WFMORUMessage oRUMessage) {
		this.oRUMessage = oRUMessage;
	}
 
}
