package com.roche.connect.common.order.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class OrderDetailsDTO {
	private Long orderId;

	private Long patientId;

	private Long patientSampleId;

	private String accessioningId;

	private String orderStatus;

	private String assayType;

	private String sampleType;

	private boolean retestSample;

	private String orderComments;

	private String activeFlag;

	private String createdBy;
	
	private String patientLastName;
	private String patientFirstName;
	private String patientGender;
	private String patientMedicalRecNo;
	private String patientDOB;	
	private String patientContactNo;
	private String treatingDoctorName;
	private String treatingDoctorContactNo;
	private String refClinicianName;
	private String refClinicianFaxNo;
	private String otherClinicianName;
	private String otherClinicianFaxNo;
	private String refClinicianClinicName;
	private Integer gestationalAgeWeeks;
	private Integer gestationalAgeDays;
	private String ivfStatus;
	private String fetusNumber;
	private Timestamp receivedDate;
	private Timestamp collectionDate;
	private List<PatientDTO> subReportList;
	private AssayDTO assay;
	private PatientDTO patient;
	
	
	
	public Integer getGestationalAgeWeeks() {
		return gestationalAgeWeeks;
	}

	public void setGestationalAgeWeeks(Integer gestationalAgeWeeks) {
		this.gestationalAgeWeeks = gestationalAgeWeeks;
	}

	public Integer getGestationalAgeDays() {
		return gestationalAgeDays;
	}

	public void setGestationalAgeDays(Integer gestationalAgeDays) {
		this.gestationalAgeDays = gestationalAgeDays;
	}

	public String getIvfStatus() {
		return ivfStatus;
	}

	public void setIvfStatus(String ivfStatus) {
		this.ivfStatus = ivfStatus;
	}

	public String getFetusNumber() {
		return fetusNumber;
	}

	public void setFetusNumber(String fetusNumber) {
		this.fetusNumber = fetusNumber;
	}

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Timestamp getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
	}

	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp createdDateTime;

	private String updatedBy;
	
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Timestamp updatedDateTime;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getPatientSampleId() {
		return patientSampleId;
	}

	public void setPatientSampleId(Long patientSampleId) {
		this.patientSampleId = patientSampleId;
	}

	public String getAccessioningId() {
		return accessioningId;
	}

	public void setAccessioningId(String accessioningId) {
		this.accessioningId = accessioningId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public boolean isRetestSample() {
        return retestSample;
    }

    public void setRetestSample(boolean retestSample) {
        this.retestSample = retestSample;
    }

    public String getOrderComments() {
		return orderComments;
	}

	public void setOrderComments(String orderComments) {
		this.orderComments = orderComments;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public AssayDTO getAssay() {
		return assay;
	}

	public void setAssay(AssayDTO assay) {
		this.assay = assay;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Timestamp getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Timestamp updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
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
	public String getPatientGender() {
		return patientGender;
	}
	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
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
	public String getPatientContactNo() {
		return patientContactNo;
	}
	public void setPatientContactNo(String patientContactNo) {
		this.patientContactNo = patientContactNo;
	}
	public String getTreatingDoctorName() {
		return treatingDoctorName;
	}
	public void setTreatingDoctorName(String treatingDoctorName) {
		this.treatingDoctorName = treatingDoctorName;
	}
	public String getTreatingDoctorContactNo() {
		return treatingDoctorContactNo;
	}
	public void setTreatingDoctorContactNo(String treatingDoctorContactNo) {
		this.treatingDoctorContactNo = treatingDoctorContactNo;
	}
	public String getRefClinicianName() {
		return refClinicianName;
	}
	public void setRefClinicianName(String refClinicianName) {
		this.refClinicianName = refClinicianName;
	}
	public String getRefClinicianFaxNo() {
		return refClinicianFaxNo;
	}
	public void setRefClinicianFaxNo(String refClinicianFaxNo) {
		this.refClinicianFaxNo = refClinicianFaxNo;
	}
	public String getOtherClinicianName() {
		return otherClinicianName;
	}
	public void setOtherClinicianName(String otherClinicianName) {
		this.otherClinicianName = otherClinicianName;
	}
	public String getOtherClinicianFaxNo() {
		return otherClinicianFaxNo;
	}
	public void setOtherClinicianFaxNo(String otherClinicianFaxNo) {
		this.otherClinicianFaxNo = otherClinicianFaxNo;
	}
	public String getRefClinicianClinicName() {
		return refClinicianClinicName;
	}
	public void setRefClinicianClinicName(String refClinicianClinicName) {
		this.refClinicianClinicName = refClinicianClinicName;
	}

	public List<PatientDTO> getSubReportList() {
		return subReportList;
	}

	public void setSubReportList(List<PatientDTO> subReportList) {
		this.subReportList = subReportList;
	}
	
	
}
