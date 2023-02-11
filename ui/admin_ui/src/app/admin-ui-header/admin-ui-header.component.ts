import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { UI_URI } from '../utils/api-constants';
import { AppTitleService } from '../shared/app-title.service';
import { SharedService } from '../services/shared.service';
import { HeaderInfo } from '../models/header.model';
import { ConfirmDialogService } from '../services/confirm-dialog.service';
import { AdminApiService } from '../services/admin-api.service';
import { AppNotificationService } from '../services/appnotification.service';
import { getRoleBasedIcons } from '../utils/overview.util';
import { NotificationtoasterService } from '../services/notificationtoaster.service';
import { NOTFTOPICSCONST } from '../utils/notification.util';
import { PermissionService } from '../services/permission.service';

@Component({
  selector: 'app-admin-ui-header',
  templateUrl: './admin-ui-header.component.html',
  styleUrls: ['./admin-ui-header.component.css']
})
export class AdminUiHeaderComponent implements OnInit, OnDestroy {

  // Admin UI header configuration
  @Input() headerInfo: HeaderInfo = {
    headerName: 'Create order',
    isBackRequired: true
  };
  @Input() isShadowRequired = false;
  allnotificationCount: any;
  notification: any;
  viewedNotificationCount: any;
  userRole: string;
  userName: string;
  gridIcons: any;
  ordersNTCount = 0;
  workflowNTCount = 0;
  connectionsNTCount = 0;
  administrationNTCount = 0;

  constructor(
    private _router: Router,
    private _loc: Location,
    private _appTitle: AppTitleService,
    private _sharedSV: SharedService,
    private _confDialogSvc: ConfirmDialogService,
    private _adminapi: AdminApiService,
    private _appNt: AppNotificationService,
    private _NTS: NotificationtoasterService,
    private _PS: PermissionService
    ) {
    this._appTitle.getTitle();
  }

  ngOnInit() {
    this.ordersNTCount = 0;
    this.workflowNTCount = 0;
    this.connectionsNTCount = 0;
    this.administrationNTCount = 0;
    this.viewedNotificationCount = null;
    this.getUserInfo();
    this.getGridIconsInfo();
  }

  ngOnDestroy() {
    this.ordersNTCount = 0;
    this.workflowNTCount = 0;
    this.connectionsNTCount = 0;
    this.administrationNTCount = 0;
    this.viewedNotificationCount = null;
    if (this.notification !== undefined) {
      this.notification.unsubscribe();
    }
}

  /**
  * onNavigatingToAddUser called to navigate to the Add User screen
  */
  public onNavigatingToAddUser(): void {
    this._router.navigate(['add-user']);
  }

  // public onNavigate(url: string): void {
  //   const appProperties = this._sharedSV.getSharedData('appProperties');
  //   window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/${url}`;
  // }

  public goBack(): void {
    if (this.headerInfo.isAnyFormInvalid) {
      this._confDialogSvc.openConfirmDialog()
      .afterClosed()
      .subscribe(confirmStatus => {
        // tslint:disable-next-line:no-unused-expression
        confirmStatus ? this._router.navigate(this.headerInfo.backUrl) : 'Not to do anything';
      });
    } else {
      // this._router.navigate(this.headerInfo.backUrl);
      this._loc.back();
    }
  }

  onChangePassword(event: Event) {
    event.preventDefault();
    const appProperties = this._sharedSV.getSharedData('appProperties');
    sessionStorage.setItem('changePwdBackURL', window.location.href);
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/admin_ui/#/change-password`;
  }

  navigateWorkFlow(assayDetails) {
    window.location.reload();
    const appProperties = this._sharedSV.getSharedData('appProperties');
    // tslint:disable-next-line:max-line-length
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/connect_ui/#/workflow?assayType=${assayDetails}`;
  }

