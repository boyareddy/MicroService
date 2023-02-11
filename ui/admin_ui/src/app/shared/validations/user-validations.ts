import { AbstractControl, FormGroup } from '@angular/forms';
import { UserErrorMessages } from '../error-messages/userErrorMessages';

export class UserValidation {
    /**
     * FirstName Validations should contain only alphabets and one space between two word.
     * @param control data
     */
    static NameValidations(control: AbstractControl) {
        console.log(control);
        const name = control.value;
        const oneSpcae = /\s\s+/g;
        const specialCharRes = /^[a-zA-Z\s-']+$/;
        const specCharStart = /^[a-zA-Z]/;
        const specCharEnd = /[a-zA-Z]+$/;
        const oneChar = /^(?=.*[a-zA-Z])([a-zA-Z\s-']+)$/;
        let constrolNam = null;
        if (control.parent) {
            const formGroup = control.parent.controls;
            constrolNam = Object.keys(formGroup).find(namei => control === formGroup[namei]);
            console.log(constrolNam);
        }
        if ((name === null || name === '' || name === undefined ) && constrolNam) {
            return {
                isNotValidpatientFirstNameValidations: true,
                errorMsg: UserErrorMessages.errorMsg['name'][constrolNam]
            };
        }
        if (name !== null && name !== '' && name !== undefined) {
            if (!specialCharRes.test(name)) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: UserErrorMessages.errorMsg['name']['onlyaphabets']
                };
            }
            if (oneSpcae.test(name)) {
               {
                    return {
                        isNotValidpatientFirstNameValidations: true,
                        errorMsg: UserErrorMessages.errorMsg['name']['oneSpaceNeed']
                    };
                }
            }
            if (!specCharStart.test(name) || !specCharEnd.test(name) || !oneChar.test(name)) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: UserErrorMessages.errorMsg['name']['sepcialCharRes']
                };
            }
            if (name.length < 3) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: UserErrorMessages.errorMsg['name']['minlength']
                };
            }
            if (name.length > 30) {
                return {
                    isNotValidpatientFirstNameValidations: true,
                    errorMsg: UserErrorMessages.errorMsg['name']['maxlength']
                };
            }
        }
        return null;
    }

    static EmailValidations(control: AbstractControl) {
        const email = control.value;
        const alphabets = /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/;
        if (email === '' || email === null || email === undefined) {
            return {
                isNotValidEmail: true, errorMsg: UserErrorMessages.errorMsg['email']['required']
            };
        } else if (!alphabets.test(control.value)) {
            return {
                isNotValidEmail: true, errorMsg: UserErrorMessages.errorMsg['email']['valid']
            };
        } else {
            const emailAddr = email.split('@')[0];
            const domainName = email.split('@')[1];
            if (emailAddr.length > 64) {
                return {
                    isNotValidEmail: true,
                    errorMsg: UserErrorMessages.errorMsg['email']['maxlengthEmailName']
                };
            }
            if (domainName.length > 255) {
                return {
                    isNotValidEmail: true,
                    errorMsg: UserErrorMessages.errorMsg['email']['maxlengthDomainName']
                };
            }
        }
    }

    static UsernameValidations(control: AbstractControl) {
        const username = control.value.toLowerCase();
        const regExp = /^[0-9a-z_.]+$/;
        const oneChar = /^(?=.*[a-z])([0-9a-z\s_.]+)$/;
        if (control.value === '' || control.value === null || control.value === undefined) {
            return {
                isNotValidUserName: true, errorMsg: UserErrorMessages.errorMsg['userName']['required']
            };
        } else {
            if ((username.length < 3 || username.length > 20) && (regExp.test(username))) {
                return {
                    isNotValidUserName: true,
                    errorMsg: UserErrorMessages.errorMsg['userName']['minLenght']
                };
            }
         if (!oneChar.test(username) && regExp.test(username)) {
                return {
                    isNotValidUserName: true,
                    errorMsg: UserErrorMessages.errorMsg['userName']['onechar']
                };
            } else if (!regExp.test(username)) {
                return {
                    isNotValidUserName: true, errorMsg: UserErrorMessages.errorMsg['userName']['pattern']
                };
            }
        }

        if (username.length > 20) {
            return {
                isNotValidUserName: true,
                errorMsg: UserErrorMessages.errorMsg['userName']['maxlength']
            };
        }

        return null;
    }

    static userNameVal(control: AbstractControl) {
        const val = control.value;
        if (val === '' || val == null || val === undefined) {
            console.log('val------', val);
            return {
                isNotValidUserName: true, errorMsg: UserErrorMessages.errorMsg['userName']['required']
            };
        }
        return null;
    }

    static oldPasswordVal(control: AbstractControl) {
        const val = control.value;
        if (val === '' || val == null || val === undefined) {
            return {
                isNotValidOldPassword: true, errorMsg: UserErrorMessages.errorMsg['oldpassword']['required']
            };
        }
        return null;
    }

    /**
     * Password Validations.
     * @param control data
     */
    static PasswordValidations(control: AbstractControl) {
        const passwordVal = control.value;
        let userName = null;
        if (control.parent) {
            console.log(control.parent.get('loginName').value, 'control.parent.get');
            userName = control.parent.get('loginName').value;
        }
        const oneSpcae = /\s\s+/g;
        const specialCharRes = /^[a-zA-Z\s-']+$/;
        const specCharStart = /^[a-zA-Z]/;
        const specCharEnd = /[a-zA-Z]+$/;
        const oneChar = /^(?=.*[a-zA-Z])([a-zA-Z\s-']+)$/;
        if (passwordVal === '' || passwordVal === null || passwordVal === undefined) {
            return {
                isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['required']
            };
        }
        const userNameExist = UserValidation.checkForUserName(passwordVal, userName);
        const specChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;
        let charRepeated = false;
        const charArr = [];
        let i = 0;
        passwordVal.replace(/(.)\1*/g, function(m, $1) {
            charArr[i] = {};
            const keyName = $1;
            let indexVal = null;
            console.log(m, 'mmmmmm');
            console.log(keyName, 'keyName');
            // if (m.length > 4) {
            //     charRepeated = true;
            // }
            charArr.forEach((val, ind) => {
                const ObjKeyName = Object.keys(val)[0];
                if (ObjKeyName === keyName) {
                    indexVal = ind;
                }
            });
            if (indexVal !== null) {
                charArr[indexVal][keyName] = charArr[indexVal][keyName] + m.length;
            } else {
                charArr[i][keyName] = m.length;
                i++;
            }
        });
        charArr.forEach((val, key) => {
            const ObjKeyName_ = Object.keys(val)[0];
            if ( val[ObjKeyName_] !== null && val[ObjKeyName_] > 4) {
                charRepeated = true;
            }
        });
        if (passwordVal.length < 8 || passwordVal.length > 25
            || passwordVal.replace(/[^A-Z]/g, '').length === 0
            || passwordVal.replace(/[^a-z]/g, '').length === 0
            || passwordVal.replace(/[^0-9]/g, '').length === 0
            || passwordVal.match(specChar) === null
            || charRepeated
            || userNameExist) {
            return {
                isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['multipleRules']
            };
        }
        return null;
    }

    static confirmPasswordValidations(control: AbstractControl) {
        if (control.value === '' || control.value === null || control.value === undefined) {
            return {
                isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['cpassword']['required']
            };
        }

        if (control.value.length < 6) {
            return {
                isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['minlength']
            };
        }

        if (control.value.length > 25) {
            return {
                isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['maxlength']
            };
        }
        return null;

    }

    static matchPassword(vpwd: any) {
        console.log('MatchPassword', vpwd);
        if (vpwd.parent) {
            const password = vpwd.parent.value.password;
            const confirmPassword = vpwd.value;
            if (password !== confirmPassword) {
                return {
                    isMatchpasswordValidation: true,
                    errorMsg: UserErrorMessages.errorMsg['password']['matchPwd']
                };
            } else {
                return null;
            }
        }
    }

    /**
     * phone number only numbers and + symbol allowed minimum length 7 and maximum length 16
     * @param control phone number
     */
    static phoneNumberValidations(control: AbstractControl) {
        const phoneNumber = control.value;
        const alphanumeric = new RegExp('^[0-9+]*$');
        if (phoneNumber !== '' || phoneNumber != null || phoneNumber !== undefined) {
            if (!(alphanumeric.test(phoneNumber))) {
                return {
                    isNotValidphoneNumber: true,
                    errorMsg: UserErrorMessages.errorMsg['phoneNumber']['number']
                };
            }
            if (phoneNumber.length > 20) {
                return {
                    isNotValidphoneNumber: true,
                    errorMsg: UserErrorMessages.errorMsg['phoneNumber']['maxLength']
                };
            }
        }
        return null;
    }

    static onValidatingCheckbox(control: AbstractControl) {
        // console.log('onValidatingCheckbox', control);
        if (control && control.value.length > 0) {
            return null;
        } else {
            return {
                selectUserRole: true,
                errorMsg: UserErrorMessages.errorMsg.userRole.selectOne
            };
        }
    }

    static addressValidation(control: AbstractControl) {
        if (control && control.value.length > 50) {
            return {
                address: true,
                errorMsg: UserErrorMessages.errorMsg.address1.maxlength
            };
        } else {
            return null;
        }
    }

    static checkForUserName(inputStr, compareWith) {
        if (inputStr && compareWith) {
            const compareWithArr = inputStr.toLowerCase().split('');
            if (compareWithArr.length > 4) {
                for (let i = 0; i < compareWithArr.length; i++) {
                    let usrNameStr = '';
                    let inc = 1;
                    for (let j = i; j < compareWithArr.length; j++) {
                        if ( inc < 6) {
                            usrNameStr = usrNameStr + compareWithArr[j];
                        }
                        inc++;
                    }
                    const inputStrReg = new RegExp(usrNameStr, 'g');
                    if (usrNameStr.length > 4 && compareWith.toLowerCase().match(inputStrReg) !== null) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }
        return false;
    }
}
