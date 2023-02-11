/*******************************************************************************
 * ResponseDTO.java                  
 *  Version:  1.0
 * 
 *  Authors:  gosula.r
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
 *   gosula.r@hcl.com : Updated copyright headers
 * 
 * *********************
 * 
 *  Description:
 * 
 * *********************
 ******************************************************************************/
package com.roche.lpcamel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * The Class ResponseDTO.
 *
 * @param <T> the generic type
 */
public class ResponseDTO <T> implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * The Enum Status.
     */
	public enum Status {

		/** The success. */
		SUCCESS,
		/** The failure. */
		FAILURE;
	}
    
    /**
     * Instantiates a new response DTO.
     */
    public ResponseDTO() {
        this.status = Status.SUCCESS;
    }
    
    /** The status. */
    private Status status;
    
    /** The errors. */
    private List<Error> errors = new ArrayList<>();
    
    /**
     * Adds the error.
     *
     * @param code the code
     * @param message the message
     */
    public void addError(String code, String message) {
        addError(new Error(code, message, null));
    }
    
    /**
     * Adds the error.
     *
     * @param code the code
     * @param message the message
     * @param params the params
     */
    public void addError(String code, String message, List<String> params) {
        addError(new Error(code, message, params));
    }
    
    /**
     * Adds the error.
     *
     * @param code the code
     */
    public void addError(String code) {
        addError(new Error(code, "", null));
    }
    
    /**
     * Adds the error.
     *
     * @param error the error
     */
    public void addError(Error error) {
        this.status = Status.FAILURE;
        this.errors.add(error);
    }
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
     * Gets the errors.
     *
     * @return the errors
     */
    public List<Error> getErrors() {
        return errors;
    }
    
    /**
     * Sets the errors.
     *
     * @param errors the new errors
     */
    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
    
    /**
     * Checks if is successful.
     *
     * @return true, if successful
     */
    public boolean isSuccessful() {
        return this.getStatus() == Status.SUCCESS;
    }
    
    /** The response. */
    private T response;
    
    /**
     * Gets the response.
     *
     * @return the response
     */
    public T getResponse() {
        return response;
    }
    
    /**
     * Sets the response.
     *
     * @param t the new response
     */
    public void setResponse(T t) {
        this.response = t;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override public String toString() {
        return "ResponseDTO [status=" + status + ", errors=" + errors + ", response=" + response + "]";
    }
    
   public class Error implements Serializable {
        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 3925331655977411290L;
        private String code;
        private String message;
        private List<String> params;
        
        public Error() {
            super();
        }
        
        public Error(String code, String message, List<String> params) {
            super();
            this.code = code;
            this.message = message;
            this.params = params;
        }
        
        public Error(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public List<String> getParams() {
            return params;
        }
        
        public void setParams(List<String> params) {
            this.params = params;
        }
        
        @Override public String toString() {
            return "Error [code=" + code + ", message=" + message + ", params=" + params + "]";
        }
        
    }
    
}
