
/*******************************************************************************
 * HtpSimulatorRestApiImpl.java                  
 *  Version:  1.0
 * 
 *  Authors:  umashankar d
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
 *  umashankar-d@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.forte.adapter.rest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.forte.adapter.model.ComplexIdDetails;
import com.roche.connect.forte.adapter.model.Cycle;
import com.roche.connect.forte.adapter.model.ForteJob;
import com.roche.connect.forte.adapter.repository.ComplexIdDetailsWriteRepository;
import com.roche.connect.forte.adapter.repository.CycleWriteRepository;
import com.roche.connect.forte.adapter.repository.ForteJobWriteRepository;
import com.roche.connect.forte.adapter.services.RunService;
import com.roche.connect.forte.adapter.util.CycleStatus;
import com.roche.connect.forte.adapter.util.ForteConstans;

/**
 * The Class HtpSimulatorRestApiImpl.
 */
@Component
public class ForteAdapterRestApiImpl implements ForteAdapterRestApi {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(ForteAdapterRestApiImpl.class);

	/** The htp simulator service. */
	@Autowired
	RunService forteSimulatorService;

	/** The cycle write repository. */
	@Autowired
	CycleWriteRepository cycleWriteRepository;
	
	@Autowired
	ForteJobWriteRepository forteJobWriteRepository;

	@Autowired
	ComplexIdDetailsWriteRepository complexIdDetailsWriteRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#hitPing(java.util.
	 * Map)
	 */
	@Override
	public Response hitPing() throws HMTPException {

		logger.info("Enter HtpSimulatorRestApiImpl.hitPing method ");
		return Response.ok().build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.roche.connect.htp.adapter.rest.HtpSimulatorRestApi#hitHello(java.util
	 * .Map)
	 */
	@Override
	public Response hitHello(Map<String, Object> requestBody) throws HMTPException {

		logger.info("Enter HtpSimulatorRestApiImpl.hitHello method ");
		logger.info("Finished HtpSimulatorRestApiImpl.Hello method ");
		return Response.ok().build();
	}

	@Override
	public Response getJob() throws HMTPException {

		Cycle cycle = cycleWriteRepository.findTopByStatusAndSendToSecondaryFlagAndTypeOrderByUpdatedDateTime(ForteConstans.HTPCOMPLETE,ForteConstans.NO, ForteConstans.FILETYPE);
		if (cycle != null) {
			ComplexIdDetails complexIdDetails = complexIdDetailsWriteRepository.findByDeviceRunID(cycle.getRunId());
			ForteJob forteJob = forteSimulatorService.getForteJob(cycle);
			forteJobWriteRepository.save(forteJob);
			if (complexIdDetails != null) {
				String htpComplexId = complexIdDetails.getComplexId();
				cycle.setSendToSecondaryFlag(ForteConstans.YES);
				cycleWriteRepository.save(cycle);
				return forteSimulatorService.getPatientDetails(htpComplexId,cycle,forteJob.getId().toString());
			}

		}
		return Response.serverError().build();
	}

	@Override
	public Response updateJobStatus(String jobId,Map<String, Object> requestBody) throws HMTPException {
		
		Timestamp currentDateTime = new Timestamp(new Date().getTime());
		ForteJob forteJob = forteJobWriteRepository.findById(jobId);
		Response response = Response.ok().build();
		forteJob.setJobStatus(requestBody.get("job_status").toString());
		forteJob.setUpdatedDateandTime(currentDateTime);
		if(requestBody.get("job_status").toString().equalsIgnoreCase(ForteConstans.INPROGRESS)) {
			forteJob.setEstimatedTimeToCompletion(requestBody.get("estimated_time_to_completion").toString());
		}else if(requestBody.get("error").toString().equalsIgnoreCase(ForteConstans.ERROR)) {
			forteJob.setErrorKey(requestBody.get("error_key").toString());
		}else if(requestBody.get("job_status").toString().equalsIgnoreCase(ForteConstans.DONE)) {
			List<Cycle> cycles = cycleWriteRepository.findByRunIdAndType(forteJob.getDeviceRunId(),ForteConstans.FILETYPE);
			Cycle cycle = cycles.get(0);
			if(cycle.getTransferComplete().equalsIgnoreCase(CycleStatus.COMPLETE.toString())) {
				List<ForteJob> forteJobs = forteJobWriteRepository.findByDeviceRunIdAndJobStatus(forteJob.getDeviceRunId(),ForteConstans.DONE);
				if(cycles.size()==forteJobs.size()) {
					ComplexIdDetails complexIdDetails = complexIdDetailsWriteRepository.findByDeviceRunID(forteJob.getDeviceRunId());
					response = forteSimulatorService.updateJobStatus(ForteConstans.DONE,ForteConstans.SECONDARY,complexIdDetails.getComplexId());
				}
			}
			forteJob.setQc(requestBody.get("qc").toString());
			forteJob.setOutFilePath(requestBody.get("outfile").toString());
			forteJob.setOutFileChecksum(requestBody.get("outfile_checksum").toString());
			
		}
		forteJobWriteRepository.save(forteJob);
		
		return response;
	}

}