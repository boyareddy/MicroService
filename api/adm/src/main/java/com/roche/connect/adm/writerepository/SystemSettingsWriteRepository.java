package com.roche.connect.adm.writerepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantWriteRepository;
import com.roche.connect.adm.model.SystemSettings;

@Repository
public interface SystemSettingsWriteRepository extends MultiTenantWriteRepository<SystemSettings>{
	
	
	@Modifying
	@Query("delete from SystemSettings ss where ss.company.id=:domainId and ss.attributeType not like '%Backup'")
	public void deleteByDomainId(@Param("domainId")long domainId);
}
