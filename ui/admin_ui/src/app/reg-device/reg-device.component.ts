import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Location } from '@angular/common';
import { HeaderInfo } from '../models/header.model';
import { FormGroup, FormBuilder, FormArray, FormControl, Validators } from '@angular/forms';
import { DmService } from '../services/dm.service';
import { setCss, deviceCompleteStatus } from '../utils/device-icon.util';
import { NewDevice, DeviceAttribute,  DeviceForm, Device } from '../models/device.model';
import { SharedService } from '../services/shared.service';
import { Router, ActivatedRoute } from '@angular/router';
import { vldSpecialCar, vldRequired, vldAlphaNum, vldRequiredSelect } from '../utils/validation.util';
import { errorRegxFields, errFieldNames, errorFields } from '../utils/validation-error-fields.const';
import { UserValidation } from '../shared/validations/user-validations';
import { scrollTop } from '../utils/window.util';
import { removeEmpty } from '../utils/array-util';
import { ConfirmDialogService } from '../services/confirm-dialog.service';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';
import { regx } from '../utils/regx.const';
import { TranslateService } from '@ngx-translate/core';
import { ValueAlterPipe } from '../shared/pipes/value-alter.pipe';

@Component({
  selector: 'app-reg-device',
  templateUrl: './reg-device.component.html',
  styleUrls: ['./reg-device.component.scss']
})
export class RegDeviceComponent implements OnInit {

  regDeviceForm: FormGroup;
  headerInfo: HeaderInfo = {
    currPage: 'Connections',
    isBackRequired: true,
    backUrl: ['device-list']
  };
  deviceBaseCss = 'online';
  deviceTypeList: any[];
  deviceTypeListProtocols;
  deviceStatusList: any[];
  protocols: any[];
  // regDevFormMock: DyForm[] = ADD_DEV_FORM;
  self: RegDeviceComponent;
  devIcon: any = '../../../assets/Images/userIcon.png';
  currPage: 'add' | 'edit' = 'add';
  deviceId: null | string = null;
  fieldDisabled = false;
  isDeviceInProgress = false;
  toggleHL7Version: boolean = false;
  public duplicateSerialNumber: string;
  public duplicateName: string;
  public isDeviceTypeSel = true;
  public enableProtocol = [];

  constructor(public _fb: FormBuilder,
    private _dmSvc: DmService,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _route: ActivatedRoute,
    private _confDialogSvc: ConfirmDialogService,
    private _snkBarSvc: SnackBarService,
    private _cdr: ChangeDetectorRef,
    private _location: Location,
    private translateSvc: TranslateService) {
    this.self = this;
  }

  ngOnInit() {
    this.initDeviceDetailForm();

    this.loadDeviceTypes();
    this.loadDeviceStatusList();
    this.loadProtocols();
    // setTimeout((res) => {
    //   this.protocols = ['interest1', 'interest2', 'interest3'];
    // });

    this._route.params.subscribe(params => {
      const devSlNo: string = params['serialNo'];
      if (devSlNo) {
        this.currPage = 'edit';
        this.loadDeviceDetail(devSlNo);
      }
    });
  }


  public loadDeviceDetail(serialNo: string): void {
    this._dmSvc.getDeviceDetail(serialNo).subscribe(device => {
      if (device && device.length > 0) {
        this.deviceId = device[0].deviceId;
        const deviceDetail: DeviceForm = this.getDeviceFormData(device[0]);
        this.duplicateSerialNumber = deviceDetail.serialNo;
        this.duplicateName = deviceDetail.name;
        this._dmSvc.getDevRunStatus(deviceDetail.serialNo, 'InProgress').subscribe(devRunStatus => {
          this.enableProtocol = device[0].deviceType.attributes.supportedProtocols;
          console.log('devRunStatus', devRunStatus);
          if (deviceDetail.state === 'ACTIVE' || deviceDetail.state === 'INACTIVE' || devRunStatus) {
            this.fieldDisabled = true;
          }

          if(devRunStatus){
            this.isDeviceInProgress = true;
          }

          for (const control in this.regDeviceForm.controls) {
            if (control === 'protocol') {
              deviceDetail[control].forEach(arr => {
                (this.regDeviceForm.controls[control] as FormArray).push(new FormControl(arr));

                // Commented for REST Auth requirement descope from Sprint 9 later it may used.
                // if(arr === 'REST'){
                //   this.toggleClientinfo('ADD', deviceDetail);
                // }
                if(arr === 'HL7'){
                  this.toggleHL7Version = true;
                }
              });
            } else {
              this.regDeviceForm.controls[control].setValue(deviceDetail[control]);
            }
          }

        }, errr => { });
      } else {
        this._snkBarSvc.showSuccessSnackBar('No data is available', SnackbarClasses.successBottom1);
      }
    }, error => {
      this._snkBarSvc.showErrorSnackBar('Error occured', SnackbarClasses.errorBottom1);
    });
  }

