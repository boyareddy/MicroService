/*******************************************************************************
 * AssayIntegrationService.java Version: 1.0 Authors: somesh_r
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
 * ********************* ChangeLog: V.Prem@hcl.com : Updated copyright headers
 * ********************* ********************* Description: Class implementation
 * that provides integration support for AMM. *********************
 ******************************************************************************/
package com.roche.connect.wfm.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.MolecularIDTypeDTO;
import com.roche.connect.wfm.constants.WfmConstants.API_URL;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.AssayTypeDTO;
import com.roche.connect.wfm.model.DeviceTestOptionsDTO;
import com.roche.connect.wfm.model.ProcessStepActionDTO;
import com.roche.connect.wfm.model.WfmDTO;

@Service("assayIntegrationService") public class AssayIntegrationService {
    
    OrderIntegrationService orderIntegrationService = new OrderIntegrationService();
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    /**
     * Method that returns Assay type id for a given assay type.
     * @param assayType
     * @return assyTypeId - Long
     * @throws HMTPException
     */     
	public List<AssayTypeDTO> findAssayDetails() throws HMTPException, IOException {   
        String url = "";
        try {
            logger.info("Calling AMM to find Assay Type Details");
            url = RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
                null, null);
            Invocation.Builder assayClient =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            List<AssayTypeDTO> listAssays = assayClient.get(new GenericType<List<AssayTypeDTO>>() {});
            
            if (!listAssays.isEmpty()) {
                return listAssays;
            }
            
            logger.info("Find Assay Details Completed..");
        } catch (IOException  exp) {
            logger.error("Error occurred while calling at findAssayDetails() ",exp);
            throw new HMTPException(exp);
            
        }
        return Collections.emptyList();
        	
    }
    
    /**
     * Method that returns Process steps for a given assayTypeId
     * @param assayTypeID
     * @return List of ProcessStepActionDTO
     * @throws HMTPException
     */
    public List<ProcessStepActionDTO> findProcessStepForAssay(String assayTypeId) throws HMTPException,IOException {
        
        String url = "";
        List<ProcessStepActionDTO> listProcessSteps = null;
        try {
            logger.info(" -> findProcessStepForAssay()::Call AMM to find Process Step Details");
            url = RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
                WfmURLConstants.AMM_ASSAY_API_PATH + assayTypeId, WfmURLConstants.PROCESS_STEP_ACTION, null);
            Invocation.Builder assayClient =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            listProcessSteps = assayClient.get(new GenericType<List<ProcessStepActionDTO>>() {});
            
        } catch (IOException exp) {
            logger.error("Error occurred while calling at findProcessStepForAssay ",exp);
            throw new HMTPException(exp);
        }
        logger.info(" <- findProcessStepForAssay()");
        return (!listProcessSteps.isEmpty()) ? listProcessSteps : null;
        
    }
    
    /**
     * Method to return AssayTypeDTO list for a given assay type.
     * @param assayType
     * @return List of AssayTypeDTO
     * @throws HMTPException
     */
    public List<AssayTypeDTO> findAssayDetail(String assayType) throws HMTPException,IOException {
        
        String url = "";
        List<AssayTypeDTO> listAssays = null;
        try {
            if (assayType == null) {
                throw new HMTPException("Assay type cannot be null.");
            }
            logger.info(" -> findAssayDetail()::Call AMM to find Assay Type Details");
            url = RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_TYPE,
                assayType, null);
            Invocation.Builder assayClient =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            listAssays = assayClient.get(new GenericType<List<AssayTypeDTO>>() {});
        }catch (IOException exp) {
            logger.error("Error occurred while calling at findAssayDetail(String assayType) ",exp);
            throw new HMTPException(exp);
        } 
        logger.info(" <- findAssayDetail()");
        return (!listAssays.isEmpty()) ? listAssays : null;
        
    }
    
    public List<DeviceTestOptionsDTO> findDeviceTestOptions(String assayType, String deviceType, String processStepName)
        throws HMTPException,IOException {
        
        String url = "";
        try {
            logger.info("Calling AMM to find Test Options Details");
            if (processStepName != null) {
                url = RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
                    WfmURLConstants.AMM_ASSAY_API_PATH + assayType, WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType
                        + WfmURLConstants.AMM_AMPERSAND + WfmURLConstants.AMM_PROCESSSTEP_PARAM + processStepName,
                    null);
            } else {
                url = RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "",
                    WfmURLConstants.AMM_ASSAY_API_PATH + assayType,
                    WfmURLConstants.AMM_DEVICE_TEST_OPTIONS + deviceType, null);
            }
            Invocation.Builder assayClient =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            List<DeviceTestOptionsDTO> listTestOptions =
                assayClient.get(new GenericType<List<DeviceTestOptionsDTO>>() {});
            
            if (!listTestOptions.isEmpty()) {
                return listTestOptions;
            }
            
        } catch (IOException exp) {
            logger.error("Error occurred while calling at findDeviceTestOptions(Long id,String deviceType) ",exp);
            throw new HMTPException(exp);
        } 
        logger.info("Find Test Options Details Completed..");
        return Collections.emptyList();
        
    }
    
    /**
     * Method that returns process steps for given accessioning id.
     * @param accessioningId
     * @return List of ProcessStepActionDTO
     * @throws HMTPException
     * @throws OrderNotFoundException
     */
    public List<ProcessStepActionDTO> getProcessStepsByAccessioningID(String accessioningId)
        throws HMTPException,
        OrderNotFoundException,IOException {
        List<WfmDTO> orderList = null;
        String assayType;
        orderList = orderIntegrationService.findOrder(accessioningId);
        logger.info("-> getProcessStepsByAccessioningID()::OrderDetails ", accessioningId, orderList);
        assayType = orderList.get(0).getAssayType();
        logger.info("<- getProcessStepsByAccessioningID()");
        return findProcessStepForAssay(assayType);
    }
    
    /**
     * Method that returns TestOptions by accessioning id and device.
     * @param accessioningId
     * @param deviceType
     * @return List of DeviceTestOptionsDTO object.
     * @throws HMTPException
     * @throws IOException 
     */
    public List<DeviceTestOptionsDTO> getTestOptionsByAccessioningIDandDevice(String accessioningId, String deviceType)
        throws HMTPException, IOException {
        List<WfmDTO> orderList = null;
        String assayType = null;
        logger.info(
            " -> getTestOptionsByAccessioningIDandDevice()::Call AMM to find device test options details for given accessioning id and device type");
        try {
            orderList = orderIntegrationService.findOrder(accessioningId);
            assayType = orderList.get(0).getAssayType();
        } catch (OrderNotFoundException e) {
            logger.error("Exception throwing for Order not available", e);
        }
        return findDeviceTestOptions(assayType, deviceType, null);
    }
    
    public List<MolecularIDTypeDTO> getMolecularIdByAssayTypeandPlateTypeAndPlateLocation(String assayType, String plateType,
        String plateLocation) throws HMTPException,IOException {
        
        String url = "";
        try {
            logger.info("Calling AMM to Get MolicularId By Using AssayType , PlateType and PlateLocation");
                url =
                    RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
                        WfmURLConstants.AMM_MOLECULAR_ID_ASSAY + assayType + WfmURLConstants.AMM_AMPERSAND
                            + WfmURLConstants.AMM_MOLECULAR_ID_PLATETYPE + plateType + WfmURLConstants.AMM_AMPERSAND
                            + WfmURLConstants.AMM_MOLECULAR_ID_PLATELOCATION + plateLocation,
                        null);
                Invocation.Builder assayClient =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            List<MolecularIDTypeDTO> listMolicularIds = assayClient.get(new GenericType<List<MolecularIDTypeDTO>>() {});
            logger.info("Molicular ID Details:   "+ listMolicularIds);
            
            if (!listMolicularIds.isEmpty()) {
                return listMolicularIds;
            }
            
        } catch (IOException exp) {
            logger.error("\"Error occurred while calling at getMolicularIdByAssayTypeandPlateTypeAndPlateLocation(String assayType, String plateType,String plateLocation) ",exp);
            throw new HMTPException(exp);
        } 
        logger.info("Getting MolicularId By Using AssayType, PlateType and PlateLocation Completed..");
        return Collections.emptyList();
        
    }
    
    /**
     * Method that returns TestOptions by accessioning id and device.
     * @param accessioningId
     * @param deviceType
     * @return List of DeviceTestOptionsDTO object.
     * @throws HMTPException
     */
    public List<DeviceTestOptionsDTO> getTestOptionsByAccessioningIDandDeviceandProcessStep(String accessioningId,
        String deviceType, String processStepName) throws HMTPException,IOException {
        List<WfmDTO> orderList = null;
        String assayType = null;
        logger.info(
            " -> getTestOptionsByAccessioningIDandDeviceandProcessStep()::Call AMM to find device test options details for given accessioning id and device type and Process Stepname.");
        try {
            orderList = orderIntegrationService.findOrder(accessioningId);
            assayType = orderList.get(0).getAssayType();
        } catch (OrderNotFoundException e) {
            logger.error("Exception throwing for Order not available", e);
        }
        return findDeviceTestOptions(assayType, deviceType, processStepName);
    }
    
    /**
     * Method that returns TestOptions by accessioning id and device.
     * @param accessioningId
     * @param deviceType
     * @return List of DeviceTestOptionsDTO object.
     * @throws HMTPException
     */
    public List<DeviceTestOptionsDTO> getTestOptionsByOrderIdandDevice(List<WfmDTO> orderDetail, String deviceType)
        throws HMTPException,IOException {
        String assayType = null;
        logger.info(
            " -> getTestOptionsByOrderIdandDevice()::Call AMM to find device test options details for given order id and device type");
        assayType = orderDetail.get(0).getAssayType();
        return findDeviceTestOptions(assayType, deviceType, null);
    }
    
    /**
     * Method that returns TestOptions by accessioning id and device.
     * @param accessioningId
     * @param deviceType
     * @return List of DeviceTestOptionsDTO object.
     * @throws HMTPException
     */
    public List<DeviceTestOptionsDTO> getTestOptionsByOrderIdandDeviceandProcessStep(List<WfmDTO> orderDetail,
        String deviceType, String processStepName) throws HMTPException,IOException {
        String assayType = null;
        logger.info(
            " -> getTestOptionsByOrderIdandDeviceandProcessStep()::Call AMM to find device test options details for given order id and device type and process step name    ");
        assayType = orderDetail.get(0).getAssayType();
        return findDeviceTestOptions(assayType, deviceType, processStepName);
    }
    
    
    /**
     * Method that returns accessioningId by containerId.
     * @param containerId
     * @param stepName
     * @return accessioningId.
     * @throws HMTPException
     * @throws UnsupportedEncodingException
     */
    public String findAccessingIdByContainerId(String containerId, String stepName)
        throws IOException,
        HMTPException,
        OrderNotFoundException {
        
        String[] container = containerId.trim().split("_");
        ResultIntegrationService resultIntegrationService = new ResultIntegrationService();
        
        List<WfmDTO> listWfmDto =
            resultIntegrationService.findAccessingIdByContainerId(container[0], container[1], stepName);
        logger.info("findAccessingIdByConntainerId result :" + listWfmDto);
        
        if (listWfmDto.isEmpty()) {
            logger.warn("findAccessingIdByConntainerId : " + listWfmDto.size());
        }
        
        return listWfmDto.iterator().next().getAccessioningId();
    }
    
    /**
     * Method that returns validation of ProcessStep.
     * @param accessioningId
     * @param deviceType
     * @param processStep
     * @return True/False.
     * @throws HMTPException
     * @throws OrderNotFoundException
     */   
    public boolean validateAssayProcessStep(String accessioningId, String deviceType, String processStep)
        throws HMTPException,
        OrderNotFoundException,IOException {
        
        List<ProcessStepActionDTO> processSteps = getProcessStepsByAccessioningID(accessioningId);
        
        logger.info("assayIntegrationService.getProcessStepsByAccessioningID result :" + processSteps);
        
        if (processSteps.isEmpty()) {
            logger.warn("getProcessStepsByAccessioningID size is zero : " + processSteps.size());
        }
        return (processSteps.stream().anyMatch(process -> process.getDeviceType().equalsIgnoreCase(deviceType)
            && process.getProcessStepName().equalsIgnoreCase(processStep)));
    }
    
    /**
     * Method that returns validation of ProcessStep By manual.
     * @param accessioningId
     * @param deviceType
     * @param processStep
     * @param ManualFlag
     * @return True/Flase.
     * @throws HMTPException
     * @throws OrderNotFoundException
     */   
    public boolean validateAssayProcessStepManual(String accessioningId, String deviceType, String processStep,
        String manualFlag) throws HMTPException, OrderNotFoundException,IOException {
        
        List<ProcessStepActionDTO> processSteps = getProcessStepsByAccessioningID(accessioningId);
        
        logger.info("assayIntegrationService.getProcessStepsByAccessioningID result :" + processSteps);
        
        if (processSteps.isEmpty()) {
            logger.warn("getProcessStepsByAccessioningID is zero : " + processSteps.size());
        }
        return (processSteps.stream()
            .anyMatch(process -> process.getDeviceType().equalsIgnoreCase(deviceType)
                && process.getProcessStepName().equalsIgnoreCase(processStep)
                && process.getManualVerificationFlag().equalsIgnoreCase(manualFlag)));
    }
    
	public List<com.roche.connect.common.amm.dto.ProcessStepActionDTO> getProcessStepAction(String assayType,
			String deviceType) throws HMTPException {

		logger.info("MessageProcessorService.getProcessStepAction: " + assayType + "," + deviceType);

		List<com.roche.connect.common.amm.dto.ProcessStepActionDTO> processStepActions = null;

		String url = RestClientUtil.getUrlString(API_URL.AMM_API_URL.toString(), "", WfmURLConstants.AMM_ASSAY_API_PATH,
				"/" + assayType + WfmURLConstants.PROCESS_STEP_ACTION, null);

		try {

			if (deviceType != null)
				url = url + "?" + WfmURLConstants.ADM_DEVICE_URL + deviceType;

			logger.info("Calling Api to AMM to get process step actions: " + url);

			Invocation.Builder assayClient = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8),
					null);
			processStepActions = assayClient
					.get(new GenericType<List<com.roche.connect.common.amm.dto.ProcessStepActionDTO>>() {
					});

		} catch (IOException exp) {
			logger.error("Exception occured while calling AMM getWFProcessData: " + exp.getMessage());
			 throw new HMTPException(exp);
		}

		return processStepActions;
	}
    
}
