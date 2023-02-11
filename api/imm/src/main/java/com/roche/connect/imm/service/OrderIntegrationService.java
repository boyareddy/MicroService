/*******************************************************************************
 * OrderIntegrationService.java Version: 1.0 Authors: somesh_r
 * ********************* Copyright (c) 2018 Roche Sequencing Solutions (RSS) -
 * CONFIDENTIAL All Rights Reserved. NOTICE: All information contained herein
 * is, and remains the property of COMPANY. The intellectual and technical
 * concepts contained herein are proprietary to COMPANY and may be covered by
 * U.S. and Foreign Patents, patents in process, and are protected by trade
 * secret or copyright law. Dissemination of this information or reproduction of
 * this material is strictly forbidden unless prior written permission is
 * obtained from COMPANY. Access to the source code contained herein is hereby
 * forbidden to anyone except current COMPANY employees, managers or contractors
 * who have executed Confidentiality and Non-disclosure agreements explicitly
 * covering such access The copyright notice above does not evidence any actual
 * or intended publication or disclosure of this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret,
 * of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS
 * WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF
 * APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS
 * SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
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
 *  Description: Class implementation that provides integration support for OMM.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/

package com.roche.connect.imm.service;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.imm.utils.RestClient;
import com.roche.connect.imm.utils.UrlConstants;

@Service
public class OrderIntegrationService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${connect.omm_host_url}")
	private String ommHostUrl;

	private ObjectMapper objectMapper = new ObjectMapper();

	public List<ContainerSamplesDTO> getDPCRContainerSamples() throws HMTPException {

		try {

			logger.info("Calling OMM to get ContainerSamples");

			String url = ommHostUrl + UrlConstants.OMM_CONTAINER_SAMPLES_URL;

			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);
			return orderClient.get(new GenericType<List<ContainerSamplesDTO>>() {
			});

		} catch (Exception exp) {
			logger.error("Error occurred get ContainerSamples from OMM" + exp.getMessage(), exp);
			throw new HMTPException(exp);
		}

	}

	public List<ContainerSamplesDTO> getDPCRContainerSamples(String deviceRunId) throws HMTPException {

		try {

			logger.info("Calling OMM to get ContainerSamples");

			if (StringUtils.isEmpty(deviceRunId))
				throw new NullPointerException("DeviceRunId is NULL or empty");

			String url = ommHostUrl + UrlConstants.OMM_CONTAINER_SAMPLES_URL + "?devicerunid=" + deviceRunId;
			Invocation.Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), null);

			return builder.get(new GenericType<List<ContainerSamplesDTO>>() {
			});

		} catch (Exception exp) {
			logger.error("Error occurred get ContainerSamples from OMM" + exp.getMessage(), exp);
			throw new HMTPException(exp);
		}

	}

	public boolean updateContainerSamples(List<ContainerSamplesDTO> containerSamplesDTOList, String token)
			throws HMTPException {

		logger.info(" -> updateDeviceRunID()::Update containerSampleslist");
		try {

			String url = ommHostUrl + UrlConstants.OMM_CONTAINER_SAMPLES_URL;
			logger.info("containerSamplesDTOList: " + objectMapper.writeValueAsString(containerSamplesDTOList));

			Response response = RestClient.put(url, containerSamplesDTOList, token, null);

			logger.info(" <- updateContainerSamples ResponseCode: " + response.getStatus());

			return (response.getStatus() == HttpStatus.SC_OK);

		} catch (Exception exp) {
			logger.error("Error occurred while calling at OMM updateDeviceRunID Api" + exp.getMessage());
			throw new HMTPException(exp);
		}

	}

	/* Added for Forte SECONDARY details */
	public List<OrderDTO> getPatientAssayDetailsByAccesssioningId(String accessioningId) throws HMTPException {

		logger.info("Entering getPatientAssayDetailsByAccesssioningId accessioningId:" + accessioningId);

		try {
			String url = ommHostUrl + UrlConstants.OMM_ORDER_DETAILS_URL + accessioningId;

			logger.info("Calling OMM to get AssayDetails URL: " + url);

			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);

			List<OrderDTO> orders = orderClient.get(new GenericType<List<OrderDTO>>() {
			});

			logger.info("Result from OMM: " + objectMapper.writeValueAsString(orders));
			return orders;

		} catch (Exception exp) {
			logger.error("Error occurred get AssayDetails from OMM" + exp.getMessage(), exp);
			throw new HMTPException(exp);
		}
	}

	public OrderDTO getOrderByOrderId(Long orderId) throws HMTPException {

		logger.info("Entering getOrderByOrderId orderId:" + orderId);

		try {
			String url = ommHostUrl + UrlConstants.OMM_ORDER_URL + orderId;

			logger.info("Calling OMM to get Order URL: " + url);

			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);

			Response response = orderClient.get();
			OrderParentDTO order = response.readEntity(OrderParentDTO.class);

			logger.info("Order Result from OMM by OrderId: " + objectMapper.writeValueAsString(order));
			return order.getOrder();

		} catch (Exception exp) {
			logger.error("Error occurred get order from OMM" + exp.getMessage(), exp);
			throw new HMTPException(exp);
		}
	}
	
	
	public List<OrderDTO> getOrder(String accessioningId, String token) throws HMTPException {

		logger.info("Entering getOrder accessioningId:" + accessioningId);

		try {
			String url = ommHostUrl + UrlConstants.OMM_ORDER_BY_ACCESSIONING_ID_URL + "?accessioningID="
					+ accessioningId;

			logger.info("Calling OMM to get Order URL: " + url);

			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);
			
			if (token != null)
				orderClient.header(RestClient.COOKIE_STR, RestClient.COOKIE_KEY + token);
			
			Response response = orderClient.get();
			
			if(response.getStatus() != HttpStatus.SC_OK)
				return Collections.emptyList();
			
			List<OrderDTO> orders = response.readEntity(new GenericType<List<OrderDTO>>() {});

			logger.info("Order from OMM by accessioningId: " + objectMapper.writeValueAsString(orders));
			return orders;

		} catch (Exception exp) {
			logger.error("Error occurred get order from OMM" + exp.getMessage(), exp);
			throw new HMTPException(exp);
		}
	}

	public boolean isRunIDValid(String deviceRunId) throws HMTPException {
		logger.info(" -> validate deviceRunID");
		try {
			String url = ommHostUrl + UrlConstants.OMM_CONTAINER_SAMPLES_VALIDATE + "?devicerunid=" + deviceRunId;
			logger.info("Calling OMM_CONTAINER_SAMPLES_VALIDATE to URL: " + url);
			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);
			Response response = orderClient.get();
			logger.info(" <- deviceRunId ResponseCode: " + response.getStatus());
			return (response.getStatus() == HttpStatus.SC_OK);
				
		} catch (Exception exp) {
			logger.error("Error occurred while calling at OMM validate DeviceRunID Api" + exp.getMessage());
			throw new HMTPException(exp);
		}

	}

}