  getNotificationCount() {
    if (!this.headerInfo.isLoginPage) {
      this.notification = this._adminapi.getNotifications().subscribe(resp => {
        this._appNt.setNOTF(resp);
        this.viewedNotificationCount = this._appNt.getNotfByRouter()
          ? this._appNt.getNotfByRouter()
          : [];
        const ordersNT = this._appNt.getNotf(NOTFTOPICSCONST.ORDERS);
        const workflowNT = this._appNt.getNotf(NOTFTOPICSCONST.WORKFLOW);
        const connectionsNT = this._appNt.getNotf(NOTFTOPICSCONST.CONNECTIONS);
        const administrationNT = this._appNt.getNotf(NOTFTOPICSCONST.ADMINISTRATION);
        const ordersNTS = this._NTS.getHighestSeverity(ordersNT);
        const workflowNTS = this._NTS.getHighestSeverity(workflowNT);
        const connectionsNTS = this._NTS.getHighestSeverity(connectionsNT);
        const administrationNTS = this._NTS.getHighestSeverity(administrationNT);
        this.ordersNTCount = ordersNT ? ordersNT.length : 0;
        this.workflowNTCount = workflowNT ? workflowNT.length : 0;
        this.connectionsNTCount = connectionsNT ? connectionsNT.length : 0;
        this.administrationNTCount = administrationNT ? administrationNT.length : 0;
        console.log(this.gridIcons, 'this.gridIcons ========');
        this.gridIcons.forEach(element => {
          if (element.key === 'view_order_on_dashboard') {
            Object.assign(element, {count: this.ordersNTCount}, {severity: ordersNTS});
          } else if (element.key === 'view_workflow_on_dashboard') {
            Object.assign(element, {count: this.workflowNTCount}, {severity: workflowNTS});
          } else if (element.key === 'view_connections_on_dashboard') {
            Object.assign(element, {count: this.connectionsNTCount}, {severity: connectionsNTS});
          } else if (element.key === 'view_settings_on_dashboard') {
            Object.assign(element, {count: this.administrationNTCount}, {severity: administrationNTS});
          }
          console.log(element, 'element $$$$$$4');
        });
        console.log(this.gridIcons, 'this.circles ========');
      }, error => {
        console.log(error);
      });
    }
  }

  getNTColor() {
    if (this.viewedNotificationCount) {
      return `badge badge${this._NTS.getHighestSeverity(
        this.viewedNotificationCount
      )}`;
    }
  }

  goToNotification() {
    sessionStorage.setItem('NF_ROUTER', this._router.url);
    window.location.href = '/connect_ui/#/notification';
  }

  onGlobalSearch() {
    sessionStorage.setItem('globalSearchBack', window.location.href);
    this._router.navigate(['search']);
  }

  onNavigateHome() {
    if (!this.headerInfo.isLoginPage) {
      window.location.href = '/connect_ui/#/overview';
    } else {
      if (this.headerInfo.isHomeNavigateRequired) {
        window.location.href = '/connect_ui/#/overview';
      }
    }
  }

  public onNavigate(item: any): void {
    if (item) {
      if (item.navigateUrl) {
        // console.log(item.navigateUrl);
        window.location.href = item.navigateUrl;
      } else {
        // console.log('home');
        window.location.href = '/connect_ui/#/overview';
      }
    } else {
      // console.log('home');
      window.location.href = '/connect_ui/#/overview';
    }
  }

  getUserInfo() {
    const userDetails = sessionStorage.getItem('usersInfo');
    const name = sessionStorage.getItem('userName');
    if (userDetails && name) {
      const userData = JSON.parse(userDetails);
      this.userRole = userData.roleInfo;
      this.userName = name;
    }
  }

  getGridIconsInfo(): any {
    if (!this.headerInfo.isLoginPage) {
      this._adminapi.getPermissionsList().subscribe((res1: any) => {
        if (res1) {
          const rolesPermission = res1;
          let userPermissions;
          if (rolesPermission) {
            userPermissions = rolesPermission.toString().split(',');
            this.gridIcons = getRoleBasedIcons(userPermissions);
            this.getNotificationCount();
          }
        }
      }, error1 => {
          console.log(error1, 'checkPermission error');
      });
    }
  }
}

