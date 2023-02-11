package com.roche.connect.omm.util;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.hmtp.common.client.exceptions.HMTPException;
import com.hcl.hmtp.common.server.ThreadSessionManager;
import com.hcl.hmtp.common.server.logger.HMTPLogger;
import com.hcl.hmtp.common.server.logger.HMTPLoggerFactory;
import com.hcl.hmtp.common.server.rest.RestClientUtil;
import com.roche.connect.common.amm.dto.AssayInputDataValidationsDTO;
import com.roche.connect.common.amm.dto.AssayListDataDTO;
import com.roche.connect.common.amm.dto.AssayTypeDTO;
import com.roche.connect.common.amm.dto.SampleTypeDTO;
import com.roche.connect.common.amm.dto.TestOptionsDTO;
import com.roche.connect.common.order.dto.OrderParentDTO;
import com.roche.connect.omm.model.Order;
import com.roche.connect.omm.readrepository.OrderReadRepository;
import com.roche.connect.omm.rest.OrderCrudRestApiImpl;
import com.roche.connect.omm.util.OMMConstant.CSV;

@Component
public class BulkOrderValidationUtil {
	@Autowired
	OrderReadRepository orderReadRepository;

	private HMTPLogger logger = HMTPLoggerFactory.getLogger(this.getClass().getName());
	private boolean isError = false;

	public void validateOtherClinicianName(String otherClinicianName, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String oneSpace = CSV.ONE_SPACE.toString();
		String specialCharRes = CSV.SPECIAL_CHAR_RES.toString();
		if (StringUtils.isNotBlank(otherClinicianName) && !Pattern.matches(specialCharRes, otherClinicianName)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed characters are alphabets, hyphen, apostrophe and one space in Other clinician.");
		} else if (StringUtils.isNotBlank(otherClinicianName) && !Pattern.matches(oneSpace, otherClinicianName)) {
			errors.add(CSV.ROW.toString() + row + " Only one space allowed in Other clinician.");
		} else if (StringUtils.isNotBlank(otherClinicianName) && (otherClinicianName.length() > 30)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 30 characters in Other clinician.");
		} else {
			orderParentDTO.getOrder().getPatient().setOtherClinicianName(otherClinicianName);
		}

	}

	public void validateAccessioningId(String accessioningId, int row, OrderParentDTO orderParentDTO,
			List<String> errors, List<String> accessioningIdList) {
		String validateAccessioningIdPos = "^[a-zA-Z0-9-_]+$";
		String validateAccessioningIdNeg = "^[-_]+$";
		if (StringUtils.isEmpty(accessioningId)) {
			errors.add(CSV.ROW.toString() + row + " Please enter the Accessioning ID.");
		} else if (!(StringUtils.isNotEmpty(accessioningId) && accessioningId.length() <= 20)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length for Accessioning ID is 20 characters ");
		} else if (StringUtils.isNotEmpty(accessioningId) && accessioningId.length() <= 20
				&& !Pattern.matches(validateAccessioningIdPos, accessioningId)) {
			errors.add(CSV.ROW.toString() + row + " Allowed special characters are hyphen and underscore for Accessioning ID.");
		} else if (StringUtils.isNotEmpty(accessioningId) && accessioningId.length() <= 20
				&& (Pattern.matches(validateAccessioningIdNeg, accessioningId))) {
			errors.add(CSV.ROW.toString() + row + " Accessioning ID must have at least one alpha numeric character.");
		} else if (isAccessioningDuplicate(accessioningId, accessioningIdList)) {
			errors.add(CSV.ROW.toString() + row + " Accessioning ID already exists. Please enter unique value.");
		} else {
			orderParentDTO.getOrder().setAccessioningId(accessioningId);
		}

	}

	public void validateAssayType(OrderCrudRestApiImpl orderCrudRestApiImpl, String assayType, int row,
			OrderParentDTO orderParentDTO, List<String> errors) {

		if (StringUtils.isEmpty(assayType)) {
			errors.add(CSV.ROW.toString() + row + " Please enter valid Assay Type to proceed with order creation.");
		} else {
			Map<String, Object> totalDetails = orderCrudRestApiImpl.getTotalMap();
			List<AssayTypeDTO> assayTypelist = (List<AssayTypeDTO>) totalDetails.get("assayTypeList");

			if (assayTypelist.stream().anyMatch(e -> assayType.equals(e.getAssayType())))
				orderParentDTO.getOrder().setAssayType(assayType.toUpperCase());
			else
				errors.add(CSV.ROW.toString() + row + " Please enter valid Assay Type to proceed with order creation.");
		}

	}

