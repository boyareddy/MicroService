/*******************************************************************************
 * RunResultsSpecifications.java                  
 *  Version:  1.0
 * 
 * Authors: prasant.sahoo
 * 
 * ==================================
 * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 * All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  executed Confidentiality and Non-disclosure agreements explicitly covering such access
 * 
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART. 
 *  ==================================
 * ChangeLog:
 ******************************************************************************/
package com.roche.connect.rmm.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.roche.connect.rmm.model.SampleResults;

public class RunResultsSpecifications {
	private RunResultsSpecifications() {
	}

	public static Specification<SampleResults> getSampleResults(String deviceId, String processStepName,
			String outputContainerId, String inputContainerId, String inputContainerPosition,
			String outputContainerPosition, String accessioningId) {

		return (Root<SampleResults> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (outputContainerId != null) {
				predicates.add(cb.equal(root.get("outputContainerId"), outputContainerId));
			}

			if (inputContainerId != null) {
				predicates.add(cb.equal(root.get("inputContainerId"), inputContainerId));
			}

			if (inputContainerPosition != null) {
				predicates.add(cb.equal(root.get("inputContainerPosition"), inputContainerPosition));
			}

			if (outputContainerPosition != null) {
				predicates.add(cb.equal(root.get("outputContainerPosition"), outputContainerPosition));
			}
			
			if (accessioningId != null) {
				predicates.add(cb.equal(root.get("accesssioningId"), accessioningId));
			}
			
			if (deviceId != null) {
				predicates.add(cb.equal(root.join("runResultsId").get("deviceId"), deviceId));
			}

			if (processStepName != null) {
				predicates.add(cb.equal(root.join("runResultsId").get("processStepName"), processStepName));
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));

		};
	}
}
