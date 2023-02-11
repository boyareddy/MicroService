import { AbstractControl } from '@angular/forms';

const userNameErrorMsg = {
    'required': 'Please enter the Username.'
};
const passwordErrorMsg = {
    'required': 'Please enter the Password.'
};

export class LoginValidation {
    static userNameValidation(control: AbstractControl) {
        const val = control.value;
        if (val === '' || val == null || val === undefined) {
            /* console.log('if block' + userNameErrorMsg['required']); */
            return {
                'isNotValidUsername': true,
                'errorMsg': userNameErrorMsg['required']
            };
        }
        return null;
    }

    static passwordValidation(control: AbstractControl) {
        const val = control.value;
        if (val === '' || val == null || val === undefined) {
           /*  console.log('if block passwordErrorMsg'); */
            return {
                'isNotValidPassword': true,
                'errorMsg': passwordErrorMsg['required']
            };
        }
        return null;
    }
}
