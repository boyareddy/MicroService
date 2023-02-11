package com.camel.router;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.dataformat.HL7DataFormat;
import org.apache.camel.processor.interceptor.TraceEventHandler;
import org.apache.camel.processor.interceptor.TraceInterceptor;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.camel.dto.HL7HeaderSegmentDTO;
import com.camel.dto.MessageContainerDTO;
import com.camel.service.ResponseHandlerService;
import com.camel.util.InstanceUtil;
import com.camel.util.MP24ParamConfig;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v251.group.SSU_U03_SPECIMEN_CONTAINER;
import ca.uhn.hl7v2.model.v251.message.QBP_Q11;
import ca.uhn.hl7v2.model.v251.message.SSU_U03;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.QPD;
import ca.uhn.hl7v2.model.v251.segment.SPM;

@Component
class HL7Router extends RouteBuilder {
    @Value("${server.port}")
    String serverPort;
    
    @Autowired
    CamelContext camelContext;
    
    @Autowired
    ResponseHandlerService responseHandlerService;
    
    private static final Logger logger = LogManager.getLogger(HL7Router.class);

	@Override
	public void configure() throws Exception {
		
		Tracer tracer = new Tracer();
		tracer.addTraceHandler(new TraceEventHandler() {	
			
			@Override
			public void traceExchangeOut(ProcessorDefinition<?> node, Processor target, TraceInterceptor traceInterceptor,
					Exchange exchange, Object traceState) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object traceExchangeIn(ProcessorDefinition<?> node, Processor target, TraceInterceptor traceInterceptor,
					Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void traceExchange(ProcessorDefinition<?> node, Processor target, TraceInterceptor traceInterceptor,
					Exchange exchange) throws Exception {
				
				traceInterceptor.getTracer().getDefaultTraceFormatter().setShowBody(true);
				logger.info(exchange.getIn().getHeaders().values().toString()) ;				
			}
		});
		
		camelContext.addInterceptStrategy(tracer);
		
		MP24ParamConfig.getInstance().getMp24ConfigDetails();
		HL7DataFormat dataFormat = new HL7DataFormat();
		from("properties:{{camel.hl7router.endpoint}}")
		.unmarshal(dataFormat)
		.process(new AsyncProcessor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean process(Exchange exchange, AsyncCallback callback) {
				
				Message message = exchange.getIn().getBody(Message.class);
				logger.info(exchange.getIn().getBody(String.class));
				String msgType = message.getHeader("CamelHL7MessageType").toString();
				MessageContainerDTO containerDTO = new MessageContainerDTO();
				containerDTO.setExchange(exchange);
				containerDTO.setCallback(callback);
				ca.uhn.hl7v2.model.Message message1 = (ca.uhn.hl7v2.model.Message)exchange.getIn().getBody();
				QBP_Q11 qbp_Q11;
				SSU_U03 ssu_U03;
				if(msgType.equalsIgnoreCase("QBP")) {
					qbp_Q11 = (QBP_Q11) message1;
			        Type sampleIdSeg = qbp_Q11.getQPD().getQpd3_UserParametersInsuccessivefields().getData();
			        try {
						String sampleId = sampleIdSeg.encode();
						HL7HeaderSegmentDTO headerSegmentDTO = new HL7HeaderSegmentDTO();
						MSH msh1 = (MSH)qbp_Q11.get("MSH");
						QPD qpd1 = (QPD)qbp_Q11.get("QPD");
						headerSegmentDTO.setSendingApplicationName(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
						headerSegmentDTO.setSendingFacility(msh1.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setReceivingFacility(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());
						
						headerSegmentDTO.setSampleID(sampleId);
						headerSegmentDTO.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
						headerSegmentDTO.setCharacterSet(msh1.getMsh18_CharacterSet().toString());
						headerSegmentDTO.setMessageType(msh1.getMsh9_MessageType().getMsg1_MessageCode().getValue());
						headerSegmentDTO.setProcessingId(msh1.getMsh11_ProcessingID().getPt1_ProcessingID().getValue());
						headerSegmentDTO.setQueryDef_Id(qpd1.getQpd1_MessageQueryName().getCe1_Identifier().getValue());
						headerSegmentDTO.setQueryDef_desc(qpd1.getQpd1_MessageQueryName().getCe2_Text().getValue());
						headerSegmentDTO.setQueryDef_EncodingSystem(qpd1.getQpd1_MessageQueryName().getCe3_NameOfCodingSystem().getValue());
						headerSegmentDTO.setVersionId(msh1.getMsh12_VersionID().getVid1_VersionID().getValue());
						containerDTO.setHeaderSegment(headerSegmentDTO);
						InstanceUtil.getInstance().getConnectionMap().put(sampleId, containerDTO);
						MP24ParamConfig.getInstance().getMp24ConfigDetails();
						responseHandlerService.convertQBPToMsgPayload(headerSegmentDTO);
//						responseHandlerService.sendResponseToMP24Mock(sampleId);
					} catch (HL7Exception e) {
						e.printStackTrace();
					}
				} else if(msgType.equalsIgnoreCase("SSU")) {
					ssu_U03 = (SSU_U03) message1;
					String sampleID = "";
			        try {
						HL7HeaderSegmentDTO headerSegmentDTO = new HL7HeaderSegmentDTO();
						MSH msh1 = (MSH)ssu_U03.get("MSH");
						SSU_U03_SPECIMEN_CONTAINER sacContainer = ssu_U03.getSPECIMEN_CONTAINER();
						SPM spm = sacContainer.getSPECIMEN().getSPM();
						headerSegmentDTO.setSendingApplicationName(msh1.getMsh3_SendingApplication().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setDeviceSerialNumber(msh1.getMsh3_SendingApplication().getHd2_UniversalID().getValue());
						headerSegmentDTO.setSendingFacility(msh1.getMsh4_SendingFacility().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setReceivingFacility(msh1.getMsh6_ReceivingFacility().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setReceivingApplication(msh1.getMsh5_ReceivingApplication().getHd1_NamespaceID().getValue());
						headerSegmentDTO.setMessageControlId(msh1.getMsh10_MessageControlID().getValue());
						sampleID = spm.getSpm3_SpecimenParentIDs(0).getEip1_PlacerAssignedIdentifier()
								.getEi1_EntityIdentifier().getValue();
						headerSegmentDTO.setSampleID(sampleID);
						headerSegmentDTO.setDateTimeMessageGenerated(msh1.getMsh7_DateTimeOfMessage().getTs1_Time().getValue());
						headerSegmentDTO.setCharacterSet(msh1.getMsh18_CharacterSet().toString());
						headerSegmentDTO.setMessageType(msh1.getMsh9_MessageType().getMsg1_MessageCode().getValue());
						headerSegmentDTO.setProcessingId(msh1.getMsh11_ProcessingID().getPt1_ProcessingID().getValue());
						headerSegmentDTO.setVersionId(msh1.getMsh12_VersionID().getVid1_VersionID().getValue());
						containerDTO.setHeaderSegment(headerSegmentDTO);
						InstanceUtil.getInstance().getConnectionMap().put(sampleID, containerDTO);
//						responseHandlerService.sendACKToMP24Mock(sampleID);
						responseHandlerService.convertSSUToMsgPayload(ssu_U03);
					} catch (HL7Exception e) {
						e.printStackTrace();
					}
				}
				return false;
			}
		});
	}
}

