/**
 * StringConstants is the class which holds the information of all constants used in the application
 * It reduces the complexity of the application
 */
export class StringConstants {
    /**
     * Maintain the constant name
     */
    public static stringLibrary = {
        CANCEL_UPDATE_ORDER : 'Do you want to proceed without saving the changes'
    };

    /**
     * STRING() takes msgKey as input and returns the matched value from the stringLibrary
     * @param messageKey Key
     */
    public static STRING(messageKey: string): string {
        return this.stringLibrary[messageKey];
    }
 }

export enum SnackbarClasses {
    errorBottom1 = 'failed-snackbar-bottom1',
    errorBottom2 = 'failed-snackbar-bottom2',
    successBottom1 = 'success-snackbar-bottom1',
    successBottom2 = 'success-snackbar-bottom2'
}

export enum TosterClasses {
    InfoBottom1 = 'info-toster-bottom1',
    InfoBottom2 = 'info-toster-bottom2',
    WarningBottom1 = 'warn-toster-bottom1',
    WarningBottom2 = 'warn-toster-bottom2',
    ErrorBottom1 = 'error-toster-bottom1',
    ErrorBottom2 = 'error-toster-bottom2'
}

export enum ProcessStepNames {
    ANALYSIS = 'Analysis'
}

export enum IconsInfo {
    WARNING = 'rc-warning',
    ERROR = 'rc-error',
    WARNING_30 = 'rc-warning-30',
    ERROR_30 = 'rc-error-30'
}

export enum RequiredFieldValidationsForOrder {
    COMMENTS = 'Comments',
    MATERNAL_AGE = 'Maternal age',
    GESTATIONAL_AGE = 'Gestational age',
    IVF_STATUS = 'IVF status',
    EGG_DONOR = 'Egg Donor',
    EGG_DONOR_AGE = 'Egg donor age',
    NUMBER_OF_FETUS = 'Number of fetus',
    FIRST_NAME = 'First name',
    LAST_NAME = 'Last name',
    MEDICAL_REC_NUMBER = 'Medical record number',
    DOB = 'DOB',
    REFERRING_CLINICIAN = 'Referring clinician',
    LABORATORY_ID = 'Laboratory ID',
    OTHER_CLINICIAN = 'Other clinician',
    CLINIC_NAME = 'Clinic name',
    REASON_FOR_REFERRAL = 'Reason for Referral',
    ACCOUNT_NUMBER = 'Account #'
}

export enum OrderSections {
    ORDER = 'order',
    ASSAY = 'assay',
    PATIENT = 'patient'
}

interface DefaultKey {
    dbKey: string;
    orderKey: string;
}

export const patientInfo: DefaultKey[] = [
    {
      dbKey: RequiredFieldValidationsForOrder.FIRST_NAME,
      orderKey: 'patientFirstName'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.LAST_NAME,
      orderKey: 'patientLastName'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.DOB,
      orderKey: 'patientDOB'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.MEDICAL_REC_NUMBER,
      orderKey: 'patientMedicalRecNo'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.LABORATORY_ID,
      orderKey: 'labId'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.ACCOUNT_NUMBER,
      orderKey: 'accountNumber'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.OTHER_CLINICIAN,
      orderKey: 'otherClinicianName'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.CLINIC_NAME,
      orderKey: 'clinicName'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.REFERRING_CLINICIAN,
      orderKey: 'refClinicianName'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.REASON_FOR_REFERRAL,
      orderKey: 'reasonForReferral'
    }
  ];

  export const assayInfo: DefaultKey[] = [
    {
      dbKey: RequiredFieldValidationsForOrder.MATERNAL_AGE,
      orderKey: 'maternalAge'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.GESTATIONAL_AGE,
      orderKey: 'gestationalAgeWeeks'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.GESTATIONAL_AGE,
      orderKey: 'gestationalAgeDays'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.IVF_STATUS,
      orderKey: 'ivfStatus'
    },
    {
      dbKey: RequiredFieldValidationsForOrder.NUMBER_OF_FETUS,
      orderKey: 'fetusNumber'
    }
  ];

  export const orderDetailsInfo: DefaultKey[] = [
    {
      dbKey: RequiredFieldValidationsForOrder.COMMENTS,
      orderKey: 'orderComments'
    }
  ];

  export enum FormFieldNames {
    MATERNAL_AGE = 'Maternal age at delivery',
    GESTATIONAL_AGE_WEEKS = 'Gestational age at blood draw weeks',
    GESTATIONAL_AGE_DAYS = 'Gestational age at blood draw days',
    EGG_DONOR_AGE = 'Egg Donor Age at collection'
  }

export const multipleNotfMsg = 'There are multiple notifications awaiting confirmation. Please go to Message center to confirm.';
