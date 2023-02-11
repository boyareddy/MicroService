/*******************************************************************************
 *  MapEntityToDTO.java                  
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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.order.dto.ContainerDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.common.rmm.dto.RunReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SampleProtocolDTO;
import com.roche.connect.common.rmm.dto.SampleReagentsAndConsumablesDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDetailDTO;
import com.roche.connect.common.rmm.dto.SearchOrder;
import com.roche.connect.common.rmm.dto.SearchRunResult;
import com.roche.connect.rmm.model.RunReagentsAndConsumables;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleReagentsAndConsumables;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;

@Service
public class MapEntityToDTO {
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	public SampleResultsDTO getSampleDTOFromSampleResults(SampleResults sampleResults) throws HMTPException {
		SampleResultsDTO sampleResultsDTO = new SampleResultsDTO();
		try {
			sampleResultsDTO.setRunResultsId(sampleResults.getRunResultsId().getId());
			sampleResultsDTO.setSampleResultId(sampleResults.getId());
			sampleResultsDTO.setAccesssioningId(sampleResults.getAccesssioningId());
			sampleResultsDTO.setOrderId(sampleResults.getOrderId());
			sampleResultsDTO.setInputContainerId(sampleResults.getInputContainerId());
			sampleResultsDTO.setOutputContainerId(sampleResults.getOutputContainerId());
			sampleResultsDTO.setReceivedDate(sampleResults.getReceivedDate());
			sampleResultsDTO.setVerifiedDate(sampleResults.getVerifiedDate());
			sampleResultsDTO.setVerifiedBy(sampleResults.getVerifiedBy());
			sampleResultsDTO.setInputContainerPosition(sampleResults.getInputContainerPosition());
			sampleResultsDTO.setInputKitId(sampleResults.getInputKitId());
			sampleResultsDTO.setOutputContainerPosition(sampleResults.getOutputContainerPosition());
			sampleResultsDTO.setOutputContainerType(sampleResults.getOutputContainerType());
			sampleResultsDTO.setOutputKitId(sampleResults.getOutputKitId());
			sampleResultsDTO.setStatus(sampleResults.getStatus());
			sampleResultsDTO.setResult(sampleResults.getResult());
			sampleResultsDTO.setFlag(sampleResults.getFlag());
			sampleResultsDTO.setComments(sampleResults.getComments());
			sampleResultsDTO.setUpdatedBy(sampleResults.getUpdatedBy());
			sampleResultsDTO.setUpdatedDateTime(sampleResults.getUpdatedDateTime());
			sampleResultsDTO.setCreatedBy(sampleResults.getCreatedBy());
			sampleResultsDTO.setCreatedDateTime(sampleResults.getCreatedDate());
			sampleResultsDTO.setAssayType(sampleResults.getRunResultsId().getAssayType());
			sampleResultsDTO.setSampleType(sampleResults.getSampleType());
			// Sample reagents and consumables
			if (sampleResults.getSampleReagentsAndConsumables() != null
					&& !sampleResults.getSampleReagentsAndConsumables().isEmpty()) {
				List<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumablesDTOs = new ArrayList<>();
				SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = null;
				for (SampleReagentsAndConsumables sampleReagentsAndConsumables : sampleResults
						.getSampleReagentsAndConsumables()) {
					sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();
					sampleReagentsAndConsumablesDTO
							.setSampleReagentsAndConsumablesId(sampleReagentsAndConsumables.getId());
					sampleReagentsAndConsumablesDTO.setType(sampleReagentsAndConsumables.getType());
					sampleReagentsAndConsumablesDTO.setAttributeName(sampleReagentsAndConsumables.getAttributeName());
					sampleReagentsAndConsumablesDTO.setAttributeValue(sampleReagentsAndConsumables.getAttributeValue());
					sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleReagentsAndConsumables.getUpdatedBy());
					sampleReagentsAndConsumablesDTO.setCreatedBy(sampleReagentsAndConsumables.getCreatedBy());
					sampleReagentsAndConsumablesDTO
							.setCreatedDateTime(sampleReagentsAndConsumables.getCreatedDate());
					sampleReagentsAndConsumablesDTO
							.setUpdatedDateTime(sampleReagentsAndConsumables.getUpdatedDateTime());
					sampleReagentsAndConsumablesDTOs.add(sampleReagentsAndConsumablesDTO);
				}
				sampleResultsDTO.setSampleReagentsAndConsumables(sampleReagentsAndConsumablesDTOs);
			}

			// Sample protocol
			if (sampleResults.getSampleProtocol() != null && !sampleResults.getSampleProtocol().isEmpty()) {
				List<SampleProtocolDTO> sampleProtocolDTOs = new ArrayList<>();
				SampleProtocolDTO sampleProtocolDTO = null;
				for (SampleProtocol sampleProtocol : sampleResults.getSampleProtocol()) {
					sampleProtocolDTO = new SampleProtocolDTO();
					sampleProtocolDTO.setSampleProtocolId(sampleProtocol.getId());
					sampleProtocolDTO.setProtocolName(sampleProtocol.getProtocolName());
					sampleProtocolDTO.setUpdatedBy(sampleProtocol.getUpdatedBy());
					sampleProtocolDTO.setUpdatedDateTime(sampleProtocol.getUpdatedDateTime());
					sampleProtocolDTO.setCreatedBy(sampleProtocol.getCreatedBy());
					sampleProtocolDTO.setCreatedDateTime(sampleProtocol.getCreatedDate());
					sampleProtocolDTOs.add(sampleProtocolDTO);
				}
				sampleResultsDTO.setSampleProtocol(sampleProtocolDTOs);
			}

			// Sample results details
			if (sampleResults.getSampleResultsDetail() != null && !sampleResults.getSampleResultsDetail().isEmpty()) {
				List<SampleResultsDetailDTO> sampleResultsDetailDTOs = new ArrayList<>();
				SampleResultsDetailDTO sampleResultsDetailDTO = null;
				for (SampleResultsDetail sampleResultsDetail : sampleResults.getSampleResultsDetail()) {
					sampleResultsDetailDTO = new SampleResultsDetailDTO();
					sampleResultsDetailDTO.setSampleResultsDetailId(sampleResultsDetail.getId());
					sampleResultsDetailDTO.setAttributeName(sampleResultsDetail.getAttributeName());
					sampleResultsDetailDTO.setAttributeValue(sampleResultsDetail.getAttributeValue());
					sampleResultsDetailDTO.setUpdatedBy(sampleResultsDetail.getUpdatedBy());
					sampleResultsDetailDTO.setUpdatedDateTime(sampleResultsDetail.getUpdatedDateTime());
					sampleResultsDetailDTO.setCreatedBy(sampleResultsDetail.getCreatedBy());
					sampleResultsDetailDTO.setCreatedDateTime(sampleResultsDetail.getCreatedDate());
					sampleResultsDetailDTOs.add(sampleResultsDetailDTO);
				}
				sampleResultsDTO.setSampleResultsDetail(sampleResultsDetailDTOs);
			}

		} catch (Exception exp) {
			logger.error("Error while mapping SampleResults to SampleResultsDTO : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
		return sampleResultsDTO;
	}

	public ProcessStepValuesDTO mapsampleResultstoProcessDTO(SampleResults sampleResults) {
		ProcessStepValuesDTO processStepActionValue = new ProcessStepValuesDTO();
		List<String> protocolnames = new ArrayList<>();
		
		processStepActionValue.setAccesssioningId(sampleResults.getAccesssioningId());
		processStepActionValue.setSampleResultId(sampleResults.getId());
		processStepActionValue.setOrderId(sampleResults.getOrderId());
		processStepActionValue.setOutputContainerId(sampleResults.getOutputContainerId());
		processStepActionValue.setOutputContainerPosition(sampleResults.getOutputContainerPosition());
		processStepActionValue.setOutputKitId(sampleResults.getOutputKitId());
		processStepActionValue.setOutputContainerType(sampleResults.getOutputContainerType());
		processStepActionValue.setPlateType(sampleResults.getOutputPlateType());
		processStepActionValue.setRunResultId(sampleResults.getRunResultsId().getId());
		processStepActionValue.setDeviceId((sampleResults.getRunResultsId().getDeviceId()));

		processStepActionValue.setCreatedBy(sampleResults.getRunResultsId().getCreatedBy());
		processStepActionValue.setCreatedDateTime(sampleResults.getRunResultsId().getCreatedDate());

		processStepActionValue.setProcessStepName(sampleResults.getRunResultsId().getProcessStepName());

		processStepActionValue.setRunStatus((sampleResults.getStatus()));
		processStepActionValue.setRunFlag((sampleResults.getFlag()));
		processStepActionValue.setOperatorName((sampleResults.getRunResultsId().getOperatorName()));
		processStepActionValue.setComments((sampleResults.getComments()));
		processStepActionValue.setUpdatedBy((sampleResults.getRunResultsId().getUpdatedBy()));
		processStepActionValue.setRunStartTime((sampleResults.getRunResultsId().getRunStartTime()));
		processStepActionValue.setRunCompletionTime((sampleResults.getRunResultsId().getRunCompletionTime()));
		processStepActionValue.setRunRemainingTime((sampleResults.getRunResultsId().getRunRemainingTime()));
		processStepActionValue.setSampleType(sampleResults.getSampleType());
		List<RunResultsDetailDTO> runResultsDetailDTOs = null;
		if (sampleResults.getRunResultsId().getRunResultsDetail() != null
				&& !sampleResults.getRunResultsId().getRunResultsDetail().isEmpty()) {
			runResultsDetailDTOs = new ArrayList<>();
			for (RunResultsDetail runResultsDetail : sampleResults.getRunResultsId().getRunResultsDetail()) {
				RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
				runResultsDetailDTO.setAttributeName(runResultsDetail.getAttributeName());
				runResultsDetailDTO.setAttributeValue(runResultsDetail.getAttributeValue());
				runResultsDetailDTO.setRunResultsDetailsId(runResultsDetail.getId());
				runResultsDetailDTOs.add(runResultsDetailDTO);
			}
		}
		processStepActionValue.setRunResultsDetailDTO(runResultsDetailDTOs);

		List<SampleProtocolDTO> sampleprotocollist = convertSampleProtocolToSampleProtocolDTO(
				(List<SampleProtocol>) sampleResults.getSampleProtocol());
		for (SampleProtocolDTO sampleProtocol : sampleprotocollist) {
		    if(sampleProtocol.getProtocolName()!=null) {
			protocolnames.add(sampleProtocol.getProtocolName());
		    }
		}
		processStepActionValue.setProtocolName(protocolnames);

		// adding sample_results_detail for dPCR
		List<SampleResultsDetailDTO> sampleResultsDetailDTOs = new ArrayList<>();
		List<SampleResultsDetailDTO> sampleResultsDetailList = convertSampleResultsDetailToSampleResultsDetailDTO(
				(List<SampleResultsDetail>) sampleResults.getSampleResultsDetail());
		for(SampleResultsDetailDTO sampleResultsDetail:sampleResultsDetailList)
		{
			SampleResultsDetailDTO sampleResultsDetailDTO=new SampleResultsDetailDTO();
			sampleResultsDetailDTO.setAttributeName(sampleResultsDetail.getAttributeName());
			sampleResultsDetailDTO.setAttributeValue(sampleResultsDetail.getAttributeValue());
			sampleResultsDetailDTOs.add(sampleResultsDetailDTO);
		}
		List<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumablesDTO = convertSampleReagentsAndConsumablesToReagentDTO((List<SampleReagentsAndConsumables>) sampleResults.getSampleReagentsAndConsumables());
		processStepActionValue.setUniqueReagentsAndConsumables(sampleReagentsAndConsumablesDTO);
		processStepActionValue.setSampleResultsDetailDTO(sampleResultsDetailDTOs);

		return processStepActionValue;
	}

	public RunResultsDTO getRunResultsDTOFromRunResults(RunResults runResults) throws HMTPException {
		RunResultsDTO runResultsdto = new RunResultsDTO();
		try {
			runResultsdto.setRunResultId(runResults.getId());
			runResultsdto.setDeviceId(runResults.getDeviceId());
			runResultsdto.setProcessStepName(runResults.getProcessStepName());
			runResultsdto.setDeviceRunId(runResults.getDeviceRunId());
			runResultsdto.setRunStatus(runResults.getRunStatus());
			runResultsdto.setDvcRunResult(runResults.getDvcRunResult());
			runResultsdto.setAssayType(runResults.getAssayType());
			runResultsdto.setRunFlag(runResults.getRunFlag());
			runResultsdto.setOperatorName(runResults.getOperatorName());
			runResultsdto.setComments(runResults.getComments());
			runResultsdto.setRunStartTime(runResults.getRunStartTime());
			runResultsdto.setRunCompletionTime(runResults.getRunCompletionTime());
			runResultsdto.setRunRemainingTime(runResults.getRunRemainingTime());
			runResultsdto.setVerifiedDate(runResults.getVerifiedDate());
			runResultsdto.setVerifiedBy(runResults.getVerifiedBy());
			runResultsdto.setUpdatedBy(runResults.getUpdatedBy());
			runResultsdto.setUpdatedDateTime(runResults.getUpdatedDateTime());
			runResultsdto.setCreatedBy(runResults.getCreatedBy());
			runResultsdto.setCreatedDateTime(runResults.getCreatedDate());
			
			// Run results details
			if (!runResults.getRunResultsDetail().isEmpty()) {
				List<RunResultsDetailDTO> runResultsDetailDTOs = new ArrayList<>();
				RunResultsDetailDTO runResultsDetailDTO = null;
				for (RunResultsDetail runResultsDetail : runResults.getRunResultsDetail()) {
					runResultsDetailDTO = new RunResultsDetailDTO();
					runResultsDetailDTO.setRunResultsDetailsId(runResultsDetail.getId());
					runResultsDetailDTO.setAttributeName(runResultsDetail.getAttributeName());
					runResultsDetailDTO.setAttributeValue(runResultsDetail.getAttributeValue());
					runResultsDetailDTO.setUpdatedBy(runResultsDetail.getUpdatedBy());
					runResultsDetailDTO.setUpdatedDateTime(runResultsDetail.getUpdatedDateTime());
					runResultsDetailDTO.setCreatedBy(runResultsDetail.getCreatedBy());
					runResultsDetailDTO.setCreatedDateTime(runResultsDetail.getCreatedDate());
					runResultsDetailDTOs.add(runResultsDetailDTO);
				}
				runResultsdto.setRunResultsDetail(runResultsDetailDTOs);
			}
			
		} catch (Exception exp) {
			logger.error("Error while mapping RunResults to RunResultsDTO : " + exp.getMessage());
			throw new HMTPException(exp.getMessage());
		}
		return runResultsdto;
	}

	public SearchRunResult getSearchRunResultsDTOFromRunResults(RunResults runResults,
			final Map<String, String> deviceMap, Map<String, ContainerDTO> containerMap) {
		SearchRunResult searchRunResult = new SearchRunResult();

		searchRunResult.setRunResultId(runResults.getId());
		searchRunResult.setProcessStepName(runResults.getProcessStepName());
		searchRunResult.setDeviceRunId(runResults.getDeviceRunId());
		searchRunResult.setRunStatus(runResults.getRunStatus());
		searchRunResult.setOperatorName(runResults.getOperatorName());
		searchRunResult.setRunCompletionTime(runResults.getRunCompletionTime());
		searchRunResult.setSampleCount(runResults.getSampleResults().size());
		searchRunResult.setDevice(deviceMap.get(runResults.getDeviceId()));
		searchRunResult.setContainer(containerMap.get(runResults.getDeviceRunId()));
		if(containerMap.get(runResults.getDeviceRunId()) != null) 
			searchRunResult.setSampleCount(containerMap.get(runResults.getDeviceRunId()).getSampleCount());
		
		return searchRunResult;
	}

	public SearchOrder getSearchOrderFromSampleResults(SampleResults sampleResults) {
		SearchOrder searchOrder = new SearchOrder();

		searchOrder.setRunResultsId(sampleResults.getRunResultsId().getId());
		searchOrder.setSampleResultId(sampleResults.getId());
		searchOrder.setAccessioningId(sampleResults.getAccesssioningId());
		searchOrder.setOrderId(sampleResults.getOrderId());
		searchOrder.setStatus(sampleResults.getStatus());
		searchOrder.setFlags(sampleResults.getFlag());
		searchOrder.setAssayType(sampleResults.getRunResultsId().getAssayType());
		searchOrder.setSampleType(sampleResults.getSampleType());
		searchOrder.setProcessStepName(sampleResults.getRunResultsId().getProcessStepName());

		return searchOrder;
	}

	public SearchOrder getSearchOrderFromOrderDTO(OrderDTO orderDto) {
		SearchOrder searchOrder = new SearchOrder();

		searchOrder.setAccessioningId(orderDto.getAccessioningId());
		searchOrder.setOrderId(orderDto.getOrderId());
		searchOrder.setStatus(StringUtils.capitalize(orderDto.getOrderStatus()));
		searchOrder.setAssayType(orderDto.getAssayType());
		searchOrder.setSampleType(orderDto.getSampleType());

		return searchOrder;
	}
	
	public RunResultsDTO convertToDTO(SampleResults sampleResults) throws HMTPException {

		// Run results
		RunResultsDTO runResultsDTO = new RunResultsDTO();
		runResultsDTO.setRunResultId(sampleResults.getRunResultsId().getId());
		runResultsDTO.setDeviceId(sampleResults.getRunResultsId().getDeviceId());
		runResultsDTO.setProcessStepName(sampleResults.getRunResultsId().getProcessStepName());
		runResultsDTO.setDeviceRunId(sampleResults.getRunResultsId().getDeviceRunId());
		runResultsDTO.setRunStatus(sampleResults.getRunResultsId().getRunStatus());
		runResultsDTO.setDvcRunResult(sampleResults.getRunResultsId().getDvcRunResult());
		runResultsDTO.setRunFlag(sampleResults.getRunResultsId().getRunFlag());
		runResultsDTO.setOperatorName(sampleResults.getRunResultsId().getOperatorName());
		runResultsDTO.setComments(sampleResults.getRunResultsId().getComments());
		runResultsDTO.setRunStartTime(sampleResults.getRunResultsId().getRunStartTime());
		runResultsDTO.setAssayType(sampleResults.getRunResultsId().getAssayType());
		runResultsDTO.setRunCompletionTime(sampleResults.getRunResultsId().getRunCompletionTime());
		runResultsDTO.setRunRemainingTime(sampleResults.getRunResultsId().getRunRemainingTime());
		runResultsDTO.setVerifiedDate(sampleResults.getRunResultsId().getVerifiedDate());
		runResultsDTO.setVerifiedBy(sampleResults.getRunResultsId().getVerifiedBy());
		runResultsDTO.setUpdatedBy(sampleResults.getRunResultsId().getUpdatedBy());
		runResultsDTO.setUpdatedDateTime(sampleResults.getRunResultsId().getUpdatedDateTime());
		runResultsDTO.setCreatedBy(sampleResults.getRunResultsId().getCreatedBy());
		runResultsDTO.setCreatedDateTime(sampleResults.getRunResultsId().getCreatedDate());

		getSampleDTOFromSampleResults(sampleResults);

		// Run reagents and consumables
		if (!sampleResults.getRunResultsId().getRunReagentsAndConsumables().isEmpty()) {
			List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTOs = new ArrayList<>();
			RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = null;
			for (RunReagentsAndConsumables runReagentsAndConsumables : sampleResults.getRunResultsId()
					.getRunReagentsAndConsumables()) {
				runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
				runReagentsAndConsumablesDTO.setRunReagentsAndConsumablesId(runReagentsAndConsumables.getId());
				runReagentsAndConsumablesDTO.setType(runReagentsAndConsumables.getType());
				runReagentsAndConsumablesDTO.setAttributeName(runReagentsAndConsumables.getAttributeName());
				runReagentsAndConsumablesDTO.setAttributeValue(runReagentsAndConsumables.getAttributeValue());
				runReagentsAndConsumablesDTO.setUpdatedBy(runReagentsAndConsumables.getUpdatedBy());
				runReagentsAndConsumablesDTO.setUpdatedDateTime(runReagentsAndConsumables.getUpdatedDateTime());
				runReagentsAndConsumablesDTO.setCreatedBy(runReagentsAndConsumables.getCreatedBy());
				runReagentsAndConsumablesDTO.setCreatedDateTime(runReagentsAndConsumables.getCreatedDateTime());
				runReagentsAndConsumablesDTOs.add(runReagentsAndConsumablesDTO);
			}
			runResultsDTO.setRunReagentsAndConsumables(runReagentsAndConsumablesDTOs);
		}

		// Run results details
		if (!sampleResults.getRunResultsId().getRunResultsDetail().isEmpty()) {
			List<RunResultsDetailDTO> runResultsDetailDTOs = new ArrayList<>();
			RunResultsDetailDTO runResultsDetailDTO = null;
			for (RunResultsDetail runResultsDetail : sampleResults.getRunResultsId().getRunResultsDetail()) {
				runResultsDetailDTO = new RunResultsDetailDTO();
				runResultsDetailDTO.setRunResultsDetailsId(runResultsDetail.getId());
				runResultsDetailDTO.setAttributeName(runResultsDetail.getAttributeName());
				runResultsDetailDTO.setAttributeValue(runResultsDetail.getAttributeValue());
				runResultsDetailDTO.setUpdatedBy(runResultsDetail.getUpdatedBy());
				runResultsDetailDTO.setUpdatedDateTime(runResultsDetail.getUpdatedDateTime());
				runResultsDetailDTO.setCreatedBy(runResultsDetail.getCreatedBy());
				runResultsDetailDTO.setCreatedDateTime(runResultsDetail.getCreatedDate());
				runResultsDetailDTOs.add(runResultsDetailDTO);
			}
			runResultsDTO.setRunResultsDetail(runResultsDetailDTOs);
		}
		return runResultsDTO;
	}

	public List<SampleProtocolDTO> convertSampleProtocolToSampleProtocolDTO(List<SampleProtocol> sampleResultslist) {

		List<SampleProtocolDTO> sampleProtocoldtos = new ArrayList<>();
		for (SampleProtocol sampleProtocol : sampleResultslist) {
			SampleProtocolDTO sampleProtocolDTO = new SampleProtocolDTO();
			sampleProtocolDTO.setSampleProtocolId(sampleProtocol.getId());
			sampleProtocolDTO.setProtocolName(sampleProtocol.getProtocolName());
			sampleProtocolDTO.setUpdatedBy(sampleProtocol.getUpdatedBy());
			sampleProtocolDTO.setUpdatedDateTime(sampleProtocol.getUpdatedDateTime());
			sampleProtocolDTO.setCreatedBy(sampleProtocol.getCreatedBy());
			sampleProtocolDTO.setCreatedDateTime(sampleProtocol.getCreatedDate());
			sampleProtocoldtos.add(sampleProtocolDTO);
		}
		return sampleProtocoldtos;
	}

	public List<SampleReagentsAndConsumablesDTO> convertSampleReagentsAndConsumablesToReagentDTO(
			List<SampleReagentsAndConsumables> sampleReagentslist) {

		List<SampleReagentsAndConsumablesDTO> sampleReagentsAndConsumablesDTOs = new ArrayList<>();
		for (SampleReagentsAndConsumables sampleReagentsAndConsumables : sampleReagentslist) {
			SampleReagentsAndConsumablesDTO sampleReagentsAndConsumablesDTO = new SampleReagentsAndConsumablesDTO();

			sampleReagentsAndConsumablesDTO.setSampleReagentsAndConsumablesId(sampleReagentsAndConsumables.getId());
			sampleReagentsAndConsumablesDTO.setType(sampleReagentsAndConsumables.getType());
			sampleReagentsAndConsumablesDTO.setAttributeName(sampleReagentsAndConsumables.getAttributeName());
			sampleReagentsAndConsumablesDTO.setAttributeValue(sampleReagentsAndConsumables.getAttributeValue());
			sampleReagentsAndConsumablesDTO.setUpdatedBy(sampleReagentsAndConsumables.getUpdatedBy());
			sampleReagentsAndConsumablesDTO.setUpdatedDateTime(sampleReagentsAndConsumables.getUpdatedDateTime());
			sampleReagentsAndConsumablesDTO.setCreatedBy(sampleReagentsAndConsumables.getCreatedBy());
			sampleReagentsAndConsumablesDTO.setCreatedDateTime(sampleReagentsAndConsumables.getCreatedDate());
			sampleReagentsAndConsumablesDTOs.add(sampleReagentsAndConsumablesDTO);

		}
		return sampleReagentsAndConsumablesDTOs;
	}

	public List<SampleResultsDetailDTO> convertSampleResultsDetailToSampleResultsDetailDTO(
			List<SampleResultsDetail> sampleResultsDetaillist) {

		List<SampleResultsDetailDTO> sampleResultsDetailDTOs = new ArrayList<>();
		for (SampleResultsDetail sampleResultsDetail : sampleResultsDetaillist) {
			SampleResultsDetailDTO sampleResultsDetailDTO = new SampleResultsDetailDTO();
			sampleResultsDetailDTO.setSampleResultsDetailId(sampleResultsDetail.getId());
			sampleResultsDetailDTO.setAttributeName(sampleResultsDetail.getAttributeName());
			sampleResultsDetailDTO.setAttributeValue(sampleResultsDetail.getAttributeValue());
			sampleResultsDetailDTO.setUpdatedBy(sampleResultsDetail.getUpdatedBy());
			sampleResultsDetailDTO.setUpdatedDateTime(sampleResultsDetail.getUpdatedDateTime());
			sampleResultsDetailDTO.setCreatedBy(sampleResultsDetail.getCreatedBy());
			sampleResultsDetailDTO.setCreatedDateTime(sampleResultsDetail.getCreatedDate());
			sampleResultsDetailDTOs.add(sampleResultsDetailDTO);
		}
		return sampleResultsDetailDTOs;
	}

	public List<RunReagentsAndConsumablesDTO> convertRunReagentsAndConsumablesToRunReagentsAndConsumablesDTO(
			List<RunReagentsAndConsumables> runReagentsList) {

		List<RunReagentsAndConsumablesDTO> runReagentsAndConsumablesDTOs = new ArrayList<>();
		for (RunReagentsAndConsumables runReagentsAndConsumables : runReagentsList) {
			RunReagentsAndConsumablesDTO runReagentsAndConsumablesDTO = new RunReagentsAndConsumablesDTO();
			runReagentsAndConsumablesDTO.setRunReagentsAndConsumablesId(runReagentsAndConsumables.getId());
			runReagentsAndConsumablesDTO.setType(runReagentsAndConsumables.getType());
			runReagentsAndConsumablesDTO.setAttributeName(runReagentsAndConsumables.getAttributeName());
			runReagentsAndConsumablesDTO.setAttributeValue(runReagentsAndConsumables.getAttributeValue());
			runReagentsAndConsumablesDTO.setUpdatedBy(runReagentsAndConsumables.getUpdatedBy());
			runReagentsAndConsumablesDTO.setUpdatedDateTime(runReagentsAndConsumables.getUpdatedDateTime());
			runReagentsAndConsumablesDTO.setCreatedBy(runReagentsAndConsumables.getCreatedBy());
			runReagentsAndConsumablesDTO.setCreatedDateTime(runReagentsAndConsumables.getCreatedDateTime());
			runReagentsAndConsumablesDTOs.add(runReagentsAndConsumablesDTO);
		}
		return runReagentsAndConsumablesDTOs;
	}

	// Run results details
	public List<RunResultsDetailDTO> convertRunResultsDetailToRunResultsDetailDTODTO(
			List<RunResultsDetail> runresultslist) {
		List<RunResultsDetailDTO> runResultsDetailDTOs = new ArrayList<>();
		for (RunResultsDetail runResultsDetail : runresultslist) {
			RunResultsDetailDTO runResultsDetailDTO = new RunResultsDetailDTO();
			runResultsDetailDTO.setRunResultsDetailsId(runResultsDetail.getId());
			runResultsDetailDTO.setAttributeName(runResultsDetail.getAttributeName());
			runResultsDetailDTO.setAttributeValue(runResultsDetail.getAttributeValue());
			runResultsDetailDTO.setUpdatedBy(runResultsDetail.getUpdatedBy());
			runResultsDetailDTO.setUpdatedDateTime(runResultsDetail.getUpdatedDateTime());
			runResultsDetailDTO.setCreatedBy(runResultsDetail.getCreatedBy());
			runResultsDetailDTO.setCreatedDateTime(runResultsDetail.getCreatedDate());
			runResultsDetailDTOs.add(runResultsDetailDTO);
		}
		return runResultsDetailDTOs;
	}
}
