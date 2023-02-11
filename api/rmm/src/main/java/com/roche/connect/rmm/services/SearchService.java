package com.roche.connect.rmm.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.common.constant.AssayType;
import com.roche.connect.common.constant.MessageType;
import com.roche.connect.common.constant.StatusMessage;
import com.roche.connect.common.dmm.DeviceDTO;
import com.roche.connect.common.order.dto.ContainerDTO;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderElements;
import com.roche.connect.common.order.dto.OrderStatus;
import com.roche.connect.common.rmm.dto.SearchOrder;
import com.roche.connect.common.rmm.dto.SearchOrderElements;
import com.roche.connect.common.rmm.dto.SearchRunResult;
import com.roche.connect.common.rmm.dto.SearchRunResultElements;
import com.roche.connect.common.spring.data.OffsetBasedPageRequest;
import com.roche.connect.common.util.RunStatusConstants;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.readrepository.RunResultsReadRepository;
import com.roche.connect.rmm.readrepository.SampleResultsReadRepository;
import com.roche.connect.rmm.util.MapEntityToDTO;

@Service
public class SearchService {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private RunResultsReadRepository runResultsReadRepository;

	@Autowired
	private SampleResultsReadRepository sampleResultsReadRepository;

	@Autowired
	private OrderIntegrationService orderIntegrationService;

	@Autowired
	private DeviceIntegrationService deviceIntegrationService;

	@Autowired
	private MapEntityToDTO mapEntityToDTO;

	public SearchRunResultElements getSearchRunResultElements(String searchQuery, int offset, int limit) {

		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();

		Pageable pageable = new OffsetBasedPageRequest(offset, limit);

		Page<RunResults> runResultsPage = runResultsReadRepository
				.findByDeviceRunIdContainingIgnoreCaseAndCompanyId(searchQuery, domainId, pageable);

		List<RunResults> runResultsList = runResultsPage.getContent();

		Map<String, ContainerDTO> containerMap = new HashMap<>();
		runResultsList.stream()
				.filter(r -> (r.getRunStatus().equals(RunStatusConstants.MP96_PENDING)
						&& r.getProcessStepName().equals(MessageType.MP96_NAEXTRACTION)
						&& r.getAssayType().equals(AssayType.NIPT_DPCR)))
				.forEach(r -> {

					List<ContainerSamplesDTO> containerSamples = orderIntegrationService
							.getDPCRContainerSamples(r.getDeviceRunId());

					if (containerSamples == null || containerSamples.isEmpty())
						return;

					ContainerDTO containerDTO = new ContainerDTO();
					containerDTO.setContainerId(containerSamples.get(0).getContainerID());
					containerDTO.setContainerType(containerSamples.get(0).getContainerType());
					long count = containerSamples.stream()
							.filter(c -> c.getStatus().equals(StatusMessage.MP96_SENT_TO_DEVICE)).count();
					containerDTO.setSampleCount(count);

					containerMap.put(r.getDeviceRunId(), containerDTO);

				});

		List<String> deviceIds = runResultsList.stream().map(RunResults::getDeviceId).collect(Collectors.toList());

		List<DeviceDTO> deviceList = new ArrayList<>();
		try {
			deviceList = deviceIntegrationService.getDevice(deviceIds);
		} catch (Exception e) {
			logger.error("Failed to get Device information,", e);
		}

		Map<String, String> deviceMap = deviceList.stream().filter(Objects::nonNull)
				.collect(Collectors.toMap(DeviceDTO::getSerialNo, x -> x.getDeviceType().getName()));

		Set<SearchRunResult> searchRunResult = runResultsList.stream().filter(Objects::nonNull).map(
				runResults -> mapEntityToDTO.getSearchRunResultsDTOFromRunResults(runResults, deviceMap, containerMap))
				.collect(Collectors.toSet());

		SearchRunResultElements searchRunResultElements = new SearchRunResultElements();
		searchRunResultElements.setTotalElements(runResultsPage.getTotalElements());
		searchRunResultElements.setRunResults(searchRunResult);
		return searchRunResultElements;
	}

	public SearchOrderElements getSearchOrderElements(String searchQuery, int offset, int limit) {

		long domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();

		int pageNumber = 1;
		if (limit != 0) {
			pageNumber = (offset / limit) + 1;
		}

		pageNumber = pageNumber < 1 ? 1 : pageNumber;
		Pageable pageable = new PageRequest(pageNumber - 1, limit);

		Page<SampleResults> sampleResultsPage = sampleResultsReadRepository
				.findByAccessioningIdContainingIgnoreCase(searchQuery, domainId, pageable);

		Collection<OrderDTO> searchUnassignedOrders = new ArrayList<>();
		int numberOfSampleResultElement = sampleResultsPage.getNumberOfElements();

		OrderElements orderElements = getSearchUnassignedOrderElements(searchQuery, sampleResultsPage, offset,
				limit);
		if (orderElements != null) {
			searchUnassignedOrders = orderElements.getOrders();
		}

		List<SearchOrder> searchOrders = sampleResultsPage.getContent().stream().filter(Objects::nonNull)
				.map(mapEntityToDTO::getSearchOrderFromSampleResults).collect(Collectors.toList());

		if (numberOfSampleResultElement < limit) {
			searchOrders.addAll(searchUnassignedOrders.stream().filter(Objects::nonNull)
					.map(mapEntityToDTO::getSearchOrderFromOrderDTO).collect(Collectors.toList()));
		}

		long totalElements = orderElements != null
				? (sampleResultsPage.getTotalElements() + orderElements.getTotalElements())
				: sampleResultsPage.getTotalElements();

		SearchOrderElements searchOrderElements = new SearchOrderElements();
		searchOrderElements.setTotalElements(totalElements);
		searchOrderElements.setOrders(searchOrders);
		return searchOrderElements;

	}

	private OrderElements getSearchUnassignedOrderElements(String searchQuery, Page<SampleResults> sampleResultsPage,
			int offset, int limit) {

		try {

			int numberOfSampleResultElement = sampleResultsPage.getNumberOfElements();
			int orderOffset = 0;
			int orderLimit = limit;
			if (numberOfSampleResultElement == 0) {
				long totalSampleResultRecord = sampleResultsPage.getTotalElements();

				orderOffset = (int) (offset - totalSampleResultRecord);
			} else if (numberOfSampleResultElement < limit) {
				orderLimit = limit - numberOfSampleResultElement;
			}

			return orderIntegrationService.searchOrderByAccessioningId(searchQuery, OrderStatus.ORDER_STATUS_UNASSIGNED,
					orderOffset, orderLimit);

		} catch (Exception e) {
			logger.error("Failed to search unassigned order", e);
			return null;
		}
	}
}
