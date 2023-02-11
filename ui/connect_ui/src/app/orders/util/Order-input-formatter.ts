interface OrderInputFormatter {
  order: Order;
}

interface Order {
  orderId: number;
  accessioningId: (number | string);
  assayType: (number | string);
  sampleType: string;
  retestSample: boolean;
  orderComments: any;
  reqFieldMissingFlag: boolean;
  assay: Assay;
  patient: Patient;
}

interface Patient {
  patientFirstName: string;
  patientLastName: string;
  patientDOB: (Date | string | number);
  patientMedicalRecNo: string;
  labId: string;
  accountNumber: string;
  otherClinicianName: string;
  clinicName: string;
  refClinicianName: string;
  reasonForReferral: string;
}

interface Assay {
  maternalAge: number;
  gestationalAgeWeeks: number;
  gestationalAgeDays: number;
  eggDonor: string;
  eggDonorAge: number;
  ivfStatus: string;
  fetusNumber: string;
  collectionDate: (Date | string | number);
  receivedDate: (Date | string | number);
  testOptions: any;
}

interface TestOptions {
  Harmony: (boolean | string);
  SCAP: (boolean | string);
  FetalSex: (boolean | string);
  MX: (boolean | string);
}

export function OrderFormatter(OldFormat) {
  /* console.log('convertion Code',OldFormat);
  console.log(OldFormat.order);
  console.log(OldFormat.order.assay);
  console.log(OldFormat.order.assay.testOptions);
  console.log(OldFormat.order.patient); */

  const output_order_json = OldFormat.order;
  const output_order_assay_json = OldFormat.order.assay;
  const output_order_patient_json = OldFormat.order.patient;
  const output_order_assay_testOption_json = OldFormat.order.assay.testOptions;

  const RequiredFormat: OrderInputFormatter = {
    'order': {
      'orderId': output_order_json.orderId,
      'accessioningId': output_order_json.accessioningId,
      'assayType': output_order_json.assayType,
      'sampleType': output_order_json.sampleType,
      'retestSample': output_order_json.retestSample,
      'orderComments': output_order_json.orderComments,
      'reqFieldMissingFlag': output_order_json.reqFieldMissingFlag,
      'assay': {
        'maternalAge': output_order_assay_json.maternalAge,
        'gestationalAgeWeeks': output_order_assay_json.gestationalAgeWeeks,
        'gestationalAgeDays': output_order_assay_json.gestationalAgeDays,
        'eggDonor': output_order_assay_json.eggDonor,
        'eggDonorAge': output_order_assay_json.eggDonorAge,
        'ivfStatus': output_order_assay_json.ivfStatus,
        'fetusNumber': output_order_assay_json.fetusNumber,
        'collectionDate': new Date(output_order_assay_json.collectionDate),
        'receivedDate': new Date(output_order_assay_json.receivedDate),
        'testOptions': {}
      },
      'patient': {
        'patientFirstName': output_order_patient_json.patientFirstName,
        'patientLastName': output_order_patient_json.patientLastName,
        'patientDOB': output_order_patient_json.patientDOB ?  new Date(output_order_patient_json.patientDOB) : '' ,
        'patientMedicalRecNo': output_order_patient_json.patientMedicalRecNo,
        'labId': output_order_patient_json.labId,
        'accountNumber': output_order_patient_json.accountNumber,
        'otherClinicianName': output_order_patient_json.otherClinicianName,
        'clinicName': output_order_patient_json.clinicName,
        'refClinicianName': output_order_patient_json.refClinicianName,
        'reasonForReferral': output_order_patient_json.reasonForReferral
      }
    }
  };

  RequiredFormat.order.assay.testOptions = output_order_assay_testOption_json;

  return RequiredFormat;
}










