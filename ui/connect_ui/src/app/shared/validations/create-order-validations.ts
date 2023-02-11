import { AbstractControl, FormGroup, ValidatorFn } from '@angular/forms';
import { CreateOrderErrorMessages } from '../error-messages/createOrderErrorMessages';
import { FormFieldNames } from 'src/app/standard-names/constants';

export class CreateOrderValidation {

    /**
     * Accessioning id should contain only numbers with _ and -
     * @param control data
     */
    static accessioningIdValidations(control: AbstractControl) {
        const accessioningId = control.value;
        if (accessioningId === '' || accessioningId == null || accessioningId === undefined) {
            return {
                isNotValidAccessioningId: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['required']
            };
        } else {
            // const onlyNumber = new RegExp('^[0-9_-]*$');
            const accessioningIdPattern1 = new RegExp('^[a-zA-Z0-9-_]+$');
            const accessioningIdPattern2 = new RegExp('^[-_]+$');

           /*  if (!(onlyNumber.test(accessioningId))) {
                return {
                    isNotValidAccessioningId: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['number']
                };
            } */
            if (!(accessioningIdPattern1.test(accessioningId) && (!accessioningIdPattern2.test(accessioningId)))) {
                if (!accessioningIdPattern2.test(accessioningId)) {
                    return {
                        isNotValidAccessioningId: true,
                        errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['specialCharacters']
                     };
                } else {
                    return {
                        isNotValidAccessioningId: true,
                        errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['specialCharactersRes']
                    };
                }

        }
        // const onlyNumber_ = control.value.split(/[\-\_]+/);
            // for (const item of  onlyNumber_) {
            //     if (item === '') {
            //         return {
            //             isNotValidAccessioningId: true,
            //             errorMsg: CreateOrderErrorMessages.errorMsg['accessioningId']['specialCharactersRes']
            //         };
            //     }
            // }
    }
        return null;
    }

    /**
     * Assay Type should be not null
     * @param control data
     */
    static assayTypeValidations(control: AbstractControl) {
        const assayData = control.value;
        if (assayData === '' || assayData == null || assayData === undefined) {
            return {
                isNotValidAssayType: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['assayType']['required']
            };
        }
        return null;
    }

    /**
     * Sample type should not be null
     * @param control data
     */
    static sampleTypeValidations(control: AbstractControl) {
        const sampleTypeData = control.value;
        if (sampleTypeData === '' || sampleTypeData == null || sampleTypeData === undefined) {
            return {
                isNotValidSampleType: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['sampleType']['required']
            };
        }
        return null;
    }

    static testOptionValidation(control: AbstractControl) {
        const selectedOptions = [];
        if (control && control.value) {
            Object.keys(control.value).forEach(key => {
                if (control.value[key]) {
                    selectedOptions.push(key);
                }
            });
            if (selectedOptions.length > 0) {
                return null;
            } else {
                return {valid: false};
            }
        } else {
            return {valid: false};
        }
    }

    /**
     * Comments characters should not greater than 150 characters
     * @param control data
     */
    static commentsValidations(control: AbstractControl) {
        const commentsData = control.value;
        if (commentsData !== null && commentsData !== '' && commentsData !== undefined) {
            if (commentsData.length > 150) {
                return {
                    isNotValidComments: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['comments']['maxLength']
                };
            }
        }
        return null;
    }

     /**
     * Maternal age should not be null and range should between 1 to 100
     * @param control data
     */
    static maternalAgeValidations(control: AbstractControl) {
        console.log('Required');
        const maternalAge = control.value;
        const alphabet = /^\d+$/;
        if (maternalAge === '' || maternalAge == null || maternalAge === undefined) {
            console.log(maternalAge);
            return {
                isNotValidMaternalAge: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['maternalAge']['required']
            };
        } else {
            if (!alphabet.test(maternalAge)) {
                return {
                    isNotValidMaternalAge : true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['maternalAge']['number']
                };
            }
        }
    }

    /**
     * Maternal age should not be null and range should between 1 to 100
     * @param control data
     */
    static RangeValidations(min: number, max: number, fieldName: string) {
        return (control: AbstractControl): { [key: string]: any } => {
            console.log('min', min, 'max', max);
            const maternalAgedata = control.value;
            const maternalAge = parseInt(maternalAgedata, 10);
            console.log(maternalAge);
            if (maternalAge > -1) {
                if (!(maternalAge >= min && maternalAge <= max)) {
                    if (fieldName !== FormFieldNames.MATERNAL_AGE) {
                        return {
                            isNotValidMaternalAge: true,
                            errorMsg: `Allowed ${fieldName} range from ${min} to ${max}.`
                        };
                    } else {
                        return {
                            isNotValidMaternalAge: true,
                            errorMsg: `Allowed ${fieldName} range from ${min} to ${max} inclusive.`
                        };
                    }
                }
            }
            return null;
        };
    }

