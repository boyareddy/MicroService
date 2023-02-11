package com.roche.connect.common.htp.status;

import java.util.List;

public class StartPlanned extends HtpStatus {

	private long seqRunBatchSize;
	private List<String> complexId;
	private List<String> sequencingProtocols;

	public long getSeqRunBatchSize() {
		return seqRunBatchSize;
	}

	public void setSeqRunBatchSize(long seqRunBatchSize) {
		this.seqRunBatchSize = seqRunBatchSize;
	}

	public List<String> getComplexId() {
		return complexId;
	}

	public void setComplexId(List<String> complexId) {
		this.complexId = complexId;
	}

	public List<String> getSequencingProtocols() {
		return sequencingProtocols;
	}

	public void setSequencingProtocols(List<String> sequencingProtocols) {
		this.sequencingProtocols = sequencingProtocols;
	}

}
