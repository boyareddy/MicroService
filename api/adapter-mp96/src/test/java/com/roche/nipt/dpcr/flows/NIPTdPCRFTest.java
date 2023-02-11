package com.roche.nipt.dpcr.flows;

import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.lang3.CharEncoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.rest.RestClientUtil;

import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.nipt.dpcr.util.SecurityToken;
import com.roche.connect.common.order.dto.ContainerSamplesDTO;

public class NIPTdPCRFTest {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
	public static void main(String[] args) {
		
		String mp96_adapter_host="localhost";
		String mp96_adapter_port="4447";
		String mp96_adapter_timeout="900000";
		
		String lp24_adapter_host="localhost";
		String lp24_adapter_port="4445";
		String lp24_adapter_timeout="900000";
	
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());	           
        
        String wellPlateId=sdf.format(timestamp);
        String dateId=wellPlateId.replace(".", "");
        		
		
		timestamp = new Timestamp(System.currentTimeMillis());	           
        
        String timestampId=sdf.format(timestamp);
        timestampId=timestampId.replace(".", "");
        
        timestampId="1"+timestampId;
        //start number for accessing id
        long numAccessingId = 120190124175230l;//Long.parseLong(timestampId);
        
        // wellplate id
        wellPlateId="WP 20190124175230"; //"WP "+dateId;
	       
