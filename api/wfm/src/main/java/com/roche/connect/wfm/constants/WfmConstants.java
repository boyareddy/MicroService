/*******************************************************************************
 * WfmConstants.java Version: 1.0 Authors: somesh_r *********************
 * Copyright (c) 2018 Roche Sequencing Solutions (RSS) - CONFIDENTIAL All Rights
 * Reserved. NOTICE: All information contained herein is, and remains the
 * property of COMPANY. The intellectual and technical concepts contained herein
 * are proprietary to COMPANY and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from COMPANY.
 * Access to the source code contained herein is hereby forbidden to anyone
 * except current COMPANY employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access
 * The copyright notice above does not evidence any actual or intended
 * publication or disclosure of this source code, which includes information
 * that is confidential and/or proprietary, and is a trade secret, of COMPANY.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC
 * DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN
 * CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE
 * CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS TO
 * REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR
 * SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * ********************* ChangeLog: somesh_r@hcl.com : Updated copyright headers
 * ********************* ********************* Description: Constant class
 * implementation to declare enum constants. *********************
 ******************************************************************************/
package com.roche.connect.wfm.constants;

/**
 * Constant class implementation to declare enum constants.
 */
public class WfmConstants {
    
    /**
     * Constants used at workflow level.
     */
    public enum WORKFLOW {
        NIPT("NIPTHTP"), DPCR("NIPTDPCR"), ONCOLOGY("ONCOLOGY");
        
        private final String text;
        
        WORKFLOW(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    } 
    
    /**
     * Response constants used in workflows.
     */
    public enum WORKFLOW_RESPONSE {
        SUCCESS("Success"), SUCCESS_1("SUCCESS"), U03ACK("U03ACK"), RSP("RSP"), ORDEREVENTDATE("orderEventDate"),AR("AR");
        
        private final String text;
        
