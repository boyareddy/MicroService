package com.roche.connect.commonutils.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.roche.connect.common.util.RunStatusConstants;

public class RunStatusConstantsTest {
	
	@Test
	public void  testEnumTypes(){
		Assert.assertEquals(RunStatusConstants.MP96_COMPLETED.toString(), "Completed");
		Assert.assertEquals(RunStatusConstants.MP24_COMPLETED.toString(), "Completed");
		Assert.assertEquals(RunStatusConstants.MP96_FAILED.toString(), "Failed");
		Assert.assertEquals(RunStatusConstants.MP24_ABORTED.toString(), "Aborted");
		Assert.assertEquals(RunStatusConstants.MP96_COMPLETED_WITH_FLAGS.toString(), "Completed with flags");
		Assert.assertEquals(RunStatusConstants.MP96_PENDING.toString(), "Pending");
		Assert.assertEquals(RunStatusConstants.MP24_COMPLETED_WITH_FLAGS.toString(), "Completed with flags");
		
		      
	}

}