  public initDeviceDetailForm(): void {
    this.regDeviceForm = this._fb.group({
      name: ['', Validators.compose([vldSpecialCar({errorCode: 'deviceMgmt.validation.devSlNoSpec'}, regx.HYP_IN_MID), vldRequired("deviceMgmt.validation.devNameReq")])],
      deviceType: ['', vldRequiredSelect("deviceMgmt.validation.devTypeReq")],
      deviceStatus: ['offline'],
      protocol: this._fb.array([], [UserValidation.onValidatingCheckbox]),
      serialNo: ['', Validators.compose([vldRequired("deviceMgmt.validation.devSlNoReq"), vldSpecialCar({errorCode: 'deviceMgmt.validation.devSlNoSpec'}, regx.HYP_IN_MID)])],
      make: [''],
      ipAddress: [''],
      hostName: [''],
      model: [''],
      hwVersion: [''],
      swVersion: [''],
      protocolVersion: [''],
      location: [''],
      isRegistered: [true],
      status: [true],
      description: [''],
      state: ['NEW'],
      site: [''],
      gatewayId: [''],
      devComplteStatus: ['Offline']
      // For future requirement.
      // name: [null, vldAlphaNum(errorFields.dev_name_alpha_num)]
    });
    // this.regDeviceForm.controls.protocol.setValue([{'HL7': true}, {'Rest': true}]);
  }

  public loadDeviceTypes(): void {
    this._dmSvc.getDeviceTypes().subscribe(deviceTypes => {
      this.deviceTypeList = deviceTypes;
      console.log(this.deviceTypeList);
    }, error => {
      console.log('Error occured');
    });
  }

  public loadDeviceStatusList(): void {
    this._dmSvc.getDeviceStatuses().subscribe(statuses => {
      this.deviceStatusList = statuses;
    }, error => {
      console.log('Error occured');
    });
  }

  public loadProtocols(): void {
    setTimeout(result => {
      this._dmSvc.getProtocols().subscribe(protocols => {
        this.protocols = protocols;
        this.deviceTypeListProtocols = protocols;
      }, error => {
        console.log('Error occured');
      });
    });
  }

  public addDevice(): void {
    const formData = this.regDeviceForm.value;
    formData.protocol = formData.protocol ? removeEmpty(formData.protocol) : formData.protocol;
    delete formData.devComplteStatus;

    if (this.currPage === 'add') {
      this._dmSvc.postNewRegDevice(formData).subscribe(device => {
        this._sharedSvc.setSharedData('DEVICE_ADDED', true);
        this._router.navigate(['device-list']);
      }, error => {
        console.log('Error', error);
      });
    } else {
      // Commented for REST Auth requirement descope from Sprint 9 later it may used.
      // let clientId = this.regDeviceForm.value.clientId || this.regDeviceForm.value.clientId === '' ? this.regDeviceForm.value.clientId : null;
      // let password = this.regDeviceForm.value.password || this.regDeviceForm.value.password === '' ? this.regDeviceForm.value.password : null;
      // let tempFormData = {...this.regDeviceForm.value, clientId: clientId, password: password}

      this._dmSvc.postDeviceDetail(this.deviceId, formData).subscribe(device => {
        this._sharedSvc.setSharedData('DEVICE_UPDATED', true);
        this._router.navigate(['device-list']);
      }, error => {
        console.log('Error', error);
      });
    }
  }

  public updateDeviceStyle() {
    // Set the device base CSS name.
    this.deviceBaseCss = setCss(this.regDeviceForm.value.deviceStatus);
  }

