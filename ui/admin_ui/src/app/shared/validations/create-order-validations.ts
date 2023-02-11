import { AbstractControl, FormGroup } from '@angular/forms';
import { CreateOrderErrorMessages } from '../error-messages/createOrderErrorMessages';

export class CreateOrderValidation {

    static accessioningIdValidations(control: AbstractControl) {
        if (control.value === '' || control.value == null || control.value === undefined) {
            return {
                isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['required']
            };
        } else {
            const onlyNumber = new RegExp('^[0-9]*$');
            if (!(onlyNumber.test(control.value))) {
                return {
                    isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['number']
                };
            }
        }
        return null;
    }

   /*  static maternalAgeValidations(control: AbstractControl) {
        if (control.value !== "" || control.value !== null || control.value !== undefined) {
            return {
                isNotValidmaternalAgeValidations: true, errorMsg: CreateOrderErrorMessages.errorMsg['maternalAge']['required']
            };
        }
        return null;
    } */


    static NameValidations(control: AbstractControl) {
        if (control.value === '' || control.value == null || control.value === undefined) {
            return {
                isNotValidpatientFirstNameValidations: true, errorMsg: CreateOrderErrorMessages.errorMsg['firstName']['required']
            };
    } else {
        const alphabets = new RegExp('^[a-zA-Z]*$');
        if (!alphabets.test(control.value)) {
            return {
                isNotValidpatientFirstNameValidations: true, errorMsg: CreateOrderErrorMessages.errorMsg['patientFirstName']['onlyaphabets']
            };
        }
    }if (control.value.length > 30) {
        return {
            isNotValidpatientFirstNameValidations: true, errorMsg: CreateOrderErrorMessages.errorMsg['patientFirstName']['maxlength']
        };
    }
    return null;

}


    static recordNumberValidations(control: AbstractControl) {
        const recordNumber = control.value;
        const alphanumeric = new RegExp('^[a-zA-Z0-9]*$');
        if (recordNumber !== '' || recordNumber != null || recordNumber !== undefined) {
            if (!(alphanumeric.test(recordNumber))) {
                return {
                    isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['recordNumber']['alphanumeric']
                };
            } else if (recordNumber.length > 0 && (recordNumber.length < 15 || recordNumber.length > 15)) {
                return {
                    isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['recordNumber']['minLength']
                };
            }
        }
        return null;
    }

    static phoneFaxNumberValidations(control: AbstractControl) {
        const phoneNumber = control.value;
        const alphanumeric = new RegExp('^[0-9+]*$');
        if (phoneNumber !== '' || phoneNumber != null || phoneNumber !== undefined) {
            if (!(alphanumeric.test(phoneNumber))) {
                return {
                    isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['number']
                };
            } else if (phoneNumber.length > 0 && phoneNumber.length < 15) {
                return {
                    isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['minLength']
                };
            }
            if (phoneNumber.length > 15) {
                return {
                    isNotValidAccessioningId: true, errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['maxLength']
                };
            }
        }
        return null;
    }
}
