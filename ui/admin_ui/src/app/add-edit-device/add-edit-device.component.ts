import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Location } from '@angular/common';
import { HeaderInfo } from '../models/header.model';
import { FormGroup, FormBuilder, FormArray, FormControl, Validators } from '@angular/forms';
import { DmService } from '../services/dm.service';
import { setCss, deviceCompleteStatus } from '../utils/device-icon.util';
import { NewDevice, DeviceAttribute,  DeviceForm, Device } from '../models/device.model';
import { SharedService } from '../services/shared.service';
import { Router, ActivatedRoute } from '@angular/router';
import { vldSpecialCar, vldRequired, vldSpaceCar, vldAlphaNum, vldRequiredSelect } from '../utils/validation.util';
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
import { AdminApiService } from 'src/app/services/admin-api.service';
import { UserErrorMessages } from 'src/app/shared/error-messages/userErrorMessages';
import { DEVICE_TYPES, SEQUENCER_TYPE, HTP_TYPE, NON_HTP_TYPE, TTV2ANALYSIS_TYPE, QIASYMPHONY } from '../utils/device-type.config';

@Component({
  selector: 'app-add-edit-device',
  templateUrl: './add-edit-device.component.html',
  styleUrls: ['./add-edit-device.component.scss']
})
export class AddEditDeviceComponent implements OnInit {

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
  self: AddEditDeviceComponent;
  devIcon: any = '../../../assets/Images/userIcon.png';
  currPage: 'add' | 'edit' = 'add';
  deviceId: null | string = null;
  fieldDisabled = false;
  isDeviceInProgress = false;
  // tslint:disable-next-line:no-inferrable-types
  toggleHL7Version: boolean = false;
  // tslint:disable-next-line:no-inferrable-types
  isAuthRequired: boolean = false;
  required: 'required' | '' = '';
  selectedDeviceType: any;
  isSequencer: boolean = false;
  sequencerTypes: string[] = ["Illumina Sequencer", 'QIAsymphony'];
  public deviceTypeConfig: string[];
  public duplicateSerialNumber: string;
  public duplicateName: string;
  public isDeviceTypeSel = true;
  public enableProtocol = [];
  public isTTv2Device = false;
  public qiaSymphony;

  constructor(public _fb: FormBuilder,
    private _dmSvc: DmService,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _adminService: AdminApiService,
    private _route: ActivatedRoute,
    private _confDialogSvc: ConfirmDialogService,
    private _snkBarSvc: SnackBarService,
    private _cdr: ChangeDetectorRef,
    private _location: Location,
    private translateSvc: TranslateService) {
    this.self = this;
  }

