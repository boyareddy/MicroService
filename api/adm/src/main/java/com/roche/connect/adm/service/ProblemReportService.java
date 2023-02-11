package com.roche.connect.adm.service;

import java.io.File;
import java.io.IOException;

import com.hcl.hmtp.common.client.exceptions.HMTPException;

public interface ProblemReportService {
	public File getAuditDetailsFile(String fromDate, String toDate, String companydomainname,String folderPath)throws HMTPException;
	public File getDeviceSummaryLogs(String fromDate, String toDate, String folderPath)throws HMTPException, IOException;
	public void generateProblemReport(String fromDate, String toDate) throws HMTPException;
}
