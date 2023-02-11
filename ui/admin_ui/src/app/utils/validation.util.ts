import { AbstractControl } from '@angular/forms';
import { regx } from './regx.const';
import { FieldInvalid } from '../models/validation.model';
import { errorFields } from './validation-error-fields.const';


export const vldAlphaNum = (errorCode: FieldInvalid): any => {
    return (control: AbstractControl) => {
        if (control.value && regx.ALPHA_NUM.test(control.value)) {
            return null;
        } else {
            return errorCode;
        }
    };
};

export const vldSpecialCar = (errorCode: FieldInvalid, regExp: RegExp): any => {
    return (control: AbstractControl) => {
        // if(control.value && regExp.test(control.value)){
        //     return null;
        // }else{
        //     return errorCode;
        // }
        return regExp.test(control.value) ? null : errorCode;
    };
};

export const vldSpaceCar = (errorCode: FieldInvalid, regExp: RegExp): any => {
    return (control: AbstractControl) => {
        // if(control.value && regExp.test(control.value)){
        //     return null;
        // }else{
        //     return errorCode;
        // }
        console.log(control.value, 'control.value');
        console.log(regExp.test(control.value), regExp.test(control.value) ? errorCode : null);
        return regExp.test(control.value) ? errorCode : null;
    };
};

export const vldGenSpecialCar = (fieldName: string, regExp: RegExp): any => {
    return (control: AbstractControl) => {
        return regExp.test(control.value) ? null : { errorCode: `${errorFields.allow_dot.errorCode} ${fieldName}.` };
    };
};

export const vldRequired = (fieldName: string): any => {
    return (control: AbstractControl) => {
        if (control.value && typeof control.value === 'object') {
            return null;
        }
        if (control.value && control.value.trim() !== '') {
            return null;
        } else {
            //return { errorCode: `${errorFields.required.errorCode} ${fieldName}.` };
            return { errorCode: `${fieldName}` };
        }
    };
};

export const vldRequiredSelect = (fieldName: string): any => {
    return (control: AbstractControl) => {
        if (control.value && typeof control.value === 'object') {
            return null;
        }
        if (control.value && control.value.trim() !== '') {
            return null;
        } else {
            //return { errorCode: `${errorFields.requiredSelect.errorCode} ${fieldName}.` };
            return { errorCode: `${fieldName}` };
        }
    };
};

export const vldDuplicateEntity = (serviceImplMethod: any, errorCode: string): any => {
    return serviceImplMethod.subscribe(entityList => {
        if(entityList.length > 0){
            return {
                errorCode: errorCode
            };
        }else{
            return null;
        }
    }, error => {
        return null;
    });
}
