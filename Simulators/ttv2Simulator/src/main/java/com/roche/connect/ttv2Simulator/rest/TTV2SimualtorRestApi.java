package com.roche.connect.ttv2Simulator.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.expression.ParseException;
import org.springframework.web.bind.annotation.RequestBody;

import com.hcl.hmtp.common.client.exceptions.HMTPException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@Api(value = "TTV2 Simulator")
@Path("/")
public interface TTV2SimualtorRestApi {

	
	/**
	 * Hit Hello
	 * @param requestBody
	 * @return
	 * @throws HMTPException
	 * @throws ParseException
	 */
	@ApiOperation(value = "create job ", notes = "create job ")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "create job") })
	@POST
	@Path("analysis")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createJob(@RequestBody Map<String, Object> requestBody);
	
	@ApiOperation(value = "Return folder URI path", notes = "illumina SequencerRootPath information")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
            @ApiResponse(code = 200, message = "got SequencerRootPath") })
    @GET
    @Path("analysis/{jobId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobStatus(@PathParam("jobId") String jobId);
	
	@GET
    @Path("analysis/iframe")
    @Produces(MediaType.TEXT_HTML)
    public Response getIframe(@QueryParam("jobId") String jobId);
	
}
