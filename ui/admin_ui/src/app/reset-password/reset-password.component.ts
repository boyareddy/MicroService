import { getPasswordErrorMsgs } from './../utils/password-policy';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatSnackBar, ErrorStateMatcher } from '@angular/material';
import { UserValidation } from '../shared/validations/user-validations';
import { FormGroup, FormBuilder, Validators, FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { AdminApiService } from './../services/admin-api.service';
import { UI_URI } from '../utils/api-constants';
import { passwordPolicy } from '../utils/password-policy';
import { CustomSnackBarComponent } from '../custom-snack-bar/custom-snack-bar.component';
import { SharedService } from '../services/shared.service';
import { UserErrorMessages } from '../shared/error-messages/userErrorMessages';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';

/**
 * ResetPwdUser represents the type of ResetPwdUser.
 */
export interface ResetPwdUser {
  loginName: string;
  password: string;
  verifyPassword: string;
  id: number;
  company: any;
}


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control:  FormControl | null,  form:  FormGroupDirective | NgForm | null):  boolean {
    return (control.invalid && control.touched);
  }
}

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  code: any;
  headerInfo: any = {
    headerName: 'Workflow manager',
    isLoginPage: true
  };
  resetPwdTokenDetail: any;
  commonXHRErrorMsg = 'Error Occured';
  matcher = new MyErrorStateMatcher();
  isrOldPasswordAvailable = false;
  isChangePassword = false;
  loginUsrName = '';
  userVisited;
  apiProp: any;

  constructor ( private _fb: FormBuilder,
                private _route: ActivatedRoute,
                private _adminApiSvc: AdminApiService,
                private _snackBar: MatSnackBar,
                private _sharedSvc: SharedService,
                private _snkBarSvc: SnackBarService) {}

  /**
   * ngOnInit() calls while page load.
   * First to get the User Name from the URL query string.
   * getUserForPwdReset() calls to the user detail using the User Name.
   * After getUserForPwdReset() the form needs to be updated.
   */
  public ngOnInit() {
    this.apiProp = this._sharedSvc.getSharedData('appProperties');
    this.loadFormFields();
    // To get the encrypted code from query param.
    this._route.params.subscribe(params => {
      console.log(params, 'params');
      // Set the the encrypted code globally.
      this.code = params['code'];
      console.log('CODE > ', params);
      // To get user details using the encrypted code.
      this._adminApiSvc.getUserForPwdReset(this.code).subscribe(result => {
        console.log('ngOnInit > ', result);
        // Assigning the getUserForPwdReset() response to the resetPwdTokenDetail globally.
        this.resetPwdTokenDetail = result;
        // Set the user visited flag
        this.userVisited = this.resetPwdTokenDetail.visited ? 'VISITED' : 'NEW';
        // result.loginId contains loginId and domain as a string with '#' as separator.
        // Splitting the result.loginId to get loginName and domain.
        const splittedLoginId = result.loginId.split('#');
        // Assigning the loginName from the result.loginId split/array.
        const loginName = splittedLoginId[0];
        // tslint:disable-next-line:max-line-length
        // Creating the resetPwdUser object to set the Reset User Form after getting the user details from the api using the unique encrypted code.
        const resetPwdUser: ResetPwdUser = {
          loginName: loginName,
          password: null,
          verifyPassword: null,
          id: result.id,
          company: {
            'domainName': splittedLoginId[2]
          }
        };
        // Reset the Reset User Form.
        this.resetForm.setValue(resetPwdUser);
      }, error => {
        console.log('Error', error);
        // Displaying the error message if is there any error.
        this._snkBarSvc.showErrorSnackBar(this.commonXHRErrorMsg, SnackbarClasses.errorBottom1);
        if (error && error.status === 20000) {
          this.resetForm.get('password').setErrors({
            isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['multipleRules']['multipleRulesError']
          });
          this.resetForm.get('password').updateValueAndValidity();
        }
        // Reset the form values if present.
        this.resetForm.reset();
      });
    });
  }

  /**
   * resetPwdWithoutAuth() calls when user clicks the Reset Password button.
   * One service api is being called and which is not authenticated.
   * Defect involves: 5481.
  */
  public resetPwdWithoutAuth(resetForm: any): any {
    const encNewPwd: string = resetForm.value.password;
    const encEncCode: string = encodeURIComponent(this.code);

    const encodedValue = { 'token': encEncCode, 'password': encNewPwd};
    this._adminApiSvc.resetPwdWithoutAuth(encodedValue).subscribe(result => {
      sessionStorage.setItem('resetPassword', 'true');
      this.onNavigate('connect_ui');
    }, error => {
      console.log('Error', error);
      this._snkBarSvc.showErrorSnackBar(this.commonXHRErrorMsg, SnackbarClasses.errorBottom1);
    });
  }

  /**
   * resetPwd() calls when user clicks the Reset Password button.
   * Three service apis are being called in this.
   * getUserIdForPwdReset() service method calls to get the User Id.
   * resetPwd() service method calls to save the password in the DB.
   * disableEncryptionCode() service method calls to set visited flag as true in DB.
   * Finally to redirect to the login screen of Connect UI.
   * @param resetForm
   */
  public resetPwd(resetForm: any) {
    console.log('resetPwd', resetForm);
    this._adminApiSvc.getUserIdForPwdReset(resetForm.value.loginName).subscribe(result => {
      const userId: number = result.id;
      resetForm.value.id = userId;
      // Validate the password at API level.
      this._adminApiSvc.validatePasswordAtAPILevel(resetForm.value.loginName, resetForm.value.password).subscribe(validatedPwd => {

        // Store password once the password is validated.
        if (validatedPwd.status === 1) {

          // tslint:disable-next-line:no-shadowed-variable
          this._adminApiSvc.resetPwd(resetForm.value).subscribe(result => {
            console.log('result', result);
            this.resetPwdTokenDetail.visited = true;
            this.resetPwdTokenDetail.id = userId;
            // tslint:disable-next-line:no-shadowed-variable
            this._adminApiSvc.disableEncryptionCode(this.resetPwdTokenDetail).subscribe(result => {
              console.log('disableEncryptionCode', result);
              sessionStorage.setItem('resetPassword', 'true');
              this.onNavigate('connect_ui');
            }, error => {
              this._snkBarSvc.showErrorSnackBar(this.commonXHRErrorMsg, SnackbarClasses.errorBottom1);

            });
          }, error => {
            console.log('Error', error);
              this._snkBarSvc.showErrorSnackBar(this.commonXHRErrorMsg, SnackbarClasses.errorBottom1);

          });
        } else {
          const validatedPwdErrors = getPasswordErrorMsgs(passwordPolicy, validatedPwd.errors);
          this._sharedSvc.setSharedData('multiSnackBarMsgs', validatedPwdErrors);
          this.onDisplayingErrorCustomSnackBar();
          this._sharedSvc.deleteSharedData('multiSnackBarMsgs');
        }
      });
    }, error => {
      console.log('getUserIdForPwdReset', error);
      this._snkBarSvc.showErrorSnackBar(this.commonXHRErrorMsg, SnackbarClasses.errorBottom1);

    });
  }

  /**
   * loadFormFields() loads the form fields into the FormGroup
  */
  public loadFormFields(): void {
    this.resetForm = this._fb.group({
      loginName: ['', Validators.required],
      password: ['', [UserValidation.PasswordValidations]],
      verifyPassword: ['', [UserValidation.matchPassword, UserValidation.confirmPasswordValidations]],
      id: [''],
      company: [{
        'domainName': this.apiProp.admin_ui.DOMAIN
      }],
    },
  );
  }

  /**
   * checkWithOldPassword() calls when user focus Out from new password input.
   * One service api is being called to check with old password.
  */
  public checkWithOldPassword(): any {
    console.log('checkForOldPassword called');
    if (this.isChangePassword) {
      const encNewPwd: string = btoa(this.resetForm.value.password);
      this._adminApiSvc.checkWithOldPassword(this.loginUsrName, encNewPwd).subscribe(result => {
          console.log(result, 'checkWithOldPassword result');
          this.isrOldPasswordAvailable = true;
          this.resetForm.get('password').setErrors({
            isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['multipleRules']
          });
      }, error => {
        console.log('checkWithOldPassword Error', error);
      });
    }
  }

  /**
   * onNavigate() calls on seccess of the password creation.
   * @param url
   */
  public onNavigate(url: string): void {
    const appProperties = this._sharedSvc.getSharedData('appProperties');
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/${url}`;
  }

  /**
   * onDisplayingErrorCustomSnackBar shows the snack bar popup
  */
  public onDisplayingErrorCustomSnackBar(action?: string) {
    this._snackBar.openFromComponent(CustomSnackBarComponent, {
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }

  /**
   * check the type of error message
   */
  getErrorType(errorMsg) {
    console.log(typeof errorMsg);
    return typeof errorMsg;
  }
}
