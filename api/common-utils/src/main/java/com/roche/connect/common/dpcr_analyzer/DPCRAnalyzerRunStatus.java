package com.roche.connect.common.dpcr_analyzer;

public class DPCRAnalyzerRunStatus {

	public static final String NOT_STARTED = "NOT STARTED";
	public static final String IN_PROGRESS = "Inprogress";
	
	public static final String ID_PROCESSED = "ID";
	public static final String ES_HALTED = "ES";
	public static final String SS_ABORTED = "SS";
	
	public static final String PROCESSED = "Processed";
	public static final String ABORTED = "Aborted";
	public static final String PASSED = "Passed";
	
	public static final String COMPLETED = "Completed";
	public static final String COMPLETED_WITH_FLAGS = "Completed with flags";
	private DPCRAnalyzerRunStatus() {
		
	}
}
