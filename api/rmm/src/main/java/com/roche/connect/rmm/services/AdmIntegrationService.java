package com.roche.connect.rmm.services;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.adm.dto.SystemSettingsDto;
import com.roche.connect.rmm.jasper.dto.Imagepojo;
import com.roche.connect.rmm.util.RMMConstant;

@Service
public class AdmIntegrationService {
	
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	
	public List<SystemSettingsDto> getSystemSettings() throws HMTPException {
		try {
			final String url = RestClientUtil.getUrlString("pas.adm_api_url", "", "/json/rest/api/v1/systemsettings",
					"", null);
			Invocation.Builder admClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
			return admClient.get(new GenericType<List<SystemSettingsDto>>() {
			});
		} catch (Exception e) {
			logger.error("Error while fetching system settings");
			throw new HMTPException(e);
		}
	}
	
	public Imagepojo getLabLogo() throws HMTPException {
		try {
			final String url = RestClientUtil.getUrlString("pas.adm_api_url", "", "/json/rest/api/v1/lablogo",
					"", null);
			Invocation.Builder admClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);

			InputStream bytess = admClient.get(new GenericType<InputStream>() {
			});
			
			Imagepojo pojo = new Imagepojo();
			pojo.setStream(bytess);
			return pojo;
		    
		} catch (Exception e) {
			logger.error("Error while fetching system settings");
			throw new HMTPException(e);
		}
	}
	
	public String getLabDetails(List<SystemSettingsDto> systemSettings){
		StringBuilder sb = new StringBuilder();
		String labName = null;
		String phoneNumber = null;
		String labAddress1 = null;
		String labAddress2 = null;
		String labAddress3 = null;
		
		List<SystemSettingsDto> systemSettings1 = systemSettings.stream().
				filter(p -> "labName".equalsIgnoreCase(p.getAttributeName())).collect(Collectors.toList());
		if(!systemSettings1.isEmpty()) {
			labName = systemSettings1.get(0).getAttributeValue();
		}
		
		List<SystemSettingsDto> systemSettings2 = systemSettings.stream().filter(p -> "phoneNumber".equalsIgnoreCase(p.getAttributeName())).collect(Collectors.toList());
		if(!systemSettings2.isEmpty()) {
			phoneNumber = systemSettings2.get(0).getAttributeValue();
		}
		
		List<SystemSettingsDto> systemSettings3 = systemSettings.stream().filter(p -> "labAddress1".equalsIgnoreCase(p.getAttributeName())).collect(Collectors.toList());
		if(!systemSettings3.isEmpty()) {
			labAddress1 = systemSettings3.get(0).getAttributeValue();
		}
		
		List<SystemSettingsDto> systemSettings4 = systemSettings.stream().filter(p -> "labAddress2".equalsIgnoreCase(p.getAttributeName())).collect(Collectors.toList());
		if(!systemSettings4.isEmpty()) {
			labAddress2 = systemSettings4.get(0).getAttributeValue();
		}
		
		List<SystemSettingsDto> systemSettings5 = systemSettings.stream().filter(p -> "labAddress3".equalsIgnoreCase(p.getAttributeName())).collect(Collectors.toList());
		if(!systemSettings5.isEmpty()) {
			labAddress3 = systemSettings5.get(0).getAttributeValue();
		}
		
		if(!StringUtils.isEmpty(labName)){
			sb.append(labName);
			if(!StringUtils.isEmpty(labAddress1)){
				sb.append(" | ");
			}else{
				sb.append("\n");
			}
		}
		if(!StringUtils.isEmpty(labAddress1)){
			sb.append(labAddress1);
			sb.append("\n");
		}
		if(!StringUtils.isEmpty(labAddress2)){
			sb.append(labAddress2);
			sb.append("\n");
		}
		if(!StringUtils.isEmpty(labAddress3)){
			sb.append(labAddress3);
			sb.append("\n");
		}
		if(!StringUtils.isEmpty(phoneNumber)){
			sb.append("Tel-");
			sb.append(phoneNumber);
		}
		return sb.toString();
		
	}
}
