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
package com.roche.dpcr;

import javax.sql.DataSource;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.hcl.hmtp.common.server.util.NoSecurityUrlPatterns;
import com.hcl.hmtp.common.server.util.PersistenceInitUtil;
import com.hcl.hmtp.configuration.application.ApplicationTemplateWithAmqpConfiguration;
import com.roche.dpcr.socket.SocketConfiguration;

import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

@Import(value = { RepositoryConfiguration.class, SpringDataRestConfiguration.class,
		ApplicationTemplateWithAmqpConfiguration.class, SwaggerConfiguration.class })
@SpringBootApplication(exclude = { AuditAutoConfiguration.class })
@ComponentScan(basePackages = { "com.roche.dpcr", "com.roche.dpcr.service", "com.roche.dpcr.socket" })
@Configuration
public class ApplicationBoot extends AbstractAnnotationConfigDispatcherServletInitializer {
	public static void main(String[] args) throws Exception {
		final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(ApplicationBoot.class);
		final ConfigurableApplicationContext applicationContext = applicationBuilder.run(args);
		MDC.put("moduleName", ConfigurationParser.getString("pas.logged_module_name"));
		PersistenceInitUtil.initialize(applicationContext);

		applicationContext.getBean(SocketConfiguration.class);

	}

	@Lazy
	@Autowired
	private SocketConfiguration socketConfiguration;

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder,
			DataSource dataSource) {
		return builder.dataSource(dataSource).persistenceUnit("writePersistentUnit")
				.packages("com.hcl.hmtp.common.server.entity", "com.hcl.hmtp.application.server.entity").build();
	}

	@Value("${server.port}")
	String serverPort;

	@Bean
	public NoSecurityUrlPatterns noSecurityUrlPattern() {
		return new NoSecurityUrlPatterns("/**");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/rest/*" };
	}

}
