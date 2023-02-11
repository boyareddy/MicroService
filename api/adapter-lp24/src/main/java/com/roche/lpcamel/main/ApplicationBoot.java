/*******************************************************************************
 * ApplicationBoot.java
 *  Version:  1.0
 *
 *  Authors:  gosula.r
 *
 * *********************
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE,
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.
 *
 * *********************
 *  ChangeLog:
 *
 *   gosula.r@hcl.com : Updated copyright headers
 *
 * *********************
 *
 *  Description:
 *
 * *********************
 ******************************************************************************/
package com.roche.lpcamel.main;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.hcl.hmtp.common.server.util.NoSecurityUrlPatterns;
import com.hcl.hmtp.common.server.util.PersistenceInitUtil;
import com.hcl.hmtp.configuration.application.ApplicationTemplateWithAmqpConfiguration;

/**
 * The Class ApplicationBoot.
 */
@Import(value = { /*SpringDataRestConfiguration.class,*/ ApplicationTemplateWithAmqpConfiguration.class })
@ComponentScan(basePackages={"com.roche.lpcamel.main", "com.roche.lpcamel.router", "com.roche.lpcamel.service"})
@Configuration
@SpringBootApplication(exclude = { AuditAutoConfiguration.class })
public class ApplicationBoot extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
    public static void main(String[] args) throws Exception {
        final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(ApplicationBoot.class);
        final ConfigurableApplicationContext applicationContext = applicationBuilder.run(args);
        MDC.put("moduleName", ConfigurationParser.getString("pas.logged_module_name"));
        PersistenceInitUtil.initialize(applicationContext);
    }

	/**
	 * Entity manager factory.
	 *
	 * @param builder
	 *            the builder
	 * @param dataSource
	 *            the data source
	 * @return the local container entity manager factory bean
	 */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder.dataSource(dataSource).persistenceUnit("writePersistentUnit").
                packages("com.hcl.hmtp.common.server.entity",
                         "com.hcl.hmtp.application.server.entity").build();
     }

	/**
	 * No security url pattern.
	 *
	 * @return the no security url patterns
	 */
    @Bean
    public NoSecurityUrlPatterns noSecurityUrlPattern() {
        return new NoSecurityUrlPatterns("/**");
    }

	/** The server port. */
    @Value("${server.port}")
    String serverPort;

	/** The camel context. */
    CamelContext camelContext;


	/**
	 * Context.
	 *
	 * @return the camel context
	 */
    @Bean
    public CamelContext context() {

        SimpleRegistry reg = new SimpleRegistry();
        reg.put("hl7codec", new HL7MLLPCodec());
        camelContext = new DefaultCamelContext(reg);
        return camelContext;

    }


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.support.
	 * AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.support.
	 * AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses(
	 * )
	 */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#
	 * getServletMappings()
	 */
    @Override
    protected String[] getServletMappings() {
        return new String[] {
                   "/rest/*"
        };
    }
}



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
