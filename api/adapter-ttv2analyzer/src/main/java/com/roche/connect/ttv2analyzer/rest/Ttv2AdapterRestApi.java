package com.roche.connect.ttv2analyzer.rest;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import com.hcl.hmtp.common.client.exceptions.HMTPException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@Api(value = "TTV2 Analyzer")
@Path("/")
public interface Ttv2AdapterRestApi {

	
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
	@Path("ttv2/hello")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hitHello();
	
	@ApiOperation(value = "get SequencerRootPath information", notes = "illumina SequencerRootPath information")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
			@ApiResponse(code = 200, message = "got SequencerRootPath") })
	@GET
	@Path("ttv2/rootPath")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSequencerRootPath();
	
	@ApiOperation(value = "file upload to TTV2 analyzer", notes = "file upload to TTV2 analyzer")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
            @ApiResponse(code = 200, message = "File uploaded successfully") })
    @POST
    @Path("ttv2/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fileUpload(@RequestBody Map<String, Object> requestBody) throws HMTPException, IOException;
	
	@ApiOperation(value = "Get Done Status", notes = "Getting Done Status")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "The requested URL not found"),
            @ApiResponse(code = 200, message = "Hitting Done") })
    @POST
    @Path("ttv2/done")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDone(@RequestBody Map<String, Object> requestBody) throws HMTPException;	
	
	@GET
    @Path("ttv2/iframe")
    @Produces("application/html")
    public Response getIframeUrl() throws IOException;
    
    
	
}