  public formatDeviceFormData(formData: any): NewDevice {
    const formattedDevice: NewDevice = {} as NewDevice;
    formattedDevice.attributes = {} as DeviceAttribute;
    if (formData) {
      formattedDevice.deviceName = formData.name;
      formattedDevice.deviceType = formData.deviceType;
      formattedDevice.status = true;
      formattedDevice.serialNo = formData.serialNo;
      formattedDevice.attributes.deviceStatus = formData.deviceStatus ? formData.deviceStatus : '';
      formattedDevice.attributes.hwVersion = formData.hwVersion ? formData.hwVersion : '';
      formattedDevice.attributes.swVersion = formData.swVersion ? formData.swVersion : '';
      formattedDevice.attributes.isRegistered = true;
      formattedDevice.attributes.location = formData.location ? formData.location : '';
      formattedDevice.attributes.make = formData.make ? formData.make : '';
      formattedDevice.attributes.model = formData.model ? formData.model : '';
      formattedDevice.attributes.protocol = formData.protocol ? formData.protocol : '';
      formattedDevice.attributes.protocolVersion = formData.protocolVersion ? formData.protocolVersion : '';
    }
    return formattedDevice;
  }

  public getDeviceFormData(xhrDeviceData: any): DeviceForm {
    const formattedDevice: DeviceForm = {} as DeviceForm;
    let valueAlterPipe: ValueAlterPipe = new ValueAlterPipe();
    let devCompleteStatus: string;
    if (xhrDeviceData) {
      formattedDevice.name = xhrDeviceData.name;
      formattedDevice.deviceType = xhrDeviceData.deviceType.deviceTypeId;
      formattedDevice.status = xhrDeviceData.status;
      formattedDevice.serialNo = xhrDeviceData.serialNo;
      formattedDevice.deviceStatus = xhrDeviceData.attributes.deviceStatus;
      formattedDevice.hwVersion = xhrDeviceData.attributes.hwVersion;
      formattedDevice.swVersion = xhrDeviceData.attributes.swVersion;
      formattedDevice.isRegistered = xhrDeviceData.attributes.isRegistered;
      formattedDevice.location = xhrDeviceData.attributes.location;
      formattedDevice.make = xhrDeviceData.attributes.make;
      formattedDevice.model = xhrDeviceData.attributes.model;
      // tslint:disable-next-line:max-line-length
      formattedDevice.protocol = Array.isArray(xhrDeviceData.attributes.protocol) ? removeEmpty(xhrDeviceData.attributes.protocol) : [xhrDeviceData.attributes.protocol];
      // formattedDevice.protocol = [''];
      formattedDevice.protocolVersion = xhrDeviceData.attributes.protocolVersion;
      formattedDevice.ipAddress = xhrDeviceData.ipAddress;
      formattedDevice.hostName = xhrDeviceData.attributes.hostName;
      formattedDevice.description = xhrDeviceData.description;
      formattedDevice.state = xhrDeviceData.state;
      formattedDevice.site = xhrDeviceData.site;
      formattedDevice.gatewayId = xhrDeviceData.gatewayId;

      devCompleteStatus = deviceCompleteStatus(xhrDeviceData);
      formattedDevice.devComplteStatus = valueAlterPipe.transform(devCompleteStatus, formattedDevice.state, 'INACTIVE');

      // Commented for REST Auth requirement descope from Sprint 9 later it may used.
      // formattedDevice.clientId = xhrDeviceData.attributes.clientId;
      // formattedDevice.password = xhrDeviceData.attributes.password;
    }
    return formattedDevice;
  }

  public onSelectProtocol(event): void {
    console.log(event);
    const protocols = <FormArray>this.regDeviceForm.get('protocol') as FormArray;
    const protocolVersion = this.getControlValue('protocolVersion');

    if (protocols.length > 0) {
      for (let i = 0; i < protocols.length; i++) {
        // tslint:disable-next-line:no-unused-expression
        protocols[i] === '' ? protocols.removeAt(i) : 'do nothing';
      }
    }

    if (event.checked) {
      protocols.push(new FormControl(event.source.value));
    } else {
      const ind = protocols.controls.findIndex(userRole => userRole.value === event.source.value);
      protocols.removeAt(ind);
    }

    if (protocols.length === 1 && protocols.value.indexOf('REST') > -1) {
      this.updateFormModel('protocolVersion', null);
      //this.toggleHL7Version = false;
    }if(protocols.value.indexOf('HL7') > -1){
      this.updateFormModel('protocolVersion', protocolVersion?protocolVersion:'');
      //this.toggleHL7Version = true;
    }else{
      this.updateFormModel('protocolVersion', protocolVersion?protocolVersion:'');
      //this.toggleHL7Version = false;
    }

    // Commented for REST Auth requirement descope from Sprint 9 later it may used.
    // if(protocols.value.indexOf('REST') > -1){
    //   this.toggleClientinfo('ADD', null);
    // }else{
    //   this.toggleClientinfo('REMOVE', null);
    // }
  }