        WORKFLOW_RESPONSE(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    public enum ERROR_RESPONSE {
        COMMERRORCODE("Common error code"),ERRORPROCESSQRY("WFM-001: Common Error while processing Query"),HMTPEXCEPTIONMSG("HMTPException while processing Query");
        
        private final String text;
        
        ERROR_RESPONSE(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for workflow xml files.
     */
    public enum WfmXMLFiles {
        NIPT_FLOW_XML("NIPT_Assay_Connect_v1.bpmn20.xml"), ONCOLOGY_FLOW_XML("ONCOLOGY_Assay_Connect_v1.bpmn20.xml");
        
        private final String text;
        
        WfmXMLFiles(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for workflow variables.
     */
    public enum WORKFLOW_VARIABLES {
        ACCESSIONINGID("accessioningId"), DEVICEID("deviceId"), MESSAGETYPE("messageType"),
        ACTIVITYPROCESSDATA("activityProcessData"), ORDERDETAILS("OrderDetailsToBatch"),
        CONTAINERID_8TUBE("containerId_8Tube"), ORDERSTATUS("orderStatus"),
        SENDINGAPPLICATIONNAME("sendingApplicationName"), INPUT("input"), PROTOCOLMP24("protocolMP24"),
        ACCESSIONINGIDPREPCR("accessioningIdPrePCR"), TUBECONTAINERIDPREPCR("tubecontaineridPrePCR"),
        TUBECONTAINERPOSITIONPREPCR("tubecontainerpositionPrePCR"), DEVICEIDPREPCR("deviceIdPrePcr"),
        MESSAGETYPEPREPCR("messageTypePrePcr"), SENDINGAPPLICATIONNAMEPREPCR("sendingApplicationNamePrePcr"),
        ORDERDETAILSPREPCRUPDATE("orderDetailsPrePcrUpdate"), ACCESSIONINGIDPOSTPCR("accessioningIdPostPCR"),
        TUBECONTAINERIDPOSTPCR("tubecontaineridPostPCR"), TUBECONTAINERPOSITIONPOSTPCR("tubecontainerpositionPostPCR"),
        DEVICEIDPOSTPCR("deviceIdPostPcr"), MESSAGETYPEPOSTPCR("messageTypePostPcr"),
        SENDINGAPPLICATIONNAMEPOSTPCR("sendingApplicationNamePostPcr"),
        ORDERDETAILSPOSTPCRUPDATE("orderDetailsPostPcrUpdate"), ACCESSIONINGIDSEQPCR("accessioningIdseqPre"),
        TUBECONTAINERMOLSEQPRE("tubecontainermolseqPre"), DEVICEIDSEQPRE("deviceIdseqPre"),
        SENDINGAPPLICATIONNAMESEQPRE("sendingApplicationNameseqPre"), RUNRESULTSIDSEQPRE("runResultsIdSeqPre"),
        ORDERDETAILSSEQPREPUPDATE("orderDetailsSeqPrepUpdate"), ACCESSIONINGIDFORTE("accessioningIdForte"),
        TUBECONTAINERMOLFORTE("tubecontainerForte"), DEVICEIDFORTE("deviceIdForte"),
        MESSAGETYPEFORTE("messageTypeForte"), SENDINGAPPLICATIONNAMEPREPCRUPDATE("sendingApplicationNameprePcrupdate"),
        PROCESSINGID("processingId"),
        PROTOCOLLP24PREPCR("protocolLP24prepcr"), PROTOCOLLP24POSTPCR("protocolLP24postpcr"),
        PROTOCOLLP24SEQPREP("protocolLP24seqprep"), HTP_PROTOCOL("HTP Protocol"),
        SENDINGAPPLICATIONNAMEPREPOSTUPDATE("sendingApplicationNameprePostupdate"),
        CONTAINERIDPREPCRUPDATE("containerIdPrePcrUpdate"), ACCESSIONINGIDPREPCRUPDATE("accessioningIdPrePcrUpdate"),
        DEVICEIDPREPCRUPDATE("deviceidPrePcrUpdate"), NEWCONTAINERPREPCRUPDATE("newcontainerIdPrePcrUpdate"),
        MESSAGETYPEPREPCRUPDATE("mesageTypePrePcrUpdate"), ORDERSTATUSPREPCRUPDATE("orderStatusPrePcrUpdate"),
        CONTAINERIDPOSTPCRUPDATE("containerIdPostPcrUpdate"),ASSAYTYPE("assayType"),SAMPLETYPE("sampleType"),
        ACCESSIONINGIDPOSTPCRUPDATE("accessioningIdPostPcrUpdate"), DEVICEIDPOSTPCRUPDATE("deviceidPostPcrUpdate"),
        NEWCONTAINERPOSTPCRUPDATE("newcontainerIdPostPcrUpdate"), MESSAGETYPEPOSTPCRUPDATE("mesageTypePostPcrUpdate"),
        ORDERSTATUSPOSTPCRUPDATE("orderStatusPostPcrUpdate"), CONTAINERIDSEQPREUPDATE("containerIdSeqPreUpdate"),
        CONTAINERPOSITIONSEQPREUPDATE("containerPositionSeqPreUpdate"),
        ACCESSIONINGIDSEQPREUPDATE("accessioningIdSeqPreUpdate"), DEVICEIDSEQPREUPDATE("deviceidSeqPreUpdate"),
        ORDERDETAILSSEQPREUPDATE("orderDetailsSeqPreUpdate"), MESSAGETYPESEQPREUPDATE("mesageTypeSeqPreUpdate"),
        ORDERSTATUSSEQPREUPDATE("orderStatusSeqPreUpdate"),
        SENDINGAPPLICATIONNAMESEQPREUPDATE("sendingApplicationNameSeqPreupdate"),
        ACCESSIONINGIDHTPUPDATE("accessioningIdHTPUpdate"), DEVICEIDHTPUPDATE("deviceidHTPUpdate"),
        ORDERSTATUSHTPUPDATE("orderStatusHTPUpdate"), SENDINGAPPLICATIONNAMEHTPUPDATE("sendingApplicationHTPupdate"),
        REAGENT2MLTUBE("reagent2mlTube"), REAGENT25MLTUBE("reagent25mlTube"), DC("DC"), NW("NW"),AA("AA"),AR("AR"),DUPLICATE("DUPLICATE"),NF("NF"),
        MP242ML("MagNA Pure 24 2ml"), S("S"), SC("SC"), LP24PRE_PCR("LP24Pre-PCR"), LP24POST_PCR("LP24Post-PCR"),
        PLASMA("Plasma"), LP24PREPCR("LP24PrePcr"), LP24SEQPREP("LP24SeqPrep"), LOTNO("lotNo"), BARCODE("barcode"),
        PROTOCAL("protocal"), REAGENTKITNAME("reagentKitName"), REAGENTVERSION("reagentVesion"),
        RUNSTARTTIME("runStartTime"), RUNENDTIME("runEndTime"), OUTPUTPLATETYPE("outputPlateType"),
        SAMPLEVOLUME("sampleVolume"), ELEVATEVOLUME("elevateVolume"), SOFTWAREVERSION("softwareVersion"),
        SERIALNO("serialNo"), POSITION("position"), VOLUME("volume"), EXPDATE("expDate"),
        ACCESSIONINGIDDPCRLP24("accessioningIddpcrlp24"), ORDERDETAILSDPCRLP24("orderDetailsdpcrlp24"),
        TUBECONTAINERIDDPCRLP24("tubecontaineriddPCRLP24"), TUBECONTAINERPOSITIONDPCRLP24("tubecontainerpositiondPCRLP24"),
        ORDERDETAILSDPCRLPUPDATE("orderDetailsdPCRLPUpdate"),ACCESSIONINGIDDPCR24UPDATE("accessioningIdDPCRLP24Update"),
        DEVICEIDDPCRLP24UPDATE("deviceiddPCRLP24Update"),ORDERSTATUSDPCRLP24UPDATE("orderStatusdPCRLP24Update"),
        PROTOCOLDPCRLP24("protocoldPCRLP24"),DPCRLP24("dPCRLP24"),SYSTEM("System"),PI("PI"),YYYYMMDDHHMMSS("yyyyMMddHHmmss"),ADMIN("admin"),
        LP24NOORDER("LP24-NOORDER"),ANALYZERNOORDER("dPCRAnalyzer-NOORDER"),LP24NOORDERFOUND("NO_ORDER_FOUND_LP24"),ORDERDETAILSDPCRANALYZERUPDATE("orderDetailsdPCRAnalyzerUpdate"),
        ORDERDETAILSDPCRANALYZERORU("orderDetailsdPCRAnalyzerORU"),ORDERDETAILSDPCRANALYZERSTATUS("orderDetailsdPCRAnalyzerStatus"),ACCESSIONINGIDDPCRORU("accessioningIdDPCRORU"),
        DEVICEIDDPCRORU("deviceiddPCRORU"),NAME("name"),PACKAGENAME("packageName"),VERSION("version"),KIT("kit"),QUANTITATIVEVALUE("quantitativeValue"),
        QUANTITATIVERESULT("quantitativeResult"),QUALITATIVEVALUE("qualitativeValue"),QUALITATIVERESULT("qualitativeResult"),
        PLATEID("plateId"),SERIALNUMBER("serialNumber"),FLUIDID("fluidId"),DATEANDTIME("dateandTime");
        
        private final String text;
        
        WORKFLOW_VARIABLES(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declaration for workflow status.
     */
    public enum WORKFLOW_STATUS {

        COMPLETED("Completed"), COMPLETED_WITH_FLAGS("Completed with flags"), QUERY("Query"), INPROGRESS("InProgress"),INPROCESS("InProcess"),TRANSFERCOMPLETED("TransferCompleted"),
        ABORTED("Aborted"),PASSED("Passed"),PASSED_1("passed"),NOORDER("NOORDER"),FAILED("Failed"),DONE("Done"),P("P"),F("F"),PASSEDWITHFLAG("passed with flag"),FLAGGED("flagged"), ERROR("Error"),
        NOTSTARTED("NOT STARTED"),DPCRACK("dPCRACK"),DPCRORU("dPCRORU");


        private final String text;
        
        WORKFLOW_STATUS(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declaration for Order status constants.
     */
    public enum ORDER_STATUS {
        ASSIGNED("Assigned"),INPROGRESS("InProgress"), STARTPLANNED("StartPlanned"),
        START("Start"), ABORTED("Aborted"),FAILED("Failed");
        
        private final String text;
        
        ORDER_STATUS(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declaration for Run status constants
     */
    public enum RUN_STATUS {
        STARTED("Started"), INPROCESS("InProcess"), FAILED("Failed");
        
        private final String text;
        
        RUN_STATUS(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for workflow message type constants.
     */
    public enum WORKFLOW_MESSAGE_TYPE {
        NAEXTRACTION("NA-Extraction"), QBP("QueryMessage"), LP24POSTPCRQBP("QueryMessage"), RSP("RSP"),
        DPCRNAEXTRACTION("NA Extraction"),LP24("LP24"),ANALYZERQBP("QBP"),ANALYZERSSU("ESU"),DPCRANALYZER("dPCR"),ORU("ORU");
        
        private final String text;
        
        WORKFLOW_MESSAGE_TYPE(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for sending application names.
     */
    public enum WORKFLOW_SENDINGAPPLICATIONNAME {
    	MP24("MagnaPure24"), LP24PREPCR("LP-PRE-PCR"), LP24POSTPCR("LP-POST-PCR"), LP24SEQPREP("LP-SEQ-PP"), HTP("HTP"),
        CONNECT("CONNECT"), MP96("Magna Pure 96"), LP24("LP24"),FORTE("Forte"),DPCRANALYZER("dPCR");
        
        private final String text;
        
        WORKFLOW_SENDINGAPPLICATIONNAME(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for workflow signals.
     */
    public enum WORKFLOW_SIGNALS {
        MP24U03("s1"), LP24PREPCRQBP("s2"), LP24PREPCRU03("s3"), LP24POSTPCRQBP("s4"), LP24POSTPCRU03("s5"),
        LP24SEQPREPQBP("s6"), LP24SEQPREPU03("s7"), HTPSTARTED("s8"), HTPINPROCESS("s9"), HTPCOMPLETED("s10"),HTPTRANSFERCOMPLETE("signalTransfer"),
        FORTESECONDARYEVENT("s11"), HTPFAILED("s12"),FORTETERTIARYSTARTEVENT("s13"),FORTETERTIARYINPROGRESSEVENT("s14"),FORTETERTIARYDONEEVENT("s15"),FORTETERTIARYFAILEDEVENT("s16"),REPORTSEVENT("s17");
        
        private final String text;
        
        WORKFLOW_SIGNALS(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    public enum WORKFLOW_dPCR_SIGNALS {
        MP96ACK("s1"), MP96U03("s2"), LP24QBP("s3"),LP24U03("s4"),DPCRANALYZERQBP("s5"),DPCRANALYZERACK("s6"),DPCRANALYZERESU("s7"),DPCRANALYZERFINALESU("s8");
        
        private final String text;
        
        WORKFLOW_dPCR_SIGNALS(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for workflow message types.
     */
    public enum WORKFLOW_MESSAGESOURCE {
        MPSTATUS("MPStatus"), LPQUERY("LPQuery"), LPSTATUS("LPStatus"), HTPSTATUS("HTPStatus"),
        FORTETATUS("FORETEStatus"), MP24UPDATERUNRESULT("MP24UpdateRunResult"),LP24UPDATERUNRESULT("LP24UpdateRunResult");
        
        private final String text;
        
        WORKFLOW_MESSAGESOURCE(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
        
    }
    
    public enum WORKFLOW_dPCR_MESSAGESOURCE {
        MP96STATUS("MP96Status"), ACKMP96("ACKMP96"), LPQUERY("LPQuery"), LPSTATUS("LPStatus"),DPCRANALYZER("AnalyzerQuery"),ACKANALYZER("ACKAnalyzer"), 
        DPCRANALYZERORU("AnalyzerORU"),DPCRSTATUS("AnalyzerStatus");
        
        private final String text;
        
        WORKFLOW_dPCR_MESSAGESOURCE(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
        
    }
    
    /**
     * Enum declarations for process step names.
     */
    public enum ASSAY_PROCESS_STEP_DATA {
        MP24("MP24"), MP96("MP96"), NA_EXTRACTION("NA Extraction"), LP24("LP24"), PREPCR("LP Pre PCR"),
        POSTPCR("LP Post PCR/Pooling"), SEQPREP("LP Sequencing Prep"), HTP("HTP"), FORTE("FORTE"),
        LIBRARYPREPARATION("Library Preparation"), SEQUENCING("Sequencing"), PRIMARY_ANALYSIS("Primary Analysis"),
        SECONDARY_ANALYSIS("Secondary  Analysis"), TERTIARY_ANALYSIS("Tertiary Analysis"),
        NIPT_WORKFLOW_FILE("NIPT_Assay_Connect_v1"), ONCOLOGY_WORKFLOW_FILE("ONCOLOGY_Assay_Connect_v1"),
        DPCR_WORKFLOW_FILE("NIPTDPCR_Wfm_v1"),DPCRANALYZERPROCESSNAME("dPCR"),DPCR_ANALYZER("dPCR Analyzer");
        
        private final String text;
        
        ASSAY_PROCESS_STEP_DATA(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for user manual task.
     */
    public enum ASSAY_USER_MANUAL_TASK {
        Y("Y"), N("N");
        
        private final String text;
        
        ASSAY_USER_MANUAL_TASK(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    /**
     * Enum declarations for API URIs.
     */
    public enum API_URL {
        AMM_API_URL("pas.amm_api_url"), OMM_API_URL("pas.omm_api_url"), RMM_API_URL("pas.rmm_api_url"),
        IMM_API_URL("pas.imm_api_url"),ADM_API_URL("pas.adm_api_url");
        
        private final String text;
        
        API_URL(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
    
    public enum ORDERNOTFOUND_RESPONSE {
        NOORDER("NOORDER"), CONNECT("CONNECT"), MAGANAPURE24("MagnaPure24"), RSP("RSP"), DC("DC");
        private final String text;
        
        ORDERNOTFOUND_RESPONSE(final String text) {
            this.text = text;
        }
        
        @Override public String toString() {
            return text;
        }
    }
}