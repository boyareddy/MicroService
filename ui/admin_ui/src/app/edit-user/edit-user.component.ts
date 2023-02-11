import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, FormArray, FormControl } from '@angular/forms';
import { MatSnackBar, MatDialog } from '@angular/material';
import { AdminApiService } from '../services/admin-api.service';
import QueryStrings, { SnackbarClasses } from '../utils/query-strings';
import { Router } from '@angular/router';
import { UserValidation } from '../shared/validations/user-validations';
import { EditUser } from '../models/user.model';
import { TabService } from './../services/tab.service';
import { API_CONSTANTS } from '../utils/api-constants';
import { SharedService } from '../services/shared.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '../services/snack-bar.service';
import { UserErrorMessages } from '../shared/error-messages/userErrorMessages';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  editUserForm: FormGroup;
  userName: string;
  id: number;
  userRoles: string[];
  curUserRoles: string[];
  userRolesTemp: string[];
  emailForResetPwd: string;
  headerInfo: any = {
    headerName: 'Create order',
    currPage: 'header.administration',
    isBackRequired: true
  };
  isMultiTestNotChecked = false;
  appProperties;
  iniMailId: string;
  constructor (private _fb: FormBuilder,
              private _dialogBox: MatDialog,
              private _route: ActivatedRoute,
              private _adminApiSvc: AdminApiService,
              private _router: Router,
              private _tabSvc: TabService,
              private _snackBar: MatSnackBar,
              private _sharedSvc: SharedService,
              private _snackBarSvc: SnackBarService) {}

  ngOnInit() {
  this.appProperties = this._sharedSvc.getSharedData('appProperties');
    this.loadFormFields();

    // Loading the all User Roles
    this.loadUserRoles();

    // To get the userName from query param.
    this._route.params.subscribe(params => {
      this.id = params['id'];
      this.loadUserDetail(this.id);
    });
  }

  public onNavigatingToResetPassword() {
    console.log(`https://${this.appProperties.host}:${this.appProperties.appPort}${this.appProperties.admin_ui.EMAIL_CODE}`);
    // To encode the reset email url using base64.
    // tslint:disable-next-line:max-line-length
    const resetEmailUrl: string = btoa(`https://${this.appProperties.host}:${this.appProperties.appPort}${this.appProperties.admin_ui.EMAIL_CODE}`);

    // To store triggered email in the DB for password reset.
    // tslint:disable-next-line:max-line-length
    this._adminApiSvc.saveResetEmailDetails(this.userName, this.appProperties.admin_ui.DOMAIN, this.emailForResetPwd, resetEmailUrl).subscribe(result => {
      const successMsg = 'Reset password email sent successfully.';
      this._snackBarSvc.showSuccessSnackBar(successMsg, SnackbarClasses.successBottom1);
    }, error => {
      console.log('saveResetEmailDetails', error);
    });
  }

  /**
   * loadUserDetail() loads the user detail when user enters to the Edit User page.
  */
  public loadUserDetail(id: number): void {
    this._adminApiSvc.getUser(id).subscribe((result: EditUser) => {
      // To set emailForResetPwd value globally used for Password Reset
      this.emailForResetPwd = result.email;
      this.userName = result.loginName;

      // tslint:disable-next-line:no-shadowed-variable
      const user: EditUser = (({ id,
                              firstName,
                              lastName,
                              loginName,
                              email,
                              userRole,
                              retired,
                              // tslint:disable-next-line:max-line-length
                              userPreferences }) => ({ id, firstName, lastName, loginName, email, userRole, retired, userPreferences }))(result);
      user.userRole = [];
      this.iniMailId = user.email;
      this.editUserForm.setValue(user);
      this.getUserRoleDetails(this.userName);
    }, error => {
      console.log('loadUserDetail', error);
    });
  }

  /**
   * getUserRoleDetails() loads the user role details.
  */
 public getUserRoleDetails(userName: String) {
  this._adminApiSvc.getUserRoleDetails(userName).subscribe(res => {
    if (res.length > 0) {
      const userRoles = <FormArray>this.editUserForm.get('userRole') as FormArray;
      res.forEach(elm1 => {
        userRoles.push(new FormControl(elm1.name));
      });
      this.curUserRoles = res.map(ele => ele.name);
    }
    this.userRoles = this.userRolesTemp;
  }, error => {
    this.userRoles = this.userRolesTemp;
    console.log('saveResetEmailDetails', error);
  });
}

  /**
   * onAddingUser() calls when admin clicks the save button in Add User form.
  */
  public onUpdatingUser(addUserFormData): void {
    if (this.editUserForm.dirty) {
      console.log('onAddingUser', addUserFormData);
      this._adminApiSvc.updateUser(addUserFormData.value, QueryStrings.addUserQuery(addUserFormData.value.userRole)).subscribe(result => {
        console.log('onAddingUser > result', result);
        this.updateUserRoles();
      }, error => {
        const errMessage = 'Error Occured';
        console.log('onAddingUser > Error', error);
        this._snackBarSvc.showErrorSnackBar(errMessage, SnackbarClasses.errorBottom1);
      });
    } else {
      this._dialogBox.open(ConfirmDialogComponent, {
        width: '486px',
        height: '185px',
        data: {onlyWarn: 'No changes have been made.'},
      });
    }
  }

  updateUserRoles() {
    const userRoles = this.editUserForm.controls.userRole.value;
    console.log(userRoles, 'userRoles################');
    let selectedUserRoles = [];
    this.userRolesTemp.forEach((elm: any, ind) => {
      if (userRoles.indexOf(elm.name) !== -1) {
        selectedUserRoles.push(elm);
      }
    });
    selectedUserRoles = selectedUserRoles.map((elm) => {
      return {'id': elm.id};
    });
    this._adminApiSvc.updateUserRoles(this.userName, selectedUserRoles).subscribe(result => {
      console.log('onAddingUser > result', result);
      this._tabSvc.setTab({index: 0});
      this._snackBarSvc.showSuccessSnackBar('User details updated successfully.', SnackbarClasses.successBottom1);
      this._router.navigate(['settings']);
    }, error => {
      const errMessage = 'Error Occured';
      console.log('updateUserRoles > Error', error);
      this._snackBarSvc.showErrorSnackBar(errMessage, SnackbarClasses.errorBottom1);
    });
  }

  /**
   * loadFormFields() loads the form fields into the FormGroup
  */
  public loadFormFields(): void {
    this.editUserForm = this._fb.group({
      id: [],
      firstName: ['', [UserValidation.NameValidations]],
      lastName: ['', [UserValidation.NameValidations]],
      email: ['', [UserValidation.EmailValidations]],
      loginName: ['', [UserValidation.UsernameValidations]],
      userRole: this._fb.array([], [UserValidation.onValidatingCheckbox]),
      userPreferences: [[{
        'preferenceKey': 'LOCALE',
        'value': 'en_US'
      }]],
      retired: []
    });
  }

  /**
   * loadUserRole() calls to set the User Role values
  */
  public loadUserRoles(): void {
    this._adminApiSvc.getUserRoleList().subscribe(result => {
      console.log(result, 'user role result-------');
      if (result) {
        this.userRolesTemp = result.filter(elm => elm.description && elm.description.toLowerCase() !== 'device');
      }
    }, error => {
      console.log('Error on getting user roles', error);
    });
  }

  public onSelectUserRole(event): void {
    console.log('onSelectUserRole > ', event);
    const userRoles = <FormArray>this.editUserForm.get('userRole') as FormArray;

    if (event.checked) {
      userRoles.push(new FormControl(event.source.value));
    } else {
      const ind = userRoles.controls.findIndex(userRole => userRole.value === event.source.value);
      userRoles.removeAt(ind);
    }
  }

  public onDefaultChecking(userRole): boolean {
    return this.curUserRoles.indexOf(userRole) > -1;
  }

  /**
   * goBack() calls when user cancels the Add User
  */
  public goBack(): void {
    if (!this.editUserForm.dirty) {
      this.back();
    } else {
      this._dialogBox.open(ConfirmDialogComponent, {
        width: '486px',
        height: '172px',
        data: 'Do you want to proceed without saving the changes',
      }).afterClosed().subscribe(res => {
        // tslint:disable-next-line:no-unused-expression
        res ? this.back() : 'Not to do anything';
      });
    }
  }

  public back(): void {
    this._tabSvc.setTab({index: 0});
    this._router.navigate(['settings']);
  }

  public onFocusOfTestOption() {
    console.log('onFocusOfTestOption', this.editUserForm.controls.userRole.value);
    // Make form to dirty to know any changes happened or not
    this.editUserForm.controls.userRole.markAsDirty();

    if (this.editUserForm.controls.userRole.value.length > 0) {
      this.isMultiTestNotChecked = false;
    } else {
      this.isMultiTestNotChecked = true;
    }
  }

  /**
   * To check duplicate email
  */
  public onCheckingEmailDuplication(emailId) {
    if (emailId.value && emailId.value !== '' && emailId.value !== this.iniMailId && !this.editUserForm.controls.email.errors) {
      this._adminApiSvc.checkEmailDuplication(emailId.value).subscribe(successData => {
        if (successData && successData.length >= 1) {
          this.editUserForm.controls.email.setErrors({
            errorMsg: UserErrorMessages.errorMsg['email']['exists'],
            isNotValidEmail: true
          });
        }
      });
    }
  }
}
