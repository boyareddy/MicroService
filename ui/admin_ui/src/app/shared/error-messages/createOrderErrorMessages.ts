export class CreateOrderErrorMessages {
    static errorMsg  = {
           'accessioningId':
            {
                'required': 'Please enter the Accessioning ID.',
                'number': 'Please enter only numbers.',
                'length': 'Please enter the number less then 30.'
            },
            'assayType':
            {
                'required': 'Please select assay type.'
            },
            'maternalAge': {
                'required': 'Please enter Maternal age.',
            },
            'firstName':
            {
                'required': 'Please enter the Accessioning ID.',
                'maxlength': 'The field length should not be greater than 30.',
                'onlyaphabets': 'Please enter letters only.'
            },
            'recordNumber': {
                'alphanumeric': 'The field must be alpha numeric.',
                'minLength': ' The field length must be 15.',
            },
            'phoneNumber': {
                'number': ' Please enter numbers only.',
                'minLength': ' The field should not less than 15.',
                'maxLength': 'The field should not greater than 15.'
            }
        };
}
