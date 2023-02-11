/*******************************************************************************
*
* UserDetailsDTO.java                  
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
******************************************************************************/

package com.roche.connect.adm.dto;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserDetailsDTO {
	
	private String loginName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	@JsonSerialize
	private Collection<UserPreferencesDTO> userPreferences = new ArrayList<>();
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Collection<UserPreferencesDTO> getUserPreferences() {
		return userPreferences;
	}
	public void setUserPreferences(Collection<UserPreferencesDTO> userPreferences) {
		this.userPreferences = userPreferences;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}