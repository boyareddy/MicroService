package com.roche.connect.common.mp96;

public class OULACKMessage {

	private String dateTimeMessageGenerated;
	private String deviceRunId;
	private String deviceId;
	private String status;
	private String errorCode;
	private String errorDescription;

	public String getDateTimeMessageGenerated() {
		return dateTimeMessageGenerated;
	}

	public void setDateTimeMessageGenerated(String dateTimeMessageGenerated) {
		this.dateTimeMessageGenerated = dateTimeMessageGenerated;
	}

	public String getDeviceRunId() {
		return deviceRunId;
	}

	public void setDeviceRunId(String deviceRunId) {
		this.deviceRunId = deviceRunId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override public String toString() {
        return "OULACKMessage [dateTimeMessageGenerated=" + dateTimeMessageGenerated + ", deviceRunId=" + deviceRunId
            + ", deviceId=" + deviceId + ", status=" + status + ", errorCode=" + errorCode + ", errorDescription="
            + errorDescription + "]";
    }

}
