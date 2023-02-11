import { Component, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { Login } from '../model/login';
import { AuthService } from '../AuthService/auth.service';
import { FormGroup, FormBuilder, FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { LoginValidation } from '../../shared/validations/login-validation';

import { ErrorStateMatcher } from '@angular/material/core';
import { MatSnackBar } from '@angular/material';
import { HeaderInfo } from '../../shared/header.model';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from 'src/app/standard-names/constants';
import { SharedService } from 'src/app/shared/shared.service';
import { NotificationtoasterService } from 'src/app/shared/notificationtoaster.service';
import { PermissionService } from 'src/app/shared/permission.service';

/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return (control.invalid && control.touched);
  }
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  invalidCredentials = false;
  loginFormGroup: FormGroup;
  matcher = new MyErrorStateMatcher();
  headerInfo: HeaderInfo = {
    headerName: 'Workflow manager',
    isLoginPage: true
  };
  loginSuccessMsg = 'Username or Password is incorrect.';
  userDisabledMsg = 'Your access has been revoked. Please contact your Admin.';
  resetPwdSuccessMsg = 'Password reset successful.';
  userLockedMsg = 'Your access has been revoked. Please contact your Admin.';
  resetPasswordRes = '';
  constructor(
    private fromB: FormBuilder,
    private _authService: AuthService,
    private _router: Router,
    private _snackBar: MatSnackBar,
    private _render: Renderer2,
    private translate: TranslateService,
    private _snackBarSvc: SnackBarService,
    private _sharedSvc: SharedService,
    private _NTS: NotificationtoasterService,
    private _PS: PermissionService
  ) {
    this.translate = translate;
  }

  ngOnInit() {
    this.loginFormGroup = this.fromB.group({
      'userName': ['', LoginValidation.userNameValidation],
      'password': ['', LoginValidation.passwordValidation]
    });
    // Check for reset password local storage value. Show the success message
    this.resetPasswordRes = sessionStorage.getItem('resetPassword');
    if (this.resetPasswordRes === 'true') {
      sessionStorage.removeItem('resetPassword');
      this._snackBarSvc.showSuccessSnackBar(this.resetPwdSuccessMsg, SnackbarClasses.successBottom1);
    }
  }

  navToRSRLogin() {
    const appProperties = this._sharedSvc.getData('appProperties');
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/connect_ui/#/rsr-login`;
  }

  /**
   * Checking for user authendication.
   * Sedding the username and password information to server.
   * Using the response navigatiing to orders or showing the error message.
   */
  onLogin() {
    const credentials = this.loginFormGroup.value;

   // credentials.userName = credentials.username.toLowerCase();

   credentials.userName = credentials.userName.toLowerCase();
    this._authService.checkAuth(credentials).subscribe((res) => {
      sessionStorage.setItem('userName', credentials.userName);
      document.cookie = `brownstoneauthcookie=${res};path=/`;
      const userName = sessionStorage.getItem('userName');
      const userDetails = {
        userNameDetails: `${userName}`,
        domainDetails: `${this._sharedSvc.getData('appProperties').admin_ui.DOMAIN}`
      };
      this._NTS.loadNotifToster();
      this.getUserRole(userDetails);
      // this._authService.getPermissions().subscribe((res1) => {
      //   if (res1) {
      //     this._PS.userPermissions = res1.toString();
      //   }
      // }, error1 => {
      //   this._snackBarSvc.showErrorSnackBar('Unable to get the user permission details.', SnackbarClasses.errorBottom1);
      // });
    }, error => {
      this._render.selectRootElement('#userName').focus();
      this.loginFormGroup.reset();
      console.log(error, 'error');
      if (error.toLowerCase() === 'user is disabled') {
        this._snackBarSvc.showErrorSnackBar(this.userDisabledMsg, SnackbarClasses.errorBottom1);
      } else if (error.toLowerCase() === 'user account is locked') {
        this._snackBarSvc.showErrorSnackBar(this.userLockedMsg, SnackbarClasses.errorBottom1);
      } else {
        this._snackBarSvc.showErrorSnackBar(this.loginSuccessMsg, SnackbarClasses.errorBottom1);
      }
    });
  }

  onFgtPwdclick() {
    window.location.href = `/admin_ui/#/forgot-password`;
  }

  /**
   * to get the user details
   * @param userDetails userInfo
   */
  getUserRole(userDetails) {
    this._authService.getUserRole(userDetails).subscribe(response => {
      const autoLoggedOut = sessionStorage.getItem('autoLoggedOut');
      sessionStorage.removeItem('autoLoggedOut');
      const rolesInfo: any = response;
      const roleInformation = rolesInfo.map(user => user.description);
      const rolesInfoData = roleInformation.join(', ');

      const rolesIdInfo: Array<string> = rolesInfo.map(user => user.id);
      const rolesId = rolesIdInfo.join(', ');

      const roleInfo = {
        roleID: rolesId,
        roleInfo: rolesInfoData
      };
      sessionStorage.setItem('usersInfo', JSON.stringify(roleInfo));
      console.log(autoLoggedOut, 'autoLoggedOut========');
      if (autoLoggedOut !== '' && autoLoggedOut && autoLoggedOut.indexOf('login') === -1) {
        window.location.href = autoLoggedOut;
      } else {
        // this._router.navigate(['overview']);
        const appProperties = this._sharedSvc.getData('appProperties');
        window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/connect_ui/#/overview`;
      }
    }, errorRoles => {
      this._snackBarSvc.showErrorSnackBar('Error Occured while fetching User Role', 'failed-snackbar-bottom1');
      console.log(errorRoles);
    });
  }

}
