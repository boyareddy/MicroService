import { getPasswordErrorMsgs } from './../utils/password-policy';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, ErrorStateMatcher, MatDialog } from '@angular/material';
import { UserValidation } from '../shared/validations/user-validations';
import { FormGroup, FormBuilder, Validators, FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { AdminApiService } from './../services/admin-api.service';
import { CustomSnackBarComponent } from '../custom-snack-bar/custom-snack-bar.component';
import { SharedService } from '../services/shared.service';
import { UserErrorMessages } from '../shared/error-messages/userErrorMessages';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

/**
 * ResetPwdUser represents the type of ResetPwdUser.
 */
export interface ResetPwdUser {
  loginName: string;
  oldPassword: string;
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
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  resetForm: FormGroup;
  code: any;
  headerInfo: any = {
    headerName: 'Workflow manager',
    isLoginPage: true,
    isHomeNavigateRequired: true
  };
  resetPwdTokenDetail: any;
  commonXHRErrorMsg = 'Error Occured';
  matcher = new MyErrorStateMatcher();
  isrOldPasswordAvailable = false;
  loginUsrName = '';
  apiProp: any;
  changePwdBackURL = null;

  constructor ( private _fb: FormBuilder,
                private _route: ActivatedRoute,
                private _adminApiSvc: AdminApiService,
                private _snackBar: MatSnackBar,
                private _sharedSvc: SharedService,
                private _sbSvc: SnackBarService,
                private _router: Router,
                private _dialogBox: MatDialog
                ) {}

  public ngOnInit() {
    this.changePwdBackURL = sessionStorage.getItem('changePwdBackURL');
    sessionStorage.removeItem('changePwdBackURL');
    this.apiProp = this._sharedSvc.getSharedData('appProperties');
    this.loadFormFields();
    this.loginUsrName = sessionStorage.getItem('userName');
    const resetPwdUser: ResetPwdUser = {
      loginName: this.loginUsrName,
      oldPassword: null,
      password: null,
      verifyPassword: null,
      id: null,
      company: {
        'domainName': this.apiProp.admin_ui.DOMAIN
      }
    };
    // Reset the Reset User Form.
    this.resetForm.setValue(resetPwdUser);
  }

  /**
   * changePassword() calls when user clicks the Reset Password button.
   * One service api is being called and which is not authenticated.
   * Defect involves: 5481.
  */
  // public changePassword(resetForm: any): any {
  //   const encNewPwd: string = btoa(resetForm.value.password);
  //   const encEncCode: string = encodeURIComponent(this.code);
  //   this._adminApiSvc.changePwdExistUser(resetForm.value.loginName,
  //     encNewPwd).subscribe(result => {
  //     console.log('changePwdExistUser', result);
  //     sessionStorage.setItem('resetPassword', 'true');
  //     this.onNavigate('connect_ui');
  //   }, error => {
  //     this._sbSvc.showErrorSnackBar(this.commonXHRErrorMsg, SnackbarClasses.errorBottom1);
  //   });
  // }

  /**
   * loadFormFields() loads the form fields into the FormGroup
  */
  public loadFormFields(): void {
    this.resetForm = this._fb.group({
      loginName: ['', Validators.required],
      oldPassword:  ['', [UserValidation.oldPasswordVal]],
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
   * validateUser() calls when user focus Out from old password input.
   * One service api is being called to check username and password correct.
  */
 public validateUserAndChangePwd(): any {
  console.log('validateUser called');
  if (this.resetForm.value.oldPassword) {
    // const encNewPwd: string = btoa(this.resetForm.value.password);
    // const encOldPwd: string = btoa(this.resetForm.value.oldPassword);
    const NewPwd: string = this.resetForm.value.password;
   // const encOldPwd: string = encodeURIComponent(this.resetForm.value.oldPassword);
    const encOldPwd: string = this.resetForm.value.oldPassword;
    const encNewPwd: string = this.resetForm.value.password;
    const credentials = {'userName': this.loginUsrName, 'oldPassword': encOldPwd, 'newPassword': encNewPwd};
    this._adminApiSvc.validateUser(credentials).subscribe(result => {
        console.log(result, 'checkAuth result');
        this._sbSvc.showSuccessSnackBar('Password changed successfully.', SnackbarClasses.successBottom1);
        this.back();
    }, error => {
      if (error && error.status === 20001) {
        this.resetForm.get('oldPassword').setErrors({
          isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['oldpassword']['notValid']
        });
      } else if (error && error.status === 20000) {
        this.resetForm.get('password').setErrors({
          isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['multipleRules']['multipleRulesError']
        });
      }
      console.log('validateUser Error', error);
    });
  }
}

  /**
   * checkForOldPassword() calls when user focus Out from new password input.
   * One service api is being called to check with old password.
  */
  // public checkWithOldPassword(): any {
  //   console.log('checkForOldPassword called');
  //   let resPaw = null;
  //   if (this.resetForm.value.password) {
  //     const encNewPwd: string = btoa(this.resetForm.value.password);
  //     this._adminApiSvc.checkWithOldPassword(this.loginUsrName, this.resetForm.value.password).subscribe(result => {
  //         console.log(result, 'checkForOldPassword result');
  //         resPaw = result;
  //         if (resPaw && resPaw.errors === 6) {
  //           this.isrOldPasswordAvailable = true;
  //           this.resetForm.get('password').setErrors({
  //             isNotValidpasswordValidations: true, errorMsg: UserErrorMessages.errorMsg['password']['multipleRules']
  //           });
  //         }
  //     }, error => {
  //       console.log('checkForOldPassword Error', error);
  //     });
  //   }
  // }

  /**
   * onNavigate() calls on seccess of the password creation.
   * @param url
   */
  public onNavigate(url: string): void {
    const appProperties = this._sharedSvc.getSharedData('appProperties');
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/${url}`;
  }

  public onBackBtn() {
    if (this.resetForm.dirty) {
      this._dialogBox.open(ConfirmDialogComponent, {
        width: '486px',
        height: '185px',
        data: 'Changes made are not saved. Are you sure you want to proceed',
      }).afterClosed().subscribe(res => {
        // tslint:disable-next-line:no-unused-expression
        res ? this.back() : 'Not to do anything';
      });
    } else {
        this.back();
    }
  }

  public back(): void {
    const appProperties = this._sharedSvc.getSharedData('appProperties');
    if (this.changePwdBackURL !== '' && this.changePwdBackURL && this.changePwdBackURL.indexOf('change-password') === -1) {
      window.location.href = this.changePwdBackURL;
    } else {
      // tslint:disable-next-line:max-line-length
      window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/connect_ui/#/orders`;
    }
  }
  /**
   * check the type of error message
   */
  getErrorType(errorMsg) {
    console.log(typeof errorMsg);
    return typeof errorMsg;
  }

}
