package com.roche.connect.wfm.test.testng;

import static org.testng.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.wfm.constants.WfmConstants;
import com.roche.connect.wfm.constants.WfmConstants.API_URL;
import com.roche.connect.wfm.constants.WfmConstants.ASSAY_PROCESS_STEP_DATA;
import com.roche.connect.wfm.constants.WfmConstants.ORDERNOTFOUND_RESPONSE;
import com.roche.connect.wfm.constants.WfmConstants.ORDER_STATUS;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_MESSAGESOURCE;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_MESSAGE_TYPE;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_SIGNALS;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_STATUS;
import com.roche.connect.wfm.constants.WfmConstants.WORKFLOW_VARIABLES;
import com.roche.connect.wfm.constants.WfmConstants.WfmXMLFiles;

public class WFMEnumTest {
	@Mock
	public Enum<?> validation = WfmConstants.WORKFLOW_VARIABLES.ACCESSIONINGID;

	@BeforeTest
	public void setUp() {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void variablesTest() {

		for (WORKFLOW_VARIABLES variables : WfmConstants.WORKFLOW_VARIABLES.values()) {
			assertEquals(variables, variables);
		}
		for (WORKFLOW_SIGNALS variables : WfmConstants.WORKFLOW_SIGNALS.values()) {
			assertEquals(variables, variables);
		}
		for (WORKFLOW_STATUS variables : WfmConstants.WORKFLOW_STATUS.values()) {
			assertEquals(variables, variables);
		}
		for (WfmXMLFiles variables : WfmConstants.WfmXMLFiles.values()) {
			assertEquals(variables, variables);
		}
		for (ORDER_STATUS variables : WfmConstants.ORDER_STATUS.values()) {
			assertEquals(variables, variables);
		}
		for (WORKFLOW_MESSAGE_TYPE variables : WfmConstants.WORKFLOW_MESSAGE_TYPE.values()) {
			assertEquals(variables, variables);
		}
		for (WORKFLOW_SENDINGAPPLICATIONNAME variables : WfmConstants.WORKFLOW_SENDINGAPPLICATIONNAME.values()) {
			assertEquals(variables, variables);
		}
		for (WORKFLOW_MESSAGESOURCE variables : WfmConstants.WORKFLOW_MESSAGESOURCE.values()) {
			assertEquals(variables, variables);
		}
		for (ASSAY_PROCESS_STEP_DATA variables : WfmConstants.ASSAY_PROCESS_STEP_DATA.values()) {
			assertEquals(variables, variables);
		}
		for (API_URL variables : WfmConstants.API_URL.values()) {
			assertEquals(variables, variables);
		}
		for (WORKFLOW variables : WfmConstants.WORKFLOW.values()) {
			assertEquals(variables, variables);
		}
		for (ORDERNOTFOUND_RESPONSE variables : WfmConstants.ORDERNOTFOUND_RESPONSE.values()) {
			assertEquals(variables, variables);
		}

	}

}