	public void validateSampleType(OrderCrudRestApiImpl orderCrudRestApiImpl, String sampleType,
			int row, OrderParentDTO orderParentDTO, List<String> errors) {

		if (StringUtils.isEmpty(sampleType)) {
			errors.add(CSV.ROW.toString() + row + " Please enter the valid sample type.");
		} else {

			Map<String, Object> totalDetails = orderCrudRestApiImpl.getTotalMap();
			List<SampleTypeDTO> sampleTypeList = (List<SampleTypeDTO>) totalDetails.get("sampleTypeList");

			if (sampleTypeList.stream().anyMatch(e -> sampleType.equals(e.getSampleType())))
				orderParentDTO.getOrder().setSampleType(sampleType);
			else
				errors.add(CSV.ROW.toString() + row + " Please enter the valid sample type.");
		}
	}

	// retestSample
	public void validateRetestsample(String retestSample, int row, OrderParentDTO orderParentDTO, List<String> errors) {

		if (StringUtils.isEmpty(retestSample) || (!retestSample.equals("Yes") && !retestSample.equals("No"))) {
			errors.add(CSV.ROW.toString() + row + " Please enter Yes / No for Retest sample available.");
		} else {
			orderParentDTO.getOrder().setRetestSample(retestSample.equals("Yes"));
		}

	}

	// testOptionsFetalSex
	public void validateTestOptions(String testOptions, String testOptionsFetalSex, int row,
			OrderParentDTO orderParentDTO, List<String> errors) {
		boolean validTestOptions = true;
		boolean validFetalSex = true;
		if ((testOptions == null || testOptions.isEmpty())) {
			errors.add(CSV.ROW.toString() + row + " Please mention choice of test option for T13 T18 T21.");
		}
		if ((testOptionsFetalSex == null || testOptionsFetalSex.isEmpty())) {
			errors.add(CSV.ROW.toString() + row + " Please mention choice of test option for Fetal sex.");
		}

		if ((testOptions != null && !testOptions.isEmpty()
				&& !(testOptions.equals("No") || testOptions.equals("Yes")))) {
			validTestOptions = false;
			errors.add(CSV.ROW.toString() + row + " Please mention valid choice of test option for T13 T18 T21.");
		}

		if ((testOptionsFetalSex != null && !testOptionsFetalSex.isEmpty()
				&& !(testOptionsFetalSex.equals("No") || testOptionsFetalSex.equals("Yes")))) {
			validFetalSex = false;
			errors.add(CSV.ROW.toString() + row + " Please mention valid choice of test option for Fetal sex.");
		}

		if (testOptions != null && !testOptions.isEmpty() && testOptionsFetalSex != null
				&& !testOptionsFetalSex.isEmpty() && testOptions.equals("No") && testOptionsFetalSex.equals("No")) {
			errors.add(CSV.ROW.toString() + row + " Please enter Yes for at least one Test option.");
		} else if (validTestOptions && validFetalSex && testOptions != null && testOptionsFetalSex != null) {
			Map<String, Boolean> testOptionsData = new HashMap<>();
			testOptionsData.put("Harmony Prenatal Test (T21, T18, T13)", testOptions.equals("Yes"));
			testOptionsData.put("Fetal Sex", testOptionsFetalSex.equals("Yes"));

			orderParentDTO.getOrder().getAssay().setTestOptions(testOptionsData);
		}

	}

	// orderComments
	public void validateOrderComments(String orderComments, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {

		if (orderComments.length() > 150) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 150 characters for Comments.");
		} else {
			orderParentDTO.getOrder().setOrderComments(orderComments);
		}

	}

