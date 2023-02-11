package com.roche.connect.common.lp24;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EnumMessageTypeTest {
	
	@Test
	public void  testEnumTypes(){
		Assert.assertEquals(EnumMessageType.QueryMessage.toString(), "QueryMessage");
		Assert.assertEquals(EnumMessageType.ResponseMessage.toString(), "ResponseMessage");
		Assert.assertEquals(EnumMessageType.StatusUpdateMessage.toString(), "StatusUpdateMessage");
		Assert.assertEquals(EnumMessageType.AcknowledgementMessage.toString(), "AcknowledgementMessage");
		      
	}

}
