import { Component, OnInit, ElementRef, AfterViewInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { HeaderInfo } from '../models/header.model';
import { Observable } from 'rxjs';
import { AdminApiService } from '../services/admin-api.service';
import { MatSnackBar } from '@angular/material';
import ArrayUtil from '../utils/array-util';
import { UserList } from '../models/user.model';
import { SharedService } from '../services/shared.service';
import { XhrProgressLoaderService } from '../services/xhr-progress-loader.service';
import { SnackBarService } from '../services/snack-bar.service';
import { PermissionService } from '../services/permission.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit, AfterViewInit, OnDestroy {

  step = 0;
  userList: UserList = {activeUser: [], archivedUser: []};
  commonErrorMsg = 'Error Occured';
  headerInfo: any = {
    headerName: 'Create order',
    isBackRequired: true
  };
  appProperties;
  ownerId;
  public isSprinerOn = false;
  public rectPro;
  havingAccess = false;
  userListCount = 0;
  userListCountTemp = 0;
  userRoleList = [];
  translations;
  userListRes = [];

  constructor(private _adminApiService: AdminApiService,
              private _snackBar: MatSnackBar,
              private _router: Router,
              private _sharedSvc: SharedService,
              private _snackbarSvc: SnackBarService,
              private elem: ElementRef,
              private _permission: PermissionService,
              private translateSvc: TranslateService
            ) {
              this.appProperties = this._sharedSvc.getSharedData('appProperties');
            }

  ngOnInit() {
    this._permission.checkPermissionObs('Create_User').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    if (this.appProperties) {
      this.isSprinerOn = true;
    this._adminApiService.getOwnerId(this.appProperties.admin_ui.DOMAIN).subscribe(result => {
      this.ownerId = result.id;
      this.onGettingUserList(result.id);
    }, error => {
      // tslint:disable-next-line:max-line-length
      // this.userListRes = [{'id': 14, ' loginName': 'john12', 'retired': true, 'company': null, 'email': '43396', 'modifiedDate': '2019-05-02', 'createdDate': '2019-05-02', 'editedBy': 'admin#@#hcl.com', ' retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'John', 'lastName': 'Peter', ' userPreferences': [{'id': 14, 'preferenceKey': 'LOCALE', 'value': 'en_US'}], 'contact': [], 'role': null, ' entityExtensionValues': {}}, {'id': 13, 'loginName': 'MariaMaruaMariaMaruaMariaMaruaMariaMarua', 'retired': false, 'company': null, 'email': 'testtesttesttesttesttesttest.com', 'roles': 'Lab',  'modifiedDate': '2019-05-02', ' createdDate': '2019-05-02', 'editedBy': 'admin#@#hcl.com', ' retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM', ' lastName': 'czxczxczxc', 'userPreferences': [{'id': 13, 'preferenceKey': 'LOCALE', ' value': 'en_US'}], 'contact': [], ' role': null, 'entityExtensionValues': {}}, {'id': 12, 'loginName': 'zczczc', 'retired': false, 'company': null, 'email': '41808', 'modifiedDate': '2019-04-29', ' createdDate': '2019-04-29', 'editedBy': 'admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, ' password': null, 'firstName': 'MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM', 'lastName': 'MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM', ' userPreferences': [{'id': 12, 'preferenceKey': 'LOCALE', ' value': 'en_US'}], 'contact': [], 'role': null, ' entityExtensionValues': {}}, {'id': 11, 'loginName': 'zxczxczxc', 'retired': false, 'company': null, 'email': '41795', 'modifiedDate': '2019-04-29', 'createdDate': '2019-04-29', 'editedBy': 'admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'deviceuserabcdthreeo', 'lastName': 'deviceuserabcdthreeo', 'userPreferences': [{'id': 11, ' preferenceKey': 'LOCALE', 'value': 'en_US'}], 'contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 10, 'loginName': 'zxvzxvzxv', 'retired': false, 'company' : null, 'email': '41768', 'modifiedDate': '2019-04-29', 'createdDate': '2019-04-29', 'editedBy': 'admin#@#hcl.com', 'retiredAt': null, ' deactivated': false, 'password': null, 'firstName': 'deviceuserabcdthreeo', 'lastName': 'deviceuserabcdthreeo', 'userPreferences': [{'id': 10, 'preferenceKey': 'LOCALE', 'value': 'en_US'}], ' contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 9, 'loginName': 'labmanager', ' retired': false, ' company': null, 'email': '40677', 'modifiedDate': '2019-04-25', 'createdDate': '2019-04-25', ' editedBy': 'admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'labmanager', 'lastName': ' test', 'userPreferences': [{'id': 9, 'preferenceKey': 'LOCALE' , 'value': 'en_US'}], 'contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 8 , 'loginName': 'sam', ' retired': false, 'company': null, 'email': '37719', 'modifiedDate': '2019-04-23', ' createdDate': '2019-04-23', ' editedBy': 'admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, ' password': null, 'firstName': 'sam', ' lastName': 'benni', 'userPreferences': [{'id' : 8, 'preferenceKey': 'LOCALE', 'value': 'en_US'}], ' contact': [], ' role': null, 'entityExtensionValues': {}}, {'id': 7, ' loginName': 'helptest', 'retired': false, 'company': null, ' email': '37598', ' modifiedDate': '2019-04-23', ' createdDate': '2019-04-23', 'editedBy': 'admin#@#hcl.com', ' retiredAt': null, 'deactivated': false, ' password': null, 'firstName': 'helptest', 'lastName': 'helptest', ' userPreferences': [{'id': 7, ' preferenceKey': 'LOCALE', 'value': 'en_US'}], 'contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 5, ' loginName': 'thanos', 'retired': false, ' company': null, 'email': '37284', 'modifiedDate': '2019-04-23', ' createdDate': '2019-04-23', 'editedBy': ' admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'thanos', 'lastName': '', ' userPreferences': [{'id': 5, 'preferenceKey': 'LOCALE', 'value': 'en_US'}], ' contact': [{'id': 2, 'address1': '', 'address2': ' ', 'homePhone': null, 'officePhone': '', 'dayPhone': null, 'city': '', 'state': null, 'country': '', 'zip': '', 'mobile': ''}], 'role': null, ' entityExtensionValues': {}}, {'id': 4, 'loginName': 'dualmanager', 'retired': false, 'company': null, ' email': '37063', 'modifiedDate': '2019-04-23', 'createdDate': '2019-04-23', 'editedBy': 'admin#@#hcl.com', ' retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'manager', ' lastName': 'lab', 'userPreferences': [{'id': 4, 'preferenceKey': 'LOCALE', 'value': 'en_US'}], 'contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 3, 'loginName': 'dualoperator', 'retired': false, 'company': null, 'email': '37018', 'modifiedDate': '2019-04-23', 'createdDate': ' 2019-04-23', 'editedBy': 'admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, 'password': null, ' firstName': 'operator', 'lastName': 'lab', 'userPreferences': [{'id': 3, 'preferenceKey': 'LOCALE', ' value': 'en_US'}], 'contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 2, 'loginName': 'operator', ' retired': false, 'company': null, 'email': '37001', 'modifiedDate': '2019-04-23', ' createdDate': '2019-04-23', ' editedBy': 'admin#@#hcl.com', 'retiredAt': null, 'deactivated': false, ' password': null, 'firstName': 'operator', 'lastName': 'operator', 'userPreferences': [{'id': 2, ' preferenceKey': 'LOCALE', 'value': 'en_US'}], 'contact': [], 'role': null, 'entityExtensionValues': {}}, {'id': 1, ' loginName': 'admin', 'retired': false, 'company': null, 'email': ' 36314', 'modifiedDate': null, ' createdDate': null, 'editedBy': null, 'retiredAt': null, 'deactivated': false, 'password': null, 'firstName': 'admin', 'lastName': 'brownstone', 'userPreferences': [{'id': 1, 'preferenceKey': 'LOCALE', 'value': 'en_US'}], 'contact': [{'id': 1, 'address1': 'address1', 'address2': 'address2', 'homePhone': '8989898989', 'officePhone': '8989898989', 'dayPhone': '8989898989', 'city': 'Tirupur', 'state': '82',
      // 'country': '57', ' zip': '641604', 'mobile': '9898989898'}], 'role': null, 'entityExtensionValues': {}}];
      // const splittedResult = ArrayUtil.splitArrayList(this.userListRes, 'retired');
      // this.userList.activeUser = splittedResult.positive;
      // this.userList.archivedUser = splittedResult.negetive;
      console.log('Error in getting user list', error);
      this.isSprinerOn = false;
    });
    }
  }

  ngAfterViewInit() {
    setTimeout( () => {
      if (this.isSprinerOn) {
        const rect = this.elem.nativeElement.querySelectorAll('.activeusers-container')[0].getBoundingClientRect();
        this.rectPro = rect;
    }
    }, 600);
  }

  ngOnDestroy() {
    this.isSprinerOn = false;
  }

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  /**
   * onGettingUserList retrieves the data from getUserList in service worker AdminApiService
   */
  public onGettingUserList(id): void {
    this._adminApiService.getUserList(id, true).subscribe((result: any) => {
      this.userListRes = result.beans;
      this.setRoleForUsers(this.userListRes);
      this.isSprinerOn = false;
    }, error => {
      this.isSprinerOn = false;
      console.log('Error > UserListComponent > onGettingUserList', error);
      // this.onDisplayingErrorSnackBar(this.commonErrorMsg);
      this._snackbarSvc.showErrorSnackBar(this.commonErrorMsg, ' failed-snackbar-bottom2');
    }, () => {
      this.isSprinerOn = false;
    });
  }

  setRoleForUsers(user_L) {
    this.userListCount = user_L.length;
    this.userListCountTemp = 0;
    this.userRoleList = [];
    user_L.forEach((elem, ind) => {
      this.getUserRoleDetails(elem.loginName);
    });
  }

/**
   * getUserRoleDetails() loads the user role details.
  */
public getUserRoleDetails(userName: String) {
  this._adminApiService.getUserRoleDetails(userName).subscribe(res => {
    this.userListCountTemp++;
    if (res.length > 0) {
      const userRole = res.map((elm) => {
        this.translations = this.translateSvc.translations[this.translateSvc.currentLang];
        // tslint:disable-next-line:max-line-length
        const roleTran = this.translations ? (this.translations[elm.description] ? this.translations[elm.description] : elm.description) : elm.description;
        return roleTran;
      } ).toString();
      const UserRoleArr = {};
      UserRoleArr['description'] = userRole;
      UserRoleArr['userName'] = userName;
      this.userRoleList.push(UserRoleArr);
    }
    if (this.userListCountTemp === this.userListCount) {
      this.addRoleForUsers();
    }
  }, error => {
    this.userListCountTemp++;
    if (this.userListCountTemp === this.userListCount) {
      this.addRoleForUsers();
    }
  });
}

addRoleForUsers() {
  this.userListRes.forEach(ele => {
    const findRole = this.userRoleList.find(ele1 => ele1.userName === ele.loginName);
    if (findRole) {
        ele['role'] = findRole.description.split(',').join(', ');
    }
  });
  // Filter other than device user
  this.userListRes = this.userListRes.filter(elm => elm.role && elm.role.toLowerCase() !== 'device');
  const splittedResult = ArrayUtil.splitArrayList(this.userListRes, 'deactivated');
  this.userList.activeUser = splittedResult.positive;
  this.userList.archivedUser = splittedResult.negetive;
}

  /**
   * onUserArchiving get call on success of user archiving and refreshes the user list
  */
  public onUserArchiving(successMessage: string): void {
    // The below commented code was done previously and the user list used to refresh after snackbar closed.
    // this.onDisplayingSnackBar(successMessage).subscribe(success => {
    //   this.onGettingUserList();
    // }, error => {
    //   console.log('onUserArchiving > onDisplayingSnackBar', error);
    // });

    // The below commented code was done newly and the user list used to refresh irespective of snackbar closed.
    this._snackbarSvc.showSuccessSnackBar(successMessage, 'success-snackbar-bottom2');
    // this.onDisplayingSnackBar(successMessage);
    this.onGettingUserList(this.ownerId);
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
  public onDisplayingErrorSnackBar(message: string, action?: string) {
    this._snackBar.open(message, action, {
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }

  /**
* onNavigatingToAddUser called to navigate to the Add User screen
*/
  public onNavigatingToAddUser(): void {
    this._router.navigate(['add-user']);
  }

  public accordionOpen() {
   window.scrollTo(0, 0);
  }
}
