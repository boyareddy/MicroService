package com.roche.connect.rmm.testNG;

import java.io.InputStream;
import java.util.Date;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.hcl.hmtp.common.server.entity.Company;
import com.roche.connect.rmm.jasper.dto.Imagepojo;
import com.roche.connect.rmm.jasper.dto.SampleLevelQCMetricsDTO;
import com.roche.connect.rmm.model.RunResults;
import com.roche.connect.rmm.model.RunResultsDetail;
import com.roche.connect.rmm.model.SampleProtocol;
import com.roche.connect.rmm.model.SampleResults;
import com.roche.connect.rmm.model.SampleResultsDetail;


public class UtilsTest {
    
    @InjectMocks
    Imagepojo  imagepojo ;
    
    @InjectMocks 
    RunResultsDetail runResultsDetail ;
    
    @InjectMocks SampleProtocol  sampleProtocol ;
    
    @InjectMocks SampleResultsDetail sampleResultsDetail ;
    
    @InjectMocks SampleLevelQCMetricsDTO  sampleLevelQCMetricsDTO ;
    
    @Mock InputStream stream;
    
    @BeforeTest
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void imagePojoTest() {
        imagepojo.setStream(stream);
        Assert.assertEquals(stream, imagepojo.getStream());
    }
    
    @Test
    public void runResultsDetailTest() {
        runResultsDetail.setCompany( getCompany() );
        runResultsDetail.setEditedBy("admin");
        runResultsDetail.setModifiedDate(new Date());
        RunResults runResultsMappedToRunDetails = new RunResults();
        runResultsMappedToRunDetails.setAssayType("NIPTHTP");
        runResultsDetail.setRunResultsMappedToRunDetails(runResultsMappedToRunDetails );
        Assert.assertEquals(runResultsDetail.getCompany().getId(), 1L);
        Assert.assertEquals(runResultsDetail.getOwnerPropertyName(),"company");
        Assert.assertEquals(runResultsDetail.getEditedBy(),"admin");
        Assert.assertEquals(runResultsDetail.getRunResultsMappedToRunDetails().getAssayType(),"NIPTHTP");
    }

    public Company getCompany() {
        Company company = new Company();
        company.setId(1L);
        return company;
    }
    
    @Test
    public void sampleProtocolTest() {
        SampleProtocol sampleProtocol = new SampleProtocol();
        sampleProtocol.setSampleResultsMappedToSampleProtocol(getSampleResults() );
        sampleProtocol.setCompany(getCompany());
        sampleProtocol.setEditedBy("admin");
        sampleProtocol.setModifiedDate(new Date());
        Assert.assertEquals(sampleProtocol.getCompany().getId(), 1L);
        Assert.assertEquals(sampleProtocol.getSampleResultsMappedToSampleProtocol().getAccesssioningId(), "8001");
        Assert.assertEquals(sampleProtocol.getOwnerPropertyName(),"company");
        Assert.assertEquals(sampleProtocol.getEditedBy(),"admin");
    }
    public SampleResults getSampleResults() {
    SampleResults sampleResults = new SampleResults();
    sampleResults.setAccesssioningId("8001");
    return sampleResults;
    }
    
    @Test
    public void sampleResultsDetailTest() {
        sampleResultsDetail.setSampleResultsMappedToSampleDetail(getSampleResults());
        sampleResultsDetail.setCompany( getCompany() );
        sampleResultsDetail.setEditedBy("admin");
        sampleResultsDetail.setModifiedDate(new Date());
        Assert.assertEquals(runResultsDetail.getCompany().getId(), 1L);
        Assert.assertEquals(runResultsDetail.getOwnerPropertyName(),"company");
        Assert.assertEquals(runResultsDetail.getEditedBy(),"admin");
        Assert.assertEquals(sampleResultsDetail.getSampleResultsMappedToSampleDetail().getAccesssioningId(), "8001");
    }
    
    @Test
    public void sampleLevelQCMetricsDTOTest() {
        SampleLevelQCMetricsDTO sampleLevelQCMetricsDTO = new SampleLevelQCMetricsDTO();
        sampleLevelQCMetricsDTO.setSampleID(1L);
       Assert.assertEquals( sampleLevelQCMetricsDTO.getSampleID().toString(), "1");
    }
}
