package com.roche.connect.common.amm.dto;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ModelTest {
	@Test
	public void assayTypeDTOTest(){
		AssayTypeDTO assayTypeDTO=new AssayTypeDTO();
		assayTypeDTO.setAssayType("NIPTHTP");
		assayTypeDTO.setAssayTypeId(1l);
		assayTypeDTO.setAssayVersion("v0.1");
		assayTypeDTO.setWorkflowDefFile("workflowDefFile");
		assayTypeDTO.setWorkflowDefVersion("workflowDefVersion");
		assertEquals(assayTypeDTO.getAssayType(), "NIPTHTP");
		assertEquals(assayTypeDTO.getAssayTypeId(),new Long(1));
		assertEquals(assayTypeDTO.getAssayVersion(), "v0.1");
		assertEquals(assayTypeDTO.getWorkflowDefFile(), "workflowDefFile");
		assertEquals(assayTypeDTO.getWorkflowDefVersion(), "workflowDefVersion");
	}
	@Test
	public void assayListDataDTOTest(){
		AssayListDataDTO assayListDataDTO=new AssayListDataDTO();
		assayListDataDTO.setAssayType("NIPTHTP");
		assayListDataDTO.setListType("ivf status");
		assayListDataDTO.setValue("Yes");
		assertEquals(assayListDataDTO.getAssayType(), "NIPTHTP");
		assertEquals(assayListDataDTO.getListType(), "ivf status");
		assertEquals(assayListDataDTO.getValue(), "Yes");
	}
	@Test
	public void assayInputDataValidationsDTOTest(){
		AssayInputDataValidationsDTO assayInputDataValidationsDTO=new AssayInputDataValidationsDTO();
		assayInputDataValidationsDTO.setAssayType("NIPTHTP");
		assayInputDataValidationsDTO.setExpression("NA");
		assayInputDataValidationsDTO.setFieldName("Maternal Age");
		assayInputDataValidationsDTO.setMaxVal(100l);
		assayInputDataValidationsDTO.setMinVal(1l);
		assertEquals(assayInputDataValidationsDTO.getAssayType(), "NIPTHTP");
		assertEquals(assayInputDataValidationsDTO.getExpression(), "NA");
		assertEquals(assayInputDataValidationsDTO.getFieldName(), "Maternal Age");
		assertEquals(assayInputDataValidationsDTO.getMaxVal(), Long.valueOf(100));
		assertEquals(assayInputDataValidationsDTO.getMinVal(), Long.valueOf(1));
	}
	@Test
   public void	deviceTestOptionsDTOTest(){
		DeviceTestOptionsDTO deviceTestOptionsDTO=new DeviceTestOptionsDTO();
		deviceTestOptionsDTO.setAssayType("NIPTHTP");
		deviceTestOptionsDTO.setDeviceType("MP24");
		deviceTestOptionsDTO.setProcessStepName("NA Extraction");
		deviceTestOptionsDTO.setTestName("Harmony");
		deviceTestOptionsDTO.setTestOptionId(1l);
		deviceTestOptionsDTO.setTestProtocol("Cfna ss 2000");
		
		assertEquals(deviceTestOptionsDTO.getAssayType(), "NIPTHTP");
		assertEquals(deviceTestOptionsDTO.getDeviceType(), "MP24");
		assertEquals(deviceTestOptionsDTO.getProcessStepName(), "NA Extraction");
		assertEquals(deviceTestOptionsDTO.getTestName(), "Harmony");
		assertEquals(deviceTestOptionsDTO.getTestOptionId(), Long.valueOf(1));
		assertEquals(deviceTestOptionsDTO.getTestProtocol(), "Cfna ss 2000");
		
	}
	@Test
	public void molecularIDTypeDTOTest() {
		MolecularIDTypeDTO molecularIDTypeDTO=new MolecularIDTypeDTO();
		molecularIDTypeDTO.setAssayType("NIPTHTP");
		molecularIDTypeDTO.setAssayTypeId(1l);
		molecularIDTypeDTO.setPlateLocation("A1");
		molecularIDTypeDTO.setPlateType("A");
		molecularIDTypeDTO.setMolecularId("MID1");
		assertEquals(molecularIDTypeDTO.getAssayType(), "NIPTHTP");
		assertEquals(molecularIDTypeDTO.getAssayTypeId(),1l );
		assertEquals(molecularIDTypeDTO.getMolecularId(),"MID1" );
		assertEquals(molecularIDTypeDTO.getPlateLocation(), "A1");
		assertEquals(molecularIDTypeDTO.getPlateType(), "A");
		
	}
	@Test
	public void processStepActionDTOTest() {
		ProcessStepActionDTO processStepActionDTO=new ProcessStepActionDTO();
		processStepActionDTO.setAssayType("NIPTHTP");
		processStepActionDTO.setDeviceType("MP24");
		processStepActionDTO.setEluateVolume("100");
		processStepActionDTO.setInputContainerType("Plasma tube");
		processStepActionDTO.setManualVerificationFlag("Y");
		processStepActionDTO.setOutputContainerType("8-tube strip");
		processStepActionDTO.setProcessStepName("NA Extraction");
		processStepActionDTO.setProcessStepSeq(1);
		processStepActionDTO.setReagent("REAGENT");
		processStepActionDTO.setSampleVolume("200");
		assertEquals(processStepActionDTO.getAssayType(),"NIPTHTP" );
		assertEquals(processStepActionDTO.getDeviceType(),"MP24" );
		assertEquals(processStepActionDTO.getEluateVolume(), "100");
		assertEquals(processStepActionDTO.getInputContainerType(),"Plasma tube" );
		assertEquals(processStepActionDTO.getManualVerificationFlag(),"Y" );
		assertEquals(processStepActionDTO.getOutputContainerType(),"8-tube strip" );
		assertEquals(processStepActionDTO.getProcessStepName(), "NA Extraction");
		assertEquals(processStepActionDTO.getProcessStepSeq(), Integer.valueOf(1));
		assertEquals(processStepActionDTO.getReagent(),"REAGENT");
		assertEquals(processStepActionDTO.getSampleVolume(),"200");
		
	}
	@Test
	public void sampleTypeDTOTest() {
		SampleTypeDTO sampleTypeDTO=new SampleTypeDTO();
		sampleTypeDTO.setAssayType("NIPTHTP");
		sampleTypeDTO.setMaxAgeDays(7);
		sampleTypeDTO.setSampleType("Plasma");
		 assertEquals(sampleTypeDTO.getAssayType(),"NIPTHTP");
		 assertEquals(sampleTypeDTO.getMaxAgeDays(), 7);
		 assertEquals(sampleTypeDTO.getSampleType(), "Plasma");
	}
	
	@Test
	public void testOptionsDTOTest() {
		TestOptionsDTO testOptionsDTO = new TestOptionsDTO();
		testOptionsDTO.setAssayType("NIPTHTP");
		testOptionsDTO.setSequenceId(1);
		testOptionsDTO.setTestDescription("Harmony");
		testOptionsDTO.setTestName("Harmony");
		assertEquals(testOptionsDTO.getAssayType(),"NIPTHTP" );
		assertEquals(testOptionsDTO.getSequenceId(),Integer.valueOf(1) );
		assertEquals(testOptionsDTO.getTestDescription(),"Harmony" );
		assertEquals(testOptionsDTO.getTestName(), "Harmony");
	}

	@Test
	public void	flagsDataDTOTest(){
		FlagsDataDTO	flagsDataDTO=new FlagsDataDTO();
		flagsDataDTO.setAssayType("NIPTHTP");
		flagsDataDTO.setDescription("Run was terminated by fatal error Abort run");
		flagsDataDTO.setDeviceType("dPCR Analyzer");
		flagsDataDTO.setFlagCode("AT001");
		flagsDataDTO.setProcessStepName("dPCR");
		flagsDataDTO.setSeverity("Fatal");
		flagsDataDTO.setSampleOrRunLevel("sample");
		flagsDataDTO.setSource(null);
		assertEquals(flagsDataDTO.getAssayType(),"NIPTHTP");
		assertEquals(flagsDataDTO.getDescription(), "Run was terminated by fatal error Abort run");
		assertEquals(flagsDataDTO.getDeviceType(), "dPCR Analyzer");
		assertEquals(flagsDataDTO.getProcessStepName(), "dPCR");
		assertEquals(flagsDataDTO.getSeverity(),"Fatal");
		assertEquals(flagsDataDTO.getSampleOrRunLevel(),"sample");
		assertEquals(flagsDataDTO.getSource(),null);
		assertEquals(flagsDataDTO.getFlagCode(),"AT001");
	}
}
