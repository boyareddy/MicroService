/*******************************************************************************
 * RunCrudRestApi.java                  
 *  Version:  1.0
 * 
 * Authors: prasant.sahoo
 * 
 * ==================================
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *  ==================================
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.rmm.rest;

import java.io.IOException;
import java.util.List;

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
import com.roche.connect.common.rmm.dto.ProcessStepValuesDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;
import com.roche.connect.rmm.jasper.dto.DeviceRunResultsDTO;
import com.roche.connect.rmm.model.RunResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.JRException;

@Api(value = "Result Management")
@Path("/rest/api/v1")
public interface RunCrudRestApi {

	@ApiOperation(value = "Get runResults based on deviceId,processStepName and status", notes = "Get runId")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "No active runId present"),
			@ApiResponse(code = 200, message = "Active runId is present") })
	@GET
	@Path("/runresults")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getRunResults(@QueryParam(value = "deviceid") String deviceid,
			@QueryParam(value = "processstepname") String processstepname,
			@QueryParam(value = "runstatus") String runstatus) throws HMTPException;	
	
	
	
	@ApiOperation(value = "Add runresults to runresults table", notes = "Add runresults")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "Error while adding runresults"),
			@ApiResponse(code = 200, message = "Runresults added successfully") })
	@POST
	@Path("/runresults")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addRunResults(RunResultsDTO runResultsDTO) throws HMTPException;
	
	
	
	@ApiOperation(value = "Add sampleresults to sampleresults table", notes = "Add sampleresults")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "Error while adding sampleresults"),
			@ApiResponse(code = 200, message = "sampleresults added successfully") })
	@POST
	@Path("/runresults/sampleresults")
	@Produces("application/json")
	@Consumes("application/json")
    public Response postSampleResults(SampleResultsDTO sampleResultsDTO) throws HMTPException;	
	
	
	@ApiOperation(value = "Update runresults to runresults table", notes = "Update runresults")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "Error while updating runresults"),
			@ApiResponse(code = 200, message = "Runresults updated successfully"),
			@ApiResponse(code = 404, message = "Runresults not available for this RunID")})
	@PUT
	@Path("/runresults")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateRunResults(RunResultsDTO runResultsDTO) throws HMTPException;
	
	
	@ApiOperation(value = "Get RunResults based on SampleResultsID", notes = "Get RunResults")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "SampleResultsID is not present"),
			@ApiResponse(code = 200, message = "SampleResultsID is present") })
	@GET
	@Path("/runresults/sampleresults/{sampleresultid}")
	@Produces("application/json")
	public Response getRunResults(@PathParam(value="sampleresultid") long sampleresultid) throws HMTPException;
	
	
	@ApiOperation(value = "Get SampleResults based on deviceId,processStepName,outputContainerId,outputContainerPosition", notes = "Fetched RunResults Details based on processStepName,outputContainerId,outputContainerPosition")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Error while Retrieving  getSampleResults Values"),
            @ApiResponse(code = 200, message = "Retrived SampleResults successfully ") })	
    @GET
    @Path("/runresults/sampleresults")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getSampleResults(@QueryParam("deviceId") String deviceId, @QueryParam("processStepName") String processStepName,
        @QueryParam("outputContainerId") String outputContainerId,
        @QueryParam("inputContainerId") String inputContainerId,
        @QueryParam("inputContainerPosition") String inputContainerPosition,
        @QueryParam("outputContainerPosition") String outputContainerPosition,
        @QueryParam("accessioningId") String accessioningId) throws HMTPException;
    
    
    
    @ApiOperation(value = "Get ProcessStep Results by passing accessioningId", notes = "Retrieve  ProcessStep Results")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Error while Retrieving  ProcessStepAction Values"),
    @ApiResponse(code = 200, message = "Retrieved ProcessStep Results Values successfully") })
    @GET
    @Path("/runresults/processstepresults/")
    @Produces("application/json")
    public Response getProcessStepResults(@QueryParam(value="accessioningId") String accessioningId)throws HMTPException;
    
	
	@ApiOperation(value = "Get Sample Results & Run Results by passing deviceRunId,processStepName and deviceid", notes = "Get Sample Results & Run Results")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Error while updating sample results"),
    @ApiResponse(code = 200, message = "Sample Results & Run Results returned successfully") })
    @GET
    @Path("/runresults/runresultsByDeviceId/")
    @Produces("application/json")
    public Response getSampleandRunResults(@QueryParam(value="deviceRunId") String deviceRunId, @QueryParam(value="processStepName") String processStepName, @QueryParam(value="deviceId") String deviceid)throws HMTPException;
		
	
	@ApiOperation(value = "Retrieves RunResults data based on deviceRunId", notes = "Fetched RunResults Details based on deviceRunId")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "records found for deviceId",response = void.class), 
			@ApiResponse(code = 404, message = "Resource/URL Not Found",response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class),
			@ApiResponse(code = 500, message = "Error while Retrieving  Run Results data ", response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class)
			})
	@Path("/runresults/devicerunid")
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	public Response getRunResultsByDeviceRunId(@QueryParam("deviceRunId") String deviceRunId) throws HMTPException;


    @ApiOperation(value = "find SAMPLESLIST details through DeviceId and runstatus", notes = "API service to get list of SAMPLESLIST based on deviceid and runstatus")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "Error while fetch sampleresults"),
	@ApiResponse(code = 200, message = "Fetch list of sampleresults  successfully") })
	 @GET
	 @Path("/runresults/sampleresultslist")
	 @Produces("application/json")
	 public Response findInprogressStatusByDeviceID(@QueryParam("deviceid") String deviceId,@QueryParam("runstatus") String runStatus) throws HMTPException;
    
    
    @ApiOperation(value = "getAllDetailsByRunResultsId", notes = "Fetch AllDetails based on runResultId")
   	@ApiResponses(value = {@ApiResponse(code = 500, message = "RunResultId is not Exits"),
   			@ApiResponse(code = 200, message = "Get AllDetails  based on runResultId") })
   	@GET
   	@Path("/runresults/runresultsbyid/{runresultId}")
   	@Produces("application/json")
   	public Response getAllDetailsByRunResultsId(@PathParam("runresultId")String runresultId) throws HMTPException;
    

    
    @ApiOperation(value = "Retrieves RunResults data based on processstep name", notes = "Fetched RunResults Details based on Workflow processstep")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "records found for Workflow processstep",response = void.class), 
                            @ApiResponse(code = 404, message = "Resource/URL Not Found",response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class),
                            @ApiResponse(code = 500, message = "Error while Retrieving getRunResultsByProcessStepName api call", response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class)
                                    })
    @Path("/runresults/processstep/")
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Response getRunResultsByProcessStepName(@ApiParam(value = "processstep") @QueryParam("processstep" ) String processStepName) throws HMTPException;   
    
   
    
    @ApiOperation(value = "Retrieves the list of runresults based on assaytype, status or processstep name", notes = "Retrieves the list of runresults based on assaytype,status or processstep name")
   	@ApiResponses(value = { 
   			@ApiResponse(code = 200, message = "records found for given assaytype",response = void.class), 
   			@ApiResponse(code = 404, message = "Runresults Not Found",response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class),
   			@ApiResponse(code = 500, message = "Error while Retrieving runresult list failed", response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class)
   			})
	@Path("/runresults/list")
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	public Response listActiveRuns(@QueryParam("assayType") String assayType, @QueryParam("status") String status,
			@QueryParam("processstep") String processstep) throws HMTPException;

    @ApiOperation(value = "Retrieves the list of incompletedwfs runresults based on assaytype and processstep name", notes = "Retrieves the list of incompletedwfs runresults based on assaytype and processstep name")
  	@ApiResponses(value = { 
  			@ApiResponse(code = 200, message = "records found for given assaytype",response = void.class), 
  			@ApiResponse(code = 404, message = "Runresults Not Found",response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class),
  			@ApiResponse(code = 500, message = "Error while Retrieving runresult list failed", response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class)
  			})
	@Path("/runresults/incompletedwfs")
	@GET
	@Produces("application/json")
	@Consumes("application/json")
	public Response listInCompletedWFSActiveRuns(@QueryParam("assayType") String assayType,
			@QueryParam("processstep") String processstep) throws HMTPException;
    
    
    
    @ApiOperation(value = "Get inworkflow orders", notes = "Get inworkflow orders")
  	@ApiResponses(value = { 
  			@ApiResponse(code = 200, message = "records found for inworkflow order",response = void.class), 
  			@ApiResponse(code = 404, message = "Orders not found",response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class),
  			@ApiResponse(code = 500, message = "Error while Retrieving inworkflow orders", response = com.hcl.hmtp.common.server.rest.beans.APIErrorDetail.class)
  			})
	@Path("/runresults/inworkflow")
	@GET
	@Produces("application/json")
	@Consumes("application/json")
    public Response getInWorkFlowOrders()throws HMTPException;

    
    @ApiOperation(value = "Get the count of inworkflow orders ", notes = "Get the count of inworkflow orders")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "orders count retrieval got failed "),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 200, message = "records found for given one") })
	@GET
	@Path("/runresults/inworkflowcount")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getInWorkflowOrderCount() throws HMTPException;

    @Path("/patientriskreport/{language}/{country}")
	@GET
	@Produces({"application/pdf","application/csv","application/html"})
	public Response getJasperPDFReportByLocale(@PathParam("language") String language, @PathParam("country") String country,@QueryParam("accessioningid") String accessioningId) throws JRException,IOException,HMTPException;
	

    @ApiOperation(value = "Update usercomments to sampleresults table", notes = "Update usercomments")
	@ApiResponses(value = {@ApiResponse(code = 500, message = "Error while updating usercomments"),
			@ApiResponse(code = 200, message = "usercomments updated successfully") })
	@PUT
	@Path("/runresults/updatecomments")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateUserComments(ProcessStepValuesDTO processStepValuesDTO) throws HMTPException;
	
	
	@GET
	@Path("/results/device")
	public DeviceRunResultsDTO getRunresultsByDevice(@QueryParam("devicerunid")String runid) throws HMTPException;
	  
	@GET
	@Path("/workflowreport")
	@Produces({"application/pdf"})
	public Response getWorkflowReport(@QueryParam("language") String language,@QueryParam("country") String country,@QueryParam("devicerunid") String deviceRunId) throws  IOException, JRException, HMTPException;

	@GET
	@Path("/search")
	@Produces("application/json")
	public Response searchResult(@QueryParam("query") String searchQuery, @QueryParam("offset") int offset,
			@QueryParam("limit") int limit, @QueryParam("content") String content);

	@GET
	@Path("/DeviceSummaryLog")
	@Produces("application/json")
	@Consumes("application/json")
	public List<RunResults> getCurrentRunResult(@QueryParam(value = "fromDate") String fromDate, @QueryParam(value = "toDate") String toDate) ;	
	
	@Path("/runresult/inworkflow")
	@GET
	@Produces("application/json")
	@Consumes("application/json")
    public Response getRunDetailsByAccessioningId(@QueryParam("accessioningId") String accessioningId)throws HMTPException;
}
