/*******************************************************************************
 * RunServiceImpl.java                  
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
package com.roche.connect.forte.adapter.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.forte.adapter.model.Cycle;
import com.roche.connect.forte.adapter.model.ForteJob;
import com.roche.connect.forte.adapter.repository.ComplexIdDetailsWriteRepository;
import com.roche.connect.forte.adapter.util.ForteConstans;
import com.roche.connect.forte.adapter.util.PatientCache;

/**
 * The Class RunServiceImpl.
 */
@Service
public class RunServiceImpl implements RunService {

	@Value("${adapter.imm_complex_id_details}")
	private String getComplexIdDetailsUrl;

	/** The update run info url. */
	@Value("${adapter.imm_run_update}")
	private String updateRunInfoUrl;

	@Value("${adapter.imm_getPatientDetails}")
	private String getPatientDetailsUrl;

	@Value("${pas.authenticate_url}")
	private String loginUrl;

	/** The web services. */
	@Autowired
	WebServices webServices;

	@Autowired
	ObjectMapper objectMapper;

	PatientCache cache;

	@Autowired

	ComplexIdDetailsWriteRepository complexIdDetailsWriteRepository;

	/** The logger. */
	private Logger logger = LogManager.getLogger(RunServiceImpl.class);

	@Override
	public Response getPatientDetails(String htpComplexId, Cycle cycle,String jobId) {
		List<SecondarySampleAssayDetails> secondarySampleDetails = null;
		List<Map<String,String>> sampleDetails = null;
		SecondaryJobDetailsDTO secondaryJobDetailsDTO = new SecondaryJobDetailsDTO();

		cache = PatientCache.getInstance();
		if (cache.containskey(htpComplexId)) {
			return Response.ok().entity(cache.get(htpComplexId)).build();
		} else {
			// getting the sample details from IMM
			Response response = getPatientDetailsFromIMM(htpComplexId);

			if (response.getStatus() != 200) {
				logger.info("getPatientDetailsFromIMM : ", secondaryJobDetailsDTO);
				return response;
			} else {
				secondaryJobDetailsDTO.setId(jobId);
				secondaryJobDetailsDTO.setKind(ForteConstans.SECONDARY);
				secondaryJobDetailsDTO.setSecondary_checksum(cycle.getChecksum());
				secondaryJobDetailsDTO.setSecondary_infile(cycle.getPath());
				secondarySampleDetails = (List<SecondarySampleAssayDetails>) response.readEntity(List.class);
				if(secondarySampleDetails!=null) {
					sampleDetails = getSampleDetails(secondarySampleDetails,htpComplexId);
				}
				// secondarySampleDetails = objectMapper.readValue(response, List.class);
				secondaryJobDetailsDTO.setSecondarySampleDetails(sampleDetails);
				cache.put(htpComplexId, secondaryJobDetailsDTO);
				return Response.ok().entity(secondaryJobDetailsDTO).build();
			}

		}

	}

	private List<Map<String, String>> getSampleDetails(List<SecondarySampleAssayDetails> secondarySampleDetails,String htpComplexId) {
		
		List<Map<String, String>> sampleList = new ArrayList<>();
		for(SecondarySampleAssayDetails sampleAssay : secondarySampleDetails) {
			Map<String,String> sampleMap = new HashMap<>();
			if(sampleAssay.getMaternalAge()!=null) {
				sampleMap.put(ForteConstans.METERNALAGE, sampleAssay.getMaternalAge().toString());
			}else if(sampleAssay.getGestationalAgeWeeks()!=null) {
				sampleMap.put(ForteConstans.GESTATIONALAGEWEEKS, sampleAssay.getGestationalAgeWeeks().toString());
			}
			else if(sampleAssay.getGestationalAgeDays()!=null) {
				sampleMap.put(ForteConstans.GESTATIONALAGEDAYS, sampleAssay.getGestationalAgeDays().toString());
			}
			else if(sampleAssay.getEggDonor()!=null) {
				sampleMap.put(ForteConstans.EGGSOURCE, sampleAssay.getEggDonor());
			}
			else if(sampleAssay.getFetusNumber()!=null) {
				sampleMap.put(ForteConstans.NUMBERFETUSES, sampleAssay.getFetusNumber());
			}
			else if(htpComplexId!=null) {
				sampleMap.put(ForteConstans.SAMPLETAG, htpComplexId);
			}else if(sampleAssay.getMolecularId()!=null) {
				sampleMap.put(ForteConstans.MID, sampleAssay.getMolecularId());
			}
			sampleList.add(sampleMap);
		}
		return sampleList;
	}

	@Override
	public Response getPatientDetailsFromIMM(String htpComplexId) {
		ObjectMapper mapper = new ObjectMapper();
		String url = getPatientDetailsUrl + "?messagetype=" + ForteConstans.GET
				+ "&devicetype=FORTE&source=device&deviceid=FORTE_1";
		Map<String, String> body = new HashMap<>();
		body.put("complexId", htpComplexId);
		try {
			return webServices.postRequest(url, mapper.writeValueAsString(body));
		} catch (JsonProcessingException e) {
			logger.error("RunServiceImpl.getPatientDetailsFromIMM : Error while requesting patient details from IMM");
			return Response.serverError().build();
		}
	}

	@Override
	public ForteJob getForteJob(Cycle cycle) {
		ForteJob forteJob = new ForteJob();
		Timestamp currentDateTime = new Timestamp(new Date().getTime());
		
		forteJob.setCycleId(cycle.getId());
		forteJob.setDeviceRunId(cycle.getRunId());
		forteJob.setJobType(ForteConstans.SECONDARY);
		forteJob.setSentToTertiary(ForteConstans.NO);
		forteJob.setCreatedDateandTime(currentDateTime);
		
		return forteJob;
	}

	@Override
	public Response updateJobStatus(String jobStatus, String jobType, String complexId) {
		ObjectMapper mapper = new ObjectMapper();
		String url = getPatientDetailsUrl + "?messagetype=" + ForteConstans.PUT
				+ "&devicetype=FORTE&source=device&deviceid=FORTE_1";
		
		Map<String, String>status = new HashMap<>();
		status.put("jobStatus", jobStatus);
		status.put("jobType", jobType);
		status.put("complexId", complexId);
		try {
			return webServices.postRequest(url,mapper.writeValueAsString(status));
		} catch (JsonProcessingException e) {
			logger.error("RunServiceImpl.updateJobStatus : Error while parsing and sending the status to IMM");
			return Response.serverError().build();
		}
		
	}

}
