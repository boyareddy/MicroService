package com.roche.connect.rmm.readrepository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.rmm.model.SampleResults;

public interface SampleResultsReadRepository extends MultiTenantReadRepository<SampleResults> {

	@SuppressWarnings("rawtypes")
	@Query("select sr.accesssioningId as accesssioningId, sr.outputContainerId as outputContainerId, rn.deviceId as deviceId from RunResults rn inner join rn.sampleResults sr where rn.deviceId=:deviceId AND sr.outputContainerId=:outputContainerId")
	public List<Map> getSampleIdListForInputContainerID(@Param("outputContainerId") String outputContainerId,
			@Param("deviceId") String deviceId);

	/**
	 * Get SampleResults List through accesssioningId and inputContainerId.
	 *
	 * @param accesssioningId
	 * @param inputContainerId
	 * @return the List SampleResults
	 */
	@Query("select sr from SampleResults sr where sr.accesssioningId=:accesssioningId and sr.inputContainerId=:inputContainerId and sr.company.id=:domainId")
    public List<SampleResults> findByAccesssioningIdAndInputContainerId(@Param("accesssioningId")String accesssioningId,
        @Param("inputContainerId")String inputContainerId,@Param("domainId")long domainId);

	/**
	 *  Get SampleResults List through accesssioningId and inputContainerPosition.
	 *
	 * @param accesssioningId
	 * @param inputContainerPosition
	 * @return the List SampleResults
	 */
	   @Query("select sr from SampleResults sr where sr.accesssioningId=:accesssioningId and sr.inputContainerId=:inputContainerId and sr.inputContainerPosition=:inputContainerPosition and sr.company.id=:domainId")
	    public List<SampleResults> findByAccesssioningIdAndInputContainerIdAndInputContainerPosition(@Param("accesssioningId")String accesssioningId,
	        @Param("inputContainerId")String inputContainerId,@Param("inputContainerPosition")String inputContainerPosition,@Param("domainId")long domainId);
	
	/**
	 * Get SampleResults List by accessioningId .
	 *
	 * @param accessioningId
	 * @return the process step results
	 */
	
	@Query("select sr.id from SampleResults sr where sr.accesssioningId =:accessioningId and sr.company.id=:domainId")
    public List<Long> findIdByAccesssioningId(@Param("accessioningId") String accessioningId,@Param("domainId")long domainId);
	
	
	@Query(value="SELECT s1.accessioning_id as accessioningId,s1.comments as comments,s1.order_id as orderId,"
			+ " rs.process_step_name as workflowType,rs.assay_type as assaytype,s1.sample_type as sampleType,s1.status as workflowStatus,s1.flag as flags,s1.createdate as createDate,s1.updated_date_time as updatedDateTime from (SELECT * from"
			+ "(SELECT s.sample_results_id,s.accessioning_id,s.comments,s.order_id,s.status,s.flag,s.createdate,s.run_results_id,s.updated_date_time,s.sample_type,"
			+ " ROW_NUMBER() OVER(PARTITION BY s.accessioning_id ORDER BY s.sample_results_id DESC) AS rn "
			+ " FROM sample_results s) sa Where sa.rn=1 and sa.status not in ('NOT STARTED')) s1,run_results rs"
			+ " where s1.run_results_id = rs.run_results_id"
			+ " order by s1.updated_date_time desc",nativeQuery = true)
	public List<Object> getInWorkFlowOrders();
	
	@Query(value="SELECT count(*) from "
			+ " (SELECT s.sample_results_id,s.accessioning_id,s.comments,s.order_id,s.status,s.flag,s.run_results_id,s.updated_date_time,s.sample_type,"
			+ " ROW_NUMBER() OVER(PARTITION BY s.accessioning_id ORDER BY s.sample_results_id DESC) AS rn "
			+ " FROM sample_results s) sa Where sa.rn=1 and sa.status not in ('NOT STARTED')",nativeQuery = true)
	public Long getInWorkFlowOrdersCount();
	
	
	@Query("select sr from SampleResults sr where sr.id=:sampleresultid and sr.company.id=:domainId")
    public SampleResults findRunResultBySampleResultId(@Param("sampleresultid")long sampleresultid,@Param("domainId")long domainId);

    
    @Query("select sr.company.id from SampleResults sr where sr.id=:deviceId and sr.company.id=:domainId")
    public long getCompanyIdByDeviceId(@Param("deviceId")String deviceId,@Param("domainId")long domainId);

	@Query(value = "SELECT * from "
			+ " (SELECT *, ROW_NUMBER() OVER(PARTITION BY s.accessioning_id ORDER BY s.sample_results_id DESC) AS rn "
			+ " FROM sample_results s where upper(s.accessioning_id) like upper(concat('%', :accessioningId,'%')) "
			+ " and s.companyid=:domainId ) sa  Where sa.rn=1 \n-- #pageable\n", countQuery = "SELECT count(*) from "
					+ " (SELECT *, ROW_NUMBER() OVER(PARTITION BY s.accessioning_id ORDER BY s.sample_results_id DESC) AS rn "
					+ " FROM sample_results s where upper(s.accessioning_id) like upper(concat('%', :accessioningId,'%')) "
					+ " and s.companyid=:domainId ) sa  Where sa.rn=1 ", nativeQuery = true)
	public Page<SampleResults> findByAccessioningIdContainingIgnoreCase(@Param("accessioningId") String accessioningId,
			@Param("domainId") long domainId, Pageable pageable);

	@Query(value="SELECT s1.accessioning_id as accessioningId,s1.comments as comments,s1.order_id as orderId,"
			+ " rs.process_step_name as workflowType,rs.assay_type as assaytype,s1.sample_type as sampleType,s1.status as workflowStatus,s1.flag as flags,s1.createdate as createDate,s1.updated_date_time as updatedDateTime from (SELECT * from"
			+ "(SELECT s.sample_results_id,s.accessioning_id,s.comments,s.order_id,s.status,s.flag,s.createdate,s.run_results_id,s.updated_date_time,s.sample_type,"
			+ " ROW_NUMBER() OVER(PARTITION BY s.accessioning_id ORDER BY s.sample_results_id DESC) AS rn "
			+ " FROM sample_results s where s.accessioning_id=:accessioningId) sa Where sa.rn=1 and sa.status not in ('NOT STARTED')) s1,run_results rs"
			+ " where s1.run_results_id = rs.run_results_id"
			+ " order by s1.updated_date_time desc",nativeQuery = true)
	public List<Object> getInWorkFlowOrderDetails(@Param("accessioningId") String accessioningId);
}
