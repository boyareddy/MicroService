import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { DmService } from '../services/dm.service';
import { formatDeviceList } from '../utils/device-icon.util';

@Component({
  selector: 'app-add-device',
  templateUrl: './add-device.component.html',
  styleUrls: ['./add-device.component.scss']
})
export class AddDeviceComponent implements OnInit {

  unRegDeviceList: any[];
  constructor(public dialogRef: MatDialogRef<AddDeviceComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, private _dmSvc: DmService) { }

  public ngOnInit(): void {
    this._dmSvc.getDeviceList(true).subscribe(allDevices => {
      this.unRegDeviceList = formatDeviceList(allDevices, false, true);
    }, error => {

    });
  }

  public closePopUp(): void {
    this.dialogRef.close(null);
  }

  public addUnRegDeviceDevice(unRegDevice: any): void {
    this.dialogRef.close(unRegDevice);
  }
}
