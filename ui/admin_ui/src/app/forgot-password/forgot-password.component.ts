import { Component, OnInit } from '@angular/core';
import { AdminApiService } from '../services/admin-api.service';
import { MatSnackBar, ErrorStateMatcher } from '@angular/material';
import { SharedService } from '../services/shared.service';
import { FormBuilder, Validators, FormGroupDirective, NgForm, FormControl } from '@angular/forms';
import { UserValidation } from '../shared/validations/user-validations';
import { UserErrorMessages } from '../shared/error-messages/userErrorMessages';
import { MyErrorStateMatcher } from '../reset-password/reset-password.component';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})

export class ForgotPasswordComponent implements OnInit {
  appProperties;
  emailForResetPwd;
  userName;
  forgotForm;
  headerInfo: any = {
    headerName: 'Workflow manager',
    isLoginPage: true
  };
  checkUsrExistRes;
  matcher = new MyErrorStateMatcher();

  constructor(
    private _fb: FormBuilder,
    private _adminApiSvc: AdminApiService,
    private _snackBar: MatSnackBar,
    private _sharedSvc: SharedService,
    private _snackBarSvc: SnackBarService) { }

  ngOnInit() {
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    this.loadFormFields();
  }

  /**
   * loadFormFields() loads the form fields into the FormGroup
  */
  public loadFormFields(): void {
    this.forgotForm = this._fb.group({
      loginName: ['', UserValidation.userNameVal]
    });
  }

  public checkUserNameExist() {
    this._adminApiSvc.checkUserNameExist(this.forgotForm.value.loginName).subscribe((result) => {
      this.checkUsrExistRes = result;
      if (this.checkUsrExistRes) {
        this.emailForResetPwd = this.checkUsrExistRes.email;
        this.onNavigatingToResetPassword();
      }
    },
    error => {
      this.forgotForm.get('loginName').setErrors({
        isNotValidUserName: true,
        errorMsg: UserErrorMessages.errorMsg['userName']['usrNameNotExist']
      });
      console.log('Error check UserName Exist', error);
    }
    );
  }

  public onNavigatingToResetPassword() {
    // To encode the reset email url using base64.
    // tslint:disable-next-line:max-line-length
    const resetEmailUrl: string = btoa(`https://${this.appProperties.host}:${this.appProperties.appPort}${this.appProperties.admin_ui.EMAIL_CODE}`);

    // To store triggered email in the DB for password reset.
    // tslint:disable-next-line:max-line-length
    this._adminApiSvc.sendForgotEmailDetails(this.forgotForm.value.loginName, this.appProperties.admin_ui.DOMAIN, this.emailForResetPwd, resetEmailUrl).subscribe(result => {
      const successMsg = 'Reset password email sent successfully.';
      this._snackBarSvc.showSuccessSnackBar(successMsg, SnackbarClasses.successBottom1);
      this.onNavigate('connect_ui');

    }, error => {
      console.log('sendForgotEmailDetails', error);
    });
  }

  public onBackBtn() {
    this.onNavigate('connect_ui/#/login');
  }


  /**
   * onNavigate() calls on seccess of the password creation.
   * @param url
   */
  public onNavigate(url: string): void {
    const appProperties = this._sharedSvc.getSharedData('appProperties');
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/${url}`;
  }

}
