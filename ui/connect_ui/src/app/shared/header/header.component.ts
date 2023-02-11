import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { HeaderInfo } from '../header.model';
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';
import { SharedService } from '../shared.service';
import { AppTitleService } from '../app-title.service';
import { NotificationService } from '../../notification/notification.service';
import { AppNotificationService } from '../appnotification.service';
import { NotificationtoasterService } from '../notificationtoaster.service';
import { getRoleBasedIcons } from '../utils/overview.util';
import { NOTFTOPICSCONST } from '../utils/notification.util';
import { PermissionService } from '../permission.service';
import { AuthService } from 'src/app/auth/AuthService/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  @Input() headerInfo: HeaderInfo;
  notification: any;
  allnotificationCount: any;
  viewedNotificationCount: any;
  userRole: string;
  userName: string;
  gridIcons: any;
  ordersNTCount = 0;
  workflowNTCount = 0;
  connectionsNTCount = 0;
  administrationNTCount = 0;
  isNTPage = false;

  constructor(
    private _location: Location,
    private _dialogBox: MatDialog,
    private _router: Router,
    private _sharedService: SharedService,
    private _appTitle: AppTitleService,
    private _notificationservice: NotificationService,
    private _appNt: AppNotificationService,
    private _NTS: NotificationtoasterService,
    private _PS: PermissionService,
    private _AS: AuthService
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
    this.checkForNTPage();
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

  checkForNTPage() {
    console.log(this._router.url, 'this._router.url');
    if (this._router.url === '/notification') {
      this.isNTPage = true;
    }
  }

  getGridIconsInfo(): any {
    if (!this.headerInfo.isLoginPage) {
      this._AS.getPermissions().subscribe((res1: any) => {
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

  getUserInfo() {
    const userDetails = sessionStorage.getItem('usersInfo');
    const name = sessionStorage.getItem('userName');
    if (userDetails && name) {
      const userData = JSON.parse(userDetails);
      this.userRole = userData.roleInfo;
      this.userName = name;
    }
  }

  goBack() {
    if (this.headerInfo.navigateUrl) {
      if (!this.headerInfo.queryParams) {
        this._router.navigate([this.headerInfo.navigateUrl]);
      } else {
        this._router.navigate([this.headerInfo.navigateUrl], {
          queryParams: this.headerInfo.queryParams
        });
      }
      if (this.headerInfo.removeSharedData) {
        this._sharedService.deleteData(this.headerInfo.removeSharedData);
      }
    } else {
      this._location.back();
    }
  }
  goBackWithConfirm() {
    console.log('confirm box will open');
    this._dialogBox.open(ConfirmDialogComponent, {
      width: '600px',
      height: '160px',
      data: this.headerInfo.message
    });
  }

  onChangePassword(event: Event) {
    event.preventDefault();
    const appProperties = this._sharedService.getData('appProperties');
    sessionStorage.setItem('changePwdBackURL', window.location.href);
    window.location.href = `${appProperties.protocol}://${appProperties.host}:${
      appProperties.appPort
    }/admin_ui/#/change-password`;
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

  getNotificationCount() {
    if (!this.headerInfo.isLoginPage) {
      this.notification = this._notificationservice
        .getNotifications()
        .subscribe(
          resp => {
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
          },
          error => {
            console.log(error);
          }
        );
    }
  }

  getNTColor() {
    if (this.viewedNotificationCount) {
      return `badge badge${this._NTS.getHighestSeverity(
        this.viewedNotificationCount
      )}`;
    }
  }

  navigateWorkFlow(assayDetails) {
    // tslint:disable-next-line:max-line-length
    window.location.href = `${
      this._sharedService.getData('appProperties').protocol
    }://${this._sharedService.getData('appProperties').host}:${
      this._sharedService.getData('appProperties').appPort
    }/connect_ui/#/workflow?assayType=${assayDetails}`;
  }

  goToNotification() {
    sessionStorage.setItem('NF_ROUTER', this._router.url);
    this._router.navigate(['notification']);
  }

  onNavigateHome() {
    if (!this.headerInfo.isLoginPage) {
      window.location.href = '/connect_ui/#/overview';
    }
  }

  onGlobalSearch() {
    sessionStorage.setItem('globalSearchBack', window.location.href);
    this._router.navigate(['search']);
  }
}
