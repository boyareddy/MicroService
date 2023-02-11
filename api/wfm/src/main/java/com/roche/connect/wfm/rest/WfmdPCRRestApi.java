/*******************************************************************************
 * 
 * 
 *  WfmRestApi.java                  
 *  Version:  1.0
 * 
 *  Authors:  narasimhareddyb
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
 *   narasimhareddyb@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Interface implementation that describes WFMdPCR rest apis.
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
import com.roche.connect.common.mp96.WFMQueryMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Interface implementation that describes WFMdPCR rest apis. 
 *
 */
@Api(value="dPCR Workflow Management")
@Path("/rest/api/v1")
public interface WfmdPCRRestApi {

    /**
     * API to start the WFM Process, QBP Input message.
     * @param message
     * @return Status of WFM Process - javax.ws.rs.core.Response type.
     * @throws HMTPException
     */
    @ApiOperation(value = "QBP Input", notes = "Passing QBP MESSAGE")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Message not added"),
       @ApiResponse(code = 200,message = "Message successfuly added") })
    @POST
    @Path("/NIPTdPCR/startwfmprocess") 
    @Produces({MediaType.APPLICATION_JSON }) 
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response getExecutedPCRStartWFM(WFMQueryMessage message) throws HMTPException;
    
   
    /**
     * API to update the status of existing dPCR process, MP96Status.
     * @param source
     * @param json
     * @return update status - javax.ws.rs.core.Response type.
     * @throws HMTPException
     */
    @ApiOperation(value = "U03 Message source: MP96Status", notes = "Passing U03 MESSAGE")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "Message not added"),
       @ApiResponse(code = 200,message = "Message successfuly added") })
    @POST
    @Path("/NIPTdPCR/updatewfmprocess/{source}") 
    @Produces({MediaType.APPLICATION_JSON })   
    public Response getExecutedPCRStatusUpdateWFM(@PathParam("source")  String source,@RequestBody String json) throws HMTPException;
    
   
   
    
    
   
}
