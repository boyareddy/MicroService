import { CreateOrderValidation } from './create-order-validations';
import { FormControl } from '@angular/forms';

describe('CreateOrderValidation', () => {
    it('should validate the accessioningIdValidations value as 1234', () => {
        const isValid = CreateOrderValidation.accessioningIdValidations(new FormControl('1234'));
        expect(isValid).toBe(null);
    });

    it('should validate the accessioningIdValidations value as 123_4', () => {
        const isValid = CreateOrderValidation.accessioningIdValidations(new FormControl('123_4'));
        expect(isValid).toBe(null);
    });

    it('should validate the accessioningIdValidations value as abcs', () => {
        const output = { isNotValidAccessioningId: true, errorMsg: 'Allowed special characters are hyphen and underscore.' };

        const isValid = CreateOrderValidation.accessioningIdValidations(new FormControl('@12433'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the accessioningIdValidations value as ""', () => {
        const output = {
            isNotValidAccessioningId: true,
            errorMsg: 'Please enter the Accessioning ID.'
        };
        const isValid = CreateOrderValidation.accessioningIdValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the accessioningIdValidations value as "" negative case', () => {
        const output = {
            isNotValidAccessioningId: true,
            errorMsg: 'Please scan or enter the Accessioning ID Data.'
        };
        const isValid = CreateOrderValidation.accessioningIdValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).not.toBe(JSON.stringify(output));
    });

    it('should validate the accessioningIdValidations value as 123_', () => {
        const output = {
            isNotValidAccessioningId: true,
            errorMsg: null
        };
        const isValid = CreateOrderValidation.accessioningIdValidations(new FormControl('123_'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe('null');
    });

    it('should validate the assayTypeValidations value as "" ', () => {
        const output = {
            isNotValidAssayType: true,
            errorMsg: 'Please select the Assay type.'
        };
        const isValid = CreateOrderValidation.assayTypeValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the assayTypeValidations value as NIPT ', () => {
        const isValid = CreateOrderValidation.assayTypeValidations(new FormControl('NIPT'));
        expect(isValid).toBe(null);
    });

    it('should validate the sampleTypeValidations value as "" ', () => {
        const output = {
            isNotValidSampleType: true,
            errorMsg: 'Please select the Sample type.'
        };
        const isValid = CreateOrderValidation.sampleTypeValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the sampleTypeValidations value as Plasma', () => {
        const isValid = CreateOrderValidation.sampleTypeValidations(new FormControl('Plasma'));
        expect(isValid).toBe(null);
    });

    it('should validate the testOptionValidation value as null', () => {
        const output = { valid: false };
        const isValid = CreateOrderValidation.testOptionValidation(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the testOptionValidation value as {FetalSex : false,Harmony : false, MX : false,SCAP: false}', () => {
        const output = { 'valid': false };
        const testOptions = {
            'FetalSex' : false,
            'Harmony' : false,
            'MX' : false,
            'SCAP': false
        };
        const isValid = CreateOrderValidation.testOptionValidation(new FormControl(testOptions));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the testOptionValidation value as {FetalSex : true,Harmony : false, MX : false,SCAP: false}', () => {
        const testOptions = {
            'FetalSex' : true,
            'Harmony' : false,
            'MX' : false,
            'SCAP': false
        };
        const isValid = CreateOrderValidation.testOptionValidation(new FormControl(testOptions));
        expect(isValid).toBe(null);
    });

    it('should validate the commentsValidations value as null', () => {
        const isValid = CreateOrderValidation.commentsValidations(new FormControl(null));
        expect(isValid).toBe(null);
    });

    it('should validate the commentsValidations value as undefined', () => {
        const isValid = CreateOrderValidation.commentsValidations(new FormControl(undefined));
        expect(isValid).toBe(null);
    });

    it('should validate the commentsValidations value as ""', () => {
        const isValid = CreateOrderValidation.commentsValidations(new FormControl(''));
        expect(isValid).toBe(null);
    });

    it('should validate the commentsValidations value as some text', () => {
        const isValid = CreateOrderValidation.commentsValidations(new FormControl('some text'));
        expect(isValid).toBe(null);
    });

    it('should validate the commentsValidations value as some text more than 150 character', () => {
        const output = {
            isNotValidComments: true,
            errorMsg: 'Allowed maximum field length is 150 characters.'
        };
        // tslint:disable-next-line:max-line-length
        const data = 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Neque eum, explicabo, perspiciatis corrupti atque unde quos expedita accusantium veniam facere reiciendis officia dolores illo sequi necessitatibus quod praesentium veritatis fugit, consectetur suscipit voluptates aperiam similique. Sunt placeat, nam minus qui laborum magnam necessitatibus hic dolorum in? Odio expedita laborum nobis error blanditiis maxime perspiciatis voluptatum, fugiat adipisci voluptas ipsum culpa, aspernatur consectetur soluta.';
        const isValid = CreateOrderValidation.commentsValidations(new FormControl(data));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the maternalAgeValidations value as null', () => {
        const output = {
            isNotValidMaternalAge: true,
            errorMsg: 'Please enter Maternal age.'
        };
        const isValid = CreateOrderValidation.maternalAgeValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the maternalAgeValidations value as abc', () => {
        const output = {
            isNotValidMaternalAge: true,
            errorMsg: 'Please enter only numbers.'
        };
        const isValid = CreateOrderValidation.maternalAgeValidations(new FormControl('abc'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the maternalAgeValidations below 100', () => {
        const output = {
            isNotValidMaternalAge: true,
            errorMsg: 'Allowed Maternal Age range from 1 to 100.'
        };
        const isValid = CreateOrderValidation.maternalAgeValidations(new FormControl('55'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).not.toBe(JSON.stringify(output));
    });

    it('should validate the maternalAgeValidations above 100', () => {
        const output = {
            isNotValidMaternalAge: true,
            errorMsg: 'Allowed Maternal Age range from 1 to 100.'
        };
        const isValid = CreateOrderValidation.maternalAgeValidations(new FormControl('160'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(undefined);
    });

    it('should validate the gestationalAgeWeekValidations value as null', () => {
        const output = {
            isNotValidGestationalAgeWeek: true,
            errorMsg: 'Please enter the Gestational age in weeks.'
        };
        const isValid = CreateOrderValidation.gestationalAgeWeekValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the gestationalAgeWeekValidations value as 13', () => {
        const isValid = CreateOrderValidation.gestationalAgeWeekValidations(new FormControl('13'));
        expect(isValid).toBe(null);
    });

    it('should validate the gestationalAgeDaysValidations value as null', () => {
        const output = {
            isNotValidGestationalAgeDays: true,
            errorMsg: 'Please enter the Gestational age in days.'
        };
        const isValid = CreateOrderValidation.gestationalAgeDaysValidations(new FormControl(null));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the gestationalAgeDaysValidations value as 13', () => {
        const isValid = CreateOrderValidation.gestationalAgeDaysValidations(new FormControl('13'));
        expect(isValid).toBe(null);
    });

    it('should validate the phoneAndFaxNumberValidations value as 123344556780', () => {
        const isValid = CreateOrderValidation.phoneAndFaxNumberValidations(new FormControl('123344556780'));
        expect(isValid).toBe(null);
    });

    it('should validate the phoneAndFaxNumberValidations value as 234345', () => {
        const output = { isNotphoneAndFaxNumber: true,
                         errorMsg: 'Minimum field length is 7 characters.' };
        const isValid = CreateOrderValidation.phoneAndFaxNumberValidations(new FormControl('234345'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe('null');
    });

    it('should validate the phoneAndFaxNumberValidations value as 2343!1', () => {
        const output = { isNotphoneAndFaxNumber: true,
                         errorMsg: 'Only numeric characters are allowed.' };
        const isValid = CreateOrderValidation.phoneAndFaxNumberValidations(new FormControl('2343!1'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the phoneAndFaxNumberValidations value as qwe21', () => {
        const output = { isNotphoneAndFaxNumber: true,
                         errorMsg: 'Only numeric characters are allowed.' };
        const isValid = CreateOrderValidation.phoneAndFaxNumberValidations(new FormControl('qwe21'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the medicalRecordNumberValidations value as qwe21@', () => {
        const output = { isNotValidmedicalRecordNumberValidations: true,
                         errorMsg: 'Only alpha numeric characters are allowed.' };
        const isValid = CreateOrderValidation.medicalRecordNumberValidations(new FormControl('qwe21@'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the medicalRecordNumberValidations value as 113233sdf23443442s', () => {
        const output = { isNotValidmedicalRecordNumberValidations: true,
                         errorMsg: 'Allowed maximum field length is 15 characters.' };
        const isValid = CreateOrderValidation.medicalRecordNumberValidations(new FormControl('113233sdf23443442s'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the medicalRecordNumberValidations value as qwer12', () => {
        const isValid = CreateOrderValidation.medicalRecordNumberValidations(new FormControl('qwer12'));
        expect(isValid).toBe(null);
    });

    it('should validate the nameValidations value as 113233', () => {
        const output = { isNotValidpatientFirstNameValidations: true,
                         errorMsg: 'Allowed characters are alphabets, hyphen, apostrophe and one space.' };
        const isValid = CreateOrderValidation.nameValidations(new FormControl('113233'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    // it('should validate the nameValidations value ada  edewa', () => {
    //     const output = { isNotValidpatientFirstNameValidations: true,
    //                      errorMsg: 'One space is allowed within the name.' };
    //     const isValid = CreateOrderValidation.nameValidations(new FormControl('ada  edewa'));
    //     const expectedOp = JSON.stringify(isValid);
    //     expect(expectedOp).toBe(JSON.stringify(output));
    // });

    it('should validate the nameValidations value LoremIpsumissimplydummytextofLoremIpsumissimplydummytextof', () => {
        const output = { isNotValidpatientFirstNameValidations: true,
                         errorMsg: 'Allowed maximum field length is 30 characters.' };
        // tslint:disable-next-line:max-line-length
        const isValid = CreateOrderValidation.nameValidations(new FormControl('LoremIpsumissimplydummytextofLoremIpsumissimplydummytextof'));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the nameValidations value as Johnsmir', () => {
        const isValid = CreateOrderValidation.nameValidations(new FormControl('Johnsmir'));
        expect(isValid).toBe(null);
    });

    it('should validate the receivedDateValidations value as empty', () => {
        const output = { isNotValidCollectionDate: true,
                         errorMsg: 'Please select the Received date.' };
        const isValid = CreateOrderValidation.receivedDateValidations(new FormControl(''));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the receivedDateValidations value as 01/01/2021', () => {
        const isValid = CreateOrderValidation.receivedDateValidations(new FormControl('01/01/2021'));
        expect(isValid).toBe(null);
    });

    it('should validate the collectionDateValidations value as empty', () => {
        const output = { isNotValidCollectionDate: true,
                         errorMsg: 'Please select the Collection date.' };
        const isValid = CreateOrderValidation.collectionDateValidations(new FormControl(''));
        const expectedOp = JSON.stringify(isValid);
        expect(expectedOp).toBe(JSON.stringify(output));
    });

    it('should validate the collectionDateValidations value as 12/10/2012', () => {
        const isValid = CreateOrderValidation.collectionDateValidations(new FormControl('12/10/2012'));
        expect(isValid).toBe(null);
    });


});
