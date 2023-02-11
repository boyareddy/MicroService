/*******************************************************************************
 * ApplicationBootIMM.java                  
 *  Version:  1.0
 * 
 *  Authors:  Alexander
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
 *  Alexanders@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.connect.imm;

import org.slf4j.MDC;
import org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hcl.hmtp.common.server.util.ConfigurationParser;
import com.hcl.hmtp.common.server.util.PersistenceInitUtil;
import com.hcl.hmtp.configuration.application.ApplicationTemplateWithAmqpConfiguration;

/**
 * The Class ApplicationBootIMM.
 */
@EntityScan("com.roche.connect.imm.model")
@ComponentScan(basePackages = { "com.roche.connect.imm.service", "com.roche.connect.imm.rest", "com.roche.connect.common.util" })
@EnableJpaRepositories("com.roche.connect.imm.repo")
@Import(value = { ApplicationTemplateWithAmqpConfiguration.class, RepositoryConfiguration.class })
@SpringBootApplication(exclude = { AuditAutoConfiguration.class })
public class ApplicationBootIMM {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		final SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(ApplicationBootIMM.class);
		final ConfigurableApplicationContext applicationContext = applicationBuilder.run(args);
		MDC.put("moduleName", ConfigurationParser.getString("pas.logged_module_name"));
		PersistenceInitUtil.initialize(applicationContext);
	}

}
