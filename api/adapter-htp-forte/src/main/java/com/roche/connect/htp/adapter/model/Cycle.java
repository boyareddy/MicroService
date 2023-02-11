package com.roche.connect.htp.adapter.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.entity.MultiTenantEntity;

/**
 * The Class Cycle.
 */
@Entity
@Table(name = "HTP_CYCLE")
public class Cycle implements MultiTenantEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private long id;



	/** The status. */
	@Column(name = "STATUS")
	private String status;

	/** The path. */
	@Column(name = "FILE_PATH")
	private String path;

	/** The cycles number. */
	@Column(name = "CYCLES_NUMBER")
	private int cyclesNumber;

	/** The hqr. */
	@Column(name = "HQR")
	private int hqr;

	/** The read length. */
	@Column(name = "READ_LENGTH")
	private long readLength;

	/** The run id. */
	@Column(name = "DEVICE_RUN_ID")
	private String runId;

	/** The checksum. */
	@Column(name = "CHECKSUM")
	private String checksum;

	/** The file type. */
	@Column(name = "FILE_TYPE")
	private String type;

	@Column(name = "SENT_TO_SECONDARY")
	private String sendToSecondaryFlag;

	@Column(name = "TRANSFER_COMPLETE")
	private String transferComplete;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE_TIME")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp createdDateTime;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE_TIME")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;
	
	@ManyToOne 
	@JoinColumn(name = "COMPANYID") 
	private Company company;

	@PrePersist
	protected void onCreate() {
		createdDateTime = new Timestamp(new Date().getTime());
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDateTime = new Timestamp(new Date().getTime());
	}

	public String getSendToSecondaryFlag() {
		return sendToSecondaryFlag;
	}

	public void setSendToSecondaryFlag(String sendToSecondaryFlag) {
		this.sendToSecondaryFlag = sendToSecondaryFlag;
	}

	public String getTransferComplete() {
		return transferComplete;
	}

	public void setTransferComplete(String transferComplete) {
		this.transferComplete = transferComplete;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the cycles number.
	 *
	 * @return the cycles number
	 */
	public int getCyclesNumber() {
		return cyclesNumber;
	}

	/**
	 * Sets the cycles number.
	 *
	 * @param cyclesNumber the new cycles number
	 */
	public void setCyclesNumber(int cyclesNumber) {
		this.cyclesNumber = cyclesNumber;
	}

	/**
	 * Gets the hqr.
	 *
	 * @return the hqr
	 */
	public int getHqr() {
		return hqr;
	}

	/**
	 * Sets the hqr.
	 *
	 * @param hqr the new hqr
	 */
	public void setHqr(int hqr) {
		this.hqr = hqr;
	}

	/**
	 * Gets the read length.
	 *
	 * @return the read length
	 */
	public long getReadLength() {
		return readLength;
	}

	/**
	 * Sets the read length.
	 *
	 * @param readLength the new read length
	 */
	public void setReadLength(long readLength) {
		this.readLength = readLength;
	}

	/**
	 * Gets the run id.
	 *
	 * @return the run id
	 */
	public String getRunId() {
		return runId;
	}

	/**
	 * Sets the run id.
	 *
	 * @param runId the new run id
	 */
	public void setRunId(String runId) {
		this.runId = runId;
	}

	/**
	 * Gets the checksum.
	 *
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * Sets the checksum.
	 *
	 * @param checksum the new checksum
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}


	@Override
	public String getOwnerPropertyName() {
		return "company";
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	

}
