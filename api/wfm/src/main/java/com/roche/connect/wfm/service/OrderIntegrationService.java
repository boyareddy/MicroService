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

package com.roche.connect.wfm.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmURLConstants;
import com.roche.connect.wfm.error.OrderNotFoundException;
import com.roche.connect.wfm.model.WfmDTO;

/**
 * Class implementation that provides integration support for OMM.
 */
@Service("orderIntegrationService") public class OrderIntegrationService {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    String utf8 = "UTF-8";
    
   
    
    /**
     * Method to find orders from OMM DB by accessioningId
     * @param accessioningId
     * @return List of WfmDTO object.
     * @throws HMTPException
     * @throws OrderNotFoundException
     */
    public List<WfmDTO> findOrder(String accessioningId) throws HMTPException, OrderNotFoundException {
        logger.info("OrderIntegrationService -> findOrder()");
        String url = null;
        try {
            
            url = RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(), "",
                WfmURLConstants.ORDERS_API_PATH + WfmURLConstants.ACCESSIONING_ID, accessioningId, null);
            Invocation.Builder orderClient =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            int status = orderClient.get().getStatus();
            if (status != 200) {
            	 if (status == 404)	
            		 throw new OrderNotFoundException("No order found");
            	 else if (status == 500) 
            		 throw new HMTPException("500:Internal exception occurs while calling findOrder()");
            	 else 
            		 throw new HMTPException("Exception occurs while calling findOrder()");
            }
            
            List<WfmDTO> listOrders = orderClient.get(new GenericType<List<WfmDTO>>() {});
            logger.info("OrderIntegrationService -> listOrders size::"+listOrders.size());
            return (!listOrders.isEmpty()) ? listOrders : null;
        } catch (UnsupportedEncodingException e) {
        	logger.error("Error occurred at findOrder() " + url);
            throw new HMTPException(e);
        }
    }
    
    /**
     * Method to update order status for given orders.
     * @param wfmDTO
     * @return boolean - update status.
     * @throws HMTPException
     * @throws UnsupportedEncodingException
     */
    public boolean updateOrders(WfmDTO wfmDTO) throws HMTPException, UnsupportedEncodingException {
        String url = RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(), "",
            WfmURLConstants.ORDERS_API_PATH + WfmURLConstants.ACCESSIONING_ID, wfmDTO.getAccessioningId(), null);
        Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
        
        List<OrderDTO> listOrders = orderClient.get(new GenericType<List<OrderDTO>>() {});
        OrderDTO order = (!listOrders.isEmpty()) ? listOrders.get(0) : null;
        
        if (order != null && order.getAccessioningId().equals(wfmDTO.getAccessioningId())) {
            url = RestClientUtil.getUrlString(WfmConstants.API_URL.OMM_API_URL.toString(),
                WfmURLConstants.ORDERS_API_PATH,
                WfmURLConstants.ORDER_ID + order.getOrderId() + WfmURLConstants.ORDER_STATUS_IN_WORKFLOW, "", null);
            Invocation.Builder updateOrder =
                RestClientUtil.getBuilder(URLEncoder.encode(url, WfmURLConstants.UTF_8), null);
            updateOrder.put(Entity.entity(wfmDTO, MediaType.APPLICATION_JSON));
            
        }
        
        return true;
    }
}
