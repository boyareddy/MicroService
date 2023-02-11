package com.roche.connect.htp.adapter.readrepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.htp.adapter.model.ComplexIdDetails;

@Repository
public interface ComplexIdDetailsReadRepository
		extends SimpleReadRepository<ComplexIdDetails> {

	@Query("select cd from ComplexIdDetails cd where cd.id=:id and cd.company.id=:company")
	public ComplexIdDetails findById(@Param("id")long id, @Param("company")long company);
	
	@Query("select cd from ComplexIdDetails cd where cd.deviceRunId=:runId and cd.company.id=:company")
	public ComplexIdDetails findByDeviceRunId(@Param("runId")String runId, @Param("company")long company);
	
	@Query("select cd from ComplexIdDetails cd where cd.complexId=:complexId and cd.company.id=:company")
	public ComplexIdDetails findByComplexId(@Param("complexId")String complexId, @Param("company")long company);

}
