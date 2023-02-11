package com.roche.connect.htp.adapter.readrepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.htp.adapter.model.ForteJob;

@Repository
public interface ForteJobReadRepository extends SimpleReadRepository<ForteJob> {

	public ForteJob findById(long jobId);

	public List<ForteJob> findByDeviceRunIdAndJobStatus(String deviceRunId, String jobStatus);

	@Query(nativeQuery=true, 
			value="select DISTINCT f.DEVICE_RUN_ID from FORTE_JOB f where f.SENT_TO_TERTIARY=:sentToTertiary AND f.JOB_TYPE=:jobType AND f.JOB_STATUS=:jobStatus AND f.COMPANYID = :company")
	public List<String> findDistinctDeviceRunIdBySentToTertiaryAndJobType(
			@Param("sentToTertiary") String sentToTertiary, @Param("jobType") String jobType,
			@Param("jobStatus") String jobStatus, @Param("company")long company);

	public List<ForteJob> findByDeviceRunIdAndSentToTertiaryAndJobTypeAndCompanyOrderByCreatedDateTimeAsc(String deviceRunId,
			String sentToTertiary, String jobType, long company);
}
