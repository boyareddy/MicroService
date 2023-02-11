/*******************************************************************************
 * File Name: OrderCrudRestApi.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 * 
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.order.dto.BulkOrdersDTO;
import com.roche.connect.common.rmm.dto.SampleResultsDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Order Management")
@Path("/rest/api/v1")
public interface OrderCrudRestApi {

	@ApiOperation(value = "Retrives all unassgined orders ", notes = "Retrives all unassgined orders")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "order retrive got failed "),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 200, message = "records found for given one") })
	@GET
	@Path("/order")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOrderList() throws HMTPException;
	
	
	
	@ApiOperation(value = "Retrieves all unassgined orders ", notes = "Retrives all unassgined orders")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "order retrive got failed "),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 200, message = "records found for given one") })
	@GET
	@Path("/unassignedorders")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOrderLists( @QueryParam("offset") int offset,
			@QueryParam("limit") int limit, @QueryParam(value="assayType") String assayType,@QueryParam(value="sampleType")String sampleType,@QueryParam(value="createdDateTime") String createdDateTime,
			@QueryParam(value="updatedDateTime") String updatedDateTime,@QueryParam(value="orderComments") String orderComments,@QueryParam(value="reqFieldMissingFlag") String reqFieldMissingFlag) throws HMTPException;

	@ApiOperation(value = "Retrives Order details based on OrderId ", notes = "Retrives all Order details along with patient and assay information")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "order retrive got failed"),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 400, message = "OrderId is null"),
			@ApiResponse(code = 200, message = "records found for given one") })
	@GET
	@Path("/order/{orderId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOrderByOrderId(@PathParam("orderId") Long orderId) throws HMTPException;

	@ApiOperation(value = "Retrives All Order Type details for given accessoiningid", notes = "Fetches Order Values from Order table")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "order retrive got failed"),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 400, message = "accessioningID is null"),
			@ApiResponse(code = 200, message = "records found for given one") })
	@Path("/orders")
	@GET
	@Produces("application/json")
	public Response getOrderBySampleId(@QueryParam(value = "accessioningID") String sampleId) throws HMTPException;

	@ApiOperation(value = "Creating a New Order", notes = "Creates a New Order")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Order Couldn't be Created"),
			@ApiResponse(code = 200, message = "Order Created Successfully..") })
	@POST
	@Path("/order")
	@Produces("application/json")
	@Consumes("application/json")
	@PreAuthorize("validateExpFromProperty('OrderManagement.createOrder.preauthorize')")
	public Response createOrder(@RequestBody @NotNull String requestBody) throws HMTPException;

	@ApiOperation(value = "Update Order details", notes = "Updates")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Order update has failed"),
			@ApiResponse(code = 400, message = "OrderId is null"),
			@ApiResponse(code = 200, message = "Updated Successfully..") })
	@PUT
	@Path("/order")
	@Consumes("application/json")
	@PreAuthorize("validateExpFromProperty('OrderManagement.updateOrder.preauthorize')")
	public Response update(@RequestBody String requestBody) throws HMTPException;

	@ApiOperation(value = "Update Order status based on orderId ", notes = "Updates the order status based on orderId")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Order status update failed"),
			@ApiResponse(code = 400, message = "OrderId is null"),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 200, message = "Order Status updated successfully") })
	@PUT
	@Path("/orders")
	@Produces({ MediaType.APPLICATION_JSON })
	@PreAuthorize("validateExpFromProperty('OrderManagement.updateOrderStatus.preauthorize')")
	public Response updateOrderStatus(@QueryParam(value = "orderId") Long orderId,
			@QueryParam(value = "orderStatus") String orderStatus) throws HMTPException;
	
		
	@ApiOperation(value = "Gets container samples which are in open status", notes = "fetch the container samples which are in open status")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Getting container samples failed"),
			@ApiResponse(code = 404, message = "container samples Not found"),
			@ApiResponse(code = 200, message = "container samples found") })
	@GET
	@Path("/containersamples")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getContainerSamples(@QueryParam("devicerunid") String deviceRunId,@QueryParam("containerid") String containerID) throws HMTPException;
	
	
	@ApiOperation(value = "Updates container samples", notes = "updates container samples")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Updating the container samples failed"),
			@ApiResponse(code = 404, message = "container sample updated"),
			@ApiResponse(code = 200, message = "container samples updated") })
	@PUT
	@Path("/containersamples")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("validateExpFromProperty('OrderManagement.updateContainerSamples.preauthorize')")
	public Response updateContainerSamples(@RequestBody String requestBody,
			@QueryParam("devicerunid") String deviceRunId,
			@QueryParam("accessioningID") String accessioningID,
			@QueryParam("status") String status) throws HMTPException;
	
	
	@ApiOperation(value = "Validates the container samples", notes = "validates all container samples")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "validating the container samples"),
			@ApiResponse(code = 404, message = "container samples couldn't be validated"),
			@ApiResponse(code = 200, message = "container samples are validated") })
	@POST
	@Path("/containersamples/validate")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@PreAuthorize("validateExpFromProperty('OrderManagement.validateContainerSamples.preauthorize')")
	public Response validateContainerSamples(InputStream data) throws HMTPException,IOException;
	
	
	@ApiOperation(value = "Saves the container samples", notes = "Saves the container samples")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Saving the container samples"),
			@ApiResponse(code = 404, message = "container samples couldn't be saved"),
			@ApiResponse(code = 200, message = "container samples are saves") })
	@POST
	@Path("/containersamples")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({  MediaType.APPLICATION_JSON })
	@PreAuthorize("validateExpFromProperty('OrderManagement.createContainerSamples.preauthorize')")
	public Response storeContainerSamples(@RequestBody String requestBody) throws HMTPException,IOException;
	
	
	@ApiOperation(value = "get list of containerId with open status ", notes = "get list of containerId with open status")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "get list of containerId with open status failed"),
			@ApiResponse(code = 400, message = "status is Empty or null and container not found"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Records found") })
	@GET
	@Path("/containersamples/containeridlist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListOfContainerIdWithOpenStatus(@QueryParam("status") String status) throws HMTPException;
		
	
	@ApiOperation(value = "Duplicate accessioningID ", notes = "check accessioningID is duplicate or not")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "accessioningID retrive got failed"),
            @ApiResponse(code = 404, message = "accessioningID Not valid"),
            @ApiResponse(code = 400, message = "accessioningID is null"),
            @ApiResponse(code = 200, message = "records found for given one") })
    @GET
    @Path("/order/duplicate")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response duplicate(@QueryParam("accessioningID") String  accessioningID) throws HMTPException;
	
	@ApiOperation(value = "Get the count of unassgined orders ", notes = "Get the count of unassgined orders")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "orders count retrieval got failed "),
			@ApiResponse(code = 404, message = "Order Not found"),
			@ApiResponse(code = 200, message = "records found for given one") })
	@GET
	@Path("/order/unassignedcount")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUnassignedOrderCount() throws HMTPException;
	
	@ApiOperation(value = "Container Samples for Accessioning Id", notes = "Fetching Container Samples for Given Accessioning Id")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Container Samples Fetching failed"),
            @ApiResponse(code = 404, message = "accessioningID Not valid"),
            @ApiResponse(code = 400, message = "accessioningID is null"),
            @ApiResponse(code = 200, message = "records found for given one") })
	@GET
	@Path("order/containersamples")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response containerInfoForAccessioningId(@QueryParam("accessioningId") String accessioningId) throws HMTPException;
	
	
	@ApiOperation(value = "Removing container samples for given container", notes = "Removing Container Samples for given container id")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Container Samples removal failed"),
            @ApiResponse(code = 404, message = "container id not valid"),
            @ApiResponse(code = 400, message = "container id is null"),
            @ApiResponse(code = 200, message = "records removed for given one") })
	@DELETE
	@Path("/containersamples")
	@Produces({ MediaType.APPLICATION_JSON })
	@PreAuthorize("validateExpFromProperty('OrderManagement.deleteContainerSamples.preauthorize')")
	public Response removeContainerSamples(@QueryParam("containerId") String containerId) throws HMTPException;
	
	
	@ApiOperation(value = "Removing container samples for given container", notes = "Removing Container Samples for given container id")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Container Samples removal failed"),
            @ApiResponse(code = 404, message = "container id not valid"),
            @ApiResponse(code = 400, message = "container id is null"),
            @ApiResponse(code = 200, message = "records removed for given one") })
	@PUT
	@Path("/containersamples/flag")
	@Produces({ MediaType.APPLICATION_JSON })
	@PreAuthorize("validateExpFromProperty('OrderManagement.updateContainerSamplesFlag.preauthorize')")
	public Response updateContainerSamplesFlag(@QueryParam("containerId") String containerId) throws HMTPException;
	
	
	@ApiOperation(value = "Validate bulk orders from CSV", notes = "Validate bulk orders from CSV")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Validate bulk orders from CSV failed"),
            @ApiResponse(code = 200, message = "Validate bulk orders from CSV success") })
	@POST
	@Path("/order/validatebulkorders")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	@PreAuthorize("validateExpFromProperty('OrderManagement.validateBulkOrders.preauthorize')")
	public Response validateBulkOrders(InputStream inputStream, @QueryParam("tz") String timeZone) throws HMTPException;
	
	@ApiOperation(value = "Create bulk orders from CSV", notes = "Create bulk orders from CSV")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Create bulk orders from CSV failed"),
            @ApiResponse(code = 200, message = "Create bulk orders from CSV success") })
	@POST
	@Path("/order/createbulkorders")
	@Consumes({  MediaType.APPLICATION_JSON })
	@Produces({MediaType.APPLICATION_JSON})
	@PreAuthorize("validateExpFromProperty('OrderManagement.createBulkOrders.preauthorize')")
	public Response createBulkOrders(@RequestBody BulkOrdersDTO bulkOrdersDTO) throws HMTPException;
	
	
	@ApiOperation(value = "validate ContainerSamples Status which are senttodevice based on deviceRunID", notes = "check status senttodevice")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "validate ContainerSamples Status with senttodevice failed"),
			@ApiResponse(code = 400, message = "status is Empty or null and container not found"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Records found") })
	@GET
	@Path("/containersamples/validateStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateContainerSamplesStatus(@QueryParam("devicerunid") String deviceRunId) throws HMTPException;

	@Path("/ord")
	@GET
	@Produces("application/json")
	public Response getAllOrder() throws HMTPException;

	@GET
	@Path("/order/search")
	@Produces("application/json")
	public Response searchOrder(@QueryParam("query") String searchQuery,
			@QueryParam(value = "orderstatus") String orderStatus, @QueryParam("offset") int offset,
			@QueryParam("limit") int limit);
	
	@GET
	@Path("/order/inworkflow")
	@Produces("application/json")
	public Response getInworkflowOrders() throws UnsupportedEncodingException, HMTPException;
	
	@POST
	@Path("/order/runsamplemandateflags")
	@Produces("application/json")
	public Response getMandatoryFlagByRun(List<SampleResultsDTO> sampleResults);
}
