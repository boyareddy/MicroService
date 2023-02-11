package com.roche.connect.adm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.adm.model.SystemSettings;

@Repository
public interface SystemSettingsReadRepository extends MultiTenantReadRepository<SystemSettings>{
	
	@Query(value="select ss from SystemSettings ss where ss.company.id=:domainId and ss.activeFlag='Y' ")
	List<SystemSettings> findAllSystemSettingsByType(@Param("domainId") long domainId);
	
	@Query(value="select ss.image from SystemSettings ss where ss.attributeName = 'labLogo' and ss.attributeType='reportsettings' and ss.company.id=:domainId and ss.activeFlag='Y'")
	byte[] getLabLogo(@Param("domainId") long domainId);
	
	@Query("select c from SystemSettings c where c.attributeType =:attributeType")
	public List<SystemSettings> findByAttributeType( @Param("attributeType")String attributeType);
	
	@Query("select c from SystemSettings c where c.attributeName=:attributeName")
	public SystemSettings findByAttributeName(@Param("attributeName")String attributeName);
}