  public updateFormModel(formControl: string, value: any): void{
    this.regDeviceForm.controls[formControl].setValue(value);
  }

  public getControlValue(formControl: string): any{
    return this.regDeviceForm.controls[formControl].value;
  }

  public onDefaultChecking(protocol): boolean {
    return this.regDeviceForm.value.protocol.indexOf(protocol) > -1;
  }

  public onFileUpload(event): void {
    // let devForm: any = this.regDeviceForm.value;
    // console.log('onFileUpload', event.target.files[0]);
    // this._dmSvc.deleteDeviceIcon(devForm.serialNo, devForm.deviceType, 'png', event.target.files[0]).subscribe(deleteSuccess => {
    //   this._dmSvc.postDeviceIcon(devForm.serialNo, devForm.deviceType, 'png', event.target.files[0]).subscribe(success => {
    //     console.log('Uploaded');
    //     this.devIcon = this._dmSvc.getDeviceIcon(devForm.serialNo, devForm.deviceType, 'png', event.target.files[0]);
    //     console.log('this.devIcon', this.devIcon);
    //   }, error => {
    //     console.log('Error');
    //   })
    // }, deleteError => {
    //   console.log('Error');
    // });
    if (event.target.files && event.target.files[0]) {
      const reader = new FileReader();

      // tslint:disable-next-line:no-shadowed-variable
      reader.onload = (event: ProgressEvent) => {
        this.devIcon = (<FileReader>event.target).result;
      };

      reader.readAsDataURL(event.target.files[0]);
    }
  }

  public onSelectIcon(event) {
    if (this.regDeviceForm.valid) {
      event.click();
    } else {
      scrollTop();
    }
  }

  public goBack() {
    // let backUrlComp = 'device-list';
    if (this.regDeviceForm.dirty) {
      this._confDialogSvc.openConfirmDialog()
        .afterClosed()
        .subscribe(confirmStatus => {
          if (confirmStatus) {
            // this._router.navigate([backUrlComp]);
            this._location.back();
          }
        });
    } else {
      // this._router.navigate([backUrlComp]);
      this._location.back();
    }
  }

  onChange(event) {
    console.log('event', event);
    const protocols = <FormArray>this.regDeviceForm.get('protocol') as FormArray;

    if (event.checked) {
      protocols.push(new FormControl(event.source.value));
    } else {
      const i = protocols.controls.findIndex(x => x.value === event.source.value);
      protocols.removeAt(i);
    }
  }

  public removeDevice(): void {
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    let confirmMsg = translations.deviceMgmt.confirmArchive ? translations.deviceMgmt.confirmArchive : 'deviceMgmt.confirmArchive';
    this._confDialogSvc.openConfirmDialogParam(confirmMsg, 'deviceMgmt.archive')
      .afterClosed()
      .subscribe(confirmStatus => {
        if (confirmStatus && !this.isDeviceInProgress) {
          this._dmSvc.deleteDevice(this.deviceId).subscribe(deletedDevice => {
            this._sharedSvc.setSharedData('DEVICE_DELETED', {device: this.regDeviceForm.value.name});
            this._router.navigate(['device-list']);
          }, error => {

          });
        }else if(confirmStatus && this.isDeviceInProgress){
          let archiveFail = translations.deviceMgmt.archiveFail ? translations.deviceMgmt.archiveFail : 'deviceMgmt.archiveFail';
          this._snkBarSvc.showErrorSnackBar(archiveFail, SnackbarClasses.errorBottom2);
        }else if(this.regDeviceForm.value.deviceStatus.toLowerCase() === 'online'){
          let archiveFailDevOnline = translations.deviceMgmt.archiveFailDevOnline ? translations.deviceMgmt.archiveFailDevOnline : 'deviceMgmt.archiveFailDevOnline';
          this._snkBarSvc.showErrorSnackBar(archiveFailDevOnline, SnackbarClasses.errorBottom2);
        }
      });
  }

