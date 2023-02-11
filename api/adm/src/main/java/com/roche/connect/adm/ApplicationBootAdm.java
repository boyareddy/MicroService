/*******************************************************************************
 * 
 * 
 *  ApplicationBootAdm.java                  
 *  Version:  1.0
 * 
 *  Authors:  dineshj
 * 
 * =================================
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
 * =================================
 *  ChangeLog:
 *   
 * 
 * 
 ******************************************************************************/
package com.roche.connect.adm;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.slf4j.MDC;
import org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hcl.hmtp.common.server.repositories.PasJpaSimpleReadRepositoryFactoryBean;
import com.hcl.hmtp.common.server.repositories.PasJpaSimpleWriteRepositoryFactoryBean;
import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.hcl.hmtp.common.server.util.NoSecurityUrlPatterns;
import com.hcl.hmtp.common.server.util.PersistenceInitUtil;
import com.hcl.hmtp.configuration.application.ApplicationTemplateWithAmqpConfiguration;
import com.hcl.hmtp.configuration.common.ReadDataSourceRouter;

@Import(value = { ApplicationTemplateWithAmqpConfiguration.class })
@SpringBootApplication(exclude = { AuditAutoConfiguration.class })
@ComponentScan(basePackages = "com.roche.connect.adm")
@EnableScheduling
public class ApplicationBootAdm {
	public static void main(String[] args) throws Exception {
		final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(ApplicationBootAdm.class);
		final ConfigurableApplicationContext applicationContext = applicationBuilder.run(args);
		MDC.put("moduleName", ConfigurationParser.getString("pas.logged_module_name"));
		PersistenceInitUtil.initialize(applicationContext);
	}

	@EnableJpaRepositories(basePackages = {
			"com.roche.connect" }, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager", repositoryFactoryBeanClass = PasJpaSimpleWriteRepositoryFactoryBean.class)
	static class JpaSimpleWriteRepositoryConfiguration {
	}

	@EnableJpaRepositories(basePackages = {
			"com.roche.connect" }, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager", repositoryFactoryBeanClass = PasJpaSimpleReadRepositoryFactoryBean.class)
	static class JPASimpleReadRepositoryConfiguration {
	}

	@Conditional({ com.hcl.hmtp.common.server.util.DataPartitionDisabledCondition.class })
	@Primary
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder,
			DataSource dataSource) {
		return builder.dataSource(dataSource).persistenceUnit("writePersistentUnit")
				.packages("com.hcl.hmtp.common.server.entity", "com.hcl.hmtp.application.server.entity",
						"com.hcl.hmtp.core.dataservices.optional.server.entity", "com.roche.connect.adm.model")
				.build();
	}

	@Conditional({ com.hcl.hmtp.common.server.util.DataPartitionDisabledCondition.class })
	@Bean
	public LocalContainerEntityManagerFactoryBean readEntityManagerFactory(final EntityManagerFactoryBuilder builder,
			DataSource readDataSource) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(AvailableSettings.HBM2DDL_AUTO, "validate");
		properties.put("hibernate.ddl-auto", "validate");
		return builder.dataSource(readDataSource()).persistenceUnit("readPersistenceUnit").properties(properties)
				.packages("com.hcl.hmtp.common.server.entity", "com.hcl.hmtp.application.server.entity",
						"com.roche.connect.adm.model")
				.build();
	}

	@ConfigurationProperties(prefix = "spring.read")
	@Bean
	public ReadDataSourceRouter readDataSource() {
		return new ReadDataSourceRouter();
	}

	@Bean
	public NoSecurityUrlPatterns noSecurityUrlPattern() {
		return new NoSecurityUrlPatterns(new String[] { "/notification" });
	}

}