package com.camel.main;

import org.apache.camel.CamelContext;
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


@SpringBootApplication
@ComponentScan(basePackages={"com.camel.main", "com.camel.router", "com.camel.service"})
@Configuration
public class Application extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Value("${server.port}")
    String serverPort;
    
    CamelContext camelContext;
    
    public static void main(String[] args) {
    	
        SpringApplication.run(Application.class, args);
    }

    @Bean
	public CamelContext context() {
		
		SimpleRegistry reg = new SimpleRegistry();
		reg.put("hl7codec", new HL7MLLPCodec());
		camelContext = new DefaultCamelContext(reg);
		PropertiesComponent pc = new PropertiesComponent();
    	pc.setLocation("classpath:/application.properties"); 
    	camelContext.addComponent("properties", pc);
//		camelContext.setTracing(true);
//		camelContext.getProperties().put(Exchange.LOG_DEBUG_BODY_STREAMS, "true");
		return camelContext;
		
	}
    

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {
				   "/rest/*"
		};
	}
}


