package com.roche.connect.ttv2Simulator.rest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

@Component public class TTV2SimualtorRestApiImpl implements TTV2SimualtorRestApi {
    
    private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
    
    @Value("${simulator.status}") private String status;
    @Value("${simulator.folder URI}") private String folderURI;
    @Value("${simulator.ingest URI}") private String ingestURI;
    @Value("${simulator.rtaCompletionCheckPath}") private String rtaCompletionCheckPath;
    
    

    @Override public Response createJob(Map<String, Object> requestBody) {
        logger.info("Entered into post job details");
        Map<String,String> map = new HashMap<>();
        Random random = new Random(); 
        String jobId = "JOB"+random.nextInt(4);
        map.put("jobId", jobId);
        map.put("ingestURI", ingestURI);
        return Response.ok().entity(map).build();
    }


    @Override public Response getJobStatus(String jobId) {
        logger.info("Entered into getJobStatus");
       Map<String,String> map = new HashMap<>();
        
       /* File f = new File(getRTACompleteFilePath(rtaCompletionCheckPath, jobId));
        
        if (f.exists()) {
            map.put("status", status);
            map.put("folderURI", folderURI);
            return Response.ok().entity(map).build();
        } else {
            map.put("status", "Inprogress");
            return Response.ok().entity(map).build();
        }*/
        
        map.put("status", status);
        map.put("folderURI", folderURI);
        return Response.ok().entity(map).build();
        
    }

    private String getRTACompleteFilePath(String src, String jobId) {
        logger.info("Entering getRTACompleteFilePath");
        String rtaPath = src + "/" + jobId + "/" + "upload" + "/"+ "RTAComplete.txt";
        logger.info("RUN Complete Path: " +rtaPath);
        return rtaPath;
    }


    @Override public Response getIframe(String jobId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\r\n" + 
            "<head>\r\n" + 
            "<title>");
        sb.append(jobId);
        sb.append("</title>\r\n" + 
            "</head>\r\n" + 
            "<body>\r\n" + 
            "<table>\r\n" + 
            "  <tr>\r\n" + 
            "    <th>Attribute</th>\r\n" + 
            "    <th>Value</th>\r\n" + 
            "  </tr>\r\n" + 
            "  <tr>\r\n" + 
            "    <td>Name1</td>\r\n" + 
            "    <td>Value1</td>\r\n" + 
            "  </tr>\r\n" + 
            "  <tr>\r\n" + 
            "    <td>Name2</td>\r\n" + 
            "    <td>Value2</td>\r\n" + 
            "  </tr>\r\n" + 
            "</table>" + 
            "</body>\r\n" + 
            "</html>");
        return Response.ok().entity(sb.toString()).build();
    }
    
}
