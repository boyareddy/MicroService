package com.roche.connect.amm.testNG;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hmtp.common.server.entity.Company;
import com.roche.connect.amm.model.AssayInputDataValidations;
import com.roche.connect.amm.model.AssayListData;
import com.roche.connect.amm.model.AssayType;
import com.roche.connect.amm.model.DeviceTestOptions;
import com.roche.connect.amm.model.FlagsData;
import com.roche.connect.amm.model.MolecularIDType;
import com.roche.connect.amm.model.ProcessStepAction;
import com.roche.connect.amm.model.SampleType;
import com.roche.connect.amm.model.TestOptions;

public class ModelTest {
	
	@Test
	public void assayTypeTest() throws JsonParseException, JsonMappingException, IOException {
		FileReader fr = new FileReader(new File("src/test/java/Resource/AssayType.json"));
		ObjectMapper mapper = new ObjectMapper();
		AssayType assayType = mapper.readValue(fr, AssayType.class);
		System.out.println(mapper.writeValueAsString(assayType));
		assayType.setEditedBy("admin");
		assayType.setCreatedBy("admin");
		assayType.setCreatedDate(new Date());
		assayType.setModifiedDate(new Date());
		assayType.setFlagslistData(Arrays.asList(new FlagsData()));
		Assert.assertEquals(assayType.getActiveFlag(), "Y");
		Assert.assertEquals(assayType.getSamples().size(), 1);
		Assert.assertEquals(assayType.getProcessStepActions().size(), 1);
		Assert.assertEquals(assayType.getAssayListData().size(), 1);
		Assert.assertEquals(assayType.getTestOptions().size(), 1);
		Assert.assertEquals(assayType.getAssayInputValidations().size(), 1);
		Assert.assertEquals(assayType.getSamples().stream().findFirst().get().getActiveFlag(), "Y");
		Assert.assertEquals(assayType.getProcessStepActions().stream().findFirst().get().getActiveFlag(), "Y");
		Assert.assertEquals(assayType.getAssayListData().stream().findFirst().get().getActiveFlag(), "Y");
		Assert.assertEquals(assayType.getTestOptions().stream().findFirst().get().getActiveFlag(), "Y");
		Assert.assertEquals(assayType.getAssayInputValidations().stream().findFirst().get().getActiveFlag(), "Y");
		Assert.assertEquals(assayType.getSamples().stream().findFirst().get().getAssay(), null);
		Assert.assertEquals(assayType.getProcessStepActions().stream().findFirst().get().getAssay(), null);
		Assert.assertEquals(assayType.getAssayListData().stream().findFirst().get().getAssay(), null);
		Assert.assertEquals(assayType.getTestOptions().stream().findFirst().get().getAssay(), null);
		Assert.assertEquals(assayType.getAssayInputValidations().stream().findFirst().get().getAssay(), null);
		Assert.assertEquals(assayType.getSamples().stream().distinct().findFirst().get().getId(), 1);
		Assert.assertEquals(assayType.getProcessStepActions().stream().distinct().findFirst().get().getId(), 0);
		Assert.assertEquals(assayType.getAssayListData().stream().distinct().findFirst().get().getId(), 0);
		Assert.assertEquals(assayType.getTestOptions().stream().distinct().findFirst().get().getId(), 0);
		Assert.assertEquals(assayType.getAssayInputValidations().stream().distinct().findFirst().get().getId(),0);
	}
	@Test
	public void deviceTestOptionsTest() throws IOException {
		DeviceTestOptions deviceTestOptions=new DeviceTestOptions();
		deviceTestOptions.setId(0);
		deviceTestOptions.setActiveFlag("Y");
		deviceTestOptions.setTestOptionId(1l);
		deviceTestOptions.setCreatedBy("admin");
		deviceTestOptions.setEditedBy("admin");
		deviceTestOptions.setCreatedDate(new Date());
		deviceTestOptions.setModifiedDate(new Date());
		assertNotNull(deviceTestOptions.getCreatedDate());
		assertNotNull(deviceTestOptions.getModifiedDate());
		assertEquals(deviceTestOptions.getCreatedBy(),"admin" );
		assertEquals(deviceTestOptions.getEditedBy(), "admin");
		Assert.assertEquals(deviceTestOptions.getActiveFlag(),"Y");
		Assert.assertEquals(deviceTestOptions.getId(),0);
		Assert.assertEquals(deviceTestOptions.getAssay(),null);
		Assert.assertEquals(deviceTestOptions.getTestOptionId(), new Long("1"));
		
	}
	@Test
	public void MolecularIDTypeTest() throws IOException {
		MolecularIDType molecularIDType=new MolecularIDType();
		molecularIDType.setId(1);
		molecularIDType.setActiveFlag("Y");
		molecularIDType.setCreatedBy("admin");
		molecularIDType.setEditedBy("admin");
		molecularIDType.setCreatedDate(new Date());
		molecularIDType.setModifiedDate(new Date());
		assertNotNull(molecularIDType.getCreatedDate());
		assertNotNull(molecularIDType.getModifiedDate());
		assertEquals(molecularIDType.getCreatedBy(),"admin" );
		assertEquals(molecularIDType.getEditedBy(), "admin");
		assertEquals(molecularIDType.getId(), 1);
		assertEquals(molecularIDType.getActiveFlag(), "Y");
	}
	
