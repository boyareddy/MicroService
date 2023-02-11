/*******************************************************************************
 * File Name: PatientSampleReadRepository.java            
 * Version:  1.0
 * 
 * Authors: Ankit Singh
 * 
 * =========================================
 * 
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 * 
 * =========================================
 * 
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.omm.readrepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.omm.model.PatientSamples;

public interface PatientSampleReadRepository extends MultiTenantReadRepository<PatientSamples>{
	
	/**
	 * Finds the all by sample id.
	 *
	 * @param sampleId the sample id
	 * @return List
	 */
	@Query("select ps from PatientSamples ps where ps.sampleId=:sampleId and ps.company.id=:domainId")
    public List<PatientSamples> findAllBySampleId(@Param("sampleId")String sampleId, @Param("domainId")long domainId);
	
	@Query("select ps from PatientSamples ps where ps.sampleId=:sampleId")
    public List<PatientSamples> findPatientDetailsBySampleId(@Param("sampleId")String sampleId);
}