    static eggDonorAgeSelfValidation(min: number, max: number, maternalAge: any) {
        return (control: AbstractControl): { [key: string]: any } => {
            const eggDonarAge = control;
            const eggDonarAgeVal: number = eggDonarAge.value;
            const maternalAgeVal = maternalAge.value;
            if ((maternalAgeVal !== '' && maternalAgeVal !== null && maternalAgeVal !== undefined)) {
                if (eggDonarAgeVal > -1) {
                    if ((eggDonarAgeVal >= min && eggDonarAgeVal <= max)) {
                        const maternalAge = parseInt(maternalAgeVal, 10);
                        if (!(eggDonarAgeVal <= maternalAge)) {
                            return {
                                isNotValidEggDonorAge: true,
                                errorMsg: CreateOrderErrorMessages.errorMsg['eggDonorAge']['lessThan']
                            };
                        }
                    }
                }
            }
            return null;
        };
    }

    /**
     * Gestational age week is should not null
     * @param control data
     */
    static gestationalAgeWeekValidations(control: AbstractControl) {
        const gestationalAgeWeek = control.value;
        if (gestationalAgeWeek === '' || gestationalAgeWeek == null || gestationalAgeWeek === undefined) {
            return {
                isNotValidGestationalAgeWeek: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['gestationalAgeWeeks']['required']
            };
        }
        return null;
    }

    /**
     * Gestational age days is should not null
     * @param control data
     */
    static gestationalAgeDaysValidations(control: AbstractControl) {
        const gestationalAgeDays = control.value;
        if ((gestationalAgeDays === '' || gestationalAgeDays == null || gestationalAgeDays === undefined) ) {
            return {
                isNotValidGestationalAgeDays: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['gestationalAgeDays']['required']
            };
        }
        return null;
    }

    /**
     * Egg donor age is should not null
     * @param control data
     */
    static eggDonorAgeValidations(control: AbstractControl) {
        const eggDonorAge = control.value;
        if ((eggDonorAge === '' || eggDonorAge == null || eggDonorAge === undefined) ) {
            return {
                isNotValideggDonorAge: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['eggDonorAge']['required']
            };
        }
        return null;
    }