	@Test
	public void FlagsDataTest() throws IOException {
		FlagsData flagsData=new FlagsData();
		flagsData.setAssay(new AssayType());
		flagsData.setAssayType("NIPTHTP");
		flagsData.setCompany(new Company());
		flagsData.setCreatedBy("Admin");
		flagsData.setEditedBy("Admin");
		flagsData.setSampleOrRunLevel("sample");
		flagsData.setId(1);
		flagsData.setCreatedDate(new Date());
		flagsData.setModifiedDate(new Date());
		flagsData.setProcessStepName("NA Extraction");
		flagsData.setSource("doc");
		assertEquals(flagsData.getSource(), "doc");	
		assertEquals(flagsData.getAssayType(), "NIPTHTP");
		assertEquals(flagsData.getCreatedBy(), "Admin");
		assertEquals(flagsData.getProcessStepName(), "NA Extraction");
		assertEquals(flagsData.getId(), 1);
		assertEquals(flagsData.getSampleOrRunLevel(), "sample");
		assertNotNull(flagsData.getCreatedDate());
		assertNotNull(flagsData.getModifiedDate());
		assertNotNull(flagsData.getAssay());
		assertNotNull(flagsData.getCompany());
		assertEquals(flagsData.getEditedBy(), "Admin");
	}
	@Test
	public void testOptionsTest() throws IOException {
		TestOptions testOptions=new TestOptions();
		testOptions.setCreatedBy("Admin");
		testOptions.setEditedBy("Admin");
		testOptions.setCreatedDate(new Date());
		testOptions.setModifiedDate(new Date());
		assertEquals(testOptions.getCreatedBy(), "Admin");
		assertNotNull(testOptions.getCreatedDate());
		assertNotNull(testOptions.getModifiedDate());
		assertEquals(testOptions.getEditedBy(), "Admin");
	}
	@Test
	public void assayListDataTest() throws IOException {
		AssayListData assayListData=new AssayListData();
		assayListData.setCreatedBy("Admin");
		assayListData.setEditedBy("Admin");
		assayListData.setCreatedDate(new Date());
		assayListData.setModifiedDate(new Date());
		assertEquals(assayListData.getCreatedBy(), "Admin");
		assertNotNull(assayListData.getCreatedDate());
		assertNotNull(assayListData.getModifiedDate());
		assertEquals(assayListData.getEditedBy(), "Admin");
	}
	
	@Test
	public void sampleTypeTest() throws IOException {
		SampleType sampleType=new SampleType();
		sampleType.setCreatedBy("Admin");
		sampleType.setEditedBy("Admin");
		sampleType.setCreatedDate(new Date());
		sampleType.setModifiedDate(new Date());
		assertEquals(sampleType.getCreatedBy(), "Admin");
		assertNotNull(sampleType.getCreatedDate());
		assertNotNull(sampleType.getModifiedDate());
		assertEquals(sampleType.getEditedBy(), "Admin");
	}
	@Test
	public void ProcessStepActionTest() throws IOException {
		ProcessStepAction processStepAction=new ProcessStepAction();
		processStepAction.setCreatedBy("Admin");
		processStepAction.setEditedBy("Admin");
		processStepAction.setCreatedDate(new Date());
		processStepAction.setModifiedDate(new Date());
		assertEquals(processStepAction.getCreatedBy(), "Admin");
		assertNotNull(processStepAction.getCreatedDate());
		assertNotNull(processStepAction.getModifiedDate());
		assertEquals(processStepAction.getEditedBy(), "Admin");
	}
	
	@Test
	public void assayInputDataValidationsTest() throws IOException {
		AssayInputDataValidations assayInputDataValidations=new AssayInputDataValidations();
		assayInputDataValidations.setCreatedBy("Admin");
		assayInputDataValidations.setEditedBy("Admin");
		assayInputDataValidations.setCreatedDate(new Date());
		assayInputDataValidations.setModifiedDate(new Date());
		assertEquals(assayInputDataValidations.getCreatedBy(), "Admin");
	 	assertNotNull(assayInputDataValidations.getCreatedDate());
		assertNotNull(assayInputDataValidations.getModifiedDate());
		assertEquals(assayInputDataValidations.getEditedBy(), "Admin");
	}
	
	  @Test
      public void testSetCompany() {
        Company company = new Company();
        company.setDomainName("hcl.com");
        
        DeviceTestOptions deviceTestOptions=new DeviceTestOptions();
        FlagsData flagsData=new FlagsData();
        MolecularIDType molecularIDType=new MolecularIDType();
        deviceTestOptions.setCompany(company);
        AssayInputDataValidations assayInputDataValidations=new AssayInputDataValidations();
        assayInputDataValidations.setCompany(company);
        deviceTestOptions.setAnalysisPackageName("AP1");
        molecularIDType.setCompany(company);
        AssayListData assayListData=new AssayListData();
        assayListData.setCompany(company);
        SampleType sampleType=new SampleType();
        sampleType.setCompany(company);
        TestOptions testOptions=new TestOptions();
        testOptions.setCompany(company);
        AssayType assayType=new AssayType();
        assayType.setCompany(company);
        ProcessStepAction processStepAction=new ProcessStepAction();
        processStepAction.setCompany(company);
          assertEquals(deviceTestOptions.getCompany().getDomainName(), company.getDomainName());
          assertEquals(deviceTestOptions.getOwnerPropertyName(),"company");
          assertEquals(deviceTestOptions.getAnalysisPackageName(),deviceTestOptions.getAnalysisPackageName());
          assertEquals(flagsData.getOwnerPropertyName(),"company");
          assertEquals(molecularIDType.getCompany().getDomainName(), company.getDomainName());
          assertEquals(molecularIDType.getOwnerPropertyName(),"company");
          assertEquals(assayInputDataValidations.getCompany().getDomainName(), company.getDomainName());
    }
	  @Test
	  public void testFlagsData(){
		  FlagsData flagsData=new FlagsData();
		  flagsData.setActiveFlag("Y");
		  assertEquals(flagsData.getActiveFlag(),"Y");
	  }
	 
}
