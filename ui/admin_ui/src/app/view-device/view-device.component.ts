import { Component, OnInit, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { DmService } from '../services/dm.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { setIcon, setCss, validDevice, deviceCompleteStatus } from '../utils/device-icon.util';
import { SharedService } from '../services/shared.service';
import { HeaderInfo } from '../models/header.model';
import { SnackBarService } from '../services/snack-bar.service';
import { vldAlphaNum, vldSpecialCar, vldRequired } from '../utils/validation.util';
import { errorFields } from '../utils/validation-error-fields.const';
import { SnackbarClasses } from '../utils/query-strings';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { DeviceForm } from '../models/device.model';
import { ConfirmDialogService } from '../services/confirm-dialog.service';
import { TranslateService } from '@ngx-translate/core';
import { regx } from '../utils/regx.const';
import { PermissionService } from '../services/permission.service';
import { DEVICE_TYPES } from '../utils/device-type.config';

@Component({
  selector: 'app-view-device',
  templateUrl: './view-device.component.html',
  styleUrls: ['./view-device.component.scss']
})
export class ViewDeviceComponent implements OnInit, AfterViewChecked {

  deviceDetailForm: FormGroup;
  toggleEdit = false;
  device: any;
  deviceSerialNo: string;
  deviceIcon: string;
  deviceBaseCss: string;
  toggleInpIcon: any = {
    deviceName: false,
    location: false,
    JWTCertificate: false,
    sshCertificate: false,
    clientCertificate: false,
    url: false,
    ttv2OutputDirectory: false
  };
  headerInfo: HeaderInfo = {
    headerName: 'Connections',
    isBackRequired: true,
    backUrl: ['device-list'],
    disableSubHeadLocalize: true
  };
  isDeviceInProgress: boolean = false;
  havingAccess = false;
  canUnArchive = true;
  isSequencer: boolean = false;
  sequencerTypes: string[] = ['Illumina Sequencer', 'QIAsymphony'];
  ttv2AnalysisType: string[] = ['Analysis SW TTv2'];
  isTTV2Analysis = false;
  isAttributesEditable = ['location', 'JWTCertificate', 'sshCertificate', 'clientCertificate', 'url', 'ttv2OutputDirectory'];

  constructor(private _dmSvc: DmService,
    private _route: ActivatedRoute,
    public _fb: FormBuilder,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _sbSvc: SnackBarService,
    private _matIconReg: MatIconRegistry,
    private _domSanitizer: DomSanitizer,
    private _confDialogSvc: ConfirmDialogService,
    private translateSvc: TranslateService,
    private _permission: PermissionService,
    private cdr: ChangeDetectorRef) {
      this._matIconReg
      .addSvgIcon('edit-gray', 
      this._domSanitizer.bypassSecurityTrustResourceUrl('assets/Images/edit-gray.svg'));
    }

  ngOnInit() {
    this._permission.checkPermissionObs('Update_Device').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    //  To get the device detail.
    this._route.params.subscribe(params => {

      //  Initialize the device detail form.
      this.initDeviceDetailForm();

      //  Set the device serial number from query parameter.
      this.deviceSerialNo = params['serialNo'];

      //  Load the device detail based on device serial number passed in query parameter.
      this.loadDeviceDetail(this.deviceSerialNo);
    });
  }

  public disableAddDevice(deviceListArr) {
    if (deviceListArr && deviceListArr.length > 0) {
      // tslint:disable-next-line:max-line-length
      const ttv2Analysis = deviceListArr.filter(device => device.deviceType && device.deviceType.name === DEVICE_TYPES.TTV2ANALYSIS && device.state && device.state.toLowerCase() !== 'inactive');
      console.log(ttv2Analysis, 'ttv2Analysis0000000000000000000');
      if (ttv2Analysis && ttv2Analysis.length > 0) {
        this.canUnArchive = false;
      }
    }
  }

  public loadDeviceList(): void {
    this._dmSvc.getDeviceList(true).subscribe(deviceList => {
      deviceList.sort((a, b) => (a.modifiedDate) > (b.modifiedDate) ? -1 : (a.modifiedDate) < (b.modifiedDate) ? 1 : 0);
      this.disableAddDevice(deviceList);
    }, error => {
      console.log('deviceList', error);
    });
  }

  public loadDeviceDetail(serialNo: string): void {
    let deviceStatus: string;

    this._dmSvc.getDeviceDetail(serialNo).subscribe(device => {
      // this._dmSvc.getDevRunStatus(device[0].deviceId, 'InProgress').subscribe(devRunStatus => {
      //   this.isDeviceInProgress = devRunStatus ? true: false;
        this.device = device[0];
        this.initDeviceDetailFormWithParam(this.device);
        if (this.device) {
          // Set header currPage attribute
          this.headerInfo.currPage = this.device.name;

          // tslint:disable-next-line:max-line-length
          deviceStatus = this.device && this.device.attributes && this.device.attributes.deviceStatus ? this.device.attributes.deviceStatus : null;
          console.log(this.device.deviceType, 'this.device.deviceType');
          console.log(this.ttv2AnalysisType.indexOf(this.device.deviceType.name), 'this.device.deviceType.name');
          // Check device type and toggle the screen
          if(this.device && this.device.deviceType && this.ttv2AnalysisType.indexOf(this.device.deviceType.name) > -1){
            this.isTTV2Analysis = true;
          }else{
            this.isTTV2Analysis = false;
          }

           // Check device type and toggle the screen
           if(this.device && this.device.deviceType && this.sequencerTypes.indexOf(this.device.deviceType.name) > -1){
            this.isSequencer = true;
          }else{
            this.isSequencer = false;
          }

          //  Set the device icon.
          this.deviceIcon = setIcon(deviceStatus);

          //  Set the device base CSS name.
          this.deviceBaseCss = setCss(deviceStatus);

          if (this.device.deviceType.name === DEVICE_TYPES.TTV2ANALYSIS) {
            this.loadDeviceList();
          }
        } else {
          this._sbSvc.showErrorSnackBar('No device found !', SnackbarClasses.errorBottom1);
        }
      })
    // }, error => {

    // });
  }

  public initDeviceDetailForm(): void {
    this.deviceDetailForm = this._fb.group({
      name: [],
      location: []
      //  For future requirement.
      // name: [null, vldAlphaNum(errorFields.dev_name_alpha_num)]
    });
  }

  public initDeviceDetailFormWithParam({name, attributes}): void {
    this.deviceDetailForm = this._fb.group({
      // tslint:disable-next-line:max-line-length
      name:  [name, Validators.compose([vldSpecialCar({errorCode: 'deviceMgmt.validation.devAlphaNum'}, regx.SPEC_WITH_ALPHA_NUM), vldRequired("deviceMgmt.validation.devNameReq")])],
      location: [attributes.location],
      JWTCertificate: [attributes.JWTCertificate, Validators.compose([vldRequired('deviceMgmt.validation.jwtCertificate')])],
      sshCertificate: [attributes.sshCertificate, Validators.compose([vldRequired('deviceMgmt.validation.sshCertificate')])],
      clientCertificate: [attributes.clientCertificate, Validators.compose([vldRequired('deviceMgmt.validation.clientCertificate')])],
      url: [attributes.url, Validators.compose([vldRequired('deviceMgmt.validation.url')])],
      ttv2OutputDirectory: [attributes.ttv2OutputDirectory, Validators.compose([vldRequired('deviceMgmt.validation.ttv2OutputDirectory')])]
    });
  }

  public updateDeviceDetailForm(field, iconClicked): void {
    if (!iconClicked) {
      const formValue = this.deviceDetailForm.value;
      for (const key in formValue) {
        if(key !== field)
          delete formValue[key];
      }
      this._dmSvc.postDeviceDetail(this.device.deviceId, formValue).subscribe(updatedDevice => {
        if (updatedDevice[field]) {
          this.device[field] = updatedDevice[field];
          this.deviceDetailForm.controls[field].setValue(this.device[field]);
        } else if (this.isAttributesEditable.indexOf(field) !== -1) {
          this.device.attributes[field] = updatedDevice.attributes[field];
          this.deviceDetailForm.controls[field].setValue(this.device.attributes[field]);
        }
      }, error => {

      });
    } else {
      console.log('Icon Clicked');
      //this.deviceDetailForm.controls[field].setValue(this.device[field]);
    }
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

  // public removeDevice(): void {
  //   this._dmSvc.deleteDevice(this.device.deviceId).subscribe(deletedDevice => {
  //     this._sharedSvc.setSharedData('DEVICE_DELETED', true);
  //     this._router.navigate(['device-list']);
  //   }, error => {

  //   });
  // }

  public ngAfterViewChecked(): void {
    this.cdr.detectChanges();
    let isAnyFieldOnEdit = JSON.stringify(this.toggleInpIcon);
    this.headerInfo.isAnyFormInvalid = this.deviceDetailForm.dirty && isAnyFieldOnEdit.indexOf('true') > -1;
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

  public toggleInputIcon(field: string): void{
    let fieldControl = this.deviceDetailForm.controls[field];
    console.log(fieldControl.invalid, 'fieldControl.invalid');
    if(!fieldControl.invalid){
      console.log(fieldControl.invalid, 'fieldControl.invalid');
      if(field === 'name' && fieldControl.value !== this.device.name){
          if(this.toggleInpIcon[field]){
            //this.toggleInpIcon[field] = !this.toggleInpIcon[field];
            this._dmSvc.checkDuplicateDevice(`name='${fieldControl.value}'`).subscribe(res => {
              const response = Object.keys(res);
              if (response.length > 0 && this.device.deviceId !== res[0].deviceId) {
                // console.log(res, 'device name');
                this.deviceDetailForm.controls[field].setErrors({
                  errorCode: 'deviceMgmt.validation.devNameExists'
                });
              }else{
                this.postFieldValid(field, fieldControl);
              }
            }, error => {
              console.log(error, 'error checking device name');
            });
        }else{
          this.postFieldValid(field, fieldControl);
        }
      }else{
        console.log(field, 'field');
        this.postFieldValid(field, fieldControl);
      }
    }else{
      this.device[field] =  fieldControl.value;
    }
  }

  public postFieldValid(field, fieldControl): void{
    console.log(field, 'field');
    console.log(this.toggleInpIcon[field], 'this.toggleInpIcon[field]');
    this.toggleInpIcon[field] = !this.toggleInpIcon[field];
    console.log(fieldControl.value, 'fieldControl.value');
    if (fieldControl.value && fieldControl.value !== '') {
      this.device[field] =  fieldControl.value;
    }

    this.updateDeviceDetailForm(field, this.toggleInpIcon[field]);
  }
  
  public saveFieldInfo(field: string): void{
    //console.log("saveFieldInfo", this.deviceDetailForm);
    this.toggleInputIcon(field);
  }

  public unarchiveDevice(): void{
    let tempRegDevForm: DeviceForm = {} as DeviceForm;
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    let confirmMsg = translations.deviceMgmt.conformUnarchive ? translations.deviceMgmt.conformUnarchive : 'deviceMgmt.conformUnarchive';
    this._confDialogSvc.openConfirmDialogParam(confirmMsg, 'deviceMgmt.unArchive')
      .afterClosed()
      .subscribe(confirmStatus => {
        if (confirmStatus) {
          tempRegDevForm.status = true;
          tempRegDevForm.state = 'ACTIVE';
          tempRegDevForm = Object.assign({}, tempRegDevForm, {isRetired: 'N'});
          if (this.device.attributes.protocol && this.device.attributes.protocol[0] === 'REST') {
            console.log('test');
            this._dmSvc.activateDevice(this.device.attributes.clientId).subscribe((res) => {
              console.log('User activate successful');
            }, error => {
              console.log('Error on deactivating the user.');
            });
          }
          this._dmSvc.postDeviceDetail(this.device.deviceId, tempRegDevForm).subscribe(activatedDevice => {
            this._sharedSvc.setSharedData('DEVICE_ACTIVATED', {device: this.device.name});
            this._router.navigate(['device-list']);
          }, error => {
            this._sbSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryUnArchiveAgain', SnackbarClasses.errorBottom1);
          });
        }
      });
  }

  public removeDevice(): void {
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    let confirmMsg = translations.deviceMgmt.confirmArchive ? translations.deviceMgmt.confirmArchive : 'deviceMgmt.confirmArchive';
    this._confDialogSvc.openConfirmDialogParam(confirmMsg, 'deviceMgmt.archive')
      .afterClosed()
      .subscribe(confirmStatus => {
        if (confirmStatus && this.device.attributes && this.device.attributes.deviceStatus.toLowerCase() !== 'online') {
          if (this.device.attributes.protocol && this.device.attributes.protocol[0] === 'REST') {
            console.log('test');
            this._dmSvc.deactivateDevice(this.device.attributes.clientId).subscribe((res) => {
              console.log('User deactivate successful');
            }, error => {
              console.log('Error on deactivating the user.');
            });
          }
          this._dmSvc.deleteDevice(this.device.deviceId).subscribe(deletedDevice => {
            this._sharedSvc.setSharedData('DEVICE_DELETED', {device: this.device.name});
            this._router.navigate(['device-list']);
          }, error => {
            this._sbSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryArchiveAgain', SnackbarClasses.errorBottom1);
          });
        }else if(confirmStatus && this.device.attributes && this.device.attributes.deviceStatus.toLowerCase() === 'online'){
          let archiveFail = translations.deviceMgmt.archiveFail ? translations.deviceMgmt.archiveFail : 'deviceMgmt.archiveFail';
          this._sbSvc.showErrorSnackBar(archiveFail, SnackbarClasses.errorBottom2);
        }
        // else if(this.regDeviceForm.value.deviceStatus.toLowerCase() === 'online'){
        //   let archiveFailDevOnline = translations.deviceMgmt.archiveFailDevOnline ? translations.deviceMgmt.archiveFailDevOnline : 'deviceMgmt.archiveFailDevOnline';
        //   this._sbSvc.showErrorSnackBar(archiveFailDevOnline, 'X', SnackbarClasses.errorBottom2);
        // }
      });
  }


  isEllipsisActive(e) {
    if (e) {
      return (e.offsetWidth < e.scrollWidth);
    } else {
      return false;
    }
  }
}

