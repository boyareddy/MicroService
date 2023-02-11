package com.roche.connect.commonutils.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.roche.connect.common.util.SampleStatus;

public class SampleStatusTest {
	
	@Test
	public void  testEnumTypes(){
		System.out.println(SampleStatus.COMPLETED.getText());
		Assert.assertEquals(SampleStatus.COMPLETED.getText(), "Completed");
		Assert.assertEquals(SampleStatus.INPROGRESS.getText(), "InProgress");
		Assert.assertEquals(SampleStatus.ABORTED.getText(), "Aborted");
		Assert.assertEquals(SampleStatus.PASSEDWITHFLAG.getText(), "passed with flag");
		Assert.assertEquals(SampleStatus.PASSED.getText(), "Passed");
		Assert.assertEquals(SampleStatus.FLAGGED.getText(), "flagged");
		Assert.assertEquals(SampleStatus.COMPLETED_WITH_FLAGS.getText(), "Completed with flags");
		Assert.assertEquals(SampleStatus.FAILED.getText(), "Failed");
		Assert.assertEquals(SampleStatus.PENDING.getText(), "Pending");

		      
	}

}
