package com.roche.connect.imm.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.amm.dto.ProcessStepActionDTO;
import com.roche.connect.common.enums.AssayType;
import com.roche.connect.common.enums.NiptDeviceType;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.imm.utils.IMMConstants;

@Component
public class ForteDetailImpl implements ForteDetail {
	@Autowired
	private MessageProcessorService messageProcessorService;

	@Autowired
	private AssayIntegrationService assayService;

	@Autowired
	private OrderIntegrationService orderIntegrationService;

	@Autowired
	private RmmIntegrationService rmmService;

	@Autowired
	private WFMIntegrationService wfmIntegrationService;
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	private Boolean isUpdated = false;

	@Override
	public List<SecondarySampleAssayDetails> fetchSecondaryDetails(String complexId)
			throws UnsupportedEncodingException, HMTPException {

		logger.info("Entering ForteDetailImpl.fetchSecondaryDetails: complexId: " + complexId);

		List<SecondarySampleAssayDetails> sampleAssayDetailsList = new ArrayList<>();

		List<ProcessStepActionDTO> allProcessStepList = assayService.getProcessStepAction(AssayType.NIPT.getText(),
				null);
		if (!allProcessStepList.isEmpty()) {
			
			Optional<ProcessStepActionDTO> findFirst = allProcessStepList.parallelStream()
					.filter(e -> e.getDeviceType().equalsIgnoreCase(NiptDeviceType.HTP.getText())).findFirst();
			ProcessStepActionDTO htpProcessStep = findFirst.isPresent() ? findFirst.get() : null;
			
    			if(htpProcessStep != null) {
    			    Optional<ProcessStepActionDTO> findFirst2 = allProcessStepList.parallelStream()
                        .filter(e -> e.getProcessStepSeq() == htpProcessStep.getProcessStepSeq() - 3).findFirst();
                ProcessStepActionDTO lpPreProcessStep = findFirst2.isPresent() ? findFirst2.get() : null;       
                        
                if (lpPreProcessStep != null) {
                List<SampleResultsDTO> sampleResults = messageProcessorService
                        .getSampleResultsByProcessStepAndInputContainerId(htpProcessStep.getProcessStepName(), complexId);
                logger.info("ForteDetailImpl :: SampleResultsDTO ", sampleResults);
                if (!sampleResults.isEmpty()) {
                    sampleResults.stream().forEach(id -> {
    
                        try {
                            List<OrderDTO> patientAssayDetails = orderIntegrationService
                                    .getPatientAssayDetailsByAccesssioningId(id.getAccesssioningId());
    
                            if (!patientAssayDetails.isEmpty()) {
                                logger.info("Entering If Part patientAssayDetails");
    
                                SecondarySampleAssayDetails sampleAssayDetailts = getSampleAssayDetailsFromAssayDto(
                                        id.getAccesssioningId(), patientAssayDetails.get(0).getAssay());
    
                                sampleAssayDetailts
                                        .setMolecularId(rmmService.getMolecularIdByProcessStepNameAndAccessioningId(
                                                lpPreProcessStep.getProcessStepName(),
                                                sampleAssayDetailts.getAccessioningId()));
    
                                sampleAssayDetailsList.add(sampleAssayDetailts);
                            }
    
                        } catch (HMTPException e1) {
                            logger.error("ERR:- ForteDetailImpl:: fetchSecondoaryDeatils(): " + e1.getMessage());
                        }
    
                    });
                } else {
                    logger.error("ERR :=> sampleResults isEmpty()");
                }
                
    			}
    		} else {
    		    throw new HMTPException("LP Process step name is missing");
    		}
			
		} else {
			logger.error("ERR :=> allProcessStepList isEmpty()");
		}
		return sampleAssayDetailsList;
	}

