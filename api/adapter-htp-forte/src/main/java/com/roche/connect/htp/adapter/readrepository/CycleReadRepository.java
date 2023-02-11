/*******************************************************************************
 * CycleWriteRepository.java
 *  Version:  1.0
 *
 *  Authors:  umashankar d
 *
 * *********************
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
 * *********************
 *  ChangeLog:
 *
 *  umashankar-d@hcl.com : Updated copyright headers
 *
 * *********************
 *
 *  Description:
 *
 * *********************
 ******************************************************************************/
package com.roche.connect.htp.adapter.readrepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.htp.adapter.model.Cycle;

/**
 * The Interface CycleWriteRepository.
 */
@Repository
public interface CycleReadRepository extends SimpleReadRepository<Cycle> {

	/**
	 * Find by run id.
	 *
	 * @param runId the run id
	 * @return the list
	 */
	@Query("select c from Cycle c where c.runId =:runId and c.company.id=:company")
	public List<Cycle> findByRunId( @Param("runId")String runId, @Param("company")long company);
	
	@Query(nativeQuery = true,
            value = "SELECT * FROM HTP_CYCLE a WHERE a.DEVICE_RUN_ID = :runId and a.COMPANYID = :company LIMIT 1")
	public Cycle findTopByRunId(@Param("runId")String runId, @Param("company")long company);
	
	@Query("select c from Cycle c where c.runId =:runId and c.path=:fileType and c.company.id=:company")
	public List<Cycle> findByRunIdAndType(@Param("runId")String runId, @Param("fileType")String fileType, @Param("company")long company);
	
	@Query("select c from Cycle c where c.runId =:runId and c.cyclesNumber=:cyclesNumber and c.company.id=:company")
	public List<Cycle> findByRunIdAndCyclesNumber(@Param("runId")String runID, @Param("cyclesNumber")int cyclesNumber, @Param("company")long company);
	
	@Query(nativeQuery = true,
            value = "SELECT * FROM HTP_CYCLE a WHERE a.STATUS = :status and a.COMPANYID = :company and a.SENT_TO_SECONDARY = :sendToForteFlag and a.FILE_TYPE = :type LIMIT 1")
	public Cycle findTopByStatusAndSendToSecondaryFlagAndTypeOrderByUpdatedDateTimeAsc(@Param("status")String status,
			@Param("sendToForteFlag")String sendToForteFlag, @Param("type")String type, @Param("company")long company);

	@Query("select c from Cycle c where c.runId =:runId and c.type =:type and (c.sendToSecondaryFlag !=:sentToSecondary or c.transferComplete !=:transferComplete) and c.company.id=:company")
	public List<Cycle> findCycleForTertiary(@Param("runId") String runId,
			@Param("sentToSecondary") String sentToSecondary, @Param("transferComplete") String transferComplete,
			@Param("type") String type, @Param("company")long company);
}
