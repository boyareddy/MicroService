import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, FormArray, FormControlName, FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { AdminApiService } from '../services/admin-api.service';
import QueryStrings, { SnackbarClasses } from '../utils/query-strings';
import { UserValidation } from '../shared/validations/user-validations';
import { SharedService } from '../services/shared.service';
import { TabService } from '../services/tab.service';
import { Observable } from 'rxjs';
import StringConstants from '../utils/string-constants';
import { API_CONSTANTS } from '../utils/api-constants';
import { SnackBarService } from '../services/snack-bar.service';
import { UserErrorMessages } from '../shared/error-messages/userErrorMessages';
import { PermissionService } from '../services/permission.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  addUserForm: FormGroup;
  step = 0;
  userRoles: Object[];
  isMultiTestChecked = false;
  headerInfo: any = {
    headerName: 'Create order',
    currPage: 'header.administration',
    isBackRequired: true
  };
  userPreferences: any = {

  };
  isMultiTestNotChecked = false;
  havingAccess = false;

  constructor (private _fb: FormBuilder,
               private _adminService: AdminApiService,
               private _router: Router,
               private _sharedSvc: SharedService,
               public _tabSvc: TabService,
               private _snackBar: MatSnackBar,
               private _snkBarSvc: SnackBarService,
               private _permission: PermissionService
               ) {}

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  ngOnInit() {
    this._permission.checkPermissionObs('Create_User').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    // For checking whether the cookie is valid.
    this._adminService.getOwnerId(this._sharedSvc.getSharedData('appProperties').admin_ui.DOMAIN).subscribe(result => {
      // Nothing to do
    }, error => {
      // Nothing to do
    });

    this.loadFormFields();
    this.loadUserRoles();
  }

  /**
   * onAddingUser() calls when admin clicks the save button in Add User form.
  */
  public onAddingUser(addUserFormData): void {
    const userFormData = addUserFormData.value;
    userFormData.loginName = userFormData.loginName.toLowerCase();
    this._adminService.addUser(userFormData, QueryStrings.addUserQuery(addUserFormData.value.userRole)).subscribe(result => {
      console.log('onAddingUser > result', result);
      this._snkBarSvc.showSuccessSnackBarWithLocalize('user.userAddSuccess', SnackbarClasses.successBottom2);
      this._sharedSvc.setSharedData('USER_EDITTED', addUserFormData.value.loginName);
      this._tabSvc.setTab({index: 0});
      this._router.navigate(['settings']);
    }, error => {
      console.log('onAddingUser > Error', error);
      if (error.message) {
        if (error.status  === 2502) {
          this._snkBarSvc.showErrorSnackBar(StringConstants.STRING('USER_EXIST'), SnackbarClasses.errorBottom1);
        } else {
          this._snkBarSvc.showErrorSnackBar(error.message, SnackbarClasses.errorBottom1);
        }
      }
    });
}

  /**
   * loadUserRole() calls to set the User Role values
  */
  public loadUserRoles(): void {
    // this.userRoles = [
    //   { 'name': 'Admin' },
    //   { 'name': 'Lab operator' },
    //   { 'name': 'Lab manager' }
    // ];
    this._adminService.getUserRoleList().subscribe(result => {
      console.log(result, 'user role result-------');
      if (result) {
        this.userRoles = result.filter(elm => elm.description && elm.description.toLowerCase() !== 'device');
      }
    }, error => {
      console.log('Error on getting user roles', error);
    });
  }

  /**
   * loadFormFields() loads the form fields into the FormGroup
  */
  public loadFormFields(): void {
    this.addUserForm = this._fb.group({
      firstName: ['', [UserValidation.NameValidations]],
      lastName: ['', [UserValidation.NameValidations]],
      email: ['', [UserValidation.EmailValidations]],
      loginName: ['', [UserValidation.UsernameValidations]],
      userRole: this._fb.array([], [UserValidation.onValidatingCheckbox]),
      userPreferences: [[{
        'preferenceKey': 'LOCALE',
        'value': 'en_US'
     }]]
    });
  }

  /**
   * onSelectUserRole() calls when user selects a User Role
  */
  public onSelectUserRole(event): void {
    const userRoles = <FormArray>this.addUserForm.get('userRole') as FormArray;

    if (event.checked) {
      userRoles.push(new FormControl(event.source.value));
    } else {
      const ind = userRoles.controls.findIndex(userRole => userRole.value === event.source.value);
      userRoles.removeAt(ind);
    }
  }

  /**
   * goBack() calls when user cancels the Add User
  */
  public goBack(): void {
    this._router.navigate(['settings']);
  }

  onCheckBoxValidation() {
    if (this.addUserForm && this.addUserForm.get('userRole')) {
      if ((JSON.stringify(this.addUserForm.get('userRole').value).match(/true/g) || []).length > 0) {
        return null;
      } else {
        return { valid: false };
      }
    } else {
      return { valid: false };
    }
  }

  public onFocusOfTestOption() {
    console.log('onFocusOfTestOption', this.addUserForm.controls.userRole);
    if (this.addUserForm.controls.userRole.value.length > 0) {
      this.isMultiTestNotChecked = false;
    } else {
      this.isMultiTestNotChecked = true;
    }
  }

  /**
   * To check duplicate email
  */
  public onCheckingEmailDuplication(emailId) {
    if (emailId.value && emailId.value !== '' && !this.addUserForm.controls.email.errors) {
      this._adminService.checkEmailDuplication(emailId.value).subscribe(successData => {
        if (successData && successData.length >= 1) {
          this.addUserForm.controls.email.setErrors({
            errorMsg: UserErrorMessages.errorMsg['email']['exists'],
            isNotValidEmail: true
          });
        }
      });
    }
  }

  public onCheckingUserNameuplication(loginName) {
    if (loginName.value && loginName.value !== '' && !this.addUserForm.controls.loginName.errors) {
      this._adminService.checkUserNameDuplication(loginName.value).subscribe((successData ) => {
        if (successData && successData.length >= 1) {
          this.addUserForm.controls.loginName.setErrors({
            errorMsg: UserErrorMessages.errorMsg['userName']['exists'],
            isNotValidUserName: true
          });
        }
      });
    }
  }

}
