/*******************************************************************************
 * RunService.java                  
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
package com.roche.connect.htp.adapter.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.constant.NotificationGroupType;
import com.roche.connect.common.forte.SecondaryJobDetailsDTO;
import com.roche.connect.common.forte.SecondarySampleAssayDetails;
import com.roche.connect.common.forte.TertiaryJobDetailsDTO;
import com.roche.connect.common.htp.ComplexIdDetailsDTO;
import com.roche.connect.common.rmm.dto.RunResultsDTO;
import com.roche.connect.htp.adapter.model.Cycle;
import com.roche.connect.htp.adapter.model.ForteJob;

/**
 * The Interface RunService.
 */
public interface RunService {

    /**
     * Convert json to run.
     *
     * @param json the json
     * @return the run results DTO
     * @throws ParseException the parse exception
     */
    public RunResultsDTO convertJsonToRun(Map<String, Object> json) throws ParseException;

    public Map<String, Object> getOrderDetails(long l, ComplexIdDetailsDTO complexIdDetailsDTO);

    /**
     * get the complex id details from IMM.
     *
     * @param json the json
     * @return the run ComplexIdDetailsDTO
     */
    public Response getComplexIdDetails(String complexId);

    /**
     * Creates the run.
     *
     * @param run the run
     * @throws HMTPException the HMTP exception
     */
    public void createRun(RunResultsDTO run) throws HMTPException;

    /**
     * Update run.
     *
     * @param id  the id
     * @param run the run
     * @return
     * @throws HMTPException           the HMTP exception
     * @throws JsonProcessingException the json processing exception
     */
    public Response updateRun(String id, RunResultsDTO run) throws HMTPException, JsonProcessingException;

    /**
     * Check free space.
     *
     * @return the json node
     * @throws JSONException the JSON exception
     * @throws IOException   Signals that an I/O exception has occurred.
     */
    public JsonNode checkFreeSpace() throws JSONException, IOException;

    /**
     * Update cycle.
     *
     * @param runObject the run object
     * @param id        the id
     * @return the cycle
     */
    public Cycle updateCycle(Map<String, Object> runObject, String id);

    /**
     * Check sum.
     *
     * @param path     the path
     * @param checkSum the check sum
     * @return the boolean
     * @throws IOException Signals that an I/O exception has occurred.
     */
   /** public Boolean checkSum(String path, String checkSum) throws IOException;*/

    public Response getRun(String runId);

    public Map<String, String> convertRunToJson(RunResultsDTO runResultsDTO);

    public void updateRunStatus(String id, RunResultsDTO runResultsDTO);

    public RunResultsDTO convertRunStatusToRun(Map<String, Object> requestbody);

    public String getMountPath(String deviceId, String path);

    public Response getPatientDetailsFromIMM(String htpComplexId);

    public SecondaryJobDetailsDTO getPatientDetails(String htpComplexId, Cycle cycle, String string);

    public ForteJob getForteJob(Cycle cycle);

    public Response updateJobStatusToIMM(String done, String secondary, String complexId);

    public List<Map<String, String>> getSampleDetails(List<SecondarySampleAssayDetails> secondarySampleDetails);

    public List<ForteJob> getForteJobDetailsForTertiary();

    public TertiaryJobDetailsDTO getTertiaryJobDetails() throws HMTPException;

    public SecondaryJobDetailsDTO getSecondaryJobDetails() throws HMTPException;

    public ForteJob createForteJobFromTertiaryJobDetails(String deviceId);
    public String getFolder(String path);

    public void validatePreviousCycle(String runId, int cycleNumber, long domainId, String deviceId);

    public void sendNotification(NotificationGroupType messageGroup,String... missingCycle);

    public Predicate<String> isValidType();
    public boolean checkFileSize(String path);

    public String getDeviceId(long userId);
    
    boolean isInValidRunSequence(String runId, String actualRunStatus, long domainId);

    void sendNotification(String param1, String param2, NotificationGroupType type);
    
    public Boolean validateChecksum(String path, String inputChecksum);

    
}