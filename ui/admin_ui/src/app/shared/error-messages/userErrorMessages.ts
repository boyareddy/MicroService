export class UserErrorMessages {
    static errorMsg = {
        'userName':
        {
            'required': 'Please enter the Username.',
            'maxlength': 'Allowed maximum field length is 20 characters.',
            'pattern': 'Allowed special characters are underscore and dot within the username.',
            'minLenght': 'Minimum field length is 3 characters.',
            'onechar' : 'Atleast one alphabet required.',
            'usrNameNotExist' : 'Incorrect Username.',
            'exists': 'Username already exists.'
        },
        'email': {
            'required': 'Please enter the Email address.',
            'valid': 'Please enter a valid Email address.',
            'maxlength': 'The Email address should consist of only 30 characters.',
            'maxlengthEmailName': 'The Email address name should consist of only 64 characters.',
            'maxlengthDomainName': 'The domain name should consist of only 255 characters.',
            'exists': 'Email address already exists.'
        },
        'name':
        {
            'required': 'Please enter the First name.',
            'firstName': 'Please enter the First name.',
            'lastName': 'Please enter the Last name.',
            'maxlength': 'Allowed maximum field length is 30 characters.',
            'minlength': 'Allowed minimum field length is 3 characters.',
            // tslint:disable-next-line:quotemark
            'onlyaphabets': "Allowed characters are alphabets, hyphen, apostrophe and one space.",
            'oneSpaceNeed' : 'Repeated spaces are not allowed in this field.',
            // tslint:disable-next-line:quotemark
            'sepcialCharRes': "Hyphen, apostrophe and one space are allowed within the name."
        },
        'phoneNumber': {
            'number': 'Only  numeric characters are allowed.',
            'minLength': 'Minimum field length is 7 characters.',
            'maxLength': 'Allowed maximum field length is 20 characters.'
        },
        'password':
        {
            'required': 'Please enter the New password.',
            'maxlength': 'Allowed maximum field length is 25 characters.',
            'minlength': 'Allowed minimum field length is 6 characters.',
            'matchPwd': 'Passwords do not match.',
            'maxUpp': 'Maximum number of uppercase characters: 6 characters.',
            'maxLow': 'Maximum number of lowercase characters: 6 characters.',
            'maxNum': 'Maximum of numeric digits: 6 characters.',
            'maxSpec': 'Maximum number of non-alphanumeric characters: 5 characters.',
            'minUpp': 'At least 1 uppercase letter.',
            'minLow': 'At least 1 lowercase letter.',
            'minNum': 'At least 1 numeric digit.',
            'charRepeat': 'Must not repeat character more than 4 times',
            'multipleRules': {
                'multipleRulesError': 'Password criteria not met.',
                'multipleRulesErrorArr': [
                    'Must contain a minimum of 8 characters and a maximum of 25 characters',
                    'Must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character',
                    'Must not repeat a character more than 4 times consecutively',
                    'Must not contain 4 consecutive characters from Username ',
                    'Must not repeat any of your previous 6 passwords'
                ]
            }
        },
        'cpassword': {
            'required': 'Please enter the Confirm password.'
        },
        'oldpassword': {
            'required': 'Please enter the Current password.',
            'notValid': 'Incorrect password.'
        },
        'userRole': {
            'selectOne': 'Please select atleast one User role.'
        },
        'address1': {
            'maxlength': 'Address should consist of only 50 characters.',
        }
    };
}
