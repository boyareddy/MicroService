import { Component, OnInit, AfterViewChecked, ChangeDetectorRef, ViewChild } from '@angular/core';
import { MatSnackBar, MatDialog, MatTabGroup, MatTab, MatTabHeader } from '@angular/material';
import { TabService } from './../services/tab.service';
import { SharedService } from '../services/shared.service';
import { AdminApiService } from './../services/admin-api.service';
import { Observable } from 'rxjs';
import { API_CONSTANTS } from './../utils/api-constants';
import { MyProfileComponent } from '../my-profile/my-profile.component';
import { AsyncConfirmDialogComponent } from '../async-confirm-dialog/async-confirm-dialog.component';
import { SnackBarService } from '../services/snack-bar.service';
import { PermissionService } from '../services/permission.service';
import { LabSettingsComponent } from '../lab-settings/lab-settings.component';
import { TranslateService } from '@ngx-translate/core';
import { HeaderInfo } from '../models/header.model';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit, AfterViewChecked {

  defaultTabIndex = 0;
  headerInfo: HeaderInfo = {
    currPage: 'header.administration',
    headerIcon: 'assets/Images/header/module/Administration.svg'
  };

  @ViewChild('myProfile') myProfile: MyProfileComponent;
  @ViewChild('labSettings') labSettings: LabSettingsComponent;
  @ViewChild('tabs') tabs: MatTabGroup;
  havingAccess = false;
  ishavingAccess = false;
  islabSettinghavingAccess = false;
  isHavingUserManagementAccess = false;
  isHavingAuditAccess = false;
  isHavingAboutAccess = false;
  isHavingBackupAccess = false;

  constructor ( private _cdRef: ChangeDetectorRef,
               private _tabService: TabService,
               private _dialogBox: MatDialog,
               private _sharedSvc: SharedService,
               private _snackBar: MatSnackBar,
               private _snackbarService: SnackBarService,
               private _adminApiSvc: AdminApiService,
               private _permission: PermissionService,
               private _translate: TranslateService) {}

  /**
   * ngOnInit is Angular life cycle hook method.
   */
  ngOnInit() {
   // this.havingAccess = this._permission.checkPermission('Create_User');
    // To handle tab click event
    // tslint:disable-next-line:max-line-length
    // Commented "this.tabs._handleClick = this.handlingTabClick.bind(this);" as there is no My Profile tab as of now and later will enable if it is necessary.
    this.tabs._handleClick = this.handlingTabClick.bind(this);

    // To check whether any user creation is success or not
    const USER_EDITTED = this._sharedSvc.getSharedData('USER_EDITTED');
    if (USER_EDITTED) {
      // this.onDisplayingSnackBar(`${USER_EDITTED} created successfully`);
      this._sharedSvc.deleteSharedData('USER_EDITTED');
    }

    const apiProp = this._sharedSvc.getSharedData('appProperties');
    // First to check whether there is any slected tab previously.
    // And set the selected tab index to the defaultTabIndex.
    this.defaultTabIndex = this._tabService.getSelectedTabIndex();

    // To delete the tab index in the TabService as its no more use for the current setting page.
    // this._tabService.deleteTab();

    // Setting the ownerId for other application flows
    this._adminApiSvc.getOwnerId(apiProp.admin_ui.DOMAIN).subscribe(result => {
      this._adminApiSvc.ownerId = result.id;
    }, error => {
      const errorMsg = 'Error while getting owner id';
      this._snackbarService.showErrorSnackBar(errorMsg, 'failed-snackbar-bottom2');
    });
   // this.havingAccess = this._permission.checkPermission('Create_Bulk_Orders');
    this._permission.checkPermissionObs('Get_Problem_Reports').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.ishavingAccess = res;
      this.checkDefaultIndex();
    });
    this._permission.checkPermissionObs('view_lab_settings').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.islabSettinghavingAccess = res;
      this.checkDefaultIndex();
    });
    this._permission.checkPermissionObs('view_user_management_tab').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.isHavingUserManagementAccess = res;
      this.checkDefaultIndex();
    });
    this._permission.checkPermissionObs('view_audit_log_tab').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.isHavingAuditAccess = res;
      this.checkDefaultIndex();
    });
    this._permission.checkPermissionObs('view_about_tab').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.isHavingAboutAccess = res;
      this.checkDefaultIndex();
    });
    this._permission.checkPermissionObs('view_backup_tab').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.isHavingBackupAccess = res;
      this.checkDefaultIndex();
    });
  }

  checkDefaultIndex() {
    if (!(this.islabSettinghavingAccess ||
      this.isHavingUserManagementAccess ||
      this.isHavingAuditAccess ||
      this.isHavingAboutAccess ||
      this.isHavingBackupAccess)) {
        this.defaultTabIndex = 4;
    } else {
      this.defaultTabIndex = 0;
    }
  }

  /**
   * ngAfterViewChecked is Angular life cycle hook method.
   */
  ngAfterViewChecked() {
    // this._cdRef.detectChanges();
  }

  /**
   * onTabChange() calls when user changes the tab.
  */
  public onTabChange(event): void {
    console.log('onTabChange', event);
    console.log('this.labSettings', this.labSettings);
    this._tabService.setTab(event);
    // Refresh My Profile if 'General settings' tab is clicked.
    // event && event.index === 0 ? this.myProfile.ngOnInit() : 'Not to do anything';

    // Refresh My Profile on every tab change
    // This is called to fix the defect RC-5494
    // if(event && event.index !== 2){
    //   this.myProfile.ngOnInit();
    // }
    // tslint:disable-next-line:no-unused-expression
    event && event.index === 5 ? this.labSettings.ngOnInit() : 'Not to do anything';
    // if (event && event.index !== 5) {
    //   this.labSettings.ngOnInit();
    // }
    // this.labSettings.ngOnInit();
  }

  /**
   * handlingTabClick() calls to prevent and allow the tab change based on the confiemation.
   * @param tab
   * @param tabHeader
   * @param index
   */
  public handlingTabClick(tab: MatTab, tabHeader: MatTabHeader, index: number)  {
    console.log(tab, 'tab info');
    console.log(tabHeader, 'tab header info');
    console.log(index, 'index info');
    console.log(this, 'whole object');
    const args = arguments;
    const translations = this._translate.translations[this._translate.currentLang];
    if (!tab.disabled) {
      const msg = `${translations.labSettings.warnPrompt}`;
      if (this.labSettings.labSettingsInfo.dirty) {
        const labInfo = this.labSettings;
        this._dialogBox.open(AsyncConfirmDialogComponent, {
          width: '600px',
          height: '160px',
          data: {message: `${msg}`, component: this},
        }).afterClosed().subscribe(function(res) {
          if (res.action) {
            labInfo.labSettingsInfo.controls.reportSettings.markAsPristine();
            labInfo.labSettingsInfo.controls.localisationSettings.markAsPristine();
          }
          return res.action && MatTabGroup.prototype._handleClick.apply(res.parentComp.tabs, args);
        });
      } else {
        return true && MatTabGroup.prototype._handleClick.apply(this.tabs, arguments);
      }
    }
  }

  /**
   * onDisplayingSnackBar shows the snack bar popup
  */
  public onDisplayingSnackBar(message: string, action?: string): Observable<any> {
    return this._snackBar.open(message, action, {
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar']
    }).afterDismissed();
  }

  /**
   * onDisplayingSnackBar shows the snack bar popup
  */
  public onDisplayingErrorSnackBar(message: string, action?: string): Observable<any> {
    return this._snackBar.open(message, action, {
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    }).afterDismissed();
  }
}
