package com.roche.connect.imm.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.forte.ForteStatusMessage;
import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;

public interface ForteDetail {
	List<SecondarySampleAssayDetails> fetchSecondaryDetails(String complexId) throws UnsupportedEncodingException, HMTPException;

	boolean updateForteStatus(String complexId, ForteStatusMessage forteStatus, String token);

	Future<Boolean> updateForteStatusBySampleList(ForteStatusMessage forteStatus, List<SampleResultsDTO> sampleResults,
			String token);
	
	Response getForteGetJob(Map<String, Object> map) throws UnsupportedEncodingException, HMTPException;
	
	Response getFortePutJob(String deviceId,String deviceType, Map<String, Object> map,String token);
}
