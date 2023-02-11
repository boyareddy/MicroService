/*******************************************************************************
 * File Name: OrderReadRepository.java            
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
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.omm.model.Order;

public interface OrderReadRepository extends MultiTenantReadRepository<Order> {

	/**
	 * Finds the orders.
	 *
	 * @return List
	 * Finds the workflow orders.
	 *
	 * @return List
	 * Finds the all by accessioning id.
	 *
	 * @param accessioningId the accessioning id
	 * @return List
	 * Finds the patient id by order id.
	 *
	 * @param orderId the order id
	 * @return Long
	
	/**
	 * Finds the orders.
	 *
	 * @return List
	 */
	@Query("select o.id as orderId, o.patient.id as patientId,"
			+ " o.patientSampleId as patientSampleId, o.accessioningId as accessioningId,"
			+ " o.orderStatus as orderStatus, o.assayType as assayType, o.orderComments as orderComments,o.reqFieldMissingFlag as reqFieldMissingFlag,"
			+ " o.priority as priority, o.createdDateTime as createdDateTime, o.activeFlag as activeFlag,"
			+ " o.createdBy as createdBy, o.updatedBy as updatedBy, o.updatedDateTime as updatedDateTime, "
			+ "ps.sampleType as sampleType from Order o, PatientSamples ps where o.patientSampleId = ps.id and "
			+ "o.company.id=:domainId and o.activeFlag='Y' and o.orderStatus='unassigned' order by o.updatedDateTime desc")
	public List<Map> findOrders(@Param("domainId") long domainId);
	
	/**
	 * Finds the unassigned orders count.
	 *
	 * @return List
	 */
	@Query("select count(*) from Order o, PatientSamples ps where o.patientSampleId = ps.id and "
			+ "o.company.id=:domainId and o.activeFlag='Y' and o.orderStatus='unassigned'")
	public Long findUnassignedOrdersCount(@Param("domainId") long domainId);

	/**
	 * Finds the workflow orders.
	 *
	 * @return List
	 */
	@Query("select o.id as orderId, o.patient.id as patientId,"
			+ " o.patientSampleId as patientSampleId, o.accessioningId as accessioningId,"
			+ " o.orderStatus as orderStatus, o.assayType as assayType, o.orderComments as orderComments,"
			+ " o.priority as priority, o.createdDateTime as createdDateTime, o.activeFlag as activeFlag,"
			+ " o.createdBy as createdBy, o.updatedBy as updatedBy, o.updatedDateTime as updatedDateTime,"
			+ " ps.sampleType as sampleType from Order o, PatientSamples ps where o.patientSampleId = ps.id and o.company.id=:domainId"
			+ " AND o.activeFlag='Y' and o.orderStatus='In workflow' order by o.updatedDateTime desc")
	public List<Map> findWorkflowOrders(@Param("domainId") long domainId);

	/**
	 * Finds the all by accessioning id.
	 *
	 * @param accessioningId
	 *            the accessioning id
	 * @return List
	 */
	@Query("select o from Order o where lower(o.accessioningId) =lower(:accessioningId) and o.company.id=:domainId ")
	public List<Order> findAllByAccessioningId(@Param("accessioningId") String accessioningId,
			@Param("domainId") long domainId);
	
	
	@Query("select o from Order o where lower(o.accessioningId) =lower(:accessioningId)")
	public List<Order> findOrderDetailsByAccessioningId(@Param("accessioningId") String accessioningId);

	/**
	 * Finds the patient id by order id.
	 *
	 * @param orderId
	 *            the order id
	 * @return Long
	 */
	@Query("select o.patient.id as patientId from Order o where o.activeFlag='Y' and o.id=:orderId")
	public Long findPatientIdByOrderId(@Param("orderId") Long orderId);

	@Query("select o from Order o where o.id=:orderId and o.company.id=:domainId")
	public Order findOrderByOrderId(@Param("orderId") long orderId, @Param("domainId") long domainId);
	
	@Query("select o from Order o where o.company.id=:domainId ")
	public List<Order> findAllOrders(@Param("domainId") long domainId);

	public Page<Order> findByAccessioningIdContainingIgnoreCaseAndOrderStatusAndCompanyId(String accessioningId,
			String orderStatus, long id, Pageable pageable);

}
