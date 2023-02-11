package com.roche.connect.common.htp.status;

public class InProgress extends HtpStatus{

	private long estimatedTimeRemaining;
	private long estimatedTimeRemainingBothRuns;

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
