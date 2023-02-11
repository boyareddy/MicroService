
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
package com.roche.connect.htp.adapter.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.forte.ForteJobTypes;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.common.forte.TertiaryJobDetailsDTO;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.ForteJob;
import com.roche.connect.htp.adapter.readrepository.ComplexIdDetailsReadRepository;
import com.roche.connect.htp.adapter.readrepository.CycleReadRepository;
import com.roche.connect.htp.adapter.readrepository.ForteJobReadRepository;
import com.roche.connect.htp.adapter.services.RunService;
import com.roche.connect.htp.adapter.util.CycleStatus;
import com.roche.connect.htp.adapter.util.ForteConstants;
import com.roche.connect.htp.adapter.writerepository.ForteJobWriteRepository;

/**
 * The Class HtpSimulatorRestApiImpl.
 */
@Component
public class ForteAdapterRestApiImpl implements ForteAdapterRestApi {

	/** The logger. */
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName()); 

	/** The htp simulator service. */
	@Autowired
	RunService runService;

	/** The cycle write repository. */
	@Autowired
	CycleReadRepository cycleReadRepository;

	@Autowired
	ForteJobReadRepository forteJobReadRepository;

	@Autowired
	ForteJobWriteRepository forteJobWriteRepository;

	@Autowired
	ComplexIdDetailsReadRepository complexIdDetailsReadRepository;
	
	

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

		if (requestBody != null) {
			return Response.ok().build();
		} else {
			return Response.ok().status(204).entity("No Content found in the request").build();
		}
	}

	@Override
	public Response getJob() throws HMTPException {

		logger.info("Entering Get job details");

		/**
		 * Here need to check tertiary job is available or not
		 */

		TertiaryJobDetailsDTO tertiaryJobDetails = runService.getTertiaryJobDetails();

		if (tertiaryJobDetails != null)
			return Response.status(Status.OK).entity(tertiaryJobDetails).build();

		SecondaryJobDetailsDTO secondaryDetails = runService.getSecondaryJobDetails();

		if (secondaryDetails != null)
			return Response.status(Status.OK).entity(secondaryDetails).build();

		return Response.status(Status.NO_CONTENT).entity(ForteConstants.NO_JOB_FOUND_MSG).build();
	}

	@Override
	public Response updateJobStatus(String jobId, Map<String, Object> requestBody) throws IOException, HMTPException {

		logger.info("Entering updateJobStatus: " + jobId);

		try {
			logger.info("RequestBody " + new ObjectMapper().writeValueAsString(requestBody));
		} catch (Exception e) {
			logger.info("Exception while parsing request body");
		}

		if (jobId != null && requestBody.get(ForteConstants.JOB_STATUS) != null) {

			String jobStatus = requestBody.get(ForteConstants.JOB_STATUS).toString();

			ForteJob forteJob = forteJobReadRepository.findById(Long.parseLong(jobId));

			Boolean haveToUpdateStatusInIMM = true;

			if (forteJob != null) {
				switch (jobStatus) {

				case ForteConstants.START:
					forteJob.setEstimatedTimeToCompletion(requestBody.get(ForteConstants.ESTIMATED_TIME_TO_COMPLETION) != null
							? requestBody.get(ForteConstants.ESTIMATED_TIME_TO_COMPLETION).toString()
							: null);
					break;
				case ForteConstants.INPROGRESS:
					forteJob.setEstimatedTimeToCompletion(requestBody.get(ForteConstants.ESTIMATED_TIME_TO_COMPLETION) != null
							? requestBody.get(ForteConstants.ESTIMATED_TIME_TO_COMPLETION).toString()
							: null);
					break;
				case ForteConstants.ERROR:
					forteJob.setErrorKey(
							requestBody.get(ForteConstants.ERROR_KEY) != null ? requestBody.get(ForteConstants.ERROR_KEY).toString() : null);
					break;

				case ForteConstants.DONE:
					forteJob.setQc(requestBody.get(ForteConstants.QC) != null ? requestBody.get(ForteConstants.QC).toString() : null);

					forteJob.setOutFilePath(
							runService.getMountPath(ForteConstants.FORTE, requestBody.get(ForteConstants.OUTFILE).toString()));
					forteJob.setOutFileChecksum(
							requestBody.get(ForteConstants.OUTFILE_CHECKSUM) != null ? requestBody.get(ForteConstants.OUTFILE_CHECKSUM).toString()
									: null);

					if (runService.validateChecksum(forteJob.getOutFilePath(), forteJob.getOutFileChecksum())) {

						if (forteJob.getJobType().equalsIgnoreCase(ForteConstants.SECONDARY)) {
							long domainId=ThreadSessionManager.currentUserSession().getAccessorCompanyId();
							List<Cycle> cycles = cycleReadRepository.findByRunIdAndType(forteJob.getDeviceRunId(),
									ForteConstants.FILETYPE, domainId);

							List<ForteJob> forteJobs = forteJobReadRepository
									.findByDeviceRunIdAndJobStatus(forteJob.getDeviceRunId(), ForteConstants.DONE);

							if (!cycles.isEmpty()) {

								if (!((forteJobs.size() + 1) == cycles.size() && cycles.get(0).getTransferComplete()
										.equalsIgnoreCase(CycleStatus.COMPLETE.toString()))) {
									haveToUpdateStatusInIMM = false;
								}
							} else {
								return Response.status(Status.BAD_REQUEST).entity(ForteConstants.NO_CYCLE_FOUND_MESSAGE)
										.build();
							}
						}
					} else {
						return Response.status(409).entity(ForteConstants.CHECKSUM_MISMATCH).build();
					}

					break;
				default:
					return Response.status(Status.BAD_REQUEST).entity(ForteConstants.NO_JOB_FOUND_MESSAGE).build();
				}

				forteJob.setJobStatus(jobStatus);
				Company company = new Company();
	            company.setId(ThreadSessionManager.currentUserSession().getAccessorCompanyId());
	            forteJob.setCompany(company);
				forteJobWriteRepository.save(forteJob);

				haveToUpdateStatusInIMM = haveToUpdateStatusInIMM
						&& (forteJob.getJobType().equalsIgnoreCase(ForteJobTypes.TERTIARY.getText())
								|| (forteJob.getJobType().equalsIgnoreCase(ForteConstants.SECONDARY)
										&& (forteJob.getJobStatus().equalsIgnoreCase(ForteConstants.DONE))));

				if (haveToUpdateStatusInIMM) {

					logger.info("Updating job status to IMM Forte JobId: " + forteJob.getId());
					long domainId=ThreadSessionManager.currentUserSession().getAccessorCompanyId();
					ComplexIdDetails complexIdDetails = complexIdDetailsReadRepository
							.findByDeviceRunId(forteJob.getDeviceRunId(), domainId);

					if (complexIdDetails != null)
						return runService.updateJobStatusToIMM(forteJob.getJobStatus(), forteJob.getJobType(),
								complexIdDetails.getComplexId());
					else
						throw new HMTPException(ForteConstants.COMPLEX_ID_NOT_EXIST_MESSAGE);
				}

				return Response.status(Status.OK).entity(ForteConstants.STATUS_UPDATE_MSG).build();

			}

		}

		return Response.status(Status.BAD_REQUEST).entity(ForteConstants.NO_JOB_FOUND_MESSAGE).build();

	}

}