  public unarchiveDevice(): void{
    let tempRegDevForm = this.regDeviceForm.value;
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    let confirmMsg = translations.deviceMgmt.conformUnarchive ? translations.deviceMgmt.conformUnarchive : 'deviceMgmt.conformUnarchive';
    this._confDialogSvc.openConfirmDialogParam(confirmMsg, 'deviceMgmt.unArchive')
      .afterClosed()
      .subscribe(confirmStatus => {
        if (confirmStatus) {
          tempRegDevForm.status = true;
          tempRegDevForm.state = 'ACTIVE';
          tempRegDevForm = Object.assign({}, tempRegDevForm, {isRetired: 'N'});
          this._dmSvc.postDeviceDetail(this.deviceId, tempRegDevForm).subscribe(activatedDevice => {
            this._sharedSvc.setSharedData('DEVICE_ACTIVATED', {device: tempRegDevForm.name});
            this._router.navigate(['device-list']);
          }, error => {

          });
        }
      });
  }

  public onCheckProtocol(): boolean {
    const protocol = this.regDeviceForm.value.protocol;
    const disableProtoVersion: boolean = protocol.length === 1 && protocol.indexOf('REST') > -1 ? true : false;
    return disableProtoVersion;
  }

  public duplicateDeviceDetails(key: string) {
    if (key === 'serialNo') {
      const serialNo = this.regDeviceForm.get('serialNo');
      if (((this.duplicateSerialNumber === undefined || this.duplicateSerialNumber === null || this.duplicateSerialNumber === '') ||
      (this.duplicateSerialNumber !== serialNo.value)) && serialNo.value) {
        console.log(serialNo.value, 'device serial number');
        this._dmSvc.checkDuplicateDevice(`${key}='${serialNo.value}'`).subscribe(res => {
          const response = Object.keys(res);
          if (response.length > 0) {
            console.log(res, 'device serial number');
            serialNo.setErrors({
              errorCode: 'deviceMgmt.validation.devSerialNoExists'
            });
          }
        }, error => {
          console.log(error, 'error checking device serial number');
        });
      }
      console.log(serialNo);
    } else if (key === 'name') {
      const deviceName = this.regDeviceForm.get('name');
      if (((this.duplicateName === undefined || this.duplicateName === null || this.duplicateName === '') ||
      (deviceName.value !== this.duplicateName)) && deviceName.value) {
        console.log(deviceName.value, 'device name');
        this._dmSvc.checkDuplicateDevice(`${key} = '${deviceName.value}'`).subscribe(res => {
          const response = Object.keys(res);
          if (response.length > 0) {
            // console.log(res, 'device name');
            deviceName.setErrors({
              errorCode: 'deviceMgmt.validation.devNameExists'
            });
          }
        }, error => {
          console.log(error, 'error checking device name');
        });
      }
    }
  }

  onDeviceTypeChange(event: any) {
    const localProtocols = this.protocols;
    for (let i = 0 ; i < localProtocols.length; i++) {
      const jsonData = {
        checked: false,
        source : { value: localProtocols[i]  }
      };
      // console.log(jsonData);
      this.onSelectProtocol(jsonData);
    }
    const val = event.value;
    const deviceDetails = this.deviceTypeList;
    deviceDetails.filter((value, index) => {
      if (deviceDetails[index].deviceTypeId === val) {
        this.isDeviceTypeSel = false;
        this.enableProtocol = deviceDetails[index].attributes.supportedProtocols;
        this.enableProtocol.forEach(protocol => {
          (this.regDeviceForm.controls.protocol as FormArray).push(new FormControl(protocol));
        });
      }
    });
  }

  // Commented for REST Auth requirement descope from Sprint 9 later it may used.
  // toggleClientinfo(action, formData: DeviceForm){
  //   let clientIdReq = Validators.compose([vldRequired('Client Id')]);
  //   let passwordReq = Validators.compose([vldRequired('Password')]);

  //   if(action === 'ADD' && !formData){
  //     this.regDeviceForm.addControl('clientId', new FormControl('', clientIdReq));
  //     this.regDeviceForm.addControl('password', new FormControl('', passwordReq));
  //   }if(action === 'ADD' && formData){
  //     this.regDeviceForm.addControl('clientId', new FormControl(formData.clientId, clientIdReq));
  //     this.regDeviceForm.addControl('password', new FormControl(formData.password, passwordReq));
  //   }else if(action === 'REMOVE'){
  //     this.regDeviceForm.removeControl('clientId');
  //     this.regDeviceForm.removeControl('password');
  //   }
  // }
}