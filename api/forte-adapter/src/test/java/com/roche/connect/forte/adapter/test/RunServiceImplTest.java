package com.roche.connect.forte.adapter.test;

import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.forte.adapter.model.Cycle;
import com.roche.connect.forte.adapter.model.ForteJob;
import com.roche.connect.forte.adapter.services.RunServiceImpl;

public class RunServiceImplTest {
	@InjectMocks
	RunServiceImpl runServiceImpl;
	@Mock
	Cycle cycle;

	@BeforeTest
	public void setUp() {
		runServiceImpl = new RunServiceImpl();
		cycle = new Cycle();

		cycle.setId(new UUID(0, 0));
		cycle.setRunId("127384");
	}

	@Test(priority = 1)
	public void getSampleDetailsTest() {
		ForteJob forteJob = runServiceImpl.getForteJob(cycle);
		Assert.assertEquals(forteJob.getJobType(), "secondary");
	}

}
