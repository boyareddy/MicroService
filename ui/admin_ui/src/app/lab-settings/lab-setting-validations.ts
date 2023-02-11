import { FormGroup, ValidationErrors, ValidatorFn, AbstractControl } from '@angular/forms';

export const minOneFieldREquired = (validator: ValidatorFn) => (
  formgroup: FormGroup
): ValidationErrors | null => {
  const atLeastOneValid =
    formgroup &&
    formgroup.controls &&
    Object.keys(formgroup.controls).some(
      k => !validator(formgroup.controls[k])
    );

  return atLeastOneValid ? null : { validForm: true };
};

export class LabSettingsValidation {
    static phoneNumberValidations(control: AbstractControl) {
        const value = control.value;
        const regExp = /^[a-zA-Z0-9 ()+-]*$/;
        if (!(regExp.test(value))) {
            return {
                errorMsg: 'invalidMobileNumber'
            };
        }
        return null;
    }
}

// const mobileNumber = /^(\+?)?[a-zA-Z()]\d*$/;
// const mobileNumberNoAlphabets = /[a-zA-Z]+[0-9]*$/;
// if (mobileNumberNoAlphabets.test(phoneNumber)) {
//     console.log('not');
//     return {
//         isNotphoneAndFaxNumber: true,
//         errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['number']
//     };
// }
// if (!mobileNumber.test(phoneNumber)) {
//     console.log('yes');
//    return {
//        isNotphoneAndFaxNumber: true,
//        errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['number']
//    };
// }
// if (phoneNumber.length > 20) {
//     return {
//         isNotphoneAndFaxNumber: true,
//         errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['maxLength']
//     };
// }
// return null;
