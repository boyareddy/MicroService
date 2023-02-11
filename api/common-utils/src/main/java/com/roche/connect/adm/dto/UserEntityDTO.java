/*******************************************************************************
*
* UserEntityDTO.java                  
* Version:  1.0
*
* Authors:  somesh_r
*
*********************
*
* Copyright (c) 2019  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
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
*   somesh_r@hcl.com Feb 18, 2019: Updated copyright headers
*
**********************
*
* TODO: 
*
**********************
*
* Description: write me
*
**********************
* 
* 
******************************************************************************/

package com.roche.connect.adm.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserEntityDTO {
	@JsonSerialize
	private UserDetailsDTO userDetails;
	private String password;
	public UserDetailsDTO getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetailsDTO userDetails) {
		this.userDetails = userDetails;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}