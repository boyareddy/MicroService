import { Component, OnInit, OnDestroy, ElementRef, AfterViewInit } from '@angular/core';
import { DmService } from '../services/dm.service';
import { MatDialog, MatIconRegistry } from '@angular/material';
import { AddUserComponent } from '../add-user/add-user.component';
import { AddDeviceComponent } from '../add-device/add-device.component';
import { SnackBarService } from '../services/snack-bar.service';
import { SharedService } from '../services/shared.service';
import { formatDeviceList } from '../utils/device-icon.util';
import { DeviceAttribute } from '../models/device.model';
import { Router } from '@angular/router';
import { XhrProgressLoaderService } from '../services/xhr-progress-loader.service';
import { SnackbarClasses } from '../utils/query-strings';
import { PermissionService } from '../services/permission.service';
import { DomSanitizer } from '@angular/platform-browser';
import { sort } from '../utils/array-util';
import { DEVICE_TYPES } from '../utils/device-type.config';

//  tslint:disable-next-line:interface-over-type-literal
export type Device = {
  deviceName: string,
  deviceType: string,
  status: string,
  serialNo: string,
  deviceId: string,
  equipmentStatus?: string,
  state?: string;
};

// tslint:disable-next-line:interface-over-type-literal
export type DeviceList = {
  active: any[],
  acrhived: any[]
};

@Component({
  selector: 'app-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.scss']
})
export class DeviceListComponent implements OnInit, OnDestroy, AfterViewInit {

  headerInfo: any = {
    currPage: 'Connections',
    headerIcon: 'assets/Images/header/module/Connections.svg'
  };
  public viewDevices;
  deviceList: DeviceList = {} as DeviceList;
  deviceListRes;
  public isSpinnerOn = true;
  public rectPro;
  havingAccess = false;
  deviceTypeList: any[];
  deviceTypeDisabledList: any[];
  previousPage;
  selectedIndex = 0;

  constructor(private _dmSvc: DmService,
              private _dialog: MatDialog,
              private _snackBarSvc: SnackBarService,
              private _sharedSvc: SharedService,
              private _router: Router,
              private elem: ElementRef,
              private _permission: PermissionService,
              private _matIconReg: MatIconRegistry,
              private _domSanitizer: DomSanitizer,
              ) {
    // To register the image for the icon.
    this._matIconReg
      .addSvgIcon('drop_down', 
      this._domSanitizer.bypassSecurityTrustResourceUrl('assets/Images/arrow-down.svg'));

    //  To display device delete message if any device addded, updated or deleted.
    this.showDeviceActionStatus();

  }

