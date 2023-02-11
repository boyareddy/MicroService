package com.roche.dpcr.dto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MessageExchange {

	private InputStream inp;
	private BufferedReader brinp;
	private OutputStream out;
	private String msgVersion;
	private String sendingApp;
	private String sendingFacility;
	private String recevingApp;
	private String recevingFacility;
	private String serialNo;
	private String ackTriggerEvent;
	private String runId;
	private String deviceType;
	private String deviceSubStatus;
	private String msgControlId;
	private Socket socket;

	public String getSendingApp() {
		return sendingApp;
	}

	public void setSendingApp(String sendingApp) {
		this.sendingApp = sendingApp;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}

	public String getRecevingApp() {
		return recevingApp;
	}

	public void setRecevingApp(String recevingApp) {
		this.recevingApp = recevingApp;
	}

	public String getRecevingFacility() {
		return recevingFacility;
	}

	public void setRecevingFacility(String recevingFacility) {
		this.recevingFacility = recevingFacility;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public InputStream getInp() {
		return inp;
	}

	public void setInp(InputStream inp) {
		this.inp = inp;
	}

	public BufferedReader getBrinp() {
		return brinp;
	}

	public void setBrinp(BufferedReader brinp) {
		this.brinp = brinp;
	}


	public String getMsgVersion() {
		return msgVersion;
	}

	public void setMsgVersion(String msgVersion) {
		this.msgVersion = msgVersion;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}

	public String getAckTriggerEvent() {
		return ackTriggerEvent;
	}

	public void setAckTriggerEvent(String ackTriggerEvent) {
		this.ackTriggerEvent = ackTriggerEvent;
	}

	/**
	 * @return the runId
	 */
	public String getRunId() {
		return runId;
	}

	/**
	 * @param runId the runId to set
	 */
	public void setRunId(String runId) {
		this.runId = runId;
	}
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the deviceSubStatus
	 */
	public String getDeviceSubStatus() {
		return deviceSubStatus;
	}
	/**
	 * @param deviceType the deviceSubStatus to set
	 */
	public void setDeviceSubStatus(String deviceSubStatus) {
		this.deviceSubStatus = deviceSubStatus;
	}

	/**
	 * @return the msgControlId
	 */
	public String getMsgControlId() {
		return msgControlId;
	}

	/**
	 * @param msgControlId the msgControlId to set
	 */
	public void setMsgControlId(String msgControlId) {
		this.msgControlId = msgControlId;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	
}