 /*******************************************************************************
 *  * File Name: AssayCrudRestApi.java            
 *  * Version:  1.0
 *  * 
 *  * Authors: Dasari Ravindra
 *  * 
 *  * =========================================
 *  * 
 *  * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  * All Rights Reserved.
 *  * 
 *  * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 *  * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *  * 
 *  * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 *  * 
 *  * =========================================
 *  * 
 *  * ChangeLog:
 *  ******************************************************************************/
package com.roche.connect.amm.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.hcl.hmtp.common.client.exceptions.HMTPException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Interface AssayCrudRestApi.
 */
@Api(value = "Assay Management")
@Path("/rest/api/v1")
public interface AssayCrudRestApi {

	/**
	 * Gets the assay types data.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the assay types data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives All Assay Type details ", notes = "Fetches all Assay Type details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Assay Types found for the given one"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("/assay")
	@GET
	@Produces("application/json")
	public Response getAssayTypesData(@QueryParam("assayType") String assayType) throws HMTPException;
	
	/**
	 * Gets the sample types data.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the sample types data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives All Sample Type details for given Assay Type", notes = "Fetches all Sample Type details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sample Type Details found for the given assay"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/{assayType}/sampletype")
	@GET
	@Produces("application/json")
	public Response getSampleTypesData(@PathParam("assayType") String assayType) throws HMTPException;

	/**
	 * Gets the test options data.
	 *
	 * @param assayType
	 *            the assay type
	 * @return the test options data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives Test Options for given Assay Type", notes = "Fetches Test Options details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Test Options found for the given Assay"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/{assayType}/testoptions")
	@GET
	@Produces("application/json")
	public Response getTestOptionsData(@PathParam("assayType") String assayType) throws HMTPException;

	/**
	 * Gets the device test options data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param deviceType
	 *            the device type
	 * @return the device test options data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives Device Test Options for given Assay Type", notes = "Fetches Device Test Options details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Device Test Options found for the given Assay"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/{assayType}/devicetestoptions")
	@GET
	@Produces("application/json")
	public Response getDeviceTestOptionsData(@PathParam("assayType") String assayType, @QueryParam("deviceType") String deviceType, @QueryParam("processStepName") String processStepName)
			throws HMTPException;

	/**
	 * Gets the WF process data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param deviceType
	 *            the device type
	 * @return the WF process data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives Workflow Step Actions for given Assay ", notes = "Fetches Workflow Step actions for the Assay")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Workflow Step actions found for the given Assay"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/{assayType}/processstepaction")
	@GET
	@Produces("application/json")
	public Response getWFProcessData(@PathParam("assayType") String assayType, @QueryParam("deviceType") String deviceType)
			throws HMTPException;

	/**
	 * Gets the list data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param listType
	 *            the list type
	 * @return the list data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives List Values for given Assay", notes = "Fetches List Values for given Assay")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List Value Details found for the given Assay"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/{assayType}/listdata")
	@GET
	@Produces("application/json")
	public Response getListData(@PathParam("assayType") String assayType, @QueryParam("listType") String listType) throws HMTPException;

	/**
	 * Gets the assay input validation data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param fieldName
	 *            the field name
	 * @return the assay input validation data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives All Input data validations for given Assay", notes = "Fetches All Input data validations for given Assay")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Input data validations found for the given assay"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/{assayType}/inputdatavalidations")
	@GET
	@Produces("application/json")
	public Response getAssayInputValidationData(@PathParam("assayType") String assayType,@QueryParam("fieldName") String fieldName, @QueryParam("groupName") String groupName) throws HMTPException;
	
	/**
	 * Gets the assay type ID.
	 *
	 * @param assaytype
	 *            the assaytype
	 * @return the assay type ID
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives Assay type id for given Assay", notes = "Fetches Assay type id for given Assay")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Assay type id found for the given Assay"),
			@ApiResponse(code = 500, message = "Request can't be processed")})
	@Path("assay/{assaytype}")
	@GET
	@Produces("application/json")
	public Response getAssayTypeID(@PathParam("assaytype") String assaytype) throws HMTPException;
	
	
	/**
	 * Gets the molecular id data.
	 *
	 * @param assayType
	 *            the assay type
	 * @param plateType
	 *            the plate type
	 * @param plateLocation
	 *            the plate location
	 * @return the molecular id data
	 * @throws HMTPException
	 *             the HMTP exception
	 */
	@ApiOperation(value = "Retrives Molecular ID for given Assay", notes = "Fetches all Molecular ID details for given Assay")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Molecular ID Details found for the given assay"),
            @ApiResponse(code = 500, message = "Request can't be processed")})
    @Path("assay/molecularIdDisplay")
    @GET
    @Produces("application/json")
    public Response getMolecularIdData(@QueryParam("assayType") String assayType,@QueryParam("plateType") String plateType,@QueryParam("plateLocation") String plateLocation) throws HMTPException;
	@ApiOperation(value = "Retrives Flag details for given Device and a specific device type and Flag code", notes = "Retrives Flag details for given Device and a specific device type and Flag code")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Flags Details found for the given Device type"),
            @ApiResponse(code = 500, message = "Request can't be processed")})
    @Path("assay/flagDescriptions/{deviceType}")
    @GET
    @Produces("application/json")
    public Response getFlagDescriptions( @HeaderParam( "Accept-Language" )String acceptLanguage, @PathParam("deviceType") String deviceType,@QueryParam("assayType") String assayType,@QueryParam("flagCode") String flagCode) throws HMTPException;
	
	@ApiOperation(value = "Retrives Last Process Step Actions for all Assays ", notes = "Fetches Last Process Step Actions for all Assays")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Process Step actions found"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/processstepaction")
	@GET
	@Produces("application/json")
	public Response getLastProcessStepData() throws HMTPException;
	
	
	
	@ApiOperation(value = "Retrieves UnassignedFilterData for all Assay & SampleTypes ", notes = "Fetches UnassignedFilterData for all Assay & SampleTypes")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Assay & SampleTypes found"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("/unassignedFilterData")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUnassignedFilterData() throws HMTPException;
	
	
	@ApiOperation(value = "Retrieves AssayProcessStepData for all Assay,Sample,Device & workflow Step", notes = "Fetches AssayProcessStepData for all Assay,Sample,Device & workflow Step")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Assay,Sample,Device & workflow Step found"),
			@ApiResponse(code = 500, message = "Request can't be processed") })
	@Path("assay/assayProcessStepData")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAssayProcessStepData() throws HMTPException;
	
	
}
