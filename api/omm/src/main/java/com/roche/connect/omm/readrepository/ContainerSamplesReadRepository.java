package com.roche.connect.omm.readrepository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.omm.model.ContainerSamples;

public interface ContainerSamplesReadRepository extends MultiTenantReadRepository<ContainerSamples> {
	
	@Query("select count(cs.id) as sampleCount ,cs.containerID as containerId,cs.deviceRunID as devicerunid,cs.containerType as containertype from ContainerSamples as cs where cs.status in (:statusList) and cs.company.id=:domainId and cs.activeFlag='Y' group by cs.containerID,cs.deviceRunID,cs.containerType")
	List<Map<Object, Object>> findAllContainerIdWithOpenStatus(@Param("statusList")String[] statusList,@Param("domainId") long domainId);

	@Query(value="select cs.accessioning_id as accessioningID,cs.assay_type as assayType,cs.device_id as deviceID,cs.container_id as containerID,cs.load_id as loadID,cs.status,cs.position,cs.device_run_id as deviceRunID,ord.order_id as orderID,ord.order_comments as orderComments,cs.container_sample_id as containerSampleId,cs.active_flag as activeFlag,cs.container_type as containerType,cs.created_by as createdBy,cs.created_date_time as createdDateTime,cs.updated_by as updatedBy,cs.updated_date_time as updatedDateTime from container_samples cs,orders ord where lower(cs.accessioning_id)=lower(ord.accessioning_id) and cs.companyid=:domainId and cs.container_id=:containerID and cs.status in ('open','senttodevice') and cs.active_flag='Y' ",nativeQuery = true)
	List<Object> findconatinersetsamplebyconatinerid(@Param("containerID") String containerID,
	        @Param("domainId") long domainId);
	
	@Query("select cs.accessioningID as accessioningID,cs.containerID as containerId,cs.position as position,cs.deviceRunID as devicerunid,cs.containerType as containertype from ContainerSamples as cs where lower(cs.accessioningID)=lower(:accessioningId) and cs.company.id=:domainId and cs.activeFlag='Y'")
	List<Map<Object, Object>> findSampleByAccessioningId(@Param("accessioningId")String accessioningId,@Param("domainId") long domainId);
	
	@Query("select cs.accessioningID as accessioningID,cs.containerID as containerId,cs.deviceRunID as devicerunid,cs.containerType as containertype from ContainerSamples as cs where cs.containerID=:containerID and cs.position=:position and cs.company.id=:domainId and cs.activeFlag='Y'")
	List<Map<Object, Object>> findSampleByContaineridAndPosition(@Param("containerID")String containerID,@Param("position")String position,@Param("domainId") long domainId);
	
	@Query("select distinct cs.containerID as containerID,cs.createdDateTime as createdDateTime from ContainerSamples cs where cs.status='open' and cs.company.id=:domainId and cs.activeFlag='Y' order by cs.createdDateTime asc")
	List<Map<String,Object>> findContainerIdByStatus(@Param("domainId") long domainId); 
	
	@Query(value="select cs.accessioning_id as accessioningID,cs.assay_type as assayType,cs.device_id as deviceID,cs.container_id as containerID,cs.load_id as loadID,cs.status,cs.position,cs.device_run_id as deviceRunID,ord.order_id as orderID,ord.order_comments as orderComments,cs.container_sample_id as containerSampleId,cs.active_flag as activeFlag,cs.container_type as containerType,cs.created_by as createdBy,cs.created_date_time as createdDateTime,cs.updated_by as updatedBy,cs.updated_date_time as updatedDateTime from container_samples cs LEFT OUTER JOIN orders ord ON lower(cs.accessioning_id)=lower(ord.accessioning_id) WHERE cs.companyid=:domainId and cs.container_id=:containerId and cs.status='open' and cs.active_flag='Y' ",nativeQuery = true)
	List<Object> findAllContainerSamples(@Param("domainId") long domainId,@Param("containerId") String containerId);
	
	@Query("select cs from ContainerSamples cs where cs.status=:status and cs.company.id=:domainId and cs.activeFlag='Y'")
	List<ContainerSamples> findAllContainerSamplesByStatus(@Param("status") String status,@Param("domainId") long domainId);
	
	@Query(value="select cs.accessioning_id as accessioningID,cs.assay_type as assayType,cs.device_id as deviceID,cs.container_id as containerID,cs.load_id as loadID,cs.status,cs.position,cs.device_run_id as deviceRunID,ord.order_id as orderID,ord.order_comments as orderComments,cs.container_sample_id as containerSampleId,cs.active_flag as activeFlag,cs.container_type as containerType,cs.created_by as createdBy,cs.created_date_time as createdDateTime,cs.updated_by as updatedBy,cs.updated_date_time as updatedDateTime from container_samples cs,orders ord where lower(cs.accessioning_id)=lower(ord.accessioning_id) and cs.companyid=:domainId and cs.device_run_id=:deviceRunId and cs.active_flag='Y' ",nativeQuery = true)
	List<Object> findAllContainerSamplesByDeviceRunID(@Param("deviceRunId") String deviceRunId, @Param("domainId") long domainId);
	
	@Query("select cs from ContainerSamples cs where cs.deviceRunID=:deviceRunId and cs.company.id=:domainId and cs.activeFlag='Y'")
	List<ContainerSamples> findAllContainerSamplesByDeviceRun(@Param("deviceRunId") String deviceRunId, @Param("domainId") long domainId);
	
	@Query("select distinct cs.containerID as containerID, max(cs.loadID) as loadID from ContainerSamples cs where cs.company.id=:domainId and cs.activeFlag='Y' group by cs.containerID")
	List<Map<String,Object>> findHighestLoadIdByContainer(@Param("domainId") long domainId);
	
	@Query("select cs from ContainerSamples cs where lower(cs.accessioningID)=lower(:accessioningID) and cs.activeFlag='Y'")
	public List<ContainerSamples> findContainerSamplesByAccessioningId(@Param("accessioningID") String accessioningID);
	
	@Query("select cs from ContainerSamples cs where cs.containerID=:containerID and cs.status=:status and cs.company.id=:domainId and cs.activeFlag='Y'")
	public List<ContainerSamples> findByIdAndContainerIDAndStatus(@Param("containerID") String containerID,@Param("status") String status,@Param("domainId") long domainId);
	
	@Query("select cs from ContainerSamples cs where cs.containerID=:containerID and cs.company.id=:domainId and cs.activeFlag='Y' ")
	public List<ContainerSamples> findByContainerID(@Param("containerID") String containerID,@Param("domainId") long domainId);
	
	@Query("select cs from ContainerSamples cs where cs.containerID=:containerID and cs.company.id=:domainId and cs.activeFlag=:activeFlag")
	public List<ContainerSamples> findByContainerIDandActiveFlag(@Param("containerID") String containerID,@Param("activeFlag") String activeFlag,@Param("domainId") long domainId);
}
