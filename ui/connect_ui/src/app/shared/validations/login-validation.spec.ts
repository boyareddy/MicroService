import { LoginValidation } from './login-validation';
import { FormControl } from '@angular/forms';

describe('LoginValidation', () => {
    it('should validate the username as null', () => {
        const output = {
            'isNotValidUsername': true,
            'errorMsg': 'Please enter the username.'
        };
        const input = LoginValidation.userNameValidation(new FormControl(null));
        expect(input).toEqual(output);
    });

    it('should validate the username as admin', () => {
        const output = {
            'isNotValidUsername': true,
            'errorMsg': 'Please enter the username.'
        };
        const input = LoginValidation.userNameValidation(new FormControl('admin'));
        expect(input).toEqual(null);
    });

    it('should validate the password as null', () => {
        const output = {
            'isNotValidPassword': true,
            'errorMsg': 'Please enter the password.'
        };
        const input = LoginValidation.passwordValidation(new FormControl(null));
        expect(input).toEqual(output);
    });

    it('should validate the password as admin', () => {
        const input = LoginValidation.passwordValidation(new FormControl('password'));
        expect(input).toEqual(null);
    });
});
