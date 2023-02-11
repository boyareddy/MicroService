/*******************************************************************************
 *  MapDTOToEntity.java                  
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.htp.status.HTPStatusConstants;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.model.RunReagentsAndConsumables;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;

@Service
public class MapDTOToEntity {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	public RunResults getRunResultsFromDTO(RunResultsDTO runResultsDTO) throws HMTPException{
		RunResults runResults = new RunResults();
		try{
			runResults.setId(runResultsDTO.getRunResultId());
			runResults.setDeviceId(runResultsDTO.getDeviceId());
			runResults.setProcessStepName(runResultsDTO.getProcessStepName());
			runResults.setDeviceRunId(runResultsDTO.getDeviceRunId());
			
			if (HTPStatusConstants.IN_PROGRESS.getText().equalsIgnoreCase(runResultsDTO.getRunStatus()) || HTPStatusConstants.IN_PROCESS.getText().equalsIgnoreCase(runResultsDTO.getRunStatus()))
				runResults.setRunStatus(HTPStatusConstants.IN_PROGRESS.getText());
			else 
				runResults.setRunStatus(runResultsDTO.getRunStatus());
			
			runResults.setDvcRunResult(runResultsDTO.getDvcRunResult());
			runResults.setRunFlag(runResultsDTO.getRunFlag());
			runResults.setOperatorName(runResultsDTO.getOperatorName());
			runResults.setComments(runResultsDTO.getComments());
			runResults.setAssayType(runResultsDTO.getAssayType());
			runResults.setRunStartTime(runResultsDTO.getRunStartTime());
			runResults.setRunCompletionTime(runResultsDTO.getRunCompletionTime());
			runResults.setRunRemainingTime(runResultsDTO.getRunRemainingTime());
			runResults.setVerifiedDate(runResultsDTO.getVerifiedDate());
			runResults.setVerifiedBy(runResultsDTO.getVerifiedBy());
			runResults.setUpdatedBy(runResultsDTO.getUpdatedBy());
			runResults.setUpdatedDateTime(runResultsDTO.getUpdatedDateTime());
			runResults.setCreatedBy(runResultsDTO.getCreatedBy());
			runResults.setCreatedDate(runResultsDTO.getCreatedDateTime());
		}catch(Exception exp){
			logger.error("Error while mapping RunResultsDTO to RunResults : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}		
		return runResults;
	}
	
	public List<RunReagentsAndConsumables> getRunReagentsAndConsumablesFromDTO(RunResultsDTO runResultsDTO) throws HMTPException{
		List<RunReagentsAndConsumables> runReagentsAndConsumables = new ArrayList<>();
		try{
			runResultsDTO.getRunReagentsAndConsumables().stream().forEach(p->{
				RunReagentsAndConsumables runReagentsAndConsumable = new RunReagentsAndConsumables();
				runReagentsAndConsumable.setId(p.getRunReagentsAndConsumablesId());
				runReagentsAndConsumable.setType(p.getType());
				runReagentsAndConsumable.setAttributeName(p.getAttributeName());
				runReagentsAndConsumable.setAttributeValue(p.getAttributeValue());
				runReagentsAndConsumable.setUpdatedBy(p.getUpdatedBy());
				runReagentsAndConsumable.setUpdatedDateTime(p.getUpdatedDateTime());
				runReagentsAndConsumable.setCreatedBy(p.getCreatedBy());
				runReagentsAndConsumable.setCreatedDateTime(p.getCreatedDateTime());
				RunResults runResults = new RunResults();
				runResults.setId(runResultsDTO.getRunResultId());
				runReagentsAndConsumable.setRunResultsMappedToReagents(runResults);
				runReagentsAndConsumables.add(runReagentsAndConsumable);
			});
		}catch(Exception exp){
			logger.error("Error while mapping RunResultsDTO to RunReagentsAndConsumables : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}		
		return runReagentsAndConsumables;
	}
	
	public List<RunResultsDetail> getRunResultsDetailFromDTO(RunResultsDTO runResultsDTO) throws HMTPException{
		List<RunResultsDetail> runResultsDetails = new ArrayList<>();
		try{
			runResultsDTO.getRunResultsDetail().stream().forEach(p->{
				RunResultsDetail runResultsDetail = new RunResultsDetail();
				runResultsDetail.setId(p.getRunResultsDetailsId());
				runResultsDetail.setAttributeName(p.getAttributeName());
				runResultsDetail.setAttributeValue(p.getAttributeValue());
				runResultsDetail.setUpdatedBy(p.getUpdatedBy());
				runResultsDetail.setUpdatedDateTime(p.getUpdatedDateTime());
				runResultsDetail.setCreatedBy(p.getCreatedBy());
				runResultsDetail.setCreatedDate(p.getCreatedDateTime());
				RunResults runResults = new RunResults();
				runResults.setId(runResultsDTO.getRunResultId());
				runResultsDetail.setRunResultsMappedToRunDetails(runResults);
				runResultsDetails.add(runResultsDetail);
			});
		}catch(Exception exp){
			logger.error("Error while mapping RunResultsDTO to RunResultsDetail : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
		return runResultsDetails;
	}
	
	public SampleResults getSampleResultsFromDTO(SampleResultsDTO sampleResultsDTO) throws HMTPException{
		SampleResults sampleResults = new SampleResults();
		try{
			sampleResults.setAccesssioningId(sampleResultsDTO.getAccesssioningId());
			sampleResults.setOrderId(sampleResultsDTO.getOrderId());
			sampleResults.setInputContainerId(sampleResultsDTO.getInputContainerId());
			sampleResults.setOutputContainerId(sampleResultsDTO.getOutputContainerId());
			sampleResults.setOutputContainerType(sampleResultsDTO.getOutputContainerType());
			sampleResults.setReceivedDate(sampleResultsDTO.getReceivedDate());
			sampleResults.setVerifiedDate(sampleResultsDTO.getVerifiedDate());
			sampleResults.setVerifiedBy(sampleResultsDTO.getVerifiedBy());
			sampleResults.setInputContainerPosition(sampleResultsDTO.getInputContainerPosition());
			sampleResults.setInputKitId(sampleResultsDTO.getInputKitId());
			sampleResults.setOutputContainerPosition(sampleResultsDTO.getOutputContainerPosition());
			sampleResults.setOutputPlateType(sampleResultsDTO.getOutputPlateType());
			sampleResults.setOutputKitId(sampleResultsDTO.getOutputKitId());
			
			/*if (HTPStatusConstants.IN_PROGRESS.getText().equalsIgnoreCase(sampleResults.getStatus()) || HTPStatusConstants.IN_PROCESS.getText().equalsIgnoreCase(sampleResults.getStatus()))
				sampleResults.setStatus(HTPStatusConstants.IN_PROGRESS.getText());
			else 
				sampleResults.setStatus(sampleResultsDTO.getStatus());*/
			sampleResults.setStatus(sampleResultsDTO.getStatus());
			
			sampleResults.setResult(sampleResultsDTO.getResult());
			sampleResults.setFlag(sampleResultsDTO.getFlag());
			sampleResults.setComments(sampleResultsDTO.getComments());
			sampleResults.setUpdatedBy(sampleResultsDTO.getUpdatedBy());
			sampleResults.setUpdatedDateTime(sampleResultsDTO.getUpdatedDateTime());
			sampleResults.setCreatedBy(sampleResultsDTO.getCreatedBy());
			sampleResults.setCreatedDate(sampleResultsDTO.getCreatedDateTime());
			sampleResults.setSampleType(sampleResultsDTO.getSampleType());
		}catch(Exception exp){
			logger.error("Error while mapping SampleResultsDTO to SampleResults : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}		
		return sampleResults;
	}
	
	public List<SampleReagentsAndConsumables> getSampleReagentsAndConsumablesFromDTO(SampleResultsDTO sampleResultsDTO) throws HMTPException{
		List<SampleReagentsAndConsumables> sampleReagentsAndConsumables = new ArrayList<>();
		try{
			if(!sampleResultsDTO.getSampleReagentsAndConsumables().isEmpty() && sampleResultsDTO.getSampleReagentsAndConsumables()!=null) {
			sampleResultsDTO.getSampleReagentsAndConsumables().stream().forEach(p->{
				SampleReagentsAndConsumables sampleReagentsAndConsumable = new SampleReagentsAndConsumables();
				sampleReagentsAndConsumable.setType(p.getType());
				sampleReagentsAndConsumable.setAttributeName(p.getAttributeName());
				sampleReagentsAndConsumable.setAttributeValue(p.getAttributeValue());
				sampleReagentsAndConsumable.setUpdatedBy(p.getUpdatedBy());
				sampleReagentsAndConsumable.setUpdatedDateTime(p.getUpdatedDateTime());
				sampleReagentsAndConsumable.setCreatedBy(p.getCreatedBy());
				sampleReagentsAndConsumable.setCreatedDate(p.getCreatedDateTime());
				sampleReagentsAndConsumables.add(sampleReagentsAndConsumable);
			});
			}
		}catch(Exception exp){
			logger.error("Error while mapping SampleReagentsAndConsumablesDTO to SampleReagentsAndConsumables : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}		
		return sampleReagentsAndConsumables;
	}
	
	public List<SampleResultsDetail> getSampleResultsDetailFromDTO(SampleResultsDTO sampleResultsDTO) throws HMTPException{
		List<SampleResultsDetail> sampleResultsDetails = new ArrayList<>();
		try{
			if(!sampleResultsDTO.getSampleResultsDetail().isEmpty() && sampleResultsDTO.getSampleResultsDetail()!=null) {
			sampleResultsDTO.getSampleResultsDetail().stream().forEach(p->{
				SampleResultsDetail sampleResultsDetail = new SampleResultsDetail();
				sampleResultsDetail.setAttributeName(p.getAttributeName());
				sampleResultsDetail.setAttributeValue(p.getAttributeValue());
				sampleResultsDetail.setUpdatedBy(p.getUpdatedBy());
				sampleResultsDetail.setUpdatedDateTime(p.getUpdatedDateTime());
				sampleResultsDetail.setCreatedBy(p.getCreatedBy());
				sampleResultsDetail.setCreatedDate(p.getCreatedDateTime());
				sampleResultsDetails.add(sampleResultsDetail);
			});
			}
		}catch(Exception exp){
			logger.error("Error while mapping SampleResultsDetailDTO to SampleResultsDetail : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
		return sampleResultsDetails;
	}
	
	public List<SampleProtocol> getSampleProtocolFromDTO(SampleResultsDTO sampleResultsDTO) throws HMTPException{
		List<SampleProtocol> sampleProtocols = new ArrayList<>();
		try{
			if(!sampleResultsDTO.getSampleProtocol().isEmpty() && sampleResultsDTO.getSampleProtocol() != null) {
			sampleResultsDTO.getSampleProtocol().stream().forEach(p->{
				SampleProtocol sampleProtocol = new SampleProtocol();
				sampleProtocol.setProtocolName(p.getProtocolName());
				sampleProtocol.setUpdatedBy(p.getUpdatedBy());
				sampleProtocol.setUpdatedDateTime(p.getUpdatedDateTime());
				sampleProtocol.setCreatedBy(p.getCreatedBy());
				sampleProtocol.setCreatedDate(p.getCreatedDateTime());
				sampleProtocols.add(sampleProtocol);
			});
			}
		}catch(Exception exp){
			logger.error("Error while mapping SampleProtocolDTO to SampleProtocol : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
		return sampleProtocols;
	}
}
