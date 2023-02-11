package com.roche.swam.labsimulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

@RunWith(MockitoJUnitRunner.class)
public class Mp96TestServerTest {
	
	
	
	
	  private HapiContext context;


  @Test
  public void Mp96TestServer() {
  }

  @Test
  public void connectionDiscarded() {
  }

  @Test
  public void connectionReceived() {
  }


  @Test
  public void getQueryHandler() throws HL7Exception, InterruptedException {
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  Message theIn = null; 
	  int port = 4322;
	  mpxServer.start(port);
		Assert.assertEquals(mpxServer.getServer().isRunning(), true);
		Assert.assertEquals(mpxServer.getQueryHandler().canProcess(theIn), true);
  }

  @Test
  public void getResultHandler() throws HL7Exception, InterruptedException {
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  Message theIn = null; 
	  int port = 4322;
	  mpxServer.start(port);
		Assert.assertEquals(mpxServer.getServer().isRunning(), true);
		Assert.assertEquals(mpxServer.getResultHandler().canProcess(theIn), true);
  }

  @Test
  public void getServer() {
  }

  @Test
  public void main() throws HL7Exception, InterruptedException {
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  int port = 4322;
	  mpxServer.start(port);
		Assert.assertEquals(mpxServer.getServer().isRunning(), true);
		 mpxServer.getServer().stopAndWait();
  }



  @Test
  public void start() throws HL7Exception, InterruptedException {
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  int port = 4322;
	  mpxServer.start(port);
		Assert.assertEquals(mpxServer.getServer().isRunning(), true); 
		 mpxServer.getServer().stopAndWait();
  }
  
  @Test
  public void startNegative() throws HL7Exception, InterruptedException {
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  int port = 4322;
	  mpxServer.start(port);
	  mpxServer.getServer().stopAndWait();
		Assert.assertEquals(mpxServer.getServer().isRunning(), false); 
		 mpxServer.getServer().stopAndWait();
  }
  
  @Test
  public void processWOR() throws IOException, HL7Exception, ReceivingApplicationException, InterruptedException {
	  Map<String, Object> map;
		Message m  = null;
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  int port = 4322;
	  mpxServer.start(port);
	  Assert.assertEquals(mpxServer.getServer().isRunning(), true); 
	  InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/QBP_Q11.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		
		
		String value = "{/MSH-10=MPCZC8380G5K, SENDING_PORT=57463, raw-message=MSH|^&~\\|MagNA Pure 96||||20091126081114||QBP^Q11|MPCZC8380G5K|P|2.4" + 
				"QPD|QUERY_ORDER\r\n" + 
				"RCP|I||R\r\n" + 
				", SENDING_IP=127.0.0.1}";
		value = value.substring(1, value.length()-1);           //remove curly brackets
		String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
		 map = new HashMap<>();               
		for(String pair : keyValuePairs) {                        //iterate over the pairs
		    String[] entry = pair.split("=");                   //split the pairs to get key and value 
		    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		}
		
		 m = mpxServer.getQueryHandler().processMessage(new DefaultHapiContext().getPipeParser().parse(msg), map);
		 ORM_O01 rsp = (ORM_O01) m;
		 MSH mshSegment = rsp.getMSH();
		 ORC orcSegment = rsp.getORDER().getORC();
		Assert.assertEquals("ORM", mshSegment.getMsh9_MessageType().getMsg1_MessageType().getValue());
		Assert.assertEquals("O01", mshSegment.getMsh9_MessageType().getMsg2_TriggerEvent().getValue());
		Assert.assertEquals("HL7-7",orcSegment.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue() );
		  mpxServer.getServer().stopAndWait();
  }
  

  @Test
  public void processOUL() throws HL7Exception, InterruptedException, IOException, ReceivingApplicationException {
	  Map<String, Object> map;
		Message m  = null;
	  Mp96TestServer mpxServer = new Mp96TestServer();
	  int port = 4322;
	  mpxServer.start(port);
	  Assert.assertEquals(mpxServer.getServer().isRunning(), true); 
	  InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/OUL_R21.txt");
	 
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		
		String value = "{/MSH-10=1234567890, SENDING_PORT=57465, raw-message=MSH|^&~\\|MagNA Pure 96||||20091126081114||ACK|MPCZC8380G5K|P|2.4|" + 
				"ORC|1|Order790^506_200908281313|Comment to Order 243||||||20090828130858|std|\r\n" + 
				"OBR|1|zxc|0405090000004225|RNA·LV^0.2|||20090828130828|20090828130851|||||96WellPlate||SW_Test_MP96_RNA_LV_IC_Handling·1.1|||450|200|1.0.0|506\r\n" + 
				"OBX|1||A1|||||||||||\r\n" + 
				"NTE|1||Comment for the Sample X|\r\n" + 
				", SENDING_IP=127.0.0.1}";
		value = value.substring(1, value.length()-1);           //remove curly brackets
		String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
		 map = new HashMap<>();               
		for(String pair : keyValuePairs) {                        //iterate over the pairs
		    String[] entry = pair.split("=");                   //split the pairs to get key and value 
		    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		}
		 m = mpxServer.getResultHandler().processMessage(new DefaultHapiContext().getPipeParser().parse(msg), map); 
		 ACK ack = (ACK) m;
		 MSH mshSegment = ack.getMSH();
		 Assert.assertEquals("ACK", mshSegment.getMsh9_MessageType().getMsg1_MessageType().getValue()); 
		 mpxServer.getServer().stopAndWait();
  }
  
 /* @Test
  public void processACK() throws HL7Exception, InterruptedException, IOException, ReceivingApplicationException {
	
	Mp96TestServer mpxServer = new Mp96TestServer();
	  mpxServer.start();
		  InputStream resource = this.getClass().getClassLoader().getResourceAsStream("hl7/ACK.txt");
		String msg = CharStreams.toString(new InputStreamReader(resource, Charsets.UTF_8)).replace('\n', '\r');
		ACK ackBuilder = (ACK) new DefaultHapiContext().getPipeParser().parse(msg);
		String messageControlId = UUID.randomUUID().toString().substring(0, 5);
		System.out.println("Message control ID :"+messageControlId);
		MSA msa = ackBuilder.getMSA();
		msa.getMsa2_MessageControlID().setValue("MPCZC8380G5K");
		MSH msh = ackBuilder.getMSH();
		msh.getMsh10_MessageControlID().setValue("MPCZC8380G5K");
		
		try(Socket socket =new Socket("localhost", 4444);){
				System.out.println("Connection established: " + socket.isConnected());

	            InputStream in = socket.getInputStream();
	            OutputStream out = socket.getOutputStream();
	            String ackMessageWrapper = "\u000b" + ackBuilder.toString() + "\u001c" + "\r";

	            // Send the MLLP-wrapped HL7 message to the server
	            out.write(ackMessageWrapper.getBytes());
	        	byte[] byteBuffer = new byte[2500];
	            in.read(byteBuffer);

				String response = new String(byteBuffer);
				
				Assert.assertEquals(response.contains("ACK"), true); 
		           out.close();
		           mpxServer.getServer().stopAndWait();
		           
		}*/
		 
  
  
  
}
