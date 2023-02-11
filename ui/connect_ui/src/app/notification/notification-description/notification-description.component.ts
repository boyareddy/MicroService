import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { NOTFTOPICSCONST, HTPTRIGGEREDNF } from '../../shared/utils/notification.util';
import { SharedService } from 'src/app/shared/shared.service';

@Component({
  selector: 'app-notification-description',
  templateUrl: './notification-description.component.html',
  styleUrls: ['./notification-description.component.scss']
})
export class NotificationDescriptionComponent implements OnInit, OnChanges {

  // tslint:disable-next-line:no-input-rename
  @Input('notificationRes') notificationRes;
  @Input('selectedTab') selectedTab = 0;
  HTPTRIGGEREDNF = HTPTRIGGEREDNF;

  constructor(private _SS: SharedService) { }

  ngOnInit() {
  }

  ngOnChanges() {
    if (this.notificationRes && this.notificationRes.topic === NOTFTOPICSCONST.CONNECTIONS) {
      const conMSGArr = this.notificationRes.msg.split(';');
      let message = this.notificationRes.msg;
      conMSGArr.forEach(elm => {
        const conecMSGArr = elm.split(':');
        if (conecMSGArr.length > 0 && conecMSGArr[0] === 'DeviceType') {
          this.notificationRes['deviceType'] = conecMSGArr[1];
        } else if (conecMSGArr.length > 0 && conecMSGArr[0] === 'DeviceName') {
          this.notificationRes['deviceName'] = conecMSGArr[1];
        } else if (conecMSGArr.length > 0 && conecMSGArr[0] === 'DeviceSerialNumber') {
          this.notificationRes['deviceSerialNumber'] = conecMSGArr[1];
        }  else if (conecMSGArr.length > 0 && conecMSGArr[0] === 'DescriptionCode') {
          this.notificationRes['descriptionCode'] = conecMSGArr[1];
        } else if (conecMSGArr.length > 0 && conecMSGArr[0] === 'Message') {
          message = conecMSGArr[1];
        }
      });
      this.notificationRes.msg = message;
    }
  }

  gotoDeviceDetails(deviceName: string) {
    if (deviceName) {
      const appProperties = this._SS.getData('appProperties');
      // tslint:disable-next-line:max-line-length
      const URL = `${appProperties.protocol}://${appProperties.host}:${appProperties.appPort}/admin_ui/#/device-detail/${deviceName}`;
      window.open(URL, '_blank');
    }
  }

}
