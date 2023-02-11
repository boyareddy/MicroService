package com.roche.connect.ttv2Simulator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.hcl.hmtp.common.server.entity.metadata.EntityInclusionProvider;
import com.hcl.hmtp.common.server.repositories.PasJpaSimpleReadRepositoryFactoryBean;
import com.hcl.hmtp.common.server.repositories.PasJpaSimpleWriteRepositoryFactoryBean;
import com.hcl.hmtp.core.dataservices.server.repositories.PasJpaMultiTenantReadRepositoryFactoryBean;
import com.hcl.hmtp.core.dataservices.server.repositories.PasJpaMultiTenantWriteRepositoryFactoryBean;

@Configuration
public class RepositoryConfiguration {

	 
}

/**
 * ============================================================================
 * ============ COPYRIGHT NOTICE
 * ================================================
 * ======================================== Copyright (C) 2018, HCL Technologies
 * Limited. All Rights Reserved. Proprietary and confidential. All information
 * contained herein is, and remains the property of HCL Technologies Limited.
 * Copying or reproducing the contents of this file, via any medium is strictly
 * prohibited unless prior written permission is obtained from HCL Technologies
 * Limited.
 */