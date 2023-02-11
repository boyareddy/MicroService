import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatSnackBar, MatDialog } from '@angular/material';
import { AdminApiService } from '../services/admin-api.service';
import { UserValidation } from '../shared/validations/user-validations';
import { UI_URI } from '../utils/api-constants';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

  myProfileForm: FormGroup;
  commonErrorMsg = 'Error occured';
  constructor(private _adminApiService: AdminApiService,
    private _fb: FormBuilder,
    private _dialogBox: MatDialog,
    private _snackBar: MatSnackBar,
    private _route: ActivatedRoute,
    private _router: Router,
    private _snackBarSvc: SnackBarService) {
  }

  ngOnInit() {
    const loginName = sessionStorage.getItem('userName');
    if (!loginName) {
      // window.location.href = `/connect_ui/#/login`;
    }
    this.loadFormFields();
    // this._route.params.subscribe(params => {
    //   this.loadProfile(params['loginName']);
    // });
    this.loadProfile(loginName);
  }

  /**
   * loadFormFields() loads the form fields into the FormGroup
  */
  public loadFormFields(): void {
    this.myProfileForm = this._fb.group({
      id: [''],
      loginName: [''],
      contact: this._fb.group({
        address1: ['', [UserValidation.addressValidation]],
        mobile: ['', [UserValidation.phoneNumberValidations]]
      })
    });
  }

  loadProfile(loginUserName?: string) {
    const loginName: string = loginUserName || 'admin';
    this._adminApiService.getUserId(loginName).subscribe(id => {
      console.log('loadProfile', id);
      this._adminApiService.getUserProfile(id).subscribe(profile => {
        console.log('Profile > ', profile);
        this.setMyProfile(profile);
      }, error => {
        this._snackBarSvc.showErrorSnackBar(this.commonErrorMsg, SnackbarClasses.errorBottom2);
      });
    }, error => {
      this._snackBarSvc.showErrorSnackBar(this.commonErrorMsg, SnackbarClasses.errorBottom2);
    });
  }

  setMyProfile(profile) {
    const parsedProfile = {
      id: profile.id,
      loginName: profile.loginName,
      contact: {
        address1: profile.contact[0] ? profile.contact[0].address1 : '',
        mobile: profile.contact[0] ? profile.contact[0].mobile : ''
      }
    };
    this.myProfileForm.setValue(parsedProfile);
  }

  getMyProfile() {
    const parsedProfile = {
      id: this.myProfileForm.value.id,
      loginName: this.myProfileForm.value.loginName,
      contact: [{
        address1: this.myProfileForm.value.contact.address1,
        mobile: this.myProfileForm.value.contact.mobile
      }]
    };
    return parsedProfile;
  }

  saveMyProfile() {
    if (this.myProfileForm.dirty) {
      const userProfile: any = this.getMyProfile();
      this._adminApiService.updateUserStatus(userProfile).subscribe(result => {
        console.log('saveMyProfile', result);
        // Make form clean.
        this.myProfileForm.controls.contact.markAsPristine();

        const message = 'User details saved successfully.';
        this._snackBarSvc.showSuccessSnackBar(message, SnackbarClasses.successBottom2);
      }, error => {
        this._snackBarSvc.showErrorSnackBar(this.commonErrorMsg, SnackbarClasses.errorBottom2);
      });
    } else {
      this._dialogBox.open(ConfirmDialogComponent, {
        width: '486px',
        height: '200px',
        data: { onlyWarn: 'No changes have been made.' },
      });
    }
  }
}
