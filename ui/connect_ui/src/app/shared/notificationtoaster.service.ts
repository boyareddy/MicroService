import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { NotificationtoasterComponent } from './notificationtoaster/notificationtoaster.component';
import { SharedService } from './shared.service';
import { CommonApiService } from './common-api.service';
import { TosterClasses } from '../standard-names/constants';
import { NOTFSEVERITY, NTROUTERURLS } from './utils/notification.util';
import { Router } from '@angular/router';
import { multipleNotfMsg } from '../standard-names/constants';
import { webSocket } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root'
})
export class NotificationtoasterService {

  NTMSGLIST = [];
  data;
  isToasterEnabled = false;
  roleIDs;
  roleIfos;
  isMultiToastEnable = false;
  stompClient;

  constructor(
    private _snackBar: MatSnackBar,
    private _SS: SharedService,
    private _commonapiservice: CommonApiService,
    private _router: Router
  ) { }

  public loadNotifToster() {
    setTimeout(() => {
      console.log(this.isToasterNeeded(), 'this.isToasterNeeded()');
      if (this.isToasterNeeded()) {
        const apiProp = this._SS.getData('appProperties');
        const cookieToken = this._SS.getCookieToken();
        let proto = 'ws';
        if (apiProp.protocol === 'https') {
          proto = 'wss';
        }
        // tslint:disable-next-line:max-line-length
        const URL = `${proto}://${apiProp.host}:${apiProp.admmApi.port}/${apiProp.admmApi.module}/notification?token=` + cookieToken;
        const subject = webSocket({
          url: URL,
        });
        console.log(subject, 'subject===============================');
        subject.subscribe(
          (msg: any) => {
            if (msg !== undefined && msg !== null  && msg !== '') {
              const notMsg = JSON.parse(msg);
              if (Object.keys(notMsg).length > 0) {
                this.showNFToaster(notMsg);
              }
            }
          }, // Called whenever there is a message from the server.
          (err: any) => {
            console.log(err);
          }, // Called if at any point WebSocket API signals some kind of error.
          () => console.log('complete') // Called when connection is closed (for whatever reason).
        );
        // const ws = new SockJS(URL);
        // this.stompClient = Stomp.over(ws);
        // const _thisRef = this;
        // this.stompClient.connect({ withCredentials: true }, function(frame) {
        //   _thisRef.stompClient.subscribe('/toaster', (message) => {
        //     console.log(message);
        //     if (message && message.body) {
        //       _thisRef.showNFToaster(JSON.parse(message.body));
        //     }
        //   });
        // });
        // const res = {
        //   "id": 12,
        //   "createdBy": null,
        //   "createdDateTime": "2018-07-29 07:05:18:325 UTC",
        //   "title": "Testing Unregistered LP24 device1 Testing Unregistered LP24 device1 Testing Unregistered LP24 device1",
        //   "msg": "The LP24 device is not registered and is sending messages to Connect. Please register the device to proceed further.",
        //   "userId": null,
        //   "viewed": "N",
        //   "viewedDateTime": null,
        //   "severity": "Error",
        //   "topic": "Orders",
        //   "roleInfo":['Admin']
        // };
        // this.showNFToaster(res);
      }
    }, 800);
  }

  disconnectToaster() {
    this._snackBar.dismiss();
    // if (this.stompClient && this.stompClient.status !== 'CONNECTED') {
    //   console.log(this.stompClient.status, 'this.stompClient.status');
    // }
  }

