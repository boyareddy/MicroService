/*******************************************************************************
 * 
 * 
 *  ApplicationBootWFM.java                  
 *  Version:  1.0
 * 
 *  Authors:  somesh_r
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
 *   somesh_r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: Main class implementation that controls overall execution of WFM
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
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
package com.roche.connect.wfm;

import javax.sql.DataSource;

import org.slf4j.MDC;
import org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.hcl.hmtp.common.server.util.PersistenceInitUtil;
import com.hcl.hmtp.configuration.application.ApplicationTemplateWithAmqpConfiguration;

/**
 * Main class implementation that controls overall execution of WFM
 *
 */
@EntityScan("com.roche.connect.wfm.model")
@ComponentScan(basePackages = { "com.roche.connect.wfm.service", "com.roche.connect.wfm.rest" })
@EnableJpaRepositories("com.roche.connect.wfm.repository,com.roche.connect.wfm.writerepository")
@Import(value = { ApplicationTemplateWithAmqpConfiguration.class, RepositoryConfigurationWfm.class })
@SpringBootApplication(exclude = { AuditAutoConfiguration.class })
public class ApplicationBootWFM {
	public static void main(String[] args) throws Exception {
		final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(ApplicationBootWFM.class);
		final ConfigurableApplicationContext applicationContext = applicationBuilder.run(args);
		MDC.put("moduleName", ConfigurationParser.getString("pas.logged_module_name"));
		PersistenceInitUtil.initialize(applicationContext);
	}

	/**
	 * @param builder
	 * @param dataSource
	 * @return LocalContainerEntityManagerFactoryBean
	 */
	@Primary
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder,
			DataSource dataSource) {
		return builder.dataSource(dataSource).persistenceUnit("writePersistentUnit")
				.packages("com.hcl.hmtp.common.server.entity", "com.hcl.hmtp.application.server.entity",
						"com.roche.connect.wfm.model")
				.build();
	}

}
