package com.roche.connect.omm.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.order.dto.AssayDTO;
import com.roche.connect.common.order.dto.OrderDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.common.order.dto.PatientDTO;
import com.roche.connect.common.util.DateUtil;
import com.roche.connect.common.util.DateUtilImpl;
import com.roche.connect.omm.error.CustomErrorCodes;
import com.roche.connect.omm.services.OrderService;
import com.roche.connect.omm.services.OrderServiceImpl;

public class ValidationUtil {
    OrderService orderService = new OrderServiceImpl();
    DateUtil dateUtil = new DateUtilImpl();
    Calendar patientdob = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    Calendar sampleReceivedDate = Calendar.getInstance();
    String validateNumericOneDigit = "[0-9]{1}";
    String validateString = "^[A-Z]+[a-zA-Z]*$";
    String validateStringwithOneSpecialCharacter = "^[a-zA-Z-]*$";
    String validateAccessioningIdPos = "^[a-zA-Z0-9-_]+$";
    String validateAccountNumber = "^[a-zA-Z0-9]+$";
    String validateLabId = "^[a-zA-Z0-9-]+$";
    String validateClinicName = "^[a-zA-Z\\s'-]+$";
    String validateOneSpcae = "/\\s\\s+/g";
    String validateAccessioningIdNeg = "^[-_]+$";
    String validateOnlyNumericwithplussymbol = "\\+?\\d+";
    String validateAlphaNumeric = "[A-Za-z0-9]+";
    String validateAlphabets = "^[a-zA-Z ]+$";
    String validateMultipleSpaceBetweenAlphabets = "^\\s+(?: \\s+)*$";
    List<AssayInputDataValidationsDTO> assayinputdatavalidationdto = null;
    List<AssayTypeDTO> assayTypelist = null;
    List<SampleTypeDTO> sampleTypelist = null;
    List<AssayListDataDTO> assayListDatalist = null;
    List<TestOptionsDTO> testOptionslist = null;
    
