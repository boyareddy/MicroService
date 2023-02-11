import { Component, OnInit, AfterViewChecked } from '@angular/core';
import { DmService } from '../services/dm.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder } from '@angular/forms';
import { setIcon, setCss, validDevice, deviceCompleteStatus } from '../utils/device-icon.util';
import { SharedService } from '../services/shared.service';
import { HeaderInfo } from '../models/header.model';
import { SnackBarService } from '../services/snack-bar.service';
import { vldAlphaNum } from '../utils/validation.util';
import { errorFields } from '../utils/validation-error-fields.const';
import { SnackbarClasses } from '../utils/query-strings';

@Component({
  selector: 'app-device-detail',
  templateUrl: './device-detail.component.html',
  styleUrls: ['./device-detail.component.scss']
})
export class DeviceDetailComponent implements OnInit, AfterViewChecked {

  deviceDetailForm: FormGroup;
  toggleEdit = false;
  device: any;
  deviceSerialNo: string;
  deviceIcon: string;
  deviceBaseCss: string;
  headerInfo: HeaderInfo = {
    headerName: 'Connections',
    isBackRequired: true,
    backUrl: ['device-list']
  };

  constructor(private _dmSvc: DmService,
    private _route: ActivatedRoute,
    public _fb: FormBuilder,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _sbSvc: SnackBarService) { }

  ngOnInit() {
    //  To get the device detail.
    this._route.params.subscribe(params => {

      //  Initialize the device detail form.
      this.initDeviceDetailForm();

      //  Set the device serial number from query parameter.
      this.deviceSerialNo = params['serialNo'];

      // Set header currPage attribute
      this.headerInfo.currPage = this.deviceSerialNo;

      //  Load the device detail based on device serial number passed in query parameter.
      this.loadDeviceDetail(this.deviceSerialNo);
    });
  }

  public loadDeviceDetail(serialNo: string): void {
    let deviceStatus: string;

    this._dmSvc.getDeviceDetail(serialNo).subscribe(device => {
      this.device = device[0];

      if (this.device) {
        // tslint:disable-next-line:max-line-length
        deviceStatus = this.device && this.device.attributes && this.device.attributes.deviceStatus ? this.device.attributes.deviceStatus : null;

        //  Set the device icon.
        this.deviceIcon = setIcon(deviceStatus);

        //  Set the device base CSS name.
        this.deviceBaseCss = setCss(deviceStatus);
      } else {
        this._sbSvc.showErrorSnackBar('No device found !', SnackbarClasses.errorBottom1);
      }
    }, error => {

    });
  }

  public initDeviceDetailForm(): void {
    this.deviceDetailForm = this._fb.group({
      name: []
      //  For future requirement.
      // name: [null, vldAlphaNum(errorFields.dev_name_alpha_num)]
    });
  }

  public updateDeviceDetailForm(): void {
    this.deviceDetailForm.setValue({ name: this.device.name });
  }

  public toggleEditting(): void {
    //  this.toggleEdit = !this.toggleEdit;
    //  this.toggleEdit ? this.updateDeviceDetailForm() : 'Not to do anything';
    this._router.navigate(['edit-device', this.deviceSerialNo]);
  }

  public updateDeviceDetail(): void {
    this._dmSvc.postDeviceDetail(this.device.deviceId, this.deviceDetailForm.value).subscribe(updatedDevice => {
      console.log('updateDeviceDetail', updatedDevice);
      this.toggleEdit = false;
      this.device = updatedDevice;

      //  Set deviceDetailForm as pristine.
      this.deviceDetailForm.markAsPristine();
    }, error => {

    });
  }

  public removeDevice(): void {
    this._dmSvc.deleteDevice(this.device.deviceId).subscribe(deletedDevice => {
      this._sharedSvc.setSharedData('DEVICE_DELETED', true);
      this._router.navigate(['device-list']);
    }, error => {

    });
  }

  public ngAfterViewChecked(): void {
    this.headerInfo.isAnyFormInvalid = this.deviceDetailForm.dirty;
  }

  public resetDeviceName(): void {
    this.deviceDetailForm.controls.name.reset();
    this.deviceDetailForm.markAsDirty();
  }

  get deviceCompleteStatus(){
    if(this.device)
      return deviceCompleteStatus(this.device);
    else
      return null;
  }
}
