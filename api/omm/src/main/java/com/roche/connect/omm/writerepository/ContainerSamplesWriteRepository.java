package com.roche.connect.omm.writerepository;

import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantWriteRepository;
import com.roche.connect.omm.model.ContainerSamples;

public interface ContainerSamplesWriteRepository extends MultiTenantWriteRepository<ContainerSamples> {
	
	public Long deleteByContainerID(@Param("containerId") String containerId);
}
