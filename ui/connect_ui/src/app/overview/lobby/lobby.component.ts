import { Component, OnInit, OnDestroy } from '@angular/core';
import { HeaderInfo } from 'src/app/shared/header.model';
import { getWidthDetails, getRoleBasedIcons } from 'src/app/shared/utils/overview.util';
import { NotificationService } from 'src/app/notification/notification.service';
import { AppNotificationService } from 'src/app/shared/appnotification.service';
import { NOTFTOPICSCONST } from '../../shared/utils/notification.util';
import { NotificationtoasterService } from 'src/app/shared/notificationtoaster.service';
import { PermissionService } from 'src/app/shared/permission.service';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit, OnDestroy {
  notification: any;
  ordersNTCount = 0;
  workflowNTCount = 0;
  connectionsNTCount = 0;
  administrationNTCount = 0;

  constructor(
    private _NS: NotificationService,
    private _appNt: AppNotificationService,
    private _NTS: NotificationtoasterService,
    private _PS: PermissionService
    ) { }

  circles;
  getWidth = 75;
  headerInfo: HeaderInfo = {
    headerName: 'Workflow manager',
    isLoginPage: false
  };

  ngOnInit() {
    this.getUserRole();
    this.getNotificationCount();
  }

  ngOnDestroy() {
    if (this.notification !== undefined) {
      this.notification.unsubscribe();
    }
  }

  getUserRole() {
    this._PS.checkPermissionObs('dummy').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      const rolesPermission = this._PS.getPermissions();
      let userPermissions;
      if (rolesPermission) {
        userPermissions = rolesPermission.split(',');
        this.circles = this.getRoleIcons(userPermissions);
        const { length: lenCircles } = this.circles;
        this.getWidth = getWidthDetails(lenCircles);
      }
    });
  }

  getRoleIcons(rolesPermission) {
    return getRoleBasedIcons(rolesPermission);
  }

  getNotificationCount() {
    if (!this.headerInfo.isLoginPage) {
      this.notification = this._NS
        .getNotifications()
        .subscribe(
          resp => {
            if (resp) {
              this._appNt.setNOTF(resp);
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
              console.log(this.circles, 'this.circles ========');
              if (this.circles) {
                this.circles.forEach(element => {
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
                console.log(this.circles, 'this.circles ========');
              }
            }
          },
          error => {
            console.log(error);
          }
        );
    }
  }


  navigateUrl(event) {
    if (event.navigateUrl) {
      window.location.href = event.navigateUrl;
    }
  }
}
