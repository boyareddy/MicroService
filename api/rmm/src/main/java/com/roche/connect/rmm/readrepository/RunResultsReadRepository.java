/*******************************************************************************
 * 
 * 
 *  RunResultsReadRepository.java                  
 *  Version:  1.0
 * 
 *  Authors:  surapuraju.c
 * 
 * =================================
 *  Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  All Rights Reserved.
 * 
 *  NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 *  The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *                
 * =================================
 *  ChangeLog:
 *   
 * 
 * 
 ******************************************************************************/
package com.roche.connect.rmm.readrepository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.rmm.model.RunResults;

public interface RunResultsReadRepository extends MultiTenantReadRepository<RunResults> {

	RunResults findById(long id);

	// Multi-Tenant @Querys
	
	@Query("select rrs from RunResults rrs where rrs.deviceId=:deviceid and rrs.processStepName=:processstepname and rrs.runStatus=:runstatus and rrs.company.id=:domainId")
    public RunResults findFirstByDeviceIdAndProcessStepNameAndRunStatus(@Param("deviceid")String deviceid, @Param("processstepname")String processstepname,
        @Param("runstatus")String runstatus,@Param("domainId")long domainId);
	
	
	 @Query("select rrs from RunResults rrs where rrs.deviceId=:deviceid and rrs.processStepName=:processstepname and rrs.company.id=:domainId")
	    public RunResults findFirstByDeviceIdAndProcessStepName(@Param("deviceid")String deviceid, @Param("processstepname")String processstepname,
	      @Param("domainId")long domainId);
	
	
	 @Query("SELECT rr FROM RunResults rr WHERE rr.deviceRunId =:deviceRunId and rr.company.id=:domainId")
	 public RunResults findRunResultsByDeviceRunId(@Param("deviceRunId") String deviceRunId,@Param("domainId")long domainId);
	
	 @Query("select rrs from RunResults rrs where rrs.deviceId=:deviceid and rrs.runStatus=:runstatus and rrs.company.id=:domainId")
	 public List<RunResults> findByDeviceIdAndRunStatus(@Param("deviceid")String deviceid,@Param("runstatus")String runstatus,@Param("domainId")long domainId);
	
	 @Query("SELECT rr FROM RunResults rr WHERE rr.deviceRunId =:deviceRunId and rr.processStepName=:processstepname and rr.company.id=:domainId")
	 public List<RunResults> findByDeviceRunIdAndProcessStepName(@Param("deviceRunId") String deviceRunId,@Param("processstepname")String processstepname,@Param("domainId")long domainId);
	 
	 @Query("SELECT rr FROM RunResults rr WHERE rr.deviceRunId =:deviceRunId and rr.processStepName=:processstepname and rr.deviceId=:deviceid and rr.company.id=:domainId")
	 public List<RunResults> findByDeviceRunIdAndProcessStepNameAndDeviceId(@Param("deviceRunId")String deviceRunId, @Param("processstepname")String processStepName,@Param("deviceid")String deviceid,@Param("domainId")long domainId);

	 @Query("select rr.company.id from RunResults rr where rr.id=:runResultId")
	 public long findCompanyIdByRunResultId(@Param("runResultId")long runResultId);
	 
	 @Query("SELECT rr FROM RunResults rr WHERE rr.id =:id and rr.company.id=:domainId")
	 public RunResults findRunResultByRunResultId(@Param("id") long id,@Param("domainId")long domainId);
	 
	 @Query("SELECT rr FROM RunResults rr WHERE rr.processStepName=:processstepname and rr.runStatus =:runStatus and rr.runStartTime <=:runStartTime")
	 public List<RunResults> findRunResultByProcessStepNameByRunStatusAndRunStartTime(@Param("processstepname") String processstepname, @Param("runStatus") String runStatus, @Param("runStartTime") Date runStartTime);

	public Page<RunResults> findByDeviceRunIdContainingIgnoreCaseAndCompanyId(String deviceRunId, long id,
			Pageable pageable);
	
	 @Query("SELECT rr FROM RunResults rr WHERE rr.updatedDateTime between :fromDate and :toDate")
	 public List<RunResults> findCurrentRunResult(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

}
