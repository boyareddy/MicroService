package com.roche.connect.htp.adapter.util;

public class ForteConstants {

	private ForteConstants() {
		// Empty Constructor
	}

	public static final String HTPCOMPLETE = "COMPLETE";
	public static final String NO = "NO";
	public static final String YES = "YES";
	public static final String FILETYPE = "fastq.gz";
	public static final String SECONDARY = "secondary";
	public static final String GET = "FORTE_GET_JOB";
	public static final String PUT = "FORTE_PUT_JOB";

	public static final String MATERNALAGE = "maternal_age";
	public static final String GESTATIONALAGEWEEKS = "gestational_age_weeks";
	public static final String GESTATIONALAGEDAYS = "gestational_age_days";
	public static final String EGGSOURCE = "egg_sources";
	public static final String NUMBERFETUSES = "number_fetuses";
	public static final String SAMPLETAG = "sample_tag";
	public static final String MID = "mid";

	public static final String START = "start";
	public static final String INPROGRESS = "inprogress";
	public static final String DONE = "done";
	public static final String ERROR = "error";

	public static final String FORTE = "FORTE";

	public static final String COMPLETE = "COMPLETE";
	public static final String FORTE_DEVICE_ID ="FORTE-01";
	
	public static final String ESTIMATED_TIME_TO_COMPLETION = "estimated_time_to_completion";
	public static final String JOB_STATUS="job_status";
	public static final String ERROR_KEY="error_key";
	public static final String QC="qc";
	public static final String OUTFILE="outfile";
	public static final String OUTFILE_CHECKSUM="outfile_checksum";
	
    public static final String NO_CYCLE_FOUND_MESSAGE="No Cycle found for the given Job id";
    public static final String CHECKSUM_MISMATCH ="Checksum mismatch";
    public static final String NO_JOB_FOUND_MESSAGE="No Jobs found for the given Job id";
    public static final String COMPLEX_ID_NOT_EXIST_MESSAGE = "Complex Id is not exist in Adapter DB";
    public static final String STATUS_UPDATE_MSG = "Status updated";
    public static final String NO_JOB_FOUND_MSG ="No Job Found";
    
    
}
