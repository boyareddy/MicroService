 /******************************************************************************
   *UniqueReagentsAndConsumables.java                  
   * Version:  1.0
   *
   * Authors:  Dinesh J
   *
   **********************
   * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
   * All Rights Reserved.
   *
   * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
   * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
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
   **********************
   * ChangeLog:
   *
 ******************************************************************************/
package com.roche.connect.common.rmm.dto;

public class UniqueReagentsAndConsumables {
	
	private String attributeName;
	private String attributeValue;
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result + ((attributeValue == null) ? 0 : attributeValue.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UniqueReagentsAndConsumables other = (UniqueReagentsAndConsumables) obj;
		if (attributeName == null) {
			if (other.attributeName != null) {
				return false;
			}
		} else if (!attributeName.equals(other.attributeName)) {
			return false;
		}
		if (attributeValue == null) {
			if (other.attributeValue != null) {
				return false;
			}
		} else if (!attributeValue.equals(other.attributeValue)) {
			return false;
		}
		return true;
	}
	
	

}
