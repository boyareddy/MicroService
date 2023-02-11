package com.roche.connect.rmm.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
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
import com.roche.connect.common.order.dto.OrderElements;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.util.RMMConstant;
import com.roche.connect.rmm.util.UrlConstants;

@Service
public class OrderIntegrationService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Value("${pas.omm_api_url}")
	private String ommHostUrl;

	private ObjectMapper objectMapper = new ObjectMapper();

	public OrderElements searchOrderByAccessioningId(String searchKey, String orderStatus, int offset, int limit) throws IOException {

		try {
			String url = ommHostUrl + UrlConstants.OMM_ORDER_SEARCH_URL + "?query=" + searchKey + "&orderstatus="
					+ orderStatus + "&offset=" + offset + "&limit=" + limit;
			logger.info("Calling OMM to search Order, URL: " + url);

			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);

			Response response = orderClient.get();

			if (response.getStatus() != HttpStatus.SC_OK)
				return null;

			OrderElements orderElements = response.readEntity(OrderElements.class);
			
			logger.info("Search Orders from OMM by accessioningId searchQuery: " + objectMapper.writeValueAsString(orderElements));
			return orderElements;
		} catch (IOException exp) {
			logger.error("Error occurred search order from OMM" + exp.getMessage(), exp);
			throw exp;
		}
	}
	
	public List<OrderDTO> searchByAccessioningId(String accessioningId) throws HMTPException, UnsupportedEncodingException {
		List<OrderDTO> orders;
		try {
			final String url = RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/orders?accessioningID=",
					accessioningId, null);
			Invocation.Builder ommClient = RestClientUtil.getBuilder(URLEncoder.encode(url, RMMConstant.UTF_8), null);
			orders = ommClient.get(new GenericType<List<OrderDTO>>() {
			});
		}catch(HMTPException e) {
			logger.error("Error while calling omm to fetch order");
			throw new HMTPException(e);
		}
		return orders;
	}
	
	//@SuppressWarnings("unchecked")
	public List<SampleResultsDTO> getMandatoryFieldValidations(List<SampleResultsDTO> sampleResultsDTO) throws HMTPException {
		List<SampleResultsDTO> sampleResults = new ArrayList<>();
		try {
			final String url = RestClientUtil.getUrlString("pas.omm_api_url", "", "/json/rest/api/v1/order/runsamplemandateflags",
					"", null);
			
			Response response = RestClientUtil.postOrPutBuilder(url, null).post(Entity.entity(sampleResultsDTO, MediaType.APPLICATION_JSON));
			sampleResults = response.readEntity(new GenericType<List<SampleResultsDTO>>() {
			});
		} catch(HMTPException e) {
			logger.error("Error while calling omm to fetch mandatory field validations",e.getMessage());
		}
		return sampleResults;
	}
	
	public List<ContainerSamplesDTO> getDPCRContainerSamples(String deviceRunId)  {

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
			return Collections.emptyList();
		}

	}

}
