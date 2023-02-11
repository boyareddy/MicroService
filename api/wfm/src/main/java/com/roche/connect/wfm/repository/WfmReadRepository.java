/*******************************************************************************
 * 
 * 
 *  WfmReadRepository.java                  
 *  Version:  1.0
 * 
 *  Authors:  somesh_r
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
 *   somesh_r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 * 
 * *********************
 * 
 *  Description: WfmReadRepository interface that declares methods to fetch records.
 * 
 * *********************
 * 
 * 
 ******************************************************************************/
package com.roche.connect.wfm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.wfm.model.SampleWFMStates;

/**
 * WfmReadRepository interface that declares methods to fetch records.
 *
 */
public interface WfmReadRepository extends MultiTenantReadRepository<SampleWFMStates> {
		
	/**
	 * Method to fetch sample state by process id.
	 * @param processId
	 * @return record - SampleWFMStates
	 */
	public SampleWFMStates findByProcessId(String processId);
	
	/**
     * Method to fetch sample state by accessioning id and device id.
     * @param accessioningId
     * @param deviceId
     * @return record - SampleWFMStates
     */
	
	@Query("select sws from SampleWFMStates sws where sws.accessioningId=:accessioningId and sws.deviceId=:deviceId and sws.company.id=:domainId")
	public SampleWFMStates findOneByAccessioningIdAndDeviceId(@Param("accessioningId")String accessioningId,@Param("deviceId")String deviceId,@Param("domainId") long domainId);
	
	/**
     * Method to fetch sample state by accessioning id, device id and message type.
     * @param accessioningId
     * @param deviceId
     * @param messageType
     * @return record - SampleWFMStates
     */
	
	@Query("select sws from SampleWFMStates sws where sws.accessioningId=:accessioningId and sws.deviceId=:deviceId and sws.messageType=:messageType and sws.company.id=:domainId")
	public SampleWFMStates findOneByAccessioningIdAndDeviceIdAndMessageType(@Param("accessioningId")String accessioningId,@Param("deviceId")String deviceId,@Param("messageType") String messageType,@Param("domainId") long domainId);
	
	/**
     * Method to fetch sample state by accessioning id.
     * @param accessioningId
     * @return record - SampleWFMStates
     */
	
    @Query("select sws from SampleWFMStates sws where sws.accessioningId=:accessioningId and sws.company.id=:domainId")
    public SampleWFMStates findByAccessioningId(@Param("accessioningId")String accessioningId,@Param("domainId") long domainId);

    /**
     * Method to fetch sample state by accessioning id and message type.
     * @param accessioningId
     * @param messageType
     * @return record - SampleWFMStates
     */
    @Query("select sws from SampleWFMStates sws where sws.accessioningId=:accessioningId and sws.messageType=:messageType and sws.company.id=:domainId")
    public SampleWFMStates findByAccessioningIdAndMessageType(@Param("accessioningId")String accessioningId,@Param("messageType") String messageType,@Param("domainId") long domainId);

    
    @Query("select sws from SampleWFMStates sws where sws.deviceId=:deviceId and sws.currentStatus=:currentStatus and sws.company.id=:domainId")
	public SampleWFMStates findByDviceIdAndCurrentstatus(@Param("deviceId") String deviceId,@Param("currentStatus") String currentStatus,@Param("domainId") long domainId);
}
