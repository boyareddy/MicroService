/*******************************************************************************
 * HtpSimulatorRestApi.java                  
 *  Version:  1.0
 * 
 *  Authors:  umashankar d
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
 *  umashankar-d@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.htp.adapter.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import com.hcl.hmtp.common.client.exceptions.HMTPException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Interface HtpSimulatorRestApi.
 */
@Api(value = "Forte Adapter")
@Path("/")
public interface ForteAdapterRestApi {

	/**
	 * Hit Ping
	 * @param requestBody
	 * @return
	 * @throws HMTPException
	 * @throws ParseException
	 */
	@ApiOperation(value = " Pinging simulator ", notes = "Updating ping information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "pinging to Adapter") })
	@POST
	@Path("forte/ping")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response hitPing() throws HMTPException;
	
	/**
	 * Hit Hello
	 * @param requestBody
	 * @return
	 * @throws HMTPException
	 * @throws ParseException
	 */
	@ApiOperation(value = "Update hello information", notes = "Updating hello information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Hitting Hello") })
	@POST
	@Path("forte/hello")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hitHello(@RequestBody Map<String, Object> requestBody) throws HMTPException;
	
	
	/**
	 * getJob
	 * @return
	 * @throws HMTPException
	 * @throws ParseException
	 */
	@ApiOperation(value = "Get available job information", notes = "Get available job information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Get Available jobs ") })
	@GET
	@Path("forte/jobqueue")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJob() throws HMTPException;
	
	/**
	 * updateJobStatus
	 * @param requestBody
	 * @return
	 * @throws HMTPException
	 * @throws IOException 
	 */
	@ApiOperation(value = "updateJobStatus", notes = "updated-start/inprogredd/done/error")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "updated job status") })
	@PUT
	@Path("forte/jobqueue/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateJobStatus(@PathParam("id") String id, @RequestBody Map<String, Object> requestBody) throws HMTPException, IOException;
			

	
}