	public void validateCollectionDate(String collectionDate, int row, OrderParentDTO orderParentDTO,
			List<String> errors, String timeZone) {
		isError = false;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CSV.ISO_DATE_PATTERN.toString());
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		try {
			simpleDateFormat.setLenient(false);
			Timestamp ts = new Timestamp(simpleDateFormat.parse(collectionDate).getTime());
			orderParentDTO.getOrder().getAssay().setCollectionDate(ts);
		} catch (Exception e) {
			isError = true;
			errors.add(CSV.ROW.toString() + row + " Please enter the Collection date in the MM/DD/YYYY format.");
		}
	}

	public void validateReceivedDate(String receivedDate, int row, OrderParentDTO orderParentDTO,
			List<String> errors, String timeZone) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CSV.ISO_DATE_PATTERN.toString());
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		try {
			simpleDateFormat.setLenient(false);
			Timestamp ts = new Timestamp(simpleDateFormat.parse(receivedDate).getTime());
			orderParentDTO.getOrder().getAssay().setReceivedDate(ts);
			if(!isError) {
				if (orderParentDTO.getOrder().getAssay().getCollectionDate().compareTo(new Date()) <= 0
						&& orderParentDTO.getOrder().getAssay().getReceivedDate().compareTo(new Date()) <= 0) {

					if (!isValidDate(orderParentDTO.getOrder().getAssay().getCollectionDate(),
							orderParentDTO.getOrder().getAssay().getReceivedDate())) {
						errors.add(CSV.ROW.toString() + row + " Received date should be greater than or equal to the collection date.");
					}
				} else {
					validateWithOtherDates(orderParentDTO, errors, row);
				}
			}
		} catch (Exception e) {
			isError = true;
			errors.add(CSV.ROW.toString() + row + " Please enter the Received date in the MM/DD/YYYY format.");
		}

	}

	private void validateWithOtherDates(OrderParentDTO orderParentDTO, List<String> errors, int row) {
		if (orderParentDTO.getOrder().getAssay().getCollectionDate().compareTo(new Date()) > 0) {
			errors.add(CSV.ROW.toString() + row + " Collection date should be less than or equal to current date.");
		}
		if (orderParentDTO.getOrder().getAssay().getReceivedDate().compareTo(new Date()) > 0) {
			errors.add(CSV.ROW.toString() + row + " Received date should be less than or equal to current date.");
		}
		
	}

	// M-Age
	public void validateMaternalAgeSampleType(OrderCrudRestApiImpl orderCrudRestApiImpl,
			String maternalAge, int row, OrderParentDTO orderParentDTO, List<String> errors) {
		
		long minValue = 0L;
		long maxValue = 0L;
		try {
			if (maternalAge != null && !StringUtils.isEmpty(maternalAge)) {
				Map<String, Object> assayDetails = orderCrudRestApiImpl.getTotalMap();
				List<AssayInputDataValidationsDTO> assayInputDataValidationsDTO = (List<AssayInputDataValidationsDTO>) assayDetails
						.get(CSV.INPUT_DATA_VALIDATION_LIST.toString());
				//
				for (AssayInputDataValidationsDTO assayinputDataValidationdto : assayInputDataValidationsDTO) {
					minValue = assayinputDataValidationdto.getMinVal();
					maxValue = assayinputDataValidationdto.getMaxVal();
					if (assayinputDataValidationdto.getFieldName()
							.equals(OMMConstant.Validation.MATERNAL_AGE.toString())
							&& !(Long.valueOf(maternalAge) <= assayinputDataValidationdto.getMaxVal()
									&& Long.valueOf(maternalAge) >= assayinputDataValidationdto.getMinVal()
									&& StringUtils.isNotEmpty(maternalAge))) {
						errors.add(CSV.ROW.toString() + row + " Allowed Maternal age range is from "+assayinputDataValidationdto.getMinVal()+" to "+assayinputDataValidationdto.getMaxVal()+".");

					} else {
						orderParentDTO.getOrder().getAssay().setMaternalAge(Integer.valueOf(maternalAge));
					}
				}

			}
		} catch (Exception e) {
			errors.add(CSV.ROW.toString() + row + " Allowed Maternal age range is from "+minValue+" to "+maxValue+".");
		}

	}

	// M-Age
	public void validateGestationalAge(OrderCrudRestApiImpl orderCrudRestApiImpl, 
			String gestationalAgeWeeks, int row, OrderParentDTO orderParentDTO, List<String> errors) {
		long minValue = 0L;
		long maxValue = 0L;
		try {
			if (gestationalAgeWeeks != null && !StringUtils.isEmpty(gestationalAgeWeeks)) {
				Map<String, Object> assayDetails = orderCrudRestApiImpl.getTotalMap();
				List<AssayInputDataValidationsDTO> assayInputDataValidationsDTO = (List<AssayInputDataValidationsDTO>) assayDetails
						.get(CSV.INPUT_DATA_VALIDATION_LIST.toString());
				//
				for (AssayInputDataValidationsDTO assayinputDataValidationdto : assayInputDataValidationsDTO) {
					minValue = assayinputDataValidationdto.getMinVal();
					maxValue = assayinputDataValidationdto.getMaxVal();
					if (assayinputDataValidationdto.getFieldName()
							.equals(OMMConstant.Validation.GESTATIONAL_AGE.toString())
							&& !(Long.valueOf(gestationalAgeWeeks) <= assayinputDataValidationdto.getMaxVal()
									&& Long.valueOf(gestationalAgeWeeks) >= assayinputDataValidationdto.getMinVal()
									&& StringUtils.isNotEmpty(gestationalAgeWeeks))) {
						errors.add(CSV.ROW.toString() + row + " Please enter the Gestational age in weeks ranging "+assayinputDataValidationdto.getMinVal()+ " to "+assayinputDataValidationdto.getMaxVal()+".");

					} else {
						orderParentDTO.getOrder().getAssay()
								.setGestationalAgeWeeks(Integer.valueOf(gestationalAgeWeeks));
					}
				}
			}

		} catch (Exception e) {
			errors.add(CSV.ROW.toString() + row + " Please enter the Gestational age in weeks ranging "+minValue+ " to "+maxValue+".");
		}
	}

	// G-Age days
	public void validateGestationalAgeDays(OrderCrudRestApiImpl orderCrudRestApiImpl,
			String gestationalAgeDays, int row, OrderParentDTO orderParentDTO, List<String> errors) {
		long minValue = 0L;
		long maxValue = 0L;
		try {
			if (gestationalAgeDays != null && !StringUtils.isEmpty(gestationalAgeDays)) {
				Map<String, Object> assayDetails = orderCrudRestApiImpl.getTotalMap();
				List<AssayInputDataValidationsDTO> assayInputDataValidationsDTO = (List<AssayInputDataValidationsDTO>) assayDetails
						.get(CSV.INPUT_DATA_VALIDATION_LIST.toString());
				//
				for (AssayInputDataValidationsDTO assayinputDataValidationdto : assayInputDataValidationsDTO) {
					minValue = assayinputDataValidationdto.getMinVal();
					maxValue = assayinputDataValidationdto.getMaxVal();
					if (assayinputDataValidationdto.getFieldName()
							.equals(OMMConstant.Validation.GESTATIONAL_AGE_DAYS.toString())
							&& !(Long.valueOf(gestationalAgeDays) <= assayinputDataValidationdto.getMaxVal()
									&& Long.valueOf(gestationalAgeDays) >= assayinputDataValidationdto.getMinVal()
									&& StringUtils.isNotEmpty(gestationalAgeDays))) {
						errors.add(CSV.ROW.toString() + row + "Please enter the Gestational age in days ranging "+assayinputDataValidationdto.getMinVal()+" to "+assayinputDataValidationdto.getMaxVal()+".");

					} else {
						orderParentDTO.getOrder().getAssay().setGestationalAgeDays(Integer.valueOf(gestationalAgeDays));
					}
				}
			}
		} catch (Exception e) {
			errors.add(CSV.ROW.toString() + row + "Please enter the Gestational age in days ranging "+minValue+" to "+maxValue+".");
		}
	}

	// EggDonor
	public void validateEggDonor(String ivfStatus, String eggDonor, String eggDonorAge, String maternalAge, int row,
			OrderParentDTO orderParentDTO, List<String> errors) {
		try {
			if (!(ivfStatus.equals(OMMConstant.Validation.STATUS_FALSE.toString())
							|| ivfStatus.equals(OMMConstant.Validation.STATUS_TRUE.toString())
							|| ivfStatus == null || ivfStatus.isEmpty()
							|| ivfStatus.equals(OMMConstant.Validation.MISSING.toString()))) {
				errors.add(CSV.ROW.toString() + row + " Please enter Yes/No/Missing IVF status value.");
			} else if (ivfStatus.equals("Yes")) {
				checkEggDonar(eggDonor, eggDonorAge, maternalAge, orderParentDTO, errors, row);
			}

			orderParentDTO.getOrder().getAssay().setIvfStatus(ivfStatus);

			if ((!ivfStatus.equals("Yes")) && (eggDonor != null && !eggDonor.isEmpty())) {
				errors.add(CSV.ROW.toString() + row + " Egg donor details not required basis the IVF status for this sample.");
			}
			if ((!ivfStatus.equals("Yes")) && (eggDonorAge != null && !eggDonorAge.isEmpty())) {
				errors.add(
						CSV.ROW.toString() + row + " Egg donor age is not required basis the Egg donor value for this sample.");
			}
		} catch (Exception e) {
			errors.add(CSV.ROW.toString() + row + " Allowed Egg donor age range is from 12 to 76.");
		}
	}

	private void checkEggDonar(String eggDonor, String eggDonorAge, String maternalAge, OrderParentDTO orderParentDTO, List<String> errors, int row) {
		if ((eggDonor == null || eggDonor.isEmpty())
				|| !(eggDonor.equals(OMMConstant.Validation.SELF.toString())
						|| eggDonor.equals(OMMConstant.Validation.NON_SELF.toString())
						|| eggDonor.equals(OMMConstant.Validation.MISSING.toString()))) {

			errors.add(CSV.ROW.toString() + row + " Please enter valid Egg donor value.");

		} else if (eggDonor.equals(OMMConstant.Validation.SELF.toString())
				|| eggDonor.equals(OMMConstant.Validation.NON_SELF.toString())) {
			if (eggDonorAge == null || eggDonorAge.isEmpty()) {
				errors.add(CSV.ROW.toString() + row + " Please enter the Egg donor age. ");
			} else if (Integer.parseInt(eggDonorAge) < 12 || Integer.parseInt(eggDonorAge) > 76) {
				errors.add(CSV.ROW.toString() + row + " Allowed Egg donor age range is from 12 to 76.");
			} else if(eggDonor.equals(OMMConstant.Validation.SELF.toString()) && maternalAge != null && !StringUtils.isEmpty(maternalAge) && (Integer.parseInt(eggDonorAge) > Integer.parseInt(maternalAge))){
				errors.add(CSV.ROW.toString() + row + " Egg donor age should be < = Maternal age.");
			}else {
				orderParentDTO.getOrder().getAssay().setEggDonorAge(Integer.parseInt(eggDonorAge));
			}
		} else if ((!(eggDonor.equals(OMMConstant.Validation.SELF.toString())
				|| eggDonor.equals(OMMConstant.Validation.NON_SELF.toString())))
				&& (eggDonorAge != null && !eggDonorAge.isEmpty())) {
			errors.add(CSV.ROW.toString() + row
					+ " Egg donor age is not required basis the Egg donor value for this sample.");
		}
		orderParentDTO.getOrder().getAssay().setEggDonor(eggDonor);
		
	}

	// Fetus Number
	public void validateFetusNumber(OrderCrudRestApiImpl orderCrudRestApiImpl, String fetusNumber,
			int row, OrderParentDTO orderParentDTO, List<String> errors) {
		try {
			String validateNumericOneDigit = "[1-2]{1}";
			Map<String, Object> assayDetails = orderCrudRestApiImpl.getTotalMap();
			List<AssayListDataDTO> assayListDataDTO = (List<AssayListDataDTO>) assayDetails.get("assayListData");
			//
			for (AssayListDataDTO assayinputListDataDTO : assayListDataDTO) {
				if (OMMConstant.Validation.FETUS.toString().equals(assayinputListDataDTO.getListType())
						&& StringUtils.isNotBlank(fetusNumber)
						&& !((Pattern.matches(validateNumericOneDigit, fetusNumber))
								&& fetusNumber.equals(OMMConstant.Validation.FETUS_ONE.toString())
								|| fetusNumber.equals(OMMConstant.Validation.FETUS_SECOND.toString()))) {
					errors.add(CSV.ROW.toString() + row + " Please enter valid Number of fetus.");
					break;

				}

				orderParentDTO.getOrder().getAssay().setFetusNumber(fetusNumber);
			}
		} catch (Exception e) {
			errors.add(CSV.ROW.toString() + row + " Please enter valid Number of fetus.");
		}

	}

	public void validatePatientMedicalRecNo(String patientMedicalRecNo, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		if (StringUtils.isNotBlank(patientMedicalRecNo) && (patientMedicalRecNo.length() > 15)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 15 characters in Medical record number.");
		} else if (StringUtils.isNotBlank(patientMedicalRecNo)
				&& !(Pattern.matches("[A-Za-z0-9+]+", patientMedicalRecNo))) {
			errors.add(CSV.ROW.toString() + row + " Only  alpha numeric characters are allowed in Medical record number.");
		} else {
			orderParentDTO.getOrder().getPatient().setPatientMedicalRecNo(patientMedicalRecNo);
		}

	}

	public void validatePatientDOB(String patientDOB, int row, OrderParentDTO orderParentDTO, List<String> errors, String timeZone) {
		if(StringUtils.isNotBlank(patientDOB)){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CSV.ISO_DATE_PATTERN.toString());
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		try {
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(patientDOB);
			orderParentDTO.getOrder().getPatient().setPatientDOB(patientDOB);
		} catch (Exception e) {
			errors.add(CSV.ROW.toString() + row + " Enter Date of birth in MM/DD/YYYY format.");
		}
	}
	}

	// check
	public boolean isAccessioningDuplicate(String accessioningId2, List<String> accessioningIdList) {
		long domainId = 0;
		boolean duplicateAccessiongId = false;
		boolean duplicateAccessiongIdInSheet = false;
		domainId = ThreadSessionManager.currentUserSession().getAccessorCompanyId();
		String accessioningId = accessioningId2;
		if (!accessioningIdList.isEmpty()) {
			duplicateAccessiongIdInSheet = accessioningIdList.contains(accessioningId2);
		}
		accessioningIdList.add(accessioningId2);
		List<Order> orderaccessioningId = orderReadRepository.findAllByAccessioningId(accessioningId, domainId);
		if ((!orderaccessioningId.isEmpty()) || duplicateAccessiongIdInSheet) {
			duplicateAccessiongId = true;
		}
		return duplicateAccessiongId;

	}

	public Map<String, Object> assayValidations(OrderCrudRestApiImpl orderCrudRestApiImpl, String assayType)
			throws HMTPException {
		List<AssayTypeDTO> assayTypelist = new ArrayList<>();
		List<SampleTypeDTO> sampleTypelist = new ArrayList<>();
		List<AssayListDataDTO> assayListData = new ArrayList<>();
		List<TestOptionsDTO> testOptionlist = new ArrayList<>();
		List<AssayInputDataValidationsDTO> inputDataValidationslist = new ArrayList<>();
		String apiPath = CSV.ASSAY_API_PATH.toString();
		String api = "pas.amm_api_url";
		String utf = "UTF-8";
		orderCrudRestApiImpl.setTotalMap(new HashMap<>());
		try {
			logger.info("Calling AMM to find assay types");
			String urlassayType = RestClientUtil.getUrlString(api, "", apiPath + "?assayType=" + assayType, "", null);
			Invocation.Builder orderClient = RestClientUtil.getBuilder(URLEncoder.encode(urlassayType, utf), null);
			assayTypelist = orderClient.get(new GenericType<List<AssayTypeDTO>>() {
			});
			orderCrudRestApiImpl.getTotalMap().put("assayTypeList", assayTypelist);
			logger.info("Calling AMM to SampleType");
			String urlSampleType = RestClientUtil.getUrlString(api, "", apiPath + assayType + "/sampletype", "", null);
			Invocation.Builder orderClientSample = RestClientUtil.getBuilder(URLEncoder.encode(urlSampleType, utf),
					null);
			sampleTypelist = orderClientSample.get(new GenericType<List<SampleTypeDTO>>() {
			});
			orderCrudRestApiImpl.getTotalMap().put("sampleTypeList", sampleTypelist);

			logger.info("Calling AMM to assayListData");
			String urlassayList = RestClientUtil.getUrlString(api, "", apiPath + assayType + "/listdata", "", null);
			Invocation.Builder orderClientAssayList = RestClientUtil.getBuilder(URLEncoder.encode(urlassayList, utf),
					null);
			assayListData = orderClientAssayList.get(new GenericType<List<AssayListDataDTO>>() {
			});
			orderCrudRestApiImpl.getTotalMap().put("assayListData", assayListData);

			logger.info("Calling AMM to testOptionlist");
			String urltestOptions = RestClientUtil.getUrlString(api, "", apiPath + assayType + "/testoptions", "",
					null);
			Invocation.Builder orderClienttestOptions = RestClientUtil
					.getBuilder(URLEncoder.encode(urltestOptions, utf), null);
			testOptionlist = orderClienttestOptions.get(new GenericType<List<TestOptionsDTO>>() {
			});

			orderCrudRestApiImpl.getTotalMap().put("testOptionlist", testOptionlist);

			logger.info("Calling AMM to inputDataValidationslist");
			String urlinputDataValidations = RestClientUtil.getUrlString(api, "",
					apiPath + assayType + "/inputdatavalidations", "", null);
			Invocation.Builder orderClientinputDataValidations = RestClientUtil
					.getBuilder(URLEncoder.encode(urlinputDataValidations, utf), null);
			inputDataValidationslist = orderClientinputDataValidations
					.get(new GenericType<List<AssayInputDataValidationsDTO>>() {
					});
			orderCrudRestApiImpl.getTotalMap().put(CSV.INPUT_DATA_VALIDATION_LIST.toString(), inputDataValidationslist);

		} catch (Exception exp) {
			logger.error("Error occurred while calling at AMM Api's" + exp.getMessage());
			throw new HMTPException(exp);
		}
		return orderCrudRestApiImpl.getTotalMap();
	}

	/**public void validateOtherClinicianFaxNo(String otherClinicianFaxNo, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String validateOnlyNumericwithplussymbol = CSV.VALIDATE_ONY_NUMERIC.toString();

		if (StringUtils.isNotBlank(otherClinicianFaxNo)
				&& Pattern.matches(validateOnlyNumericwithplussymbol, otherClinicianFaxNo)
				&& (otherClinicianFaxNo.length() >= 7) && (otherClinicianFaxNo.length() <= 20)) {
			orderParentDTO.getOrder().getPatient().setOtherClinicianFaxNo(otherClinicianFaxNo);
		} else if (StringUtils.isNotBlank(otherClinicianFaxNo) && (otherClinicianFaxNo.length() < 7)) {
			errors.add(CSV.ROW.toString() + row + " Allowed minimum field length is 7 characters in Other clinician fax number.");
		} else if (StringUtils.isNotBlank(otherClinicianFaxNo) && (otherClinicianFaxNo.length() > 20)) {
			errors.add(
					CSV.ROW.toString() + row + " Allowed maximum field length is 20 characters in Other clinician fax number.");
		} else if (StringUtils.isNotBlank(otherClinicianFaxNo)
				&& !Pattern.matches(validateOnlyNumericwithplussymbol, otherClinicianFaxNo)) {
			errors.add(CSV.ROW.toString() + row + " Only numeric characters are allowed in Other clinician fax number.");
		}
	}*/

	public void validateRefClinicianClinicName(String refClinicianClinicName, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String oneSpace = CSV.ONE_SPACE.toString();
		String specialCharRes = CSV.SPECIAL_CHAR_RES.toString();
		if (StringUtils.isNotBlank(refClinicianClinicName)
				&& !Pattern.matches(specialCharRes, refClinicianClinicName)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed characters are alphabets, hyphen, apostrophe and one space in Referring clinician's clinic name.");
		} else if (StringUtils.isNotBlank(refClinicianClinicName)
				&& !Pattern.matches(oneSpace, refClinicianClinicName)) {
			errors.add(CSV.ROW.toString() + row + " Only one space allowed in clinician's clinic name.");
		} else if (StringUtils.isNotBlank(refClinicianClinicName) && (refClinicianClinicName.length() > 30)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed maximum field length is 30 characters in the Referring clinician's clinic name.");
		} else {
			orderParentDTO.getOrder().getPatient().setClinicName(refClinicianClinicName);
		}

	}

	/**public void validateRefClinicianFaxNo(String refClinicianFaxNo, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {

		String validateOnlyNumericwithplussymbol = CSV.VALIDATE_ONY_NUMERIC.toString();

		if (StringUtils.isNotBlank(refClinicianFaxNo)
				&& Pattern.matches(validateOnlyNumericwithplussymbol, refClinicianFaxNo)
				&& (refClinicianFaxNo.length() >= 7) && (refClinicianFaxNo.length() <= 20)) {
			orderParentDTO.getOrder().getPatient().setRefClinicianFaxNo(refClinicianFaxNo);
		} else if (StringUtils.isNotBlank(refClinicianFaxNo) && (refClinicianFaxNo.length() < 7)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed minimum field length is 7 characters in Referring clinician fax number.");
		} else if (StringUtils.isNotBlank(refClinicianFaxNo) && (refClinicianFaxNo.length() > 20)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed maximum field length is 20 characters in Referring clinician fax number.");
		} else if (StringUtils.isNotBlank(refClinicianFaxNo)
				&& !Pattern.matches(validateOnlyNumericwithplussymbol, refClinicianFaxNo)) {
			errors.add(CSV.ROW.toString() + row + " Only numeric characters are allowed in Referring clinician fax number.");
		}
	}*/

	public void validateRefClinicianName(String refClinicianName, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String oneSpace = CSV.ONE_SPACE.toString();
		String specialCharRes = CSV.SPECIAL_CHAR_RES.toString();
		if (StringUtils.isNotBlank(refClinicianName) && !Pattern.matches(specialCharRes, refClinicianName)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed characters are alphabets, hyphen, apostrophe and one space in Referring clinician.");
		} else if (StringUtils.isNotBlank(refClinicianName) && !Pattern.matches(oneSpace, refClinicianName)) {
			errors.add(CSV.ROW.toString() + row + " Only one space allowed in Referring clinician.");
		} else if (StringUtils.isNotBlank(refClinicianName) && (refClinicianName.length() > 30)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 30 characters in Referring clinician.");
		} else {
			orderParentDTO.getOrder().getPatient().setRefClinicianName(refClinicianName);
		}

	}

	/**public void validateTreatingDoctorName(String treatingDoctorName, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String oneSpace = CSV.ONE_SPACE.toString();
		String specialCharRes = CSV.SPECIAL_CHAR_RES.toString();
		if (StringUtils.isNotBlank(treatingDoctorName) && !Pattern.matches(specialCharRes, treatingDoctorName)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed characters are alphabets, hyphen, apostrophe and one space in Treating doctor.");
		} else if (StringUtils.isNotBlank(treatingDoctorName) && !Pattern.matches(oneSpace, treatingDoctorName)) {
			errors.add(CSV.ROW.toString() + row + " Only one space allowed in Treating doctor.");
		} else if (StringUtils.isNotBlank(treatingDoctorName) && (treatingDoctorName.length() > 30)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 30 characters in Treating doctor.");
		} else {
			orderParentDTO.getOrder().getPatient().setTreatingDoctorName(treatingDoctorName);
		}

	}
*/
	/**public void validateTreatingDoctorContactNo(String treatingDoctorContactNo, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {

		String validateOnlyNumericwithplussymbol = CSV.VALIDATE_ONY_NUMERIC.toString();

		if (StringUtils.isNotBlank(treatingDoctorContactNo)
				&& Pattern.matches(validateOnlyNumericwithplussymbol, treatingDoctorContactNo)
				&& (treatingDoctorContactNo.length() >= 7) && (treatingDoctorContactNo.length() <= 20)) {
			orderParentDTO.getOrder().getPatient().setTreatingDoctorContactNo(treatingDoctorContactNo);
		} else if (StringUtils.isNotBlank(treatingDoctorContactNo) && (treatingDoctorContactNo.length() < 7)) {
			errors.add(CSV.ROW.toString() + row + " Minimum field length is 7 characters in Treating doctor phone number.");
		} else if (StringUtils.isNotBlank(treatingDoctorContactNo) && (treatingDoctorContactNo.length() > 20)) {
			errors.add(
					CSV.ROW.toString() + row + " Allowed maximum field length is 20 characters in Treating doctor phone number.");
		} else if (StringUtils.isNotBlank(treatingDoctorContactNo)
				&& !Pattern.matches(validateOnlyNumericwithplussymbol, treatingDoctorContactNo)) {
			errors.add(CSV.ROW.toString() + row + " Only numeric characters are allowed in Treating doctor phone number.");
		}
	}*/

	/**public void validatePatientContactNo(String patientContactNo, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {

		String validateOnlyNumericwithplussymbol = CSV.VALIDATE_ONY_NUMERIC.toString();

		if (StringUtils.isNotBlank(patientContactNo)
				&& Pattern.matches(validateOnlyNumericwithplussymbol, patientContactNo)
				&& (patientContactNo.length() >= 7) && (patientContactNo.length() <= 20)) {
			orderParentDTO.getOrder().getPatient().setPatientContactNo(patientContactNo);
		} else if (StringUtils.isNotBlank(patientContactNo) && (patientContactNo.length() < 7)) {
			errors.add(CSV.ROW.toString() + row + " Minimum field length is 7 characters in Phone number.");
		} else if (StringUtils.isNotBlank(patientContactNo) && (patientContactNo.length() > 20)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 20 characters in Phone number.");
		} else if (StringUtils.isNotBlank(patientContactNo)
				&& !Pattern.matches(validateOnlyNumericwithplussymbol, patientContactNo)) {
			errors.add(CSV.ROW.toString() + row + " Only numeric characters are allowed in Phone number.");
		}
	}*/

	/**public void validatePatientGender(String patientGender, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		if (StringUtils.isNotBlank(patientGender) && !(patientGender.equals("Female"))) {
			errors.add(CSV.ROW.toString() + row + " Please enter valid Gender.");
		} else if (patientGender == null || patientGender.isEmpty()) {
			errors.add(CSV.ROW.toString() + row + " Please enter valid Gender.");
		} else {
			orderParentDTO.getOrder().getPatient().setPatientGender(patientGender);
		}

	}*/

	public void validatePatientFirstName(String patientFirstName, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String oneSpace = CSV.ONE_SPACE.toString();
		String specialCharRes = CSV.SPECIAL_CHAR_RES.toString();
		if (StringUtils.isNotBlank(patientFirstName) && !Pattern.matches(specialCharRes, patientFirstName)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed characters are alphabets, hyphen, apostrophe and one space in First name.");
		} else if (StringUtils.isNotBlank(patientFirstName) && !Pattern.matches(oneSpace, patientFirstName)) {
			errors.add(CSV.ROW.toString() + row + " Only one space allowed in First name.");
		} else if (StringUtils.isNotBlank(patientFirstName) && (patientFirstName.length() > 30)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 30 characters in First name.");
		} else {
			orderParentDTO.getOrder().getPatient().setPatientFirstName(patientFirstName);
		}

	}

	public void validatePatientLastName(String patientLastName, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		String oneSpace = CSV.ONE_SPACE.toString();
		String specialCharRes = CSV.SPECIAL_CHAR_RES.toString();
		if (StringUtils.isNotBlank(patientLastName) && !Pattern.matches(specialCharRes, patientLastName)) {
			errors.add(CSV.ROW.toString() + row
					+ " Allowed characters are alphabets, hyphen, apostrophe and one space in Last name.");
		} else if (StringUtils.isNotBlank(patientLastName) && !Pattern.matches(oneSpace, patientLastName)) {
			errors.add(CSV.ROW.toString() + row + " Only one space allowed in Last name.");
		} else if (StringUtils.isNotBlank(patientLastName) && (patientLastName.length() > 30)) {
			errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 30 characters in Last name.");
		} else {
			orderParentDTO.getOrder().getPatient().setPatientLastName(patientLastName);
		}

	}

	public boolean isValidDate(Timestamp collectionDate, Timestamp receivedDate) {
		return (receivedDate != null && collectionDate != null && receivedDate.compareTo(collectionDate) >= 0);
	}

	public void validateLabId(String labId, int row, OrderParentDTO orderParentDTO, List<String> errors) {
		if(labId != null && !labId.isEmpty()) {
			if(labId.length() < 3) {
				errors.add(CSV.ROW.toString() + row + " Allowed minimum field length is 3 characters in the Lab ID.");
			}else if(labId.length() > 20) {
				errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 20 characters in the Lab ID.");
			}else if(!labId.matches("[A-Za-z0-9-]+")) {
				errors.add(CSV.ROW.toString() + row + " Allowed characters are alphabets, numbers and hyphen in Lab ID.");
			}else {
				orderParentDTO.getOrder().getPatient().setLabId(labId);
			}
		}
		
	}

	public void validateAccountNumber(String accountNo, int row, OrderParentDTO orderParentDTO, List<String> errors) {
		if(accountNo != null && !accountNo.isEmpty()) {
			if(accountNo.length() < 3) {
				errors.add(CSV.ROW.toString() + row + " Allowed minimum field length is 3 characters in the Account #.");
			}else if(accountNo.length() > 20) {
				errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 20 characters in the Account #.");
			}else if(!accountNo.matches("[A-Za-z0-9]+")) {
				errors.add(CSV.ROW.toString() + row + " Allowed characters are alphabets and numbers in Account #.");
			}else {
				orderParentDTO.getOrder().getPatient().setAccountNumber(accountNo);
			}
		}
		
	}

	public void validateReasonForReferral(String reasonForReferral, int row, OrderParentDTO orderParentDTO,
			List<String> errors) {
		if(reasonForReferral != null && !reasonForReferral.isEmpty()) {
			if(reasonForReferral.length() > 40) {
				errors.add(CSV.ROW.toString() + row + " Allowed maximum field length is 40 characters for Reason for referral.");
			}else {
				orderParentDTO.getOrder().getPatient().setReasonForReferral(reasonForReferral);
			}
		}
		
	}

}
