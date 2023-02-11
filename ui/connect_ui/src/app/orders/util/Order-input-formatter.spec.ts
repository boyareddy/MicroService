import { OrderFormatter } from './Order-input-formatter';

describe('OrderFormatter', () => {
    it('Should convert the getting order details into formdata', () => {
        const gettingOrder = {
            'order': {
                'orderId': 10000300,
                'patientId': 1363,
                'patientSampleId': 1574,
                'accessioningId': '5454555',
                'orderStatus': 'unassigned',
                'assayType': 'NIPT',
                'sampleType': 'Plasma',
                'retestSample': true,
                'orderComments': '',
                'activeFlag': 'Y',
                'createdBy': 'ADMIN',
                'createdDateTime': 1536144810335,
                'updatedBy': null,
                'updatedDateTime': 1536144885360,
                'assay': {
                    'patientAssayid': 1363,
                    'maternalAge': 54,
                    'gestationalAgeWeeks': 3,
                    'gestationalAgeDays': 2,
                    'ivfStatus': 'No',
                    'eggDonor': '',
                    'fetusNumber': '',
                    'collectionDate': 1536085800000,
                    'receivedDate': 1536085800000,
                    'testOptions': {
                        'FetalSex': true,
                        'Harmony': true,
                        'SCAP': false,
                        'MX': false
                    }
                },
                'patient': {
                    'patientId': 1363,
                    'patientLastName': 'Rami',
                    'patientFirstName': 'Rama',
                    'patientGender': 'Female',
                    'patientMedicalRecNo': '12131231231',
                    'patientDOB': 1536085800000,
                    'patientContactNo': '1231231231',
                    'treatingDoctorName': '',
                    'treatingDoctorContactNo': '',
                    'refClinicianName': '',
                    'refClinicianFaxNo': '',
                    'otherClinicianName': '',
                    'otherClinicianFaxNo': '',
                    'refClinicianClinicName': ''
                }
            }
        };

        const expectedOutput: any = {
            'order': {
              'orderId': 10000300,
              'accessioningId': '5454555',
              'assayType': 'NIPT',
              'sampleType': 'Plasma',
              'retestSample': true,
              'orderComments': '',
              'assay': {
                'maternalAge': 54,
                'gestationalAgeWeeks': 3,
                'gestationalAgeDays': 2,
                'eggDonor': '',
                'ivfStatus': 'No',
                'fetusNumber': '',
                'collectionDate': '2018-09-04T18:30:00.000Z',
                'receivedDate': '2018-09-04T18:30:00.000Z',
                'testOptions': {
                  'FetalSex': true,
                  'Harmony': true,
                  'SCAP': false,
                  'MX': false
                }
              },
              'patient': {
                'patientLastName': 'Rami',
                'patientFirstName': 'Rama',
                'patientGender': 'Female',
                'patientMedicalRecNo': '12131231231',
                'patientDOB': '2018-09-04T18:30:00.000Z',
                'patientContactNo': '1231231231',
                'treatingDoctorName': '',
                'treatingDoctorContactNo': '',
                'refClinicianName': '',
                'refClinicianFaxNo': '',
                'otherClinicianName': '',
                'otherClinicianFaxNo': '',
                'refClinicianClinicName': ''
              }
            }
          };

        const convertedOrder = JSON.stringify(OrderFormatter(gettingOrder));

        expect(convertedOrder).toBe(JSON.stringify(expectedOutput));
    });

    it('Should convert the getting order details into formdata no DOB', () => {
        const gettingOrder = {
            'order': {
                'orderId': 10000300,
                'patientId': 1363,
                'patientSampleId': 1574,
                'accessioningId': '5454555',
                'orderStatus': 'unassigned',
                'assayType': 'NIPT',
                'sampleType': 'Plasma',
                'retestSample': true,
                'orderComments': '',
                'activeFlag': 'Y',
                'createdBy': 'ADMIN',
                'createdDateTime': 1536144810335,
                'updatedBy': null,
                'updatedDateTime': 1536144885360,
                'assay': {
                    'patientAssayid': 1363,
                    'maternalAge': 54,
                    'gestationalAgeWeeks': 3,
                    'gestationalAgeDays': 2,
                    'ivfStatus': 'No',
                    'eggDonor': '',
                    'fetusNumber': '',
                    'collectionDate': 1536085800000,
                    'receivedDate': 1536085800000,
                    'testOptions': {
                        'FetalSex': true,
                        'Harmony': true,
                        'SCAP': false,
                        'MX': false
                    }
                },
                'patient': {
                    'patientId': 1363,
                    'patientLastName': 'Rami',
                    'patientFirstName': 'Rama',
                    'patientGender': 'Female',
                    'patientMedicalRecNo': '12131231231',
                    'patientDOB': null,
                    'patientContactNo': '1231231231',
                    'treatingDoctorName': '',
                    'treatingDoctorContactNo': '',
                    'refClinicianName': '',
                    'refClinicianFaxNo': '',
                    'otherClinicianName': '',
                    'otherClinicianFaxNo': '',
                    'refClinicianClinicName': ''
                }
            }
        };

        const expectedOutput: any = {
            'order': {
              'orderId': 10000300,
              'accessioningId': '5454555',
              'assayType': 'NIPT',
              'sampleType': 'Plasma',
              'retestSample': true,
              'orderComments': '',
              'assay': {
                'maternalAge': 54,
                'gestationalAgeWeeks': 3,
                'gestationalAgeDays': 2,
                'eggDonor': '',
                'ivfStatus': 'No',
                'fetusNumber': '',
                'collectionDate': '2018-09-04T18:30:00.000Z',
                'receivedDate': '2018-09-04T18:30:00.000Z',
                'testOptions': {
                  'FetalSex': true,
                  'Harmony': true,
                  'SCAP': false,
                  'MX': false
                }
              },
              'patient': {
                'patientLastName': 'Rami',
                'patientFirstName': 'Rama',
                'patientGender': 'Female',
                'patientMedicalRecNo': '12131231231',
                'patientDOB': '',
                'patientContactNo': '1231231231',
                'treatingDoctorName': '',
                'treatingDoctorContactNo': '',
                'refClinicianName': '',
                'refClinicianFaxNo': '',
                'otherClinicianName': '',
                'otherClinicianFaxNo': '',
                'refClinicianClinicName': ''
              }
            }
          };

        const convertedOrder = JSON.stringify(OrderFormatter(gettingOrder));

        expect(convertedOrder).toBe(JSON.stringify(expectedOutput));
    });
});
