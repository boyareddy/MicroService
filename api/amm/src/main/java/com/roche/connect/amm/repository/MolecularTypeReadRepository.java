 /*******************************************************************************
 *  * File Name: MolecularTypeReadRepository.java            
 *  * Version:  1.0
 *  * 
 *  * Authors: Dasari Ravindra
 *  * 
 *  * =========================================
 *  * 
 *  * Copyright (c) 2018  Roche Sequencing Solutions (RSS)  - CONFIDENTIAL
 *  * All Rights Reserved.
 *  * 
 *  * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 *  * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade  secret or copyright law.
 *  * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 *  * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have 
 *  * executed Confidentiality and Non-disclosure agreements explicitly covering such access
 *  * 
 *  * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 *  * information that is confidential and/or proprietary, and is a trade secret, of  COMPANY.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 *  * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 *  * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 *  * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 *  * 
 *  * =========================================
 *  * 
 *  * ChangeLog:
 *  ******************************************************************************/
package com.roche.connect.amm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.core.dataservices.server.repositories.MultiTenantReadRepository;
import com.roche.connect.amm.model.MolecularIDType;

/**
 * The Interface MolecularTypeReadRepository.
 */
public interface MolecularTypeReadRepository extends MultiTenantReadRepository<MolecularIDType>{
    /**
     * Find molecular ids.
     *
     * @param assayType
     *            the assay type
     * @return the list
     */
    @Query("select m from MolecularIDType m where m.assayType =:assayType and m.activeFlag='Y' and m.company.id=:domainId")
    public List<MolecularIDType> findMolecularIds(@Param("assayType") String assayType,@Param("domainId")long domainId);
    /**
     * Find molecular ids.
     *
     * @param assayType
     *            the assay type
     * @param plateType
     *            the plate type
     * @param plateLocation
     *            the plate location
     * @return the list
     */
    @Query("select m from MolecularIDType m where m.assayType =:assayType and m.plateType =:plateType and m.plateLocation =:plateLocation and m.activeFlag='Y' and m.company.id=:domainId")
    public List<MolecularIDType> findMolecularIds(@Param("assayType") String assayType,@Param("plateType") String plateType,@Param("plateLocation") String plateLocation,@Param("domainId")long domainId);
    
    /**
     * Find molecular ID type by plate type and plate location.
     *
     * @param plateType
     *            the plate type
     * @param plateLocation
     *            the plate location
     * @return the list
     */
   
    @Query("select m.molecularId from MolecularIDType m where m.plateType=:plateType and m.plateLocation=:plateLocation and m.company.id=:domainId")
    public List<MolecularIDType> findMolecularIDTypeByPlateTypeAndPlateLocation (@Param("plateType") String plateType,@Param("plateLocation") String plateLocation,@Param("domainId")long domainId);
    
    /**
     * Find molecular ID type by id and plate type.
     *
     * @param assayType
     *            the assay type
     * @param plateType
     *            the plate type
     * @return the list
     */  
    @Query("select m from MolecularIDType m where m.assayType =:assayType and m.plateType =:plateType and m.activeFlag='Y' and m.company.id=:domainId")
    public List<MolecularIDType> findMolecularIDTypeByIdAndPlateType (@Param("assayType") String assayType,@Param("plateType") String plateType,@Param("domainId")long domainId);

    /**
     * Find molecular ID type by id and plate location.
     *
     * @param assayType
     *            the assay type
     * @param plateLocation
     *            the plate location
     * @return the list
     */
    @Query("select m from MolecularIDType m where m.assayType =:assayType and m.plateLocation =:plateLocation and m.activeFlag='Y' and m.company.id=:domainId")
    public List<MolecularIDType> findMolecularIDTypeByIdAndPlateLocation (@Param("assayType") String assayType,@Param("plateLocation") String plateLocation,@Param("domainId")long domainId);

  
    @Query("select m from MolecularIDType m where m.assayType =:assayType and m.activeFlag='Y' and m.company.id=:domainId")
    public List<MolecularIDType> findByAssayType(@Param("assayType") String assayType,@Param("domainId")long domainId);

}
