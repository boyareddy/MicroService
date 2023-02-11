package com.roche.connect.omm.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

public class MandatoryFlagValidationUtil {
	private static MandatoryFlagValidationUtil instance;
	private Map<String, List<AssayInputDataValidationsDTO>> dataMap;
	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	private MandatoryFlagValidationUtil() {
		dataMap = new ConcurrentHashMap<>();
	}

	public static synchronized MandatoryFlagValidationUtil getInstance() {
		if (instance == null) {
		instance = new MandatoryFlagValidationUtil();
		}
		return instance;
	}

	public void put(String key, List<AssayInputDataValidationsDTO> value) {
		dataMap.put(key, value);
	}
	
	public List<AssayInputDataValidationsDTO> get(String key) {
		List<AssayInputDataValidationsDTO> validations = null;
		if(dataMap.containsKey(key)) {
			validations = dataMap.get(key);
		} else {
			OrderService orderService = new OrderServiceImpl();
			try {
				validations = orderService.mandatoryFieldValidationByAssay(key);
				dataMap.put(key, validations);
			} catch (UnsupportedEncodingException | HMTPException e) {
				logger.error("Error while storing the mandatory validations to dataMap");
			}
		}
		return validations;
	}
	
	public boolean containskey(String key) {
		return dataMap.containsKey(key);
	}
}
