/*******************************************************************************
 * 
 * 
 *  MessageTemplateReadRepository.java                  
 *  Version:  1.0
 * 
 *  Authors:  dineshj
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

package com.roche.connect.adm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.adm.model.MessageTemplate;

public interface MessageTemplateReadRepository extends SimpleReadRepository<MessageTemplate>{
	
	@Query("SELECT mt FROM MessageTemplate mt WHERE mt.messageGroup = :messageGroup and mt.activeFlag = 'Y' and mt.locale = :locale and mt.company.id=:domainId")
	public List<MessageTemplate> findMessageTemplate(@Param("messageGroup") String messageGroup, @Param("locale") String locale,@Param("domainId")long domainId);
	
	
	@Query("SELECT mt FROM MessageTemplate mt where mt.company.id=:domainId")
	public List<MessageTemplate> findMessageTemplates(@Param("domainId")long domainId);
	
	
	@Query("SELECT distinct mt.module FROM MessageTemplate mt where mt.company.id=:domainId order by mt.module asc")
	public List<String> findModules(@Param("domainId")long domainId);
	
}
