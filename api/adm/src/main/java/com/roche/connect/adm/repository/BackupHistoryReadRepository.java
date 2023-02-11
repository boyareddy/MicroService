package com.roche.connect.adm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.adm.model.BackupHistory;
@Repository
public interface BackupHistoryReadRepository extends SimpleReadRepository<BackupHistory>{
	
	@Query(nativeQuery = true,
            value = "select * from BACKUP_HISTORY a  order by a.CREATED_DATE_TIME desc LIMIT 1")
	public BackupHistory findFirstByOrderBycreatedDateTimeDesc();
	
	@Query("SELECT a FROM BackupHistory a where a.status =:status")
	public List<BackupHistory> findByStatus(@Param("status")String status);

	
}