  ngOnInit() {

    this.selectedDeviceType = this._sharedSvc.getSharedData('selectedDeviceType');

    if(!this.selectedDeviceType){
      this._router.navigate(["device-list"]);
    }

    this.loadDeviceTypes();
    this.initDeviceDetailForm();
    this.loadDeviceStatusList();
    this.loadProtocols();
    // setTimeout((res) => {
    //   this.protocols = ['interest1', 'interest2', 'interest3'];
    // });

    if(this.selectedDeviceType && this.selectedDeviceType.name === DEVICE_TYPES.SEQUENCER){
      this.deviceTypeConfig = SEQUENCER_TYPE;
    } else if (this.selectedDeviceType && this.selectedDeviceType.name === DEVICE_TYPES.QIASYMPHONY) {
      this.deviceTypeConfig = QIASYMPHONY;
      this.qiaSymphony = DEVICE_TYPES.QIASYMPHONY;
    } else if (this.selectedDeviceType && this.selectedDeviceType.name === DEVICE_TYPES.HTP){
      this.deviceTypeConfig = HTP_TYPE;
    } else if (this.selectedDeviceType && this.selectedDeviceType.name === DEVICE_TYPES.TTV2ANALYSIS){
      this.deviceTypeConfig = TTV2ANALYSIS_TYPE;
      this.isTTv2Device = true;
      // tslint:disable-next-line:prefer-const
      const translations = this.translateSvc.translations[this.translateSvc.currentLang];
      // tslint:disable-next-line:prefer-const
      const deviceName = translations.deviceMgmt.ttv2DeviceName ? translations.deviceMgmt.ttv2DeviceName : 'deviceMgmt.ttv2DeviceName';
      console.log(deviceName, 'deviceName=========');
      this.regDeviceForm.get('name').setValue(deviceName);
      this.regDeviceForm.get('serialNo').setValue('Dummy-TTV2-Serial-No-' + this.getRandom(9));
      console.log('Dummy-TTV2-Serial-No-' + this.getRandom(9), '---------------------');
      // tslint:disable-next-line:max-line-length
      this.regDeviceForm.addControl('JWTCertificate', this._fb.control('', Validators.compose([vldRequired('deviceMgmt.validation.jwtCertificate')])));
      // tslint:disable-next-line:max-line-length
      this.regDeviceForm.addControl('sshCertificate', this._fb.control('', Validators.compose([vldRequired('deviceMgmt.validation.sshCertificate')])));
      // tslint:disable-next-line:max-line-length
      this.regDeviceForm.addControl('clientCertificate', this._fb.control('', Validators.compose([vldRequired('deviceMgmt.validation.clientCertificate')])));
      // tslint:disable-next-line:max-line-length
      this.regDeviceForm.addControl('url', this._fb.control('', Validators.compose([vldRequired('deviceMgmt.validation.url')])));
      // tslint:disable-next-line:max-line-length
      this.regDeviceForm.addControl('ttv2OutputDirectory', this._fb.control('', Validators.compose([vldRequired('deviceMgmt.validation.ttv2OutputDirectory')])));
    }else if(this.selectedDeviceType){
      this.deviceTypeConfig = NON_HTP_TYPE;
    }

    this._route.params.subscribe(params => {
      const devSlNo: string = params['serialNo'];
      if (devSlNo) {
        this.currPage = 'edit';
        this.loadDeviceDetail(devSlNo);
      }
    });
  }

  checkFormData() {
    console.log(this.regDeviceForm, 'this.regDeviceForm');
  }

