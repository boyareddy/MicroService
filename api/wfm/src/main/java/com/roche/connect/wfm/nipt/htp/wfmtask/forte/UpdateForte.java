package com.roche.connect.wfm.nipt.htp.wfmtask.forte;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;

public class UpdateForte implements JavaDelegate {

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());

	@Override
	public void execute(DelegateExecution execution) 
	{
		logger.info("UpdateForte :: => execute()");
		
	}

}
