export class CreateOrderErrorMessages {
    static errorMsg = {
        'accessioningId':
        {
            'required': 'Please enter the Accessioning ID.',
            'length': 'Allowed maximum field length is 30 characters.',
            'specialCharacters': 'Allowed special characters are hyphen and underscore.',
            'specialCharactersRes': 'It must have at least one alpha numeric character.',
            'duplicate': 'Accessioning ID already exists. Please enter unique value.'
        },
        'assayType':
        {
            'required': 'Please select the Assay type.'
        },
        'sampleType':
        {
            'required': 'Please select the Sample type.'
        },
        'comments':
        {
            'maxLength': 'Allowed maximum field length is 150 characters.'
        },
        'maternalAge': {
            'required': 'Please enter Maternal age at delivery.',
            'range': 'Allowed Maternal age at delivery range from 1 to 100.',
            'number' : 'Please enter only numbers.'
        },
        'gestationalAgeWeeks': {
            'required': 'Please enter the Gestational age at blood draw in weeks.'
        },
        'gestationalAgeDays': {
            'required': 'Please enter the Gestational age at blood draw in days.'
        },
        'eggDonorAge': {
            'required': 'Please enter the Egg Donor Age at collection.',
            'lessThan': 'Egg Donor Age at collection should be <= the Maternal age at delivery.'
        },
        'collectionDate': {
            'required': 'Please select the Collection date.',
            'format': 'Please enter the Collection date in the format MM/DD/YYYY.'
        },
        'receivedDate': {
            'required': 'Please select the Received date.',
            'collectionAndRecivedDate': 'Received date should not be prior to the collection date.'
        },
        'testOptions': {
            'required': 'Please select at least one Test option.'
        },
        'dobDate': {
            'format': 'Please enter the Date of birth date in the format MM/DD/YYYY.',
            'errorDate': 'Please enter the correct date.',
            'minValue': 'Minimum allowed date is 99 years back.',
            'maxValue': 'Maximum allowed date value is current date.'
        },
        'name':
        {
            'maxlength': 'Allowed maximum field length is 30 characters.',
            // tslint:disable-next-line:quotemark
            'onlyaphabets': "Allowed characters are alphabets, hyphen, apostrophe and one space.",
            'oneSpaceNeed' : 'Repeated spaces are not allowed in this field.',
            // tslint:disable-next-line:quotemark
            'sepcialCharRes': "Hyphen, apostrophe and one space are allowed within the name."
        },
        'medicalRecordNumber': {
            'alphanumeric': 'Only alpha numeric characters are allowed.',
            'minLength': 'Allowed maximum field length is 15 characters.',
        },
        'phoneNumber': {
            'number': 'Only numeric characters are allowed.',
            'minLength': 'Minimum field length is 7 characters.',
            'maxLength': 'Allowed maximum field length is 20 characters.'
        },
        'accountNumber': {
            'duplicate': 'Account # already exists. Please enter a unique Account #.'
        }
    };
}
