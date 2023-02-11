/**
 * Component name: IlluminaSequencerComponent
 * Component alias name: app-illumina-sequencer
 * Created on: 4/16/2019
 * For story: RC-14068
 * Other affected stories: RC-14067
*/

import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { vldSpecialCar, vldRequired } from '../../../utils/validation.util';
import { regx } from '../../../utils/regx.const';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { DmService } from '../../../services/dm.service';
import { PermissionService } from '../../../services/permission.service';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmDialogService } from '../../../services/confirm-dialog.service';
import { SharedService } from '../../../services/shared.service';
import { Router } from '@angular/router';
import { SnackBarService } from '../../../services/snack-bar.service';
import { SnackbarClasses } from '../../../utils/query-strings';
import { DeviceForm } from '../../../models/device.model';
import { DEVICE_TYPES } from '../../../utils/device-type.config';

@Component({
  selector: 'app-illumina-sequencer',
  templateUrl: './illumina-sequencer.component.html',
  styleUrls: ['./illumina-sequencer.component.scss']
})
export class IlluminaSequencerComponent implements OnInit {

  deviceDetailForm: FormGroup;
  toggleFields: any = { name: false, outputDirectory: false, location: false };
  havingAccess = false;
  isDeviceRunInProgress: boolean = false;
  public qiaSymphony;

  @Input() device: any;

  constructor(private _fb: FormBuilder,
              private _matIconReg: MatIconRegistry,
              private _domSanitizer: DomSanitizer,
              private _dmSvc: DmService,
              private _permission: PermissionService,
              private translateSvc: TranslateService,
              private _confDialogSvc: ConfirmDialogService,
              private _sharedSvc: SharedService,
              private _router: Router,
              private _snkBarSvc: SnackBarService) { 
    this._matIconReg
      .addSvgIcon('edit-gray', 
      this._domSanitizer.bypassSecurityTrustResourceUrl('assets/Images/edit-gray.svg'));
  }

  ngOnInit() {
    this._dmSvc.getDevRunStatus(this.device.serialNo, 'InProgress').subscribe(devRunStatus => {
      if(devRunStatus){
        this.isDeviceRunInProgress = true;
      }else{
        this.isDeviceRunInProgress = false;
      }
    });
    this._permission.checkPermissionObs('Update_Device').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    this.initDeviceDetailForm();
    if (this.device.deviceType.name === DEVICE_TYPES.QIASYMPHONY) {
      this.qiaSymphony = DEVICE_TYPES.QIASYMPHONY;
      // tslint:disable-next-line:max-line-length
      this.deviceDetailForm.get('location').setValidators(Validators.compose([vldRequired('deviceMgmt.validation.location')]));
    }
  }

  initDeviceDetailForm(): void {
    this.deviceDetailForm = this._fb.group({
      name: [this.device.name, Validators.compose([vldSpecialCar({errorCode: 'deviceMgmt.validation.devAlphaNum'}, regx.SPEC_WITH_ALPHA_NUM), vldRequired("deviceMgmt.validation.devNameReq")])],
      outputDirectory: [this.device.attributes.outputDirectory, Validators.compose([vldRequired('deviceMgmt.validation.outputDirectory')])],
      location: [this.device.attributes.location]
    });
  }

  initDeviceDetailFormWithParam({name, attributes}): void {
    this.deviceDetailForm = this._fb.group({
      name:  [name, Validators.compose([vldSpecialCar({errorCode: 'deviceMgmt.validation.devAlphaNum'}, regx.SPEC_WITH_ALPHA_NUM), vldRequired("deviceMgmt.validation.devNameReq")])],
      outputDirectory: [attributes.outputDirectory, Validators.compose([vldRequired('deviceMgmt.validation.outputDirectory')])],
      location: [attributes.location]
    });
  }

  toggleControl(controlName){
    if(this.toggleFields[controlName]){
      console.log("Error", this.deviceDetailForm.controls[controlName]);

      // For attribute to check the duplicate validation.
      if(controlName === 'name'){
          if(this.deviceDetailForm.value[controlName] === this.device.name){
            this.toggleFields[controlName] = !this.toggleFields[controlName];
          }else if(this.deviceDetailForm.controls[controlName].valid){
            this._dmSvc.checkDuplicateDevice(`name='${this.deviceDetailForm.value.name}'`).subscribe(res => {
              const response = Object.keys(res);
              if (response.length > 0) {
                this.deviceDetailForm.controls[controlName].setErrors({
                  errorCode: 'deviceMgmt.validation.devNameExists'
                });
              }else{
                this.updateDeviceDetailForm(controlName);
              }
            }, error => {
              console.log(error, 'error checking device name');
            });
        }
      }
      
      // For attribute to check other than duplication check.
      else if(controlName === 'outputDirectory' && this.deviceDetailForm.controls[controlName].valid){
        // To check whether the device run is in progress
        if(this.isDeviceRunInProgress){
          // If the device run is in progress
          this.deviceDetailForm.controls[controlName].setErrors({
            errorCode: 'deviceMgmt.deviceRunInProgress'
          });
        }else{
          // If the device run is not in progress
          if(this.deviceDetailForm.value[controlName] === this.device.attributes.outputDirectory){
            this.toggleFields[controlName] = !this.toggleFields[controlName];
          }else{

            this._dmSvc.getDevRunStatus(this.device.serialNo, 'InProgress').subscribe(devRunStatus => {
              if(devRunStatus){
                this.deviceDetailForm.controls[controlName].setErrors({
                  errorCode: "Cannot update the output directory, as a run is in progress. Please try again later"
                });
              }else{
                // tslint:disable-next-line:max-line-length
                this._dmSvc.validateOutputDir(this.deviceDetailForm.value.outputDirectory, this.device.deviceType.name).subscribe(status => {
                  this.updateDeviceDetailForm(controlName);
                }, error => {
                  if(error.status === 503 || error.status === 409){
                    this.deviceDetailForm.controls[controlName].setErrors({
                      errorCode: error.error
                    });
                  }
                });
              }
            });
          }
        }
      }

      else if(controlName === 'location'){
        this.updateDeviceDetailForm(controlName);
      }
    }else{
      this.toggleFields[controlName] = !this.toggleFields[controlName];
    }
    
    // if(!this.toggleFields[controlName]){
    //   console.log(controlName, "Persist");
    // }
  }

