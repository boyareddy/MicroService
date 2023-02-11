package com.roche.connect.common.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDTO {

	private Long patientId;
	private String patientLastName;
	private String patientFirstName;
	
	private String patientMedicalRecNo;
	private String patientDOB;
	/**private String patientGender;
	private String patientContactNo;
	private String treatingDoctorName;
	private String treatingDoctorContactNo;
	private String refClinicianFaxNo;
	private String otherClinicianFaxNo;*/
	private String refClinicianName;
	private String otherClinicianName;
	private String clinicName;
	private String accountNumber;
	private String labId;
	private String reasonForReferral;
	
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getLabId() {
		return labId;
	}

	public void setLabId(String labId) {
		this.labId = labId;
	}

	public String getReasonForReferral() {
		return reasonForReferral;
	}

	public void setReasonForReferral(String reasonForReferral) {
		this.reasonForReferral = reasonForReferral;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	public String getPatientFirstName() {
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}

	public String getPatientMedicalRecNo() {
		return patientMedicalRecNo;
	}

	public void setPatientMedicalRecNo(String patientMedicalRecNo) {
		this.patientMedicalRecNo = patientMedicalRecNo;
	}

	public String getPatientDOB() {
		return patientDOB;
	}

	public void setPatientDOB(String patientDOB) {
		this.patientDOB = patientDOB;
	}

	public String getRefClinicianName() {
		return refClinicianName;
	}

	public void setRefClinicianName(String refClinicianName) {
		this.refClinicianName = refClinicianName;
	}

	public String getOtherClinicianName() {
		return otherClinicianName;
	}

	public void setOtherClinicianName(String otherClinicianName) {
		this.otherClinicianName = otherClinicianName;
	}

/**	public String getRefClinicianClinicName() {
		return refClinicianClinicName;
	}

	public void setRefClinicianClinicName(String refClinicianClinicName) {
		this.refClinicianClinicName = refClinicianClinicName;
	}
*/
}
