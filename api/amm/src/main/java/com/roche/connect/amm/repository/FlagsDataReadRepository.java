package com.roche.connect.amm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.amm.model.FlagsData;

public interface FlagsDataReadRepository extends MultiTenantReadRepository<FlagsData> {
	@Query("select a from FlagsData a where  a.activeFlag='Y' and a.assayType =:assayType and a.deviceType =:deviceType and a.flagCode =:flagCode and a.company.id=:domainId")
    public List<FlagsData> findFlagDTOByAssayTypeAndDeviceTypeAndFlageCode(@Param("assayType") String assayType,@Param("deviceType") String deviceType,@Param("flagCode") String flagCode,@Param("domainId")long domainId);

	@Query("select a from FlagsData a where  a.activeFlag='Y' and a.assayType =:assayType and a.deviceType =:deviceType and a.company.id=:domainId")
    public List<FlagsData> findFlagDTOByAssayTypeAndDeviceType(@Param("assayType") String assayType,@Param("deviceType") String deviceType,@Param("domainId")long domainId);
	
	@Query("select a from FlagsData a where  a.activeFlag='Y' and  a.deviceType =:deviceType and a.company.id=:domainId")
    public List<FlagsData> findFlagDTOByDeviceType(@Param("deviceType") String deviceType,@Param("domainId")long domainId);
	
	@Query("select a from FlagsData a where a.activeFlag='Y' and  a.deviceType =:deviceType and a.flagCode =:flagCode and a.company.id=:domainId")
    public List<FlagsData> findFlagDTOByDeviceTypeAndFlageCode(@Param("deviceType") String deviceType,@Param("flagCode") String flagCode,@Param("domainId")long domainId);
	
	
}
