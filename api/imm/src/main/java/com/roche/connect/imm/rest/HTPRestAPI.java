/*******************************************************************************
 * HTPRestAPI.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
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
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.imm.utils.UrlConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Interface HTPRestAPI.
 */
@Path(UrlConstants.BASE_URL_V1)
@Api(value = "HTP Integration Management")
public interface HTPRestAPI {

	/**
	 * Consume run information.
	 *
	 * @param runResultsDTO the run results DTO
	 * @return the response
	 * @throws HMTPException the HMTP exception
	 */
	@Path("/runs")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response createRun(RunResultsDTO runResultsDTO) throws HMTPException;

	/**
	 * Submit sample status info.
	 *
	 * @param body the body
	 * @return the response
	 * @throws HMTPException the HMTP exception
	 */
	@Path("/runs/{runId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateRun(@PathParam("runId") String runId, RunResultsDTO body) throws HMTPException;

	@ApiOperation(value = "To fetch order", notes = "Order fetch")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "No order found for complex id"),
			@ApiResponse(code = 500, message = "Error occurred while processing the request. Please contact the administrator"),
			@ApiResponse(code = 200, message = "Order fetched successfully") })
	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	Response getComplexDetails(@QueryParam("complexId") String complexId) throws HMTPException;

	@ApiOperation(value = "To fetch Run details", notes = "Run details fetch")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Run details not found."),
			@ApiResponse(code = 500, message = "Error occurred in while processing the request. Please contact the administrator"),
			@ApiResponse(code = 200, message = "Run details fetched successfully.") })
	@GET
	@Path("/runs/{runId}")
	@Produces(MediaType.APPLICATION_JSON)
	Response getRunResultsByDeviceRunId(@PathParam("runId") String runId);
}