	        try {
	        	
	        		              
	        	
	        	//create 96 orders with accessing Id
	        	List<ContainerSamplesDTO> order96Samples=create96Orders(wellPlateId,numAccessingId);
	        	
	        	// upload 96 orders with wellplateid
	        	wellPlateId=upload96CSVSamples(order96Samples);
	        	
	        		        	
	        	//mp96 query 
	        	String mp96Query="MSH|^&~\\|MagNA Pure 96||||20181204184525||QBP^Q11|MPCZC8380G5K|P|2.4\r"+
	        	                 "QPD|QUERY_ORDER\r" +
	        			         "RCP|";   	
	        	
	        	System.out.println("Mp96 Query:"+mp96Query);
	        	
	        	// post mp96 query
	        	String mp96Response=postHL7Message(mp96_adapter_host, mp96_adapter_port, mp96_adapter_timeout, mp96Query, "true");
	        	System.out.println("Mp96 Query RSP:"+mp96Response);
	        	
	        	
	        	String runId="";
	        	if(mp96Response.contains("|RND")) {
	        		mp96Response=mp96Response.substring(mp96Response.indexOf("|RND")+1);
	        		mp96Response=mp96Response.substring(0,mp96Response.indexOf("|"));
	        		runId=mp96Response;
	        		
	        	}
	        	
	        	
	        	String mp96Ack="MSH|^~\\&|||||20091126142926||ACK|20091126142925|P|2.4\r" + 
	        			"MSA|AA|"+runId+"";
	        	System.out.println("Mp96 ACK:"+mp96Ack);
	        	
	        	
	        	String mp96AckResponse=postHL7Message(mp96_adapter_host, mp96_adapter_port, mp96_adapter_timeout, mp96Ack, "true");
	        	System.out.println("Mp96 ACK RSP:"+mp96AckResponse);
	        	
	        	
	        	String mp96Result="MSH|^&~\\|||||20091126141120||OUL^R21|MPCZC8380G5K|P|2.4\r";
	        	int i=0;
	        	String ouput96WellplateId="O"+dateId;
	        	for(ContainerSamplesDTO sample: order96Samples) {
	        		
	        		String accessingId=sample.getAccessioningID();
	        		
	        		String containerPosition=sample.getPosition();
	        		
	        			        		
	        		if(i==order96Samples.size()-1) {
	        		mp96Result=mp96Result+
		        			"ORC|1|"+runId+"^"+dateId+"|||||||20181204184907|admin\r" + 
		        			"OBR|1|"+accessingId+"|"+ouput96WellplateId+"|Cellular RNA LV^0.2|||20181204164907|20181204184907|||||96WellPlate||Cellular RNA LV 0.6.4|||200|50|1.0.0|506 \r" + 
		        			"OBX|1|CE|"+containerPosition+"||20^0012123456780240^20090829000800^789||||||P||Passed\r" + 
		        			"NTE|1||sys cmds";
	        		}
	        		else {
	        			mp96Result=mp96Result+
	        					"ORC|1|"+runId+"^"+dateId+"|||||||20181204184907|admin\r" + 
			        			"OBR|1|"+accessingId+"|"+ouput96WellplateId+"|Cellular RNA LV^0.2|||20181204164907|20181204184907|||||96WellPlate||Cellular RNA LV 0.6.4|||200|50|1.0.0|506 \r" + 
			        			"OBX|1|CE|"+containerPosition+"||20^0012123456780240^20090829000800^789||||||P||Passed\r" + 
			        			"NTE|1||sys cmds\r" ;
	        		}
	        		
	        		i++;
		        			
	        		
	        	}
	        	
	        	
	        	String mp96ResultResponse=postHL7Message(mp96_adapter_host, mp96_adapter_port, mp96_adapter_timeout, mp96Result, "true");
	        	
	        	System.out.println("Mp96 Result RSP:"+mp96ResultResponse);
	        	
	        	
	        	
	        	// lp24 query
	        	String outputContainerIdMP96=ouput96WellplateId;
	        	
	        	for(ContainerSamplesDTO sample: order96Samples) {	        		
	        		        		
	        		String containerPosition=sample.getPosition();
	        		String accessingId=sample.getAccessioningID();
	        	
	        	 String lp24Query="MSH|^~\\&|LP24^LP-001^M|Roche Diagnostics|Connect|Roche Diagnostics|20181204154124||QBP^WOS^QBP_Q11|c41e62c3-d8d1-4dff-b|P|2.5.1||||||UNICODE UTF-8\r" + 
	        	 		"QPD|WOS^Work Order Step^IHE_LABTF|c41e62c3-d8d1-4dff-b|"+outputContainerIdMP96+"_"+containerPosition+"\r" + 
	        	 		"RCP|I||R";
	        	
	        	System.out.println("Lp24 query :"+lp24Query);
	        	 String lp24QueryResponse=postHL7Message(lp24_adapter_host, lp24_adapter_port, lp24_adapter_timeout, lp24Query, "true");
	        	 
	        	 System.out.println("Lp24 query RSP :"+lp24QueryResponse);
	        	 
	        	 if(lp24QueryResponse.contains(accessingId)){
	        		 
	        		 System.out.println("Valid RSP for accessing Id :"+accessingId);
	        	 }
	        	 
	        	}
	        	
	        	
	        	// lp24 reuslts
	        	timestamp = new Timestamp(System.currentTimeMillis());	           
	            
	            String lp24RunId=sdf.format(timestamp);
	            lp24RunId=lp24RunId.replace(".", "");
	            lp24RunId="RND"+lp24RunId;
	        	
	            int j=0;
	            
	            String lp24dPCRId=sdf.format(timestamp);
        		lp24dPCRId=lp24dPCRId.replace(".", "");
        		
	            int z=1;
	        	for(ContainerSamplesDTO sample: order96Samples) {	        		
	        		
	        		String containerPosition=sample.getPosition();
	        		String accessingId=sample.getAccessioningID();
	        	
	        		// lp24 reuslts
		        	timestamp = new Timestamp(System.currentTimeMillis());	           
		            
		        	if(j%8==0) {
		        		z=1;
		        		lp24dPCRId=sdf.format(timestamp);
		        		lp24dPCRId=lp24dPCRId.replace(".", "");
		            
		        	}
		            

		        	String lp24Result="MSH|^~\\&|LP24^LP-001^M|Roche Diagnostics|Connect|Roche Diagnostics|20181205183450||SSU^U03^SSU_U03|ecf0fc30-38bc-4218-a|P|2.5.1||||||UNICODE UTF-8\r" + 
		        			"EQU|LP-001|20181205183450|OP\r" + 
		        			"SAC|||2|||||R^^99ROC|dPCR chip|"+lp24dPCRId+"|"+z+"|||||||||||||ul\r" + 
		        			"OBX|1|CE|P^^99ROC|1|LPdPCR||||||F\r" + 
		        			"OBX|2|CE|PV^^99ROC|1|1.0||||||F\r" + 
		        			"OBX|3|CE|RES^^99ROC|1|Passed||||||F\r" + 
		        			"OBX|4|DR|RuntimeRange^RunExecutionTimeRange^99ROC|1|20181205183428^20181205183450||||||F\r" + 
		        			"NTE|1\r" + 
		        			"SPM|1|"+lp24dPCRId+"_"+z+"|"+outputContainerIdMP96+"_"+containerPosition+"|PLAS^Plasma^HL70487|||||||P|||Plasma\r" + 
		        			"OBX|1|CE|OrderName^^99ROC|1|"+lp24RunId+"||||||F|||||jimenj15\r" + 
		        			"OBX|2|CE|OrderResult^^99ROC|1|Passed||||||F\r" + 
		        			"OBX|3|CE|IC^^99ROC|1|74586563||||||F\r\n" + 
		        			"OBX|4|CE|MasterMix^^99ROC|1|Lightcycler;00000001;04193001||||||F\r" + 
		        			"OBX|5|CE|Kit^^99ROC|1|mRNA Isolation;00000004;04001||||||F\r" + 
		        			"OBX|6|CE|Kit^^99ROC|2|System fluid;00000004;04002||||||F\r" + 
		        			"OBX|7|CE|PI^^99ROC|1|Valid||||||F\r" + 
		        			"OBX|8|CE|MRReagentCassette^^99ROC|1|DNA Blood LV;00000000;0040||||||F\r" + 
		        			"OBX|9|CE|SR2mlReagentTube^^99ROC|1|MGP;00000001;00023||||||F\r" + 
		        			"OBX|10|CE|SR2mlReagentTube^^99ROC|1|Hexadecane;00000000;01991||||||F\r" + 
		        			"OBX|11|CE|SR25mlReagentTube^^99ROC|1|Washbuffer;00000002;01001||||||F\r" + 
		        			"OBX|12|CE|SR25mlReagentTube^^99ROC|1|Washbuffer;00000002;01002||||||F\r" + 
		        			"OBX|13|CE|SR25mlReagentTube^^99ROC|1|Specimen Diluent;00000000;00033||||||F\r" + 
		        			"OBX|14|CE|SR25mlReagentTube^^99ROC|1|Lysis;00000000;00001||||||F";
	        	
		        	System.out.println("Lp24 query :"+lp24Result);
		        	
		        	String lp24ResultResponse=postHL7Message(lp24_adapter_host, lp24_adapter_port, lp24_adapter_timeout, lp24Result, "true");
	        	 
		        	System.out.println("Lp24 Result ACK :"+lp24ResultResponse);
		        	
		        	z++;
		        	j++;
	        	 
	        	 if(lp24ResultResponse.contains("ACK")){
	        		 
	        		 System.out.println("LP24 Valid ACK for accessing Id :"+accessingId);
	        	 }
	        	 
	        	}
	        		        	
	        	
	        } catch (Exception e) {
	            System.out.println("Exception occured in Ordercreation96Samples Class");
	            e.printStackTrace();
	        } finally {
	            
	            System.out.println("Orders created successfully <<>>Please view OMM TABLE:ORDER & CONTAINER SAMPLE");
	           
	            
	        }
		
		
	}
	
	 public static Response postsamples(String url, Object object, String token, List<Object> providers)
		        throws UnsupportedEncodingException {
		        
		        Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
		        if (token != null)
		            builder.header("Cookie", "brownstoneauthcookie=" + token);
		        
		        return builder.post(Entity.entity(object, MediaType.APPLICATION_JSON));
		    }
		    
		    public static Response post(String url, Entity entity, String token, List<Object> providers)
		        throws UnsupportedEncodingException {
		        System.out.println("post url :"+url);
		        Builder builder = RestClientUtil.getBuilder(URLEncoder.encode(url, CharEncoding.UTF_8), providers);
		        if (token != null)
		            builder.header("Cookie", "brownstoneauthcookie=" + token);
		        return builder.post(entity);
		    }
		    
	
	public static List<ContainerSamplesDTO> create96Orders(String wellPlateId,long startingAccessingId) {
		
		List<OrderParentDTO> orderParentList = new ArrayList<OrderParentDTO>();
		List<ContainerSamplesDTO> containerSamples = new CopyOnWriteArrayList<ContainerSamplesDTO>();
        try {
            
            String localhost = "http://localhost:96/omm";
            String AUTHENTICATED_JSON = "/json";
            String OMM_SSU_URL = "/rest/api/v1/order";           
            
            String url = localhost + AUTHENTICATED_JSON + OMM_SSU_URL;
            
          
            long accessioningIdInt = startingAccessingId;
                        
            System.out.println("Accessing Id Starting number:"+accessioningIdInt);
            
            String token =SecurityToken.getToken();
            
            System.out.println("token:"+token);
            for (int i = 0; i <96; i++) {
                FileReader fr1 = new FileReader(new File("src/test/java/Resource/OrderCreation96samples.json"));
                ObjectMapper objectMapper1 = new ObjectMapper();
                OrderParentDTO orderParentNew1 = null;
                OrderDTO orderdto = new OrderDTO();
                ContainerSamplesDTO containersample = new ContainerSamplesDTO();
                orderParentNew1 = objectMapper1.readValue(fr1, OrderParentDTO.class);
                orderdto = orderParentNew1.getOrder();
                orderdto.setAccessioningId(Long.toString(accessioningIdInt+i));
                containersample.setAccessioningID(orderdto.getAccessioningId());
                containersample.setContainerType("96 wellplate");
                containersample.setContainerID(wellPlateId);
                containerSamples.add(containersample);
                OrderParentDTO odParentDto = new OrderParentDTO();
                odParentDto.setOrder(orderdto);
                orderParentList.add(odParentDto);
                orderParentNew1 = null;
                fr1.close();
            }
            
            for (OrderParentDTO odd: orderParentList) {
                try {
                	System.out.println("Orders Posting:"+odd);
                    //post(url, Entity.entity(odd, MediaType.APPLICATION_JSON), token, null);
                    System.out.println("Successuflly created :"+odd.getOrder().getAccessioningId());
                } catch (Exception e) {                	
                    System.out.println("error while adding orders :"+e);
                }
                
            }
        }
        catch(Exception ex) {
        	
        	 System.out.println(ex);
        }
		return containerSamples;
	}
	public static String upload96CSVSamples(List<ContainerSamplesDTO> containerSamples) {
		
		 String localhost = "http://localhost:96/omm";
         String AUTHENTICATED_JSON = "/json";
        
         String OMM_CONTAINESAMPLES = "/rest/api/v1/containersamples";
         String wellplateId="";
         
		 String s =
	                "A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,B1,B2,B3,B4,B5,B6,B7,B8,B9,B10,B11,B12,C1,C2,C3,C4,C5,C6,C7,C8,C9,C10,C11,C12,"
	                    + "D1,D2,D3,D4,D5,D6,D7,D8,D9,D10,D11,D12,E1,E2,E3,E4,E5,E6,E7,E8,E9,E10,E11,E12,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,"
	                    + "G1,G2,G3,G4,G5,G6,G7,G8,G9,G10,G11,G12,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,H11,H12";
	            String[] san = s.split(",");
	            
	            for (int i = 0; i < containerSamples.size(); i++) {
	                ContainerSamplesDTO foo = containerSamples.get(i);
	                foo.setPosition(san[i]);
	                wellplateId=foo.getContainerID();
	            }
	            
	            String urlcs = localhost + AUTHENTICATED_JSON + OMM_CONTAINESAMPLES;
	            try {
	                System.out.println(
	                    "Entered Orders::  Order creation in OMM [CONTAINER SAMPLES]ready to start");
	                String token = SecurityToken.getToken();
	                //postsamples(urlcs, containerSamples, token, null);
	                
	            } catch (Exception e) {
	                
	                System.out.println("error in creating samples creation IN CONTAINER TABLE");
	            }
	            
	         return wellplateId;
	}
	public static String postHL7Message(String host, String port, String timeout, String message, String sync) {

		
		String outMessage = "";

		if (sync == null || sync.equals("")) {
			sync = "true";
		}
		if (message == null || message.equals("")) {

			message = "";
		}
		if (timeout == null || timeout.equals("")) {

			timeout = "6000";
		}

		try {

			CamelContext context = new DefaultCamelContext();

			final org.apache.camel.impl.SimpleRegistry registry = new org.apache.camel.impl.SimpleRegistry();
			final org.apache.camel.impl.CompositeRegistry compositeRegistry = new org.apache.camel.impl.CompositeRegistry();
			compositeRegistry.addRegistry(context.getRegistry());
			compositeRegistry.addRegistry(registry);
			((org.apache.camel.impl.DefaultCamelContext) context).setRegistry(compositeRegistry);

			HL7MLLPCodec hl7codec = new HL7MLLPCodec();
			hl7codec.setCharset("iso-8859-1");

			registry.put("hl7codec", hl7codec);

			ProducerTemplate template = context.createProducerTemplate();
			template.start();

			String camelUrl = "mina2:tcp://" + host + ":" + port + "?sync=" + sync + "&codec=#hl7codec&timeout="
					+ timeout;

			System.out.println("Input HL7 : \n" + message);
			String out = (String) template.requestBody(camelUrl, message.toString().trim());

			System.out.println("Output HL7:\n " + out);
			outMessage = out;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return outMessage;

	}

}