  public showNotfToster(notfObj: any): void {
    this.data = notfObj;
    const appProperties = this._SS.getData('appProperties');
    let toasterDuration = 5000;
    if (appProperties && appProperties.toasterDuration) {
      toasterDuration = appProperties.toasterDuration;
    }
    console.log(toasterDuration, 'toasterDuration');
    this.isToasterEnabled = true;
    this._snackBar.openFromComponent(NotificationtoasterComponent, {
      data: notfObj,
      duration: toasterDuration,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [this.getRespTosterClassName(notfObj)]
    }).afterDismissed().subscribe(info => {
      console.log(info, 'info @@@@@@@@@@@@@@');
      this.isToasterEnabled = false;
      console.log(this.data, 'this.data');
      const isToasterClosed = this._SS.getData('isToasterClosed');
      this._SS.setData('isToasterClosed', false);
      if (this.NTMSGLIST && this.data && !this.isMultiToastEnable && !isToasterClosed) {
        this.NTMSGLIST = this.NTMSGLIST.filter((elm) => elm.id !== this.data.id);
        console.log(this.NTMSGLIST, 'NTMSGLIST after removed');
        if (this.NTMSGLIST.length > 0) {
          this.getNFToaster(this.NTMSGLIST);
        }
      } else {
        this.isMultiToastEnable = false;
        this.NTMSGLIST = [];
      }
    });
  }

  showNFToaster(res) {
    console.log(res, 'res ==============');

    if (res && res.severity !== NOTFSEVERITY.INFO && this.isToasterNeeded()) {
      if (this.NTMSGLIST.length > 0) {
        console.log(this.NTMSGLIST, 'NTMSGLIST before concat');
        this.NTMSGLIST.push(res);
        // NTMSGLIST.concat(res);
        console.log(this.NTMSGLIST, 'NTMSGLIST after concat');
        if (this.NTMSGLIST.length > 0) {
          if (!this.isToasterEnabled) {
            this.getNFToaster(this.NTMSGLIST);
          }
        }
      } else {
        this.NTMSGLIST.push(res);
        if (!this.isToasterEnabled) {
          this.getNFToaster(this.NTMSGLIST);
        }
      }
    }
  }

  getNFToaster(res) {
    if (this.isToasterNeeded()) {
      if (res.length > 3) {
        this.isMultiToastEnable = true;
        const mulMsg = {
          'multmsg': multipleNotfMsg,
          'severity': this.getHighestSeverity(res)
        };
        this.showNotfToster(mulMsg);
      } else {
        this.showNotfToster(res[0]);
      }
    }
  }

  canShowNF(NFObj) {
    if (NFObj.roleIds) {
      const resRoleIds = NFObj.roleIds.map(String);
      const roleIds = resRoleIds.filter(item => this.roleIDs.indexOf(item) !== -1);
      if (roleIds && roleIds.length > 0) {
        return true;
      } else {
        return false;
      }
    } else if (NFObj.roleInfo) {
      const resRoleInfos = NFObj.roleInfo.map(String);
      const resRoleInfoArr = resRoleInfos.filter(item => this.roleIfos.indexOf(item) !== -1);
      if (resRoleInfoArr && resRoleInfoArr.length > 0) {
        return true;
      } else {
        return false;
      }
    }
  }

  getRespTosterClassName(notfObj: any) {
    let btm = 'Bottom1';
    const findElm = document.getElementsByTagName('app-header');
    const findElm1 = document.getElementsByTagName('mat-tab-group');
    if (findElm1 && findElm1.length > 0) {
      btm = 'Bottom2';
    }
    return TosterClasses[notfObj.severity + '' + btm];
  }

  public getNewNotification() {
    return this._commonapiservice.get('getNewNotification', undefined, true);
  }

  public getHighestSeverity(notfArr) {
    let highestSev = NOTFSEVERITY.WARNING;
    if (notfArr) {
      notfArr.findIndex(elm => {
        if (elm.severity === NOTFSEVERITY.ERROR) {
          highestSev = NOTFSEVERITY.ERROR;
        }
      });
      return highestSev;
    }
  }

  public isToasterNeeded() {
    let counter = 0;
    for (counter = 0; counter < NTROUTERURLS.length; counter++) {
      console.log(this._router.url, 'isToasterNeeded this._router.url');
      console.log(NTROUTERURLS[counter], 'isToasterNeeded NTROUTERURLS[counter]');
      console.log(this._router.url.match(new RegExp(NTROUTERURLS[counter], 'g')), 'isToasterNeeded');
      if (this._router.url.match(new RegExp(NTROUTERURLS[counter], 'g')) !== null) {
        console.log(this._router.url.match(new RegExp(NTROUTERURLS[counter], 'g')));
          return true;
      }
    }
    return false;
  }

  getCountNadSeverity() {

  }

}