  updateDeviceDetailForm(controlName): void {
      let formValue = this.deviceDetailForm.value;
      for (const key in formValue) {
        if(key !== controlName)
          delete formValue[key];
      }
      this._dmSvc.postDeviceDetail(this.device.deviceId, formValue).subscribe(updatedDevice => {
        if(updatedDevice[controlName]){
          this.device[controlName] = updatedDevice[controlName];
          this.deviceDetailForm.controls[controlName].setValue(this.device[controlName]);
        }else if( controlName === 'outputDirectory' || controlName === 'location' ){
          this.device.attributes[controlName] = updatedDevice.attributes[controlName];
          this.deviceDetailForm.controls[controlName].setValue(this.device.attributes[controlName]);
        }
        this.toggleFields[controlName] = !this.toggleFields[controlName];
      }, error => {
        
      });
  }

  removeDevice(): void {

    this._dmSvc.getDevRunStatus(this.device.serialNo, 'InProgress').subscribe(devRunStatus => {
      if(devRunStatus){

        this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.archiveFailDevOnline', SnackbarClasses.errorBottom1);

      }else{

        let translations = this.translateSvc.translations[this.translateSvc.currentLang];
        let confirmMsg = translations.deviceMgmt.confirmArchive ? translations.deviceMgmt.confirmArchive : 'deviceMgmt.confirmArchive';
        this._confDialogSvc.openConfirmDialogParam(confirmMsg, 'deviceMgmt.archive')
          .afterClosed()
          .subscribe(confirmStatus => {
            if (confirmStatus && this.device.attributes && this.device.attributes.deviceStatus.toLowerCase() !== 'online') {
              this._dmSvc.deleteDevice(this.device.deviceId).subscribe(deletedDevice => {
                this._sharedSvc.setSharedData('DEVICE_DELETED', {device: this.device.name});
                this._router.navigate(['device-list']);
              }, error => {
                this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryAgain', SnackbarClasses.errorBottom1);
              });
            }else if(confirmStatus && this.device.attributes && this.device.attributes.deviceStatus.toLowerCase() === 'online'){
              let archiveFail = translations.deviceMgmt.archiveFail ? translations.deviceMgmt.archiveFail : 'deviceMgmt.archiveFail';
              this._snkBarSvc.showErrorSnackBar(archiveFail, SnackbarClasses.errorBottom2);
            }
          });

      }
    });
    
  }

  unarchiveDevice(): void{
    let tempRegDevForm: DeviceForm = {} as DeviceForm;
    let translations = this.translateSvc.translations[this.translateSvc.currentLang];
    let confirmMsg = translations.deviceMgmt.conformUnarchive ? translations.deviceMgmt.conformUnarchive : 'deviceMgmt.conformUnarchive';
    this._confDialogSvc.openConfirmDialogParam(confirmMsg, 'deviceMgmt.unArchive')
      .afterClosed()
      .subscribe(confirmStatus => {
        if (confirmStatus) {
          let outputDir = this.deviceDetailForm.value.outputDirectory;
          if(outputDir){
            this._dmSvc.validateOutputDirForMultipleDevice(outputDir, this.device.deviceType.name).subscribe(success => {
              tempRegDevForm.status = true;
              tempRegDevForm.state = 'ACTIVE';
              tempRegDevForm = Object.assign({}, tempRegDevForm, {isRetired: 'N'});
              this._dmSvc.postDeviceDetail(this.device.deviceId, tempRegDevForm).subscribe(activatedDevice => {
                this._sharedSvc.setSharedData('DEVICE_ACTIVATED', {device: this.device.name});
                this._router.navigate(['device-list']);
              }, error => {
                this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryUnArchiveAgain', SnackbarClasses.errorBottom1);
              });
            }, error => {
              if(error.status === 503 || error.status === 409){
                let translatedMessage = this._snkBarSvc.getSingleMessage(error.error);
                this._snkBarSvc.showErrorSnackBar(translatedMessage, SnackbarClasses.errorBottom1);
              }else{
                this._snkBarSvc.showErrorSnackBarWithLocalize('deviceMgmt.tryUnArchiveAgain', SnackbarClasses.errorBottom1);
              }
            });
          }
        }
      });
  }
}
