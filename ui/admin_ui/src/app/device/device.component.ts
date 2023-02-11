import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';

@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.scss']
})
export class DeviceComponent implements OnInit {

  @Input() deviceInfo: any;

  statusBasedCss: string;
  statusIcon: string;
  deviceStatus: string;

  constructor(private _router: Router, private _sbSvc: SnackBarService) { }

  public ngOnInit(): void {
    this.setCss();
    this.setIcon(this.deviceInfo.state === 'INACTIVE' ? 'Offline' : this.deviceInfo.status);

    if(this.deviceInfo.status && this.deviceInfo.equipmentStatus){
      this.deviceStatus = `${this.deviceInfo.status}/${this.deviceInfo.equipmentStatus}`;
    }else{
      this.deviceStatus = this.deviceInfo.status;
    }
  }

  public setCss() {
    // tslint:disable-next-line:max-line-length
    if(this.deviceInfo && this.deviceInfo.state === 'INACTIVE'){ this.statusBasedCss = 'offline' }else{ this.statusBasedCss = this.deviceInfo && this.deviceInfo.status && this.deviceInfo.status ? this.deviceInfo.status.toLowerCase().replace(' ', '_').replace('/', '_') : null };
  }

  public setIcon(status) {
    switch (status ? status.split('/')[0] : null) {
      case ('Online'):
        this.statusIcon = 'online';
        break;
      case ('Warning'):
        this.statusIcon = 'warning';
        break;
      case ('Error'):
        this.statusIcon = 'error';
        break;
      case ('Offline'):
        this.statusIcon = 'offline';
        break;
      case ('Maintenance'):
        this.statusIcon = 'offline';
        break;
      default:
        this.statusIcon = null;
        break;
    }
  }

  public navigateToDeviceDetail(deviceInfo): void {
    if (deviceInfo.state && deviceInfo.state === 'INACTIVE') {
      sessionStorage.setItem('selectedDevice', 'Archive tab');
    }
    // tslint:disable-next-line:max-line-length
    deviceInfo.serialNo ? this._router.navigate(['device-detail', deviceInfo.serialNo]) : this._sbSvc.showErrorSnackBar('The device does not have serial number',  SnackbarClasses.errorBottom2);
  }

}