	public SecondarySampleAssayDetails getSampleAssayDetailsFromAssayDto(String accessioningId, AssayDTO assayDto) {
		SecondarySampleAssayDetails sampleAssayDetails = new SecondarySampleAssayDetails();
		sampleAssayDetails.setAccessioningId(accessioningId);
		sampleAssayDetails.setEggDonor(assayDto.getEggDonor());
		sampleAssayDetails.setFetusNumber(assayDto.getFetusNumber());
		sampleAssayDetails.setGestationalAgeDays(assayDto.getGestationalAgeDays());
		sampleAssayDetails.setGestationalAgeWeeks(assayDto.getGestationalAgeWeeks());
		sampleAssayDetails.setMaternalAge(assayDto.getMaternalAge());
		return sampleAssayDetails;
	}

	@Override
	public boolean updateForteStatus(String complexId, ForteStatusMessage forteStatus, String token) {

		logger.info("Entering ForteDetailImpl.updateForteStatus: complexId: " + complexId);

		List<ProcessStepActionDTO> allProcessStepList = assayService.getProcessStepAction(AssayType.NIPT.getText(),
				NiptDeviceType.HTP.getText());

		String htpProcessStepName = !allProcessStepList.isEmpty() ? allProcessStepList.get(0).getProcessStepName()
				: null;

		try {
			List<SampleResultsDTO> sampleResults = messageProcessorService
					.getSampleResultsByProcessStepAndInputContainerId(htpProcessStepName, complexId);

				Future<Boolean> statusBySampleList = updateForteStatusBySampleList(forteStatus, sampleResults, token);
				if (statusBySampleList.isDone())
					return statusBySampleList.get();
			 else {
				throw new HMTPException("FORTE Status isn't Completed");
			}
		} catch (Exception ex) {
			logger.error("ERR:- ForteDetailImpl:: updateForteStatus(): " + ex.getMessage());
		}
		return false;
	}

	@Async
	@Override
	public Future<Boolean> updateForteStatusBySampleList(ForteStatusMessage forteStatus,
			List<SampleResultsDTO> sampleResults, String token) {

		logger.info("ForteDetailImpl <=:: updateForteStatusBySampleList");

		sampleResults.stream().forEach(i -> {
			forteStatus.setAccessioningId(i.getAccesssioningId());
			try {
				Thread.sleep(1000);
				if (wfmIntegrationService.updateForteStatus(forteStatus, token)) {
					this.isUpdated = true;
					logger.info("Data has been updated successfully at WFM");
				} 
			} catch (Exception ex) {
				logger.error("ERR:- " + Thread.currentThread().getName() + " " + ex.getMessage());
			}
		});
		logger.info("ForteDetailImpl ::=> updateForteStatusBySampleList");

		return new AsyncResult<>(isUpdated);
	}
	
	@Override
	public Response getForteGetJob(Map<String, Object> map) throws UnsupportedEncodingException, HMTPException {
		
		String complexId = map.containsKey("complexId") ? map.get("complexId").toString() : null;
		logger.info("FORTE ::=> GET Call Loaded..");

		if (complexId != null) {
			List<SecondarySampleAssayDetails> patientSampleAssayDeatils = fetchSecondaryDetails(complexId);

			return patientSampleAssayDeatils.isEmpty()
					? Response.status(Status.NOT_FOUND).entity("Complex Id does not exist").build()
					: Response.status(200).entity(patientSampleAssayDeatils).build();
		} else {
			logger.error("ERR:- FORTE GET");
			return Response.status(Status.BAD_REQUEST).build();
		}
	
	}
	
	@Override
	public Response getFortePutJob(String deviceId,String deviceType, Map<String, Object> map,String token) {
		
		String complexId = map.containsKey(IMMConstants.COMPLEX_ID_KEY_STR) ? map.get(IMMConstants.COMPLEX_ID_KEY_STR).toString() : null;
		logger.info("FORTE ::=> PUT Call Loaded..");
		ForteStatusMessage forteStatus = new ForteStatusMessage();
		forteStatus.setDeviceId(deviceId);
		forteStatus.setSendingApplication(deviceType);
		if (complexId != null && map.containsKey("jobStatus")) {
			forteStatus.setStatus(map.get("jobStatus").toString());
			forteStatus.setJobType(map.get("jobType").toString());
			return updateForteStatus(complexId, forteStatus, token)
					? Response.status(200).build()
					: Response.status(400).build();
		} else {
			logger.error("ERR:- FORTE PUT");
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
