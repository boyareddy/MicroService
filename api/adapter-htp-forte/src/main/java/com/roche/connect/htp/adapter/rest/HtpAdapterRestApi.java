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

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.htp.adapter.model.Notification;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Interface HtpSimulatorRestApi.
 */
@Api(value = "Htp Simulator")
@Path("/")
public interface HtpAdapterRestApi {

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
	@Path("htp/ping")
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
	@Path("htp/hello")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hitHello(@RequestBody Map<String, Object> requestBody) throws HMTPException;
	
	/**
	 * GoodBye Message
	 * @param message
	 * @return
	 */
	@ApiOperation(value = "GoodBye Message", notes = "Update GoodBye Message")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Hitted GoodBye Message"),
			@ApiResponse(code = 401, message = "device not registered in connect") })
	@POST
	@Path("htp/goodbye")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response goodBye();
	
	/**
	 * Creates the run.
	 *
	 * @param requestBody the request body
	 * @return the response
	 * @throws HMTPException the HMTP exception
	 * @throws ParseException the parse exception
	 */
	@ApiOperation(value = "Create Run information", notes = "Create Run information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Run information saved") })
	@POST
	@Path("htp/runs")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRun(@RequestBody Map<String, Object> requestBody) throws HMTPException, ParseException;
	
	@ApiOperation(value = "Create Run information", notes = "Create Run information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 201, message = "Run information saved") })
	@GET
	@Path("htp/orders/{complexId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderDetails(@PathParam("complexId") String complexId) throws HMTPException, ParseException;

	
	
	/**
	 * Update run.
	 *
	 * @param id the id
	 * @param requestbody the requestbody
	 * @return the response
	 * @throws HMTPException the HMTP exception
	 * @throws JsonProcessingException the json processing exception
	 * @throws ParseException 
	 */
	@ApiOperation(value = "Update Run  information", notes = "Update Run status information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Run status information updated successfully") })
	@PUT
	@Path("htp/runs/{runId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRun(@PathParam("runId") String id, @RequestBody Map<String, Object> requestbody)
			throws HMTPException, JsonProcessingException, ParseException;
	
	/**
	 * Update run.
	 *
	 * @param id the id
	 * @param requestbody the requestbody
	 * @return the response
	 * @throws HMTPException the HMTP exception
	 * @throws JsonProcessingException the json processing exception
	 * @throws ParseException 
	 */
	@ApiOperation(value = "Update Run status  information", notes = "Update Run status information")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "The requested URL not found"),
			@ApiResponse(code = 202, message = "Run status information updated successfully") })
	@PUT
	@Path("htp/runs/{runId}/run_status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRunStatus(@PathParam("runId") String id, Map<String, Object> requestbody)
			throws HMTPException, JsonProcessingException, ParseException;
	
	
	/**
	 * Update run.
	 *
	 * @param id the id
	 * @param requestbody the requestbody
	 * @return the response
	 * @throws HMTPException the HMTP exception
	 * @throws JsonProcessingException the json processing exception
	 * @throws ParseException 
	 */
	@ApiOperation(value = "get Run  information", notes = "Update Run status information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Run status information updated successfully") })
	@GET
	@Path("htp/runs/{runId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRun(@PathParam("runId") String runId);

	/**
	 * Update cycle.
	 *
	 * @param id the id
	 * @param requestBody the request body
	 * @return the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@ApiOperation(value = "Send Cycle details", notes = "api sends Cycle information recieved from Simulator  to IMM ")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found "),
			@ApiResponse(code = 200, message = "Run information saved") })
	@PUT
	@Path("htp/runs/{id}/cycle")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCycle(@PathParam("id") String id, @RequestBody Map<String, Object> requestBody)
			throws IOException;

	/**
	 * Complete cycle.
	 *
	 * @param id the id
	 * @return the response
	 */
	@ApiOperation(value = "Update cycle complete", notes = "Update cycle complete")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found "),
			@ApiResponse(code = 200, message = "Cycle completed successfully") })
	@PUT
	@Path("htp/runs/{id}/cyclecomplete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completeCycle(@PathParam("id") String id,@RequestBody Map<String, Object> requestBody );

	/**
	 * Freespace.
	 *
	 * @return the response
	 * @throws JSONException the JSON exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@ApiOperation(value = "Retrive Free space", notes = "Retrive Free space")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found "),
			@ApiResponse(code = 200, message = "Free space amount") })
	@GET
	@Path("htp/freespace")
	@Produces(MediaType.APPLICATION_JSON)
	public Response freespace() throws JSONException, IOException;

	/**
	 * Notification.
	 *
	 * @param requestBody the request body
	 * @return the response
	 * @throws JSONException the JSON exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HMTPException 
	 */
	@ApiOperation(value = "Send notification", notes = "Send notification")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found "),
			@ApiResponse(code = 200, message = "Nnotification sent successfully") })
	@POST
	@Path("htp/notification")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response notification(@RequestBody Notification notification) throws JSONException, IOException, HMTPException;
	
	/**
	 * Update run.
	 *
	 * @param id the id
	 * @param requestbody the requestbody
	 * @return the response
	 * @throws IOException 
	 * @throws HMTPException the HMTP exception
	 * @throws JsonProcessingException the json processing exception
	 * @throws ParseException 
	 */
	@ApiOperation(value = "Update Run  information", notes = "Update Run status information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "Run status information updated successfully") })
	@PUT
	@Path("htp/file")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateLogs(@PathParam("runId") String id, @RequestBody Map<String, Object> requestbody) throws IOException;
			
	
	@ApiOperation(value = "Transfer Complete ", notes = "Transfer Complete ")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found "),
            @ApiResponse(code = 200, message = "Transfer Completed ") })
    @PUT
    @Path("htp/runs/{runId}/transferscomplete")
    public Response completeFileTransfer(@PathParam("runId") String runId);
	
}
