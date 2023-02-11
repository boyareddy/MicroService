package com.roche.connect.rmm.testNG;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.connect.rmm.jasper.dto.DeviceRunResultsDTO;

public class JasperDTOTest {
	
	@Test
	public void deviceRunResultsTest() throws JsonParseException, JsonMappingException, IOException {
		FileReader fr = new FileReader(new File("src/test/java/Resource/workflowreportDPCR.json"));
		ObjectMapper mapper = new ObjectMapper();
		DeviceRunResultsDTO deviceRunResults = mapper.readValue(fr, DeviceRunResultsDTO.class);
		
		ObjectMapper mapper1 = new ObjectMapper();
		mapper1.writeValue(new File("src/test/java/Resource/workflowreportDPCR.json"), deviceRunResults);
		
		/*Assert.assertEquals(deviceRunResults.getAssayType(),null);
		Assert.assertEquals(deviceRunResults.getComments(),null);
		Assert.assertEquals(deviceRunResults.getQcResult(),null);
		Assert.assertEquals(deviceRunResults.getSamplesPassedQC(),null);
		Assert.assertEquals(deviceRunResults.getCompletionDate(),null);
		Assert.assertEquals(deviceRunResults.getNumOfSamples(),new Long(1L));
		Assert.assertEquals(deviceRunResults.getDpcrData(),deviceRunResults.getDpcrData());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData(),deviceRunResults.getLibraryPrepData());
		Assert.assertEquals(deviceRunResults.getNaExtractionData(),deviceRunResults.getNaExtractionData());
		Assert.assertEquals(deviceRunResults.getQcResultsDTO(),deviceRunResults.getQcResultsDTO());
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getAssayType(),deviceRunResults.getNaExtractionData().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getComments(),deviceRunResults.getNaExtractionData().get(0).getComments());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getDeviceId(),deviceRunResults.getNaExtractionData().get(0).getDeviceId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getDeviceRunId(),deviceRunResults.getNaExtractionData().get(0).getDeviceRunId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getDvcRunResult(),deviceRunResults.getNaExtractionData().get(0).getDvcRunResult());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getEluateVolume(),deviceRunResults.getNaExtractionData().get(0).getEluateVolume());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getOperatorId(),deviceRunResults.getNaExtractionData().get(0).getOperatorId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getOperatorName(),deviceRunResults.getNaExtractionData().get(0).getOperatorName());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getOutputFormat(),deviceRunResults.getNaExtractionData().get(0).getOutputFormat());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getPlateId(),deviceRunResults.getNaExtractionData().get(0).getPlateId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getProcessStepName(),deviceRunResults.getNaExtractionData().get(0).getProcessStepName());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getProtocolName(),deviceRunResults.getNaExtractionData().get(0).getProtocolName());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getRunFlag(),deviceRunResults.getNaExtractionData().get(0).getRunFlag());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getRunStatus(),deviceRunResults.getNaExtractionData().get(0).getRunStatus());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getSampleVolume(),deviceRunResults.getNaExtractionData().get(0).getSampleVolume());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getTotalSamplecount(),deviceRunResults.getNaExtractionData().get(0).getTotalSamplecount());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getTotalSampleFailedCount(),deviceRunResults.getNaExtractionData().get(0).getTotalSampleFailedCount());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getTotalSampleFlagCount(),deviceRunResults.getNaExtractionData().get(0).getTotalSampleFlagCount());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getWfmsflag(),deviceRunResults.getNaExtractionData().get(0).getWfmsflag());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getCompletionTime(),deviceRunResults.getNaExtractionData().get(0).getCompletionTime());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getSamples(),deviceRunResults.getNaExtractionData().get(0).getSamples());
		
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getBarcode(),deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getBarcode());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getProductionLot(),deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getProductionLot());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getVolume(),deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getVolume());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getExpirationDate(),deviceRunResults.getNaExtractionData().get(0).getMp96Consumables().get(0).getExpirationDate());
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Flags().get(0).getDescription(),deviceRunResults.getNaExtractionData().get(0).getMp96Flags().get(0).getDescription());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Flags().get(0).getFlagId(),deviceRunResults.getNaExtractionData().get(0).getMp96Flags().get(0).getFlagId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96Flags().get(0).getSeverity(),deviceRunResults.getNaExtractionData().get(0).getMp96Flags().get(0).getSeverity());
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96reagents().get(0).getName(),deviceRunResults.getNaExtractionData().get(0).getMp96reagents().get(0).getName());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96reagents().get(0).getVersion(),deviceRunResults.getNaExtractionData().get(0).getMp96reagents().get(0).getVersion());
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96sampleComments().get(0).getSampleId(),deviceRunResults.getNaExtractionData().get(0).getMp96sampleComments().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96sampleComments().get(0).getComment(),deviceRunResults.getNaExtractionData().get(0).getMp96sampleComments().get(0).getComment());
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getPlateId(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getPlateId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getAssayType(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getFlags(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getPosition(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getQualitativeResult(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getQuantitativeResult(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getSampleId(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getStatus(),deviceRunResults.getNaExtractionData().get(0).getMp96samplesData().get(0).getSampleDetails().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getAssayType(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getFlags(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getPosition(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getQualitativeResult(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getQuantitativeResult(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getSampleId(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getStatus(),deviceRunResults.getNaExtractionData().get(0).getMpsampleDetails().get(0).getStatus());
		
		
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getComments(),deviceRunResults.getLibraryPrepData().get(0).getComments());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getDeviceId(),deviceRunResults.getLibraryPrepData().get(0).getDeviceId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getDeviceRunId(),deviceRunResults.getLibraryPrepData().get(0).getDeviceRunId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getDvcRunResult(),deviceRunResults.getLibraryPrepData().get(0).getDvcRunResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getEluateVolume(),deviceRunResults.getLibraryPrepData().get(0).getEluateVolume());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getOperatorId(),deviceRunResults.getLibraryPrepData().get(0).getOperatorId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getOperatorName(),deviceRunResults.getLibraryPrepData().get(0).getOperatorName());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getOutputFormat(),deviceRunResults.getLibraryPrepData().get(0).getOutputFormat());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getProcessStepName(),deviceRunResults.getLibraryPrepData().get(0).getProcessStepName());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getProtocolName(),deviceRunResults.getLibraryPrepData().get(0).getProtocolName());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getRunFlag(),deviceRunResults.getLibraryPrepData().get(0).getRunFlag());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getRunStatus(),deviceRunResults.getLibraryPrepData().get(0).getRunStatus());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getSampleVolume(),deviceRunResults.getLibraryPrepData().get(0).getSampleVolume());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getTotalSamplecount(),deviceRunResults.getLibraryPrepData().get(0).getTotalSamplecount());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getTotalSampleFailedCount(),deviceRunResults.getLibraryPrepData().get(0).getTotalSampleFailedCount());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getTotalSampleFlagCount(),deviceRunResults.getLibraryPrepData().get(0).getTotalSampleFlagCount());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getWfmsflag(),deviceRunResults.getLibraryPrepData().get(0).getWfmsflag());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getCompletionTime(),deviceRunResults.getLibraryPrepData().get(0).getCompletionTime());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getSamples(),deviceRunResults.getLibraryPrepData().get(0).getSamples());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getRunResultId(),deviceRunResults.getLibraryPrepData().get(0).getRunResultId());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpFlags().get(0).getDescription(),deviceRunResults.getLibraryPrepData().get(0).getLpFlags().get(0).getDescription());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpFlags().get(0).getFlagId(),deviceRunResults.getLibraryPrepData().get(0).getLpFlags().get(0).getFlagId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpFlags().get(0).getSeverity(),deviceRunResults.getLibraryPrepData().get(0).getLpFlags().get(0).getSeverity());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpReagentsAndConsumables().get(0).getLotNumber(),deviceRunResults.getLibraryPrepData().get(0).getLpReagentsAndConsumables().get(0).getLotNumber());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpReagentsAndConsumables().get(0).getName(),deviceRunResults.getLibraryPrepData().get(0).getLpReagentsAndConsumables().get(0).getName());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpReagentsAndConsumables().get(0).getExpiryDate(),deviceRunResults.getLibraryPrepData().get(0).getLpReagentsAndConsumables().get(0).getExpiryDate());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpSampleComments().get(0).getComment(),deviceRunResults.getLibraryPrepData().get(0).getLpSampleComments().get(0).getComment());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpSampleComments().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpSampleComments().get(0).getSampleId());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId1List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId2List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId3List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId4List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId5List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId6List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId7List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId8List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId9List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId10List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId11List().get(0).getStatus());
		
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getAssayType(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getFlags(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getPosition(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getQualitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getQuantitativeResult(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getSampleId(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getStatus(),deviceRunResults.getLibraryPrepData().get(0).getLpsamplesData().get(0).getPlateId12List().get(0).getStatus());
		
		
		
		
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getAssayType(),deviceRunResults.getDpcrData().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getComments(),deviceRunResults.getDpcrData().get(0).getComments());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDeviceRunId(),deviceRunResults.getDpcrData().get(0).getDeviceRunId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getEluateVolume(),deviceRunResults.getDpcrData().get(0).getEluateVolume());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getOperatorId(),deviceRunResults.getDpcrData().get(0).getOperatorId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getOutputFormat(),deviceRunResults.getDpcrData().get(0).getOutputFormat());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getProcessStepName(),deviceRunResults.getDpcrData().get(0).getProcessStepName());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getProtocolName(),deviceRunResults.getDpcrData().get(0).getProtocolName());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getRunFlag(),deviceRunResults.getDpcrData().get(0).getRunFlag());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getSampleVolume(),deviceRunResults.getDpcrData().get(0).getSampleVolume());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getTotalSamplecount(),deviceRunResults.getDpcrData().get(0).getTotalSamplecount());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getCompletionTime(),deviceRunResults.getDpcrData().get(0).getCompletionTime());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getSamples(),deviceRunResults.getDpcrData().get(0).getSamples());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getRunResultId(),deviceRunResults.getDpcrData().get(0).getRunResultId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getOutputFilePath(),deviceRunResults.getDpcrData().get(0).getOutputFilePath());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getPlateId(),deviceRunResults.getDpcrData().get(0).getPlateId());
		
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrFlags().get(0).getDescription(),deviceRunResults.getDpcrData().get(0).getDpcrFlags().get(0).getDescription());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrFlags().get(0).getFlagId(),deviceRunResults.getDpcrData().get(0).getDpcrFlags().get(0).getFlagId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrFlags().get(0).getSeverity(),deviceRunResults.getDpcrData().get(0).getDpcrFlags().get(0).getSeverity());
		
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrReagents().get(0).getPartioningFluidId(),deviceRunResults.getDpcrData().get(0).getDpcrReagents().get(0).getPartioningFluidId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrReagents().get(0).getPatitioningSerialNo(),deviceRunResults.getDpcrData().get(0).getDpcrReagents().get(0).getPatitioningSerialNo());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrReagents().get(0).getPartioningTime(),deviceRunResults.getDpcrData().get(0).getDpcrReagents().get(0).getPartioningTime());
		
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrSampleComments().get(0).getComment(),deviceRunResults.getDpcrData().get(0).getDpcrSampleComments().get(0).getComment());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrSampleComments().get(0).getSampleId(),deviceRunResults.getDpcrData().get(0).getDpcrSampleComments().get(0).getSampleId());
		
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getPlateId(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getPlateId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getAssayType(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getAssayType());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getFlags(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getFlags());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getPosition(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getPosition());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getQualitativeResult(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getQualitativeResult());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getQuantitativeResult(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getQuantitativeResult());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getSampleId(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getSampleId());
		Assert.assertEquals(deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getStatus(),deviceRunResults.getDpcrData().get(0).getDpcrsamplesData().get(0).getSampleDetails().get(0).getStatus());
		*/
		
	}
}