  ngOnInit() {
    this._permission.checkPermissionObs('Create_Device').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });

    this.previousPage = sessionStorage.getItem('selectedDevice');
    if (this.previousPage && this.previousPage === 'Archive tab') {
      this.selectedIndex = 1;
      sessionStorage.removeItem('selectedDevice');
    }

    //  To load the list of devices.
    this.loadDeviceList();
    this.loadDeviceTypes();
  }

  ngOnDestroy() {
    if (this.viewDevices !== undefined) {
      this.viewDevices.unsubscribe();
    }
    this.isSpinnerOn = false;
  }

  ngAfterViewInit() {
    setTimeout( () => {
      if (this.isSpinnerOn) {
        const rect = this.elem.nativeElement.querySelectorAll('.device-container')[0].getBoundingClientRect();
        // this.rectPro = rect;
      }
    }, 600);
  }

  public loadDeviceTypes(): void {
    this._dmSvc.getDeviceTypes().subscribe(deviceTypes => {
      this.deviceTypeList = sort(deviceTypes, 'name');
      console.log(this.deviceTypeList);
      this.disableAddDevice(this.deviceListRes);
    }, error => {
      console.log('Error occured');
    });
  }

  public loadDeviceList(): void {
    let activeDevices;
    let archivedDevices;
    this.isSpinnerOn = true;
    this.viewDevices = this._dmSvc.getDeviceList(true).subscribe(deviceList => {
      deviceList.sort((a, b) => (a.modifiedDate) > (b.modifiedDate) ? -1 : (a.modifiedDate) < (b.modifiedDate) ? 1 : 0);
      this.deviceListRes = deviceList;
      this.disableAddDevice(deviceList);
      activeDevices = deviceList.filter(device => device.state && device.state.toLowerCase() !== 'inactive' );
      archivedDevices = deviceList.filter(device => device.state && device.state.toLowerCase() === 'inactive' );
      this.deviceList.active = formatDeviceList(activeDevices, true, true);
      this.deviceList.acrhived = formatDeviceList(archivedDevices, true, false);
      this.isSpinnerOn = false;
    }, error => {
      this.isSpinnerOn = false;
      console.log('deviceList', error);
    }, () => {
      this.isSpinnerOn = false;
    });
  }

  public disableAddDevice(deviceListArr) {
    if (deviceListArr && deviceListArr.length > 0 && this.deviceTypeList && this.deviceTypeList.length > 0) {
      this.deviceTypeDisabledList = this.deviceTypeList;
      // tslint:disable-next-line:max-line-length
      const ttv2Analysis = deviceListArr.filter(device => device.deviceType && device.deviceType.name === DEVICE_TYPES.TTV2ANALYSIS && device.state !== 'INACTIVE' );
      if (ttv2Analysis && ttv2Analysis.length > 0) {
        const ttv2Index = this.deviceTypeDisabledList.findIndex(device => device.name === DEVICE_TYPES.TTV2ANALYSIS);
        if (ttv2Index !== null && ttv2Index !== undefined) {
          this.deviceTypeDisabledList[ttv2Index]['disabled'] = true;
        }
      }
    }
  }

  public showDeviceActionStatus(): void {
    if (this._sharedSvc.getSharedData('DEVICE_ADDED')) {
      this._snackBarSvc.showSuccessSnackBarWithLocalize('deviceMgmt.deviceAddSuccess', SnackbarClasses.successBottom2);
      this._sharedSvc.deleteSharedData('DEVICE_ADDED');
    } else if (this._sharedSvc.getSharedData('DEVICE_DELETED')) {
      this._snackBarSvc.showSuccessSnackBarWithLocalizeAndReplace('deviceMgmt.deviceDeletedSuccess', '$$$$', this._sharedSvc.getSharedData('DEVICE_DELETED')['device'],
      SnackbarClasses.successBottom2);
      this._sharedSvc.deleteSharedData('DEVICE_DELETED');
    } if (this._sharedSvc.getSharedData('DEVICE_UPDATED')) {
      this._snackBarSvc.showSuccessSnackBar('Your device has been updated successfully',  SnackbarClasses.successBottom2);
      this._sharedSvc.deleteSharedData('DEVICE_UPDATED');
    }if (this._sharedSvc.getSharedData('DEVICE_ACTIVATED')) {
      // tslint:disable-next-line:max-line-length
      this._snackBarSvc.showSuccessSnackBarWithLocalizeAndReplace('deviceMgmt.deviceUnarchivedSuccess', '$$$$', this._sharedSvc.getSharedData('DEVICE_ACTIVATED')['device'], SnackbarClasses.successBottom2);
      this._sharedSvc.deleteSharedData('DEVICE_ACTIVATED');
    }
  }

  public loadUnregisteredDevices(deviceType): void {
    // this.openAddDevicePopUp(null);
    this._sharedSvc.setSharedData('selectedDeviceType', deviceType);
    this._router.navigate(['reg-device']);
  }

  public openAddDevicePopUp(data: any): void {
    this._dialog.open(AddDeviceComponent, {
      width: '688px',
      height: '286px',
      data: data
    }).afterClosed().subscribe(evt => {
      // tslint:disable-next-line:no-unused-expression
      evt ? this.registerDevice(evt) : 'Nothing to do';
    });
  }

  public registerDevice(unRegDevice: any): void {
    console.log('registerDevice', unRegDevice);
    const deviceAttribute: DeviceAttribute = { isRegistered: true };
    this._dmSvc.postUnRegDevice(unRegDevice.deviceId, deviceAttribute).subscribe(success => {
      this._snackBarSvc.showSuccessSnackBarWithLocalize('deviceMgmt.deviceAddSuccess', SnackbarClasses.successBottom2);
      this.loadDeviceList();
    }, error => {
      this._snackBarSvc.showErrorSnackBar('Error occurred while adding the device', SnackbarClasses.errorBottom2);
    });
  }

}
