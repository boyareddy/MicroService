package com.roche.nipt.dpcr.router;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.dataformat.HL7DataFormat;
import org.apache.camel.processor.interceptor.TraceEventHandler;
import org.apache.camel.processor.interceptor.TraceInterceptor;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.roche.nipt.dpcr.util.MP96ParamConfig;

public class HL7RouterTest extends RouteBuilder {

	private static final Logger log = LogManager.getLogger(HL7RouterTest.class);

	CamelContext camelContext = new DefaultCamelContext();
	
	String camelEndPoint = "mina2:tcp://10.150.52.80:4447?sync=true&codec=#hl7codec";

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void configure() throws Exception {
	
	
	

		HL7Router hl7Router = new HL7Router();
		Tracer tracer = new Tracer();
		tracer.addTraceHandler(new TraceEventHandler() {

			@Override
			public void traceExchangeOut(ProcessorDefinition<?> node, Processor target,
					TraceInterceptor traceInterceptor, Exchange exchange, Object traceState) throws Exception {
				// TODO Auto-generated method stub

			}

			@Override
			public Object traceExchangeIn(ProcessorDefinition<?> node, Processor target,
					TraceInterceptor traceInterceptor, Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void traceExchange(ProcessorDefinition<?> node, Processor target, TraceInterceptor traceInterceptor,
					Exchange exchange) throws Exception {

				traceInterceptor.getTracer().getDefaultTraceFormatter().setShowBody(true);
				log.info(exchange.getIn().getHeaders().values().toString());

			}
		});
		camelContext.addInterceptStrategy(tracer); {
		
		MP96ParamConfig.getInstance().getMp96ConfigDetails();
		HL7DataFormat dataFormat = new HL7DataFormat();
		hl7Router.configure();
		from(camelEndPoint).unmarshal(dataFormat).process(new AsyncProcessor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean process(Exchange exchange, AsyncCallback callback) {
				return false;
				
			}
			
		});
		
		hl7Router.configure();

	}
}
}
