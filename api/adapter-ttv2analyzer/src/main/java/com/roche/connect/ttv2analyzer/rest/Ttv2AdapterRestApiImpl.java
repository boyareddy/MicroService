package com.roche.connect.ttv2analyzer.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.common.io.CharStreams;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.roche.connect.ttv2analyzer.service.TTV2AnalyzerServiceImpl;

@Component
public class Ttv2AdapterRestApiImpl implements Ttv2AdapterRestApi {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    @Value("${adapter.illuminaSequencerRootPath}")
	private String illuminaSequencerRootPath;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Autowired
    private TTV2AnalyzerServiceImpl ttv2AnalyzerServiceImpl;
    
    @Override public Response hitHello() {
        
        logger.info("Enter Ttv2RestApiImpl.hitHello method ");
        logger.info("Finished Ttv2RestRestApiImpl.Hello method ");
        return Response.ok().build();
        
    }
    
    @Override
	public Response getSequencerRootPath() {
		logger.info("entered Ttv2RestApiImpl.getSequencerRootPath() ");
		logger.info("illuminaSequencerRootPath : "+ illuminaSequencerRootPath );
		return Response.ok().entity(illuminaSequencerRootPath).build() ;
	}

	@Override
	public Response fileUpload(Map<String, Object> requestBody) throws HMTPException, IOException {

		logger.info("Entering fileUpload");
		
		String runId = String.valueOf(requestBody.get("runId"));
		ttv2AnalyzerServiceImpl.executeTimer(runId);
		return Response.ok().build();
	}
    
    @Override public Response updateDone(Map<String, Object> requestBody){
        logger.info("Getting Done Status Successfully");
        logger.info("TTV2 Analyzer Status:  "+ requestBody.get("run_status").toString());
        logger.info("TTV2 Analyzer URI for Downloading Files:  "+ requestBody.get("uri").toString());
        return Response.ok().build();
    }

    @Override public Response getIframeUrl() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/index.html");
        InputStream stream = resource.getInputStream();
        String msg = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8)).replace('\n', '\r');
        return Response.ok(msg).type("application/html")
            .header("Content-Disposition", "inline; filename=iframe.html;")
            .build();
    }
}