    public boolean validateOrderParentDetails(OrderParentDTO orderParent) throws HMTPException {
        AssayDTO assayDTO = orderParent.getOrder().getAssay();
        OrderDTO orderDTO = orderParent.getOrder();
        PatientDTO patientDTO = orderParent.getOrder().getPatient();
        Map<String, Object> assayDetails = orderService.assayValidations(orderParent);
        
        if (orderDTO.getOrderComments().length() > 150) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                OMMConstant.ErrorMessages.ORDERCOMMENTS_MAX_LENGTH_ERR_MESSAGE.toString());
            
        }
        
        if (!(StringUtils.isNotEmpty(orderDTO.getAccessioningId()) && orderDTO.getAccessioningId().length() <= 20
            && (Pattern.matches(validateAccessioningIdPos, orderDTO.getAccessioningId()))
            && (!Pattern.matches(validateAccessioningIdNeg, orderDTO.getAccessioningId())))) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "AccessioningId " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
        }
        
        if (!StringUtils.isNotEmpty(orderDTO.getAssayType())) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "AssayType " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
        }
        
        if (!StringUtils.isNotEmpty(orderDTO.getSampleType())) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "SampleType " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
        }
        if (!(assayDTO.getCollectionDate() != null && assayDTO.getReceivedDate() != null)) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "CollectionDate  & ReceivedDate " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
        }
        
        if (assayDTO.getReceivedDate() != null) {
            Date date = new Date(assayDTO.getReceivedDate().getTime());
            sampleReceivedDate.setTimeInMillis(date.getTime());
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 5);
            calendar.add(Calendar.MINUTE, 30);
            if (sampleReceivedDate.getTime().after(calendar.getTime())) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    OMMConstant.ErrorMessages.VALIDATION_FUTURE_DATE_ERR_MESSAGE.toString());
            }
        }
        assayFieldValidations(assayDetails, orderParent);
        patientFieldValidations(patientDTO);
        
        return true;
    }
    
    public void patientNameValiadtion(OrderParentDTO orderParent) throws HMTPException {
        if (!isValidDate(orderParent.getOrder().getAssay().getCollectionDate(),
            orderParent.getOrder().getAssay().getReceivedDate())) {
            throw new HMTPException(CustomErrorCodes.INVALID_COLLECTION_DATE,
                OMMConstant.ErrorMessages.INVALID_COLLECTION_DATE.toString());
        }
        if (!isValidName(orderParent.getOrder().getPatient().getPatientFirstName())) {
            throw new HMTPException(CustomErrorCodes.INVALID_FIRST_NAME_FORMAT,
                OMMConstant.ErrorMessages.INVALID_FIRST_NAME_FORMAT.toString());
        }
        if (!isValidName(orderParent.getOrder().getPatient().getPatientLastName())) {
            throw new HMTPException(CustomErrorCodes.INVALID_LAST_NAME_FORMAT,
                OMMConstant.ErrorMessages.INVALID_LAST_NAME_FORMAT.toString());
        }
        
        if (!isValidName(orderParent.getOrder().getPatient().getOtherClinicianName())) {
            throw new HMTPException(CustomErrorCodes.INVALID_OTHER_CLINICIAN_NAME_FORMAT,
                OMMConstant.ErrorMessages.INVALID_OTHER_CLINICIAN_NAME_FORMAT.toString());
        }
        if (!isValidName(orderParent.getOrder().getPatient().getClinicName())) {
            throw new HMTPException(CustomErrorCodes.INVALID_REF_CLINICIAN_CLINIC_NAME_FORMAT,
                OMMConstant.ErrorMessages.INVALID_REF_CLINICIAN_CLINIC_NAME_FORMAT.toString());
        }
        if (!isValidName(orderParent.getOrder().getPatient().getRefClinicianName())) {
            throw new HMTPException(CustomErrorCodes.INVALID_REF_CLINICIAN_NAME_FORMAT,
                OMMConstant.ErrorMessages.INVALID_REF_CLINICIAN_NAME_FORMAT.toString());
        }
    }
    
   
    public void patientFieldValidations(PatientDTO patientDTO) throws HMTPException {
        
        if (StringUtils.isNotBlank(patientDTO.getPatientFirstName())
            && (patientDTO.getPatientFirstName().length() > 30)) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "Patient First Name " + OMMConstant.ErrorMessages.VALIDATION_MAX30LENGTH_ERR_MESSAGE);
        }
        if (StringUtils.isNotBlank(patientDTO.getPatientLastName())
            && (patientDTO.getPatientLastName().length() > 30)) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "Patient  Last Name " + OMMConstant.ErrorMessages.VALIDATION_MAX30LENGTH_ERR_MESSAGE);
        }
        
        if (StringUtils.isNotBlank(patientDTO.getPatientMedicalRecNo())
            && (patientDTO.getPatientMedicalRecNo().length() > 15
                || !Pattern.matches(validateAlphaNumeric, patientDTO.getPatientMedicalRecNo()))) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "Patient Medical Record Number " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
        }
        if (StringUtils.isNotBlank(patientDTO.getPatientDOB())) {
            
            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
            Date date;
            try {
                date = simpleDateFormat.parse(patientDTO.getPatientDOB());
            } catch (ParseException e) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    OMMConstant.ErrorMessages.VALIDATION_PATIENT_DOB_INVALID_ERR_MESSAGE.toString());
            }
            patientdob.setTimeInMillis(date.getTime());
            
            if (patientdob.getTime().after(calendar.getTime())) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    OMMConstant.ErrorMessages.VALIDATION_FUTURE_PATIENT_DOB_DATE_ERR_MESSAGE.toString());
            }
            long diff = calendar.get(Calendar.YEAR) - (long) patientdob.get(Calendar.YEAR);
            if (diff > 99) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    OMMConstant.ErrorMessages.VALIDATION_PATIENT_DOB_YEAR_ERR_MESSAGE.toString());
            }
        }
        /**
         * if (StringUtils.isNotBlank(patientDTO.getPatientContactNo()) &&
         * (patientDTO.getPatientContactNo().length() > 20 ||
         * !Pattern.matches(validateOnlyNumericwithplussymbol,
         * patientDTO.getPatientContactNo()))) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
         * "PatientContact Number " +
         * OMMConstant.ErrorMessages.VALIDATION_NUMERICS_ERR_MESSAGE); } if
         * (StringUtils.isNotBlank(patientDTO.getTreatingDoctorName()) &&
         * (patientDTO.getTreatingDoctorName().length() > 30)) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
         * "TreatingDoctor Name " +
         * OMMConstant.ErrorMessages.VALIDATION_PATIENTNAME_LENGTH_ERR_MESSAGE);
         * } if (StringUtils.isNotBlank(patientDTO.getTreatingDoctorContactNo())
         * && (patientDTO.getTreatingDoctorContactNo().length() > 20 || !Pattern
         * .matches(validateOnlyNumericwithplussymbol,
         * patientDTO.getTreatingDoctorContactNo()))) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
         * "TreatingDoctorContact Number " +
         * OMMConstant.ErrorMessages.VALIDATION_NUMERICS_ERR_MESSAGE); } if
         * (StringUtils.isNotBlank(patientDTO.getRefClinicianFaxNo()) &&
         * (patientDTO.getRefClinicianFaxNo().length() > 20 ||
         * !Pattern.matches(validateOnlyNumericwithplussymbol,
         * patientDTO.getRefClinicianFaxNo()))) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
         * "RefClinicianFaxNo " +
         * OMMConstant.ErrorMessages.VALIDATION_NUMERICS_ERR_MESSAGE); } if
         * (StringUtils.isNotBlank(patientDTO.getOtherClinicianFaxNo()) &&
         * (patientDTO.getOtherClinicianFaxNo().length() > 20 ||
         * !Pattern.matches(validateOnlyNumericwithplussymbol,
         * patientDTO.getOtherClinicianFaxNo()))) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
         * "OtherClinicianFaxNo " +
         * OMMConstant.ErrorMessages.VALIDATION_NUMERICS_ERR_MESSAGE); }
         * if((orderDTO.getAssayType().equalsIgnoreCase(CSV.NIPTDPCR.toString())
         * ) ||
         * (orderDTO.getAssayType().equalsIgnoreCase(CSV.NIPTHTP.toString()))) {
         * if(!patientDTO.getPatientGender().equalsIgnoreCase(CSV.FEMALEGENDER.
         * toString())) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE, "Patient
         * Gender " +
         * OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE); } }
         */
        
        if (StringUtils.isNotBlank(patientDTO.getRefClinicianName())
            && (patientDTO.getRefClinicianName().length() > 30)) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "RefClinician Name " + OMMConstant.ErrorMessages.VALIDATION_MAX30LENGTH_ERR_MESSAGE);
        }
        
        if (StringUtils.isNotBlank(patientDTO.getOtherClinicianName())
            && (patientDTO.getOtherClinicianName().length() > 30)) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "OtherClinician Name " + OMMConstant.ErrorMessages.VALIDATION_MAX30LENGTH_ERR_MESSAGE);
        }
        
        /**
         * if (StringUtils.isNotBlank(patientDTO.getRefClinicianClinicName()) &&
         * (patientDTO.getRefClinicianClinicName().length() > 30)) { throw new
         * HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
         * "RefClinicianClinic Name " +
         * OMMConstant.ErrorMessages.VALIDATION_MAX30LENGTH_ERR_MESSAGE); }
         */
        
        if (StringUtils.isNotBlank(patientDTO.getReasonForReferral())
            && (patientDTO.getReasonForReferral().length() > 40)) {
            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                "Reason for referral " + OMMConstant.ErrorMessages.VALIDATION_REFERRALREASON_LENGTH_ERR_MESSAGE);
        }
        if (StringUtils.isNotBlank(patientDTO.getAccountNumber())) {
            if (patientDTO.getAccountNumber().length() < 3) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Account Number  " + OMMConstant.ErrorMessages.VALIDATION_ACCOUNTNUMBER_MINLENGTH_ERR_MESSAGE);
            } else if (patientDTO.getAccountNumber().length() > 20) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Account Number " + OMMConstant.ErrorMessages.VALIDATION_ACCOUNTNUMBER_MAXLENGTH_ERR_MESSAGE);
            } else if (!Pattern.matches(validateAccountNumber, patientDTO.getAccountNumber())) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Account Number:" + OMMConstant.ErrorMessages.VALIDATION_ACCOUNTNUMBER_ERR_MESSAGE);
            }
        }
        if (StringUtils.isNotBlank(patientDTO.getLabId())) {
            if (patientDTO.getLabId().length() < 3) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Lab ID  " + OMMConstant.ErrorMessages.VALIDATION_ACCOUNTNUMBER_MINLENGTH_ERR_MESSAGE);
            } else if (patientDTO.getLabId().length() > 20) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Lab ID " + OMMConstant.ErrorMessages.VALIDATION_ACCOUNTNUMBER_MAXLENGTH_ERR_MESSAGE);
            } else if (!Pattern.matches(validateLabId, patientDTO.getLabId())) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Lab ID:" + OMMConstant.ErrorMessages.VALIDATION_LABID_ERR_MESSAGE);
            }
        }
        if (StringUtils.isNotBlank(patientDTO.getClinicName())) {
            String splitString = patientDTO.getClinicName().trim();
            if (patientDTO.getClinicName().length() > 30) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Clinic name  " + OMMConstant.ErrorMessages.VALIDATION_MAX30LENGTH_ERR_MESSAGE);
            } else if ((!Pattern.matches(validateClinicName, patientDTO.getClinicName()))) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Clinic name " + OMMConstant.ErrorMessages.VALIDATION_CLINICNAME_ERR_MESSAGE);
            } else if (splitString.contains("  ")) {
                throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                    "Clinic name:" + OMMConstant.ErrorMessages.VALIDATION_ONESPACE_ERR_MESSAGE);
            }
        }
    }
    
    public void assayFieldValidations(Map<String, Object> assayDetails, OrderParentDTO orderParent)
        throws HMTPException {
        AssayDTO assayDTO = orderParent.getOrder().getAssay();
        OrderDTO orderDTO = orderParent.getOrder();
        Map<String, Boolean> testOptions = assayDTO.getTestOptions();
        boolean check = false;
        for (Entry<String, Boolean> test: testOptions.entrySet()) {
            if (test.getValue().equals(true)) {
                check = true;
                break;
            }
        }
        if (!check) {
            throw new HMTPException("Any one Test Options should be Mandatory");
        }
        assayValidation(assayDTO, assayDetails, orderDTO);
    }
    
    @SuppressWarnings("unchecked") public void
        assayValidation(AssayDTO assayDTO, Map<String, Object> assayDetails, OrderDTO orderDTO) throws HMTPException {
        for (Entry<String, Object> ammValues: assayDetails.entrySet()) {
            if (ammValues.getKey().equals("assayTypeList")) {
                assayTypelist = (List<AssayTypeDTO>) ammValues.getValue();
                if (assayTypelist.stream()
                    .noneMatch(assayTypeDTO -> orderDTO.getAssayType().equalsIgnoreCase(assayTypeDTO.getAssayType()))) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "AssayType " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
            } else if (ammValues.getKey().equals("sampleTypeList")) {
                sampleTypelist = (List<SampleTypeDTO>) ammValues.getValue();
                if (sampleTypelist.stream().noneMatch(
                    sampleTypeDTO -> orderDTO.getSampleType().equalsIgnoreCase(sampleTypeDTO.getSampleType()))) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "SampleType " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
            } else if (ammValues.getKey().equals("inputDataValidationslist")) {
                
                if (assayDTO.getIvfStatus() != null && assayDTO.getIvfStatus().isEmpty()
                    && StringUtils.isNotBlank(assayDTO.getEggDonor())) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "EggDonar " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
                if (assayDTO.getIvfStatus() != null && assayDTO.getIvfStatus().isEmpty()
                    && (StringUtils.isNotBlank(assayDTO.getEggDonor()) || StringUtils.isBlank(assayDTO.getEggDonor()))
                    && assayDTO.getEggDonorAge() != null) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "ivf status and EggDonor " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
                if (assayDTO.getIvfStatus() != null
                    && (assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_FALSE.toString())
                        || assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_MISSING.toString()))
                    && (StringUtils.isNotBlank(assayDTO.getEggDonor()) || StringUtils.isBlank(assayDTO.getEggDonor()))
                    && assayDTO.getEggDonorAge() != null) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "EggDonor and EggDonorAge " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
                boolean ivfStatusEggdonorage = false;
                
                if (assayDTO.getIvfStatus() != null && StringUtils.isNotBlank(assayDTO.getIvfStatus())
                    && assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_TRUE.toString())
                    && StringUtils.isNotBlank(assayDTO.getEggDonor())
                    && (assayDTO.getEggDonor().equals(OMMConstant.Validation.SELF.toString())
                        || assayDTO.getEggDonor().equals(OMMConstant.Validation.NON_SELF.toString()))) {
                    ivfStatusEggdonorage = true;
                }
                
                if (assayDTO.getIvfStatus() != null
                    && assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_TRUE.toString())
                    && assayDTO.getEggDonor().equals(OMMConstant.Validation.STATUS_MISSING.toString())
                    && assayDTO.getEggDonorAge() != null) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        OMMConstant.ErrorMessages.EGG_DONOR_AGE_ERR_MESSAGE.toString()
                            + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
                if (assayDTO.getIvfStatus() != null
                    && assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_TRUE.toString())
                    && (assayDTO.getEggDonor().equals(OMMConstant.Validation.SELF.toString())
                        || assayDTO.getEggDonor().equals(OMMConstant.Validation.NON_SELF.toString()))
                    && assayDTO.getEggDonorAge() == null) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        OMMConstant.ErrorMessages.EGG_DONOR_AGE_ERR_MESSAGE.toString()
                            + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
                
                assayinputdatavalidationdto = (List<AssayInputDataValidationsDTO>) ammValues.getValue();
                for (AssayInputDataValidationsDTO assayinputDataValidationdto: assayinputdatavalidationdto) {
                    if (assayinputDataValidationdto.getFieldName()
                        .equals(OMMConstant.Validation.MATERNAL_AGE.toString())
                        && assayDTO.getMaternalAge() != null
                        && !(assayDTO.getMaternalAge() <= assayinputDataValidationdto.getMaxVal()
                            && assayDTO.getMaternalAge() >= assayinputDataValidationdto.getMinVal()
                            && StringUtils.isNotEmpty(assayDTO.getMaternalAge().toString()))) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "Maternal age " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                    } else if (assayinputDataValidationdto.getFieldName()
                        .equals(OMMConstant.Validation.GESTATIONAL_AGE_DAYS.toString())
                        && assayDTO.getGestationalAgeDays() != null
                        && !(assayDTO.getGestationalAgeDays() <= assayinputDataValidationdto.getMaxVal()
                            && assayDTO.getGestationalAgeDays() >= assayinputDataValidationdto.getMinVal()
                            && StringUtils.isNotEmpty(assayDTO.getGestationalAgeDays().toString()))) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "GestationalAgeDays " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                    } else if (assayinputDataValidationdto.getFieldName()
                        .equals(OMMConstant.Validation.GESTATIONAL_AGE.toString())
                        && assayDTO.getGestationalAgeWeeks() != null
                        && !(assayDTO.getGestationalAgeWeeks() <= assayinputDataValidationdto.getMaxVal()
                            && assayDTO.getGestationalAgeWeeks() >= assayinputDataValidationdto.getMinVal()
                            && StringUtils.isNotEmpty(assayDTO.getGestationalAgeWeeks().toString()))) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "GestationalAgeWeeks " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                    } else if (ivfStatusEggdonorage
                        && assayinputDataValidationdto.getFieldName()
                            .equals(OMMConstant.Validation.EGG_DONOR_AGE.toString())
                        && StringUtils.isNotEmpty(assayDTO.getEggDonorAge().toString())
                        && !(assayDTO.getEggDonorAge() <= assayinputDataValidationdto.getMaxVal()
                            && assayDTO.getEggDonorAge() >= assayinputDataValidationdto.getMinVal())) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            OMMConstant.ErrorMessages.EGG_DONOR_AGE_ERR_MESSAGE.toString()
                                + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                    } else if (assayDTO.getEggDonor() != null && assayDTO.getEggDonorAge() != null
                        && assayDTO.getMaternalAge() != null
                        && assayDTO.getEggDonor().equals(OMMConstant.Validation.SELF.toString())
                        && (assayDTO.getEggDonorAge() > assayDTO.getMaternalAge())) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            OMMConstant.ErrorMessages.EGGDONORAGE_METERNALAGE_ERR_MESSAGE.toString());
                    }
                    
                }
            } else if (ammValues.getKey().equals("assayListData")) {
                
                boolean ivfStatusEmpty = false;
                if (StringUtils.isBlank(assayDTO.getIvfStatus())) {
                    ivfStatusEmpty = true;
                }
                if (assayDTO.getIvfStatus() != null
                    && !(assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_FALSE.toString())
                        || assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_TRUE.toString())
                        || assayDTO.getIvfStatus().equals(OMMConstant.Validation.MISSING.toString()))
                    && !assayDTO.getIvfStatus().isEmpty()) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "IvfStatus should be either Yes or No or Missing ");
                }
                assayListDatalist = (List<AssayListDataDTO>) ammValues.getValue();
                if (!ivfStatusEmpty && assayDTO.getIvfStatus() != null
                    && !(assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_FALSE.toString())
                        || assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_MISSING.toString()))
                    && StringUtils.isBlank(assayDTO.getEggDonor())) {
                    throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                        "EggDonor " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                }
                boolean checkEggDonor = false;
                for (AssayListDataDTO assayListData: assayListDatalist) {
                    
                    if (assayDTO.getIvfStatus() != null
                        && (assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_FALSE.toString())
                            || assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_MISSING.toString()))
                        && (StringUtils.isNotBlank(assayDTO.getEggDonor())
                            || StringUtils.isBlank(assayDTO.getEggDonor()))
                        && assayDTO.getEggDonorAge() != null) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "EggDonor and EggDonarAge " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR);
                    }
                    
                    if (!ivfStatusEmpty
                        && assayListData.getListType().equals(OMMConstant.Validation.IVF_STATUS.toString())
                        && !(assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_TRUE.toString()))) {
                        checkEggDonor = true;
                        if (!(assayDTO.getEggDonor() == null || assayDTO.getEggDonor().isEmpty())) {
                            throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                                "ivf status with Egg donar "
                                    + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                        }
                    }
                    if (!ivfStatusEmpty
                        && OMMConstant.Validation.IVF_STATUS.toString().equals(assayListData.getListType())
                        && StringUtils.isNotBlank(assayDTO.getIvfStatus())
                        && !((Pattern.matches(validateString, assayDTO.getIvfStatus()))
                            && assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_TRUE.toString())
                            || assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_FALSE.toString())
                            || assayDTO.getIvfStatus().equals(OMMConstant.Validation.STATUS_MISSING.toString()))) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "IvfStatus " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                    } else if (!ivfStatusEmpty
                        && OMMConstant.Validation.EGG_DONOR.toString().equals(assayListData.getListType())
                        && !checkEggDonor && StringUtils.isNotBlank(assayDTO.getEggDonor())
                        && !((Pattern.matches(validateString, assayDTO.getEggDonor())
                            || (Pattern.matches(validateStringwithOneSpecialCharacter, assayDTO.getEggDonor())))
                            && assayDTO.getEggDonor().equals(OMMConstant.Validation.SELF.toString())
                            || assayDTO.getEggDonor().equals(OMMConstant.Validation.NON_SELF.toString())
                            || assayDTO.getEggDonor().equals(OMMConstant.Validation.MISSING.toString()))) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "Egg Donor should be either Self or Non-Self or Missing.");
                    } else if (OMMConstant.Validation.FETUS.toString().equals(assayListData.getListType())
                        && StringUtils.isNotBlank(assayDTO.getFetusNumber())
                        && !((Pattern.matches(validateNumericOneDigit, assayDTO.getFetusNumber()))
                            && assayDTO.getFetusNumber().equals(OMMConstant.Validation.FETUS_ONE.toString())
                            || assayDTO.getFetusNumber().equals(OMMConstant.Validation.FETUS_SECOND.toString()))) {
                        throw new HMTPException(CustomErrorCodes.VALIDATION_GEN_STATUS_CODE,
                            "FetusNumber " + OMMConstant.ErrorMessages.VALIDATION_IN_VALID_ERR_MESSAGE);
                    }
                }
            }
        }
    }
    
    public boolean isValidDate(Timestamp collectionDate, Timestamp receivedDate) {
        return receivedDate != null && collectionDate != null && receivedDate.compareTo(collectionDate) >= 0
            && collectionDate.compareTo(new Date()) <= 0 && receivedDate.compareTo(new Date()) <= 0;
    }
    
    public boolean isValidName(String name) {
        if (name != null && !name.isEmpty()) {
            return Pattern.matches("([a-zA-Z-']*)([ ]{0,1})([a-zA-Z]+)(([ ]{0,1})([a-zA-Z-']+))*", name);
        }
        return true;
    }
    
}