  getRandom(length) {
    return Math.floor(Math.pow(10, length - 1) + Math.random() * 9 * Math.pow(10, length - 1));
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

          if (devRunStatus) {
            this.isDeviceInProgress = true;
          }

          // tslint:disable-next-line:forin
          for (const control in this.regDeviceForm.controls) {
            if (control === 'protocol') {
              // deviceDetail[control].indexOf('REST') > -1 ? this.toggleClientinfo('ADD', deviceDetail) : 'Do Nothing';
              deviceDetail[control].indexOf('REST') > -1 ? this.isAuthRequired = true : this.isAuthRequired = false;
            }
            this.regDeviceForm.controls[control].setValue(deviceDetail[control]);
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
      // tslint:disable-next-line:max-line-length
      name: ['', Validators.compose([vldSpecialCar({errorCode: 'deviceMgmt.validation.devAlphaNum'}, regx.SPEC_WITH_ALPHA_NUM), vldRequired('deviceMgmt.validation.devNameReq')])],
      deviceType: [''],
      deviceStatus: ['offline'],
      protocol: [],
      // tslint:disable-next-line:max-line-length
      serialNo: ['', Validators.compose([vldSpecialCar({errorCode: 'deviceMgmt.validation.devAlphaNum'}, regx.SPEC_WITH_ALPHA_NUM), vldRequired('deviceMgmt.validation.devSlNoReq')])],
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
      devComplteStatus: ['Offline'],
      clientId: [null],
      password: [null],
      outputDirectory: [null]
      // For future requirement.
      // name: [null, vldAlphaNum(errorFields.dev_name_alpha_num)]
    });
    // this.regDeviceForm.controls.protocol.setValue([{'HL7': true}, {'Rest': true}]);
  }

  public loadDeviceTypes(): void {
    this._dmSvc.getDeviceTypes().subscribe(deviceTypes => {
      this.deviceTypeList = deviceTypes;

      // To set the device type.
      if(this.selectedDeviceType){
        this.currPage !== 'edit' ? this.regDeviceForm.controls.deviceType.setValue(this.selectedDeviceType.deviceTypeId) : 'Do nothing';
      }

      // To set the protocol if device has support for protocol.
      if(this.selectedDeviceType && this.sequencerTypes.indexOf(this.selectedDeviceType.name) === -1){
        this.onDeviceTypeChange({value: this.selectedDeviceType.deviceTypeId});
      }

      if((this.selectedDeviceType  && this.sequencerTypes.indexOf(this.selectedDeviceType.name) > -1)){
        this.updateFormControlValidation([{name: 'outputDirectory', validators: [vldRequired('deviceMgmt.validation.outputDirectory')]}]);
      }

      if (this.selectedDeviceType && this.selectedDeviceType.name === DEVICE_TYPES.QIASYMPHONY) {
        this.updateFormControlValidation([{name: 'location', validators: [vldRequired('deviceMgmt.validation.location')]}]);
      }
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

  public addIlluminaDevice(): void{
    // Validate the Output Directory
    // tslint:disable-next-line:max-line-length
    this._dmSvc.validateOutputDirForMultipleDevice(this.regDeviceForm.value.outputDirectory, this.selectedDeviceType.name).subscribe(status => {
      this.addDevice();
    }, error => {
      if(error.status === 503 || error.status === 409){
        let translatedMessage = this._snkBarSvc.getSingleMessage(error.error);
        this._snkBarSvc.showErrorSnackBar(translatedMessage, SnackbarClasses.errorBottom1);
      }else{
        this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryAgain', SnackbarClasses.errorBottom1);
      }
    });
  }

  validateOutputDirOnServer(outputDir){
    const outputDirectory = this.regDeviceForm.get('outputDirectory');
    if(outputDir){
      this._dmSvc.validateOutputDirForMultipleDevice(outputDir, this.selectedDeviceType.name).subscribe(status => {}, error => {
      console.log(error, 'error00000000000000000000');
        if(error.status === 503 || error.status === 409){
          let translatedMessage = this._snkBarSvc.getSingleMessage(error.error);
          outputDirectory.setErrors({
            errorCode: translatedMessage
          });
        }else{
          outputDirectory.setErrors({
            errorCode: 'deviceMgmt.tryAgain'
          });
        }
      });
    }
  }

  validateName() {
    const name = this.regDeviceForm.get('name');
    const nameValue = this.regDeviceForm.get('name').value;
    const error = regx.ONE_SPACE.test(nameValue) ? {errorCode: 'deviceMgmt.validation.devNameSpace'} : null;
    if (error) {
      name.setErrors(error);
      name.updateValueAndValidity();
    }
  }

  public addDevice(): void {
    const formData = this.regDeviceForm.value;
    formData.protocol = formData.protocol ? removeEmpty(formData.protocol) : formData.protocol;
    delete formData.devComplteStatus;
    if (this.currPage === 'add') {

      if (formData.protocol && formData.protocol.indexOf('REST') > -1) {

      this._dmSvc.addDeviceAuth(formData).subscribe(success => {
        this._dmSvc.postNewRegDevice(formData).subscribe(device => {
          // tslint:disable-next-line:max-line-length
          if (formData && formData.protocol && formData.protocol.length > 0 && formData.protocol.indexOf('REST') !== -1) {
            console.log(device, 'device===========');
            if (device) {
              const deviceId = device.deviceId;
              const userName = formData.clientId;
              this._dmSvc.mapDeviceAndUser(deviceId, userName).subscribe(deviceMap => {
                this._sharedSvc.setSharedData('DEVICE_ADDED', true);
                this._router.navigate(['device-list']);
              }, error => {
                console.log('mapDeviceAndUser error', error);
              });
            }
          } else {
            this._sharedSvc.setSharedData('DEVICE_ADDED', true);
            this._router.navigate(['device-list']);
          }
        }, error => {
          console.log('Error', error);
          this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryArchiveAgain', SnackbarClasses.errorBottom1);
        });
      }, error => {
        this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.deviceAuthFails', SnackbarClasses.errorBottom1);
          // const userName = formData.clientId;
          // this._dmSvc.postNewRegDevice(formData).subscribe(device => {
          //   if (formData && formData.protocol && formData.protocol.length > 0 && formData.protocol.indexOf('REST') !== -1) {
          //     if (device) {
          //       const deviceId = device.deviceId;
          //       this._dmSvc.mapDeviceAndUser(deviceId, userName).subscribe(deviceMap => {
          //         this._sharedSvc.setSharedData('DEVICE_ADDED', true);
          //         this._router.navigate(['device-list']);
          //       }, error2 => {
          //       });
          //     }
          //   } else {
          //     this._sharedSvc.setSharedData('DEVICE_ADDED', true);
          //     this._router.navigate(['device-list']);
          //   }
          // }, error2 => {
          //   this._snkBarSvc.showErrorSnackBar('Please try configuring the device again.', SnackbarClasses.errorBottom1);
          // });
          // delete formData.clientId;
          // delete formData.password;
      });

    } else {
      this._dmSvc.postNewRegDevice(formData).subscribe(device => {
        this._sharedSvc.setSharedData('DEVICE_ADDED', true);
        this._router.navigate(['device-list']);
      }, error => {
        console.log('Error', error);
        this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryAgain', SnackbarClasses.errorBottom1);
      });
    }

    } else {
      // Commented for REST Auth requirement descope from Sprint 9 later it may used.
      // tslint:disable-next-line:max-line-length
      const clientId = this.regDeviceForm.value.clientId || this.regDeviceForm.value.clientId === '' ? this.regDeviceForm.value.clientId : null;
      // tslint:disable-next-line:max-line-length
      const password = this.regDeviceForm.value.password || this.regDeviceForm.value.password === '' ? this.regDeviceForm.value.password : null;
      const tempFormData = {...this.regDeviceForm.value, clientId: clientId, password: password};

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
      formattedDevice.attributes.JWTCertificate = formData.JWTCertificate ? formData.JWTCertificate : '';
      formattedDevice.attributes.clientCertificate = formData.clientCertificate ? formData.clientCertificate : '';
      formattedDevice.attributes.url = formData.url ? formData.url : '';
    }
    return formattedDevice;
  }

  public getDeviceFormData(xhrDeviceData: any): DeviceForm {
    const formattedDevice: DeviceForm = {} as DeviceForm;
    // tslint:disable-next-line:prefer-const
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
      formattedDevice.clientId = xhrDeviceData.attributes.clientId;
      formattedDevice.password = xhrDeviceData.attributes.password;
      // Added for TTV2 Analysis software
      formattedDevice.JWTCertificate = xhrDeviceData.attributes.JWTCertificate;
      formattedDevice.sshCertificate = xhrDeviceData.attributes.sshCertificate;
      formattedDevice.clientCertificate = xhrDeviceData.attributes.clientCertificate;
      formattedDevice.url = xhrDeviceData.attributes.url;
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
      // this.toggleHL7Version = false;
    }if (protocols.value.indexOf('HL7') > -1) {
      this.updateFormModel('protocolVersion', protocolVersion ? protocolVersion : '');
      // this.toggleHL7Version = true;
    } else {
      this.updateFormModel('protocolVersion', protocolVersion ? protocolVersion : '');
      // this.toggleHL7Version = false;
    }

    // Commented for REST Auth requirement descope from Sprint 9 later it may used.
    // if(protocols.value.indexOf('REST') > -1){
    //   this.toggleClientinfo('ADD', null);
    // }else{
    //   this.toggleClientinfo('REMOVE', null);
    // }
  }

  public updateFormModel(formControl: string, value: any): void {
    this.regDeviceForm.controls[formControl].setValue(value);
  }

  public getControlValue(formControl: string): any {
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
    // tslint:disable-next-line:prefer-const
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    // tslint:disable-next-line:prefer-const
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
        } else if (confirmStatus && this.isDeviceInProgress) {
          // tslint:disable-next-line:prefer-const
          let archiveFail = translations.deviceMgmt.archiveFail ? translations.deviceMgmt.archiveFail : 'deviceMgmt.archiveFail';
          this._snkBarSvc.showErrorSnackBar(archiveFail, SnackbarClasses.errorBottom2);
        } else if (this.regDeviceForm.value.deviceStatus.toLowerCase() === 'online') {
          // tslint:disable-next-line:max-line-length
          const archiveFailDevOnline = translations.deviceMgmt.archiveFailDevOnline ? translations.deviceMgmt.archiveFailDevOnline : 'deviceMgmt.archiveFailDevOnline';
          this._snkBarSvc.showErrorSnackBar(archiveFailDevOnline, SnackbarClasses.errorBottom2);
        }
      });
  }

  public unarchiveDevice(): void {
    let tempRegDevForm = this.regDeviceForm.value;
    // tslint:disable-next-line:prefer-const
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    // tslint:disable-next-line:prefer-const
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

  addDeviceAssayType(deviceAssayTypes: Array<string>) {
    if (deviceAssayTypes) {
      // tslint:disable-next-line:prefer-const
      let devAssayTypeFormArray: FormArray = new FormArray([]);
      // tslint:disable-next-line:semicolon
      deviceAssayTypes.forEach(deviceAssayType => { devAssayTypeFormArray.push(new FormControl(deviceAssayType)) });
      console.log('devAssayTypeFormArray', devAssayTypeFormArray);
      this.regDeviceForm.addControl('supportedAssayTypes', devAssayTypeFormArray);
    } else {
      this.regDeviceForm.removeControl('supportedAssayTypes');
    }
  }

  onDeviceTypeChange(event: any) {
    const deviceTypeEntity = this.deviceTypeList.filter(deviceType => deviceType.deviceTypeId === event.value)[0];
    this.regDeviceForm.controls.protocol.setValue(deviceTypeEntity.attributes.supportedProtocols);

    // tslint:disable-next-line:max-line-length
    if (deviceTypeEntity && deviceTypeEntity.attributes && deviceTypeEntity.attributes.supportedProtocols &&  deviceTypeEntity.attributes.supportedProtocols.indexOf('REST') > -1) {
      this.isAuthRequired = true;
      // tslint:disable-next-line:max-line-length
      this.updateFormControlValidation([{name: 'clientId', validators: [vldRequired('deviceMgmt.validation.reqClientId')]}, {name: 'password', validators: [vldRequired('deviceMgmt.validation.reqPassword')]}]);
    } else {
      this.isAuthRequired = false;
      this.clearFormControls(['clientId', 'password']);
    }

    // Currently device assay type is on hold ( 2/8/2019 mailed by Rajadurai - RE: Conenect - Device Tagging implementation on Hold )
    // this.addDeviceAssayType(deviceTypeEntity.attributes.supportedAssayTypes);

    console.log('Event', deviceTypeEntity);
  }

  // Commented for REST Auth requirement descope from Sprint 9 later it may used.
  toggleClientinfo(action, formData: DeviceForm) {
    // tslint:disable-next-line:prefer-const
    let clientIdReq = Validators.compose([vldRequired('Client Id')]);
    // tslint:disable-next-line:prefer-const
    let passwordReq = Validators.compose([vldRequired('Password')]);

    if (action === 'ADD' && !formData) {
      this.regDeviceForm.addControl('clientId', new FormControl('', clientIdReq));
      this.regDeviceForm.addControl('password', new FormControl('', passwordReq));
    }if (action === 'ADD' && formData) {
      this.regDeviceForm.addControl('clientId', new FormControl(formData.clientId, clientIdReq));
      this.regDeviceForm.addControl('password', new FormControl(formData.password, passwordReq));
    } else if (action === 'REMOVE') {
      this.regDeviceForm.removeControl('clientId');
      this.regDeviceForm.removeControl('password');
    }
  }

  clearFormControls(controls: string[]) {
    this.required = '';
    controls.forEach(control => {
      this.regDeviceForm.get(control).setValue(null);
      this.regDeviceForm.get(control).clearValidators();
      this.regDeviceForm.get(control).updateValueAndValidity();
    });
  }

  updateFormControlValidation(controls: any[]) {
    this.required = 'required';
    controls.forEach(control => {
      this.regDeviceForm.get(control.name).setValidators(control.validators);
      this.regDeviceForm.get(control.name).updateValueAndValidity();
    });
  }

  public onCheckingClientIDduplication(clientId) {
    console.log('onCheckingClientIDduplication', this.regDeviceForm.get('clientId').value);
    const clientIdvalue =  this.regDeviceForm.get('clientId').value;
    console.log(clientIdvalue , 'clientIdvalue');
    if (clientIdvalue && clientIdvalue !== '') {
      this._adminService.checkUserNameDuplication(clientIdvalue).subscribe((successData ) => {
        console.log('successData13131224', successData);
        if (successData && successData.length >= 1) {
          this.regDeviceForm.get('clientId').setErrors({
            errorCode: 'deviceMgmt.validation.devUserNameExists'
          });
        }
      });
    }
  }

}
