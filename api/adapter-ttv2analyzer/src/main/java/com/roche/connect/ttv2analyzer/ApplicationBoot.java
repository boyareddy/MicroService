/**
 * =============================================================================
 * COPYRIGHT NOTICE
 * =============================================================================
 * Copyright (C) 2014, HCL Technologies Limited. All Rights Reserved.
 * Proprietary and confidential. All information contained herein is, and
 * remains the property of HCL Technologies Limited. Copying or reproducing the
 * contents of this file, via any medium is strictly prohibited unless prior
 * written permission is obtained from HCL Technologies Limited.
 */
package com.roche.connect.ttv2analyzer;


import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.MDC;
import org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.hcl.hmtp.common.server.util.NoSecurityUrlPatterns;
import com.hcl.hmtp.common.server.util.PersistenceInitUtil;
import com.hcl.hmtp.configuration.application.ApplicationTemplateWithAmqpConfiguration;

import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
@Import(value = {RepositoryConfiguration.class, SpringDataRestConfiguration.class, ApplicationTemplateWithAmqpConfiguration.class,SwaggerConfiguration.class})
@SpringBootApplication(exclude = { AuditAutoConfiguration.class })
//Please replace your package here if you need to add your package in the component scan and uncomment this line.
@ComponentScan(basePackages ="com.roche.connect.ttv2analyzer")  
public class ApplicationBoot {
	public static void main(String[] args) throws Exception {
		final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(ApplicationBoot.class);
		final ConfigurableApplicationContext applicationContext = applicationBuilder.run(args);
		MDC.put("moduleName", ConfigurationParser.getString("pas.logged_module_name"));
		PersistenceInitUtil.initialize(applicationContext);
	}
	
    @Bean
    public NoSecurityUrlPatterns noSecurityUrlPattern() {
           return new NoSecurityUrlPatterns(new String[] { "/**" });
    } 


}
