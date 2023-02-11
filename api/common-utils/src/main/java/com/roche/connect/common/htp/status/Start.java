package com.roche.connect.common.htp.status;

public class Start extends HtpStatus {

	private Long runId;
	private String runProtocol;
	private long estimatedTimeRemaining;
	private long estimatedTimeRemainingBothRuns;

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getRunProtocol() {
		return runProtocol;
	}

	public void setRunProtocol(String runProtocol) {
		this.runProtocol = runProtocol;
	}

	public long getEstimatedTimeRemaining() {
		return estimatedTimeRemaining;
	}

	public void setEstimatedTimeRemaining(long estimatedTimeRemaining) {
		this.estimatedTimeRemaining = estimatedTimeRemaining;
	}

	public long getEstimatedTimeRemainingBothRuns() {
		return estimatedTimeRemainingBothRuns;
	}

	public void setEstimatedTimeRemainingBothRuns(long estimatedTimeRemainingBothRuns) {
		this.estimatedTimeRemainingBothRuns = estimatedTimeRemainingBothRuns;
	}

}