    /**
     * Collection date should not be null
     * @param control dat
     */
    static collectionDateValidations(control: AbstractControl) {
        const collectionDate = control.value;
        // const date_regex = /^(0[1-9]|1[0-2])\/(0[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/ ;
        let receivedDateControl;
        if (control.parent) {
            receivedDateControl = control.parent.controls['receivedDate'].value;
        }
        if (collectionDate === '' || collectionDate === null || collectionDate === undefined) {
            return {
                isNotValidCollectionDate: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['collectionDate']['required']
            };
        } else {
            if (receivedDateControl) {
                const receivedDateObj = new Date(receivedDateControl);
                if (collectionDate) {
                    const collectionDateObj = new Date(collectionDate);
                    if (receivedDateObj < collectionDateObj ) {
                        console.log(control.parent.controls['receivedDate'], 'control.parent.controls');
                        control.parent.controls['receivedDate'].markAsTouched();
                        control.parent.controls['receivedDate'].setErrors({
                            isNotValidCollectionDate: true,
                            errorMsg: CreateOrderErrorMessages.errorMsg['receivedDate']['collectionAndRecivedDate']
                        });
                        console.log(control.parent.controls['receivedDate'], 'control.parent.controls');
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Received Date should not be null
     * @param control data
     */
    static receivedDateValidations(control: AbstractControl) {
        const receivedDate = control.value;
        let collectionDateControl;
        if (control.parent) {
            collectionDateControl = control.parent.controls['collectionDate'].value;
        }
        if (receivedDate === '' || receivedDate == null || receivedDate === undefined) {
            return {
                isNotValidCollectionDate: true,
                errorMsg: CreateOrderErrorMessages.errorMsg['receivedDate']['required']
            };
        } else {
            const receivedDateObj = new Date(receivedDate);
            if (collectionDateControl) {
                const collectionDateObj = new Date(collectionDateControl);
                if (receivedDateObj < collectionDateObj ) {
                    return {
                        isNotValidCollectionDate: true,
                        errorMsg: CreateOrderErrorMessages.errorMsg['receivedDate']['collectionAndRecivedDate']
                    };
                }
            }
        }
        return null;
    }

    /**
     * Name Validations should contain only alphabets and one space between two word.
     * @param control data
     */
    static nameValidations(control: AbstractControl) {
        const name = control.value;
        const oneSpcae = /\s\s+/g;
        const specialCharRes = /^[a-zA-Z\s-']+$/;
        const specCharStart = /^[a-zA-Z]/;
        const specCharEnd = /[a-zA-Z]+$/;
        const oneChar = /^(?=.*[a-zA-Z])([a-zA-Z\s-']+)$/;

        if (name !== null && name !== '' && name !== undefined) {
            if (!specialCharRes.test(name)) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['name']['onlyaphabets']
                };
            }
            if (oneSpcae.test(name)) {
               {
                    return {
                        isNotValidpatientFirstNameValidations: true,
                        errorMsg: CreateOrderErrorMessages.errorMsg['name']['oneSpaceNeed']
                    };
                }
            }
            if (!specCharStart.test(name) || !specCharEnd.test(name) || !oneChar.test(name)) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['name']['sepcialCharRes']
                };
            }
            if (name.length > 30) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['name']['maxlength']
                };
            }
        }
        return null;
    }


    /**
     * medical record number should be alpha numeric data and length should not greate than 15 charcters
     * @param control data
     */
    static medicalRecordNumberValidations(control: AbstractControl) {
        const medicalRecordNumber = control.value;
        const alphanumeric = new RegExp('^[a-zA-Z0-9]*$');
        if (medicalRecordNumber !== '' && medicalRecordNumber != null && medicalRecordNumber !== undefined) {
            if (!(alphanumeric.test(medicalRecordNumber))) {
                return {
                    isNotValidmedicalRecordNumberValidations: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['medicalRecordNumber']['alphanumeric']
                };
            } else if (medicalRecordNumber.length > 0 && medicalRecordNumber.length > 15) {
                return {
                    isNotValidmedicalRecordNumberValidations: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['medicalRecordNumber']['minLength']
                };
            }
        }
        return null;
    }

    /**
     * phone number only numbers and + symbol allowed minimum length 7 and maximum length 16
     * @param control phone number
     */
    static phoneAndFaxNumberValidations(control: AbstractControl) {
        const phoneNumber = control.value;
        // const alphanumericMobile = new RegExp('^[+]*[0-9]*$');
        const mobileNumber = /^(\+?)?\d*$/;
        const mobileNumberNoAlphabets = /[a-zA-Z]+[0-9]*$/;
        if (phoneNumber !== '' && phoneNumber !== null && phoneNumber !== undefined) {
            if (mobileNumberNoAlphabets.test(phoneNumber)) {
                console.log('not');
                return {
                    isNotphoneAndFaxNumber: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['number']
                };
            }
            if (!mobileNumber.test(phoneNumber)) {
                console.log('yes');
               return {
                   isNotphoneAndFaxNumber: true,
                   errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['number']
               };
            }
            if (phoneNumber.length > 20) {
                return {
                    isNotphoneAndFaxNumber: true,
                    errorMsg: CreateOrderErrorMessages.errorMsg['phoneNumber']['maxLength']
                };
            }
            return null;
        }
    }

    static minFieldValidation(minValue: number) {
        return (control: AbstractControl): {[key: string]: any} => {
            const fieldVal: string = control.value;
            if (fieldVal) {
                if (fieldVal.length < minValue) {
                    return {
                        errorMsg: `Allowed minimum field length is ${minValue} characters.`
                    };
                }
                return null;
            } else {
                return null;
            }
        };
    }

    static AlphabetsNumber(control: AbstractControl) {
        const fieldVal: string = control.value;
        const regExp = /^[a-zA-Z0-9]*$/;
        if (fieldVal) {
            if (!regExp.test(fieldVal)) {
                return {
                    errorMsg: `Allowed characters are alphabets and numbers.`
                };
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    static AlphabetsNumberSpecial(control: AbstractControl) {
        const fieldVal = control.value;
        const regExp = /^[a-zA-Z0-9-]*$/;
        if (fieldVal !== '' || fieldVal !== undefined || fieldVal !== null ) {
            if (!regExp.test(fieldVal)) {
                return {
                    errorMsg: `Allowed characters are alphabets, numbers and hyphen.`
                };
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
