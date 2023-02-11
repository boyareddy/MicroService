/*******************************************************************************
 * AdminCrudRestAPI.java                  
 *  Version:  1.0
 * 
 * Authors: dineshj
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

package com.roche.connect.adm.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.adm.dto.Message;
import com.roche.connect.adm.dto.MessageDto;


@Path("/rest/api/v1")
public interface AdminCrudRestAPI {

	@POST
	@Path("/notification")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response addNotification(@RequestBody MessageDto messageDto) throws HMTPException;


	@PUT
	@Path("/notification")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateNotification(@RequestBody List<Message> message) throws HMTPException;

	@GET
	@Path("/notification")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getNotifications() throws HMTPException;


	@GET
	@Path("/messagetemplate")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getNotificationTemplates() throws HMTPException;

	/*
	* 
	 */

	@GET
	@Path("/getversioninfo")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getVersionInfo() throws HMTPException;

	@GET
	@Path("/getAuditLog")
	@Produces("application/zip")
	@PreAuthorize("validateExpFromProperty('ConnectGetAuditTrail.getAuditDetails.preauthorize')")
	public Response getAuditDetails(@QueryParam(value = "fromDate") String fromDate,
			@QueryParam(value = "toDate") String toDate,
			@QueryParam(value = "companydomainname") String companydomainname);

	@GET
	@Path("/removeAuditFile")
	public Response removeAuditDetails(@QueryParam(value = "fileName") String fileName);

	@GET
	@Path("/modules")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getModules() throws HMTPException;


	@POST
	@Path("/createReport")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON })
	@PreAuthorize("validateExpFromProperty('ConnectProblemReports.ProblemReports.preauthorize')")
	public Response createReport(@QueryParam(value = "fromDate") String fromDate,
			@QueryParam(value = "toDate") String toDate) throws HMTPException;

    @POST
    @Path("/systemsettings")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
	@PreAuthorize("validateExpFromProperty('ConnectLabSettings.SystemSettings.preauthorize')")
    public Response saveSystemSettings(@RequestBody String data) throws HMTPException;
	

    @GET
    @Path("/systemsettings")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSystemSettings() throws HMTPException;
    
    @GET
    @Path("/lablogo")
    @Produces({"image/jpeg,image/jpg,image/png"})
    public byte[] getLabLogo() throws HMTPException;
    
	
    
}
