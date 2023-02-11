/*******************************************************************************
 * 
 * 
 *  WfmRestApi.java                  
 *  Version:  1.0
 * 
 *  Authors:  somesh_r
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
 *   somesh_r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Interface implementation that describes WFM rest apis.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.mp24.message.AdaptorRequestMessage;
import com.roche.connect.wfm.error.QueryValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Interface implementation that describes WFM rest apis. 
 *
 */
@Api(value="Workflow Management")
@Path("/rest/api/v1")
public interface WfmRestApi {
    
    /**
     * API to start the WFM Process, QBP Input message.
     * @param message
     * @return Status of WFM Process - javax.ws.rs.core.Response type.
     * @throws HMTPException
     */
    @ApiOperation(value = "QBP/U03 Input", notes = "Passing QBP/U03 MESSAGE")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Message not added"),
       @ApiResponse(code = 200,message = "Message successfully added") })
    @POST
    @Path("/startwfmprocess") 
    @Produces({MediaType.APPLICATION_JSON }) 
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response getExecuteStartWFM(AdaptorRequestMessage message) throws HMTPException;
    

    
    /**
     * API to update the status of existing process, MPStatus/LQuery/LPStatus.
     * @param source
     * @param json
     * @return update status - javax.ws.rs.core.Response type.
     * @throws HMTPException
     * @throws QueryValidationException 
     */
    @ApiOperation(value = "QBP/U03 Message source: MPStatus/LQuery/LPStatus Input", notes = "Passing QBP/U03 MESSAGE")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Message not added"),
       @ApiResponse(code = 200,message = "Message successfully added") })
    @POST
    @Path("/updatewfmprocess/{source}") 
    @Produces({MediaType.APPLICATION_JSON })   
    public Response getExecuteStatusUpdateWFM(@PathParam("source")  String source,@RequestBody String json) throws HMTPException, QueryValidationException;
    

    
    /**
     * API to deploy XML files, yet to implement.
     * @param message
     * @return deployment status - javax.ws.rs.core.Response type.
     * @throws HMTPException
     */
    @ApiOperation(value = "Xml files to store,Yet to Implement", notes = "Xml file to store into DB,Yet to Implement")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Message not added."),
        @ApiResponse(code = 200, message = "Message successfully added") })
    @POST
    @Path("/deploy") 
    @Produces({ MediaType.APPLICATION_JSON }) 
    @Consumes({ MediaType.APPLICATION_JSON })   
    public Response deployWorkflowProcess(AdaptorRequestMessage message) throws HMTPException;
    
    
    /**
     * API to update task completion status.
     * @param message
     * @return javax.ws.rs.core.Response
     * @throws HMTPException
     */
    @ApiOperation(value = "Yet to Implement ", notes = "Yet to Implement") 
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Yet to Implement"),
       @ApiResponse(code = 200, message = "Yet to Implement") })
    @POST 
    @Path("/task/{taskId}/complete") 
    @Produces({MediaType.APPLICATION_JSON }) 
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response doTaskComplete(@RequestBody AdaptorRequestMessage message) throws HMTPException;
    
    
    
    /**
     * API to get task completion status.
     * @param message
     * @return completion status - javax.ws.rs.core.Response type.
     * @throws HMTPException
     */
    @ApiOperation(value = "Yet to Implement ", notes = "Yet to Implement") 
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Yet to Implement"),
       @ApiResponse(code = 200, message = "records found for given oneYet to Implement") }) 
    @POST 
    @Path("/task/{taskId}/status") @Produces({ MediaType.APPLICATION_JSON }) 
    @Consumes({ MediaType.APPLICATION_JSON })
    
    public Response getTaskCompleteStatus(AdaptorRequestMessage message) throws HMTPException;
    
}
