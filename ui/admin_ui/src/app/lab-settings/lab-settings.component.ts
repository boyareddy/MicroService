import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminApiService } from '../services/admin-api.service';
import { SnackBarService } from '../services/snack-bar.service';
import { updateResponse, formatter, LabSettingConstants, expImages } from './lab-settings.util';
import { TranslateService } from '@ngx-translate/core';
import { minOneFieldREquired, LabSettingsValidation } from './lab-setting-validations';
import { MatDialog } from '@angular/material';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { PermissionService } from '../services/permission.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-lab-settings',
  templateUrl: './lab-settings.component.html',
  styleUrls: ['./lab-settings.component.scss']
})
export class LabSettingsComponent implements OnInit {

  public labSettingsInfo: FormGroup;
  public fileSize: any = 1;
  public url: any;
  public errorMsg: any;
  error = true;
  public fileName: any;
  public fileInfo: any = new Blob();
  public fileType: any;

  sysLang = [
    {'dec': 'English - US', 'value': 'en_US'},
    {'dec': 'French', 'value': 'French'}
  ];

  sysDateFormat = [
    'DD/MM/YYYY',
    'MM/DD/YYYY',
  ];

  sysTimeFormat = [
    '12 h',
    '24 h',
  ];

  selectedLang = {'dec': 'English - US', 'value': 'en_US'};
  selectedDateFormat = 'DD/MM/YYYY';
  selectedTimeFormat = '24 h';

  constructor(private _fb: FormBuilder,
    private _adminSvc: AdminApiService,
    private _snackbarsvc: SnackBarService,
    private _translate: TranslateService,
    private _dialogBox: MatDialog,
    private _permission: PermissionService) { }

  ngOnInit() {
    this.fileInfo = new Blob();
    this.fileName = null;
    this.labSettings();
    this._permission.checkPermissionObs('view_lab_settings').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      if (res) {
        this.getLabSettingsInfo();
      }
    });
  }

  /**
   * Labsettings form initialisation
   */
  public labSettings() {
    this.labSettingsInfo = this._fb.group({
      'localisationSettings': this._fb.group({
        systemLanguage: ['en_US'],
        dateFormat : ['DD/MM/YYYY'],
        timeFormat : ['24 h'],
      }),
      'reportSettings': this._fb.group({
        labName: [''],
        phoneNumber: ['', [LabSettingsValidation.phoneNumberValidations]],
        labAddress1: [''],
        labAddress2: [''],
        labAddress3: [''],
        labLogoImageName: [''],
      }, { validator: minOneFieldREquired(Validators.required) })
    });
  }

  /**
   * fetching lab settings Information
   */
  public getLabSettingsInfo() {
    const translations = this._translate.translations[this._translate.currentLang];
    this._adminSvc.getLabSettings().subscribe((response: any) => {
      this.getLabLogo();
      if (response && response.length === 0) {
        this.labSettings();
      } else {
        const labSettings = formatter(response);
        this.fileName = labSettings['reportSettings']['labLogoImage'];
        this.labSettingsInfo.patchValue(labSettings);
      }
    }, error => {
      const message = `${translations.labSettings.connectivityIssue}`;
      console.log(error);
      this._snackbarsvc.showErrorSnackBar(message, 'failed-snackbar-bottom2');
    });
  }

  /**
   * fetching the lab logo from service
   */
  public getLabLogo() {
    const translations = this._translate.translations[this._translate.currentLang];
    this._adminSvc.getLabLogo().subscribe((response: any) => {
      if (response) {
        console.log(response, 'response data');
        const imageName = this.labSettingsInfo.get('reportSettings.labLogoImageName').value;
        this.fileName = imageName;
        console.log(imageName);
        /* converting the blob image to file and assign to file */
        this.fileInfo = this.convertblobToFile(response, imageName);
        console.log(this.fileInfo);
      }
    }, error => {
      // console.log(error);
      // /* error snackbar in case fetching lab settings */
      // this._snackbarsvc.showErrorSnackBar('Error fetching lablogo', 'failed-snackbar-bottom2');
      const message = `${translations.labSettings.connectivityIssue}`;
      console.log(error);
      this._snackbarsvc.showErrorSnackBar(message, 'failed-snackbar-bottom2');
    });
  }

  /**
   * by passing blobfile and image name returning the image file
   * adding the image name and lastmodified date to blob
   * @param blobFile blob file
   * @param imageFile imageFile
   */
  public convertblobToFile = (blobFile: Blob, imageFileName: string): File => {
    // const b: any = blobFile;
    // b.lastModifiedDate = new Date();
    // b.name = imageFileName;
    const file = new File([blobFile], imageFileName, {type: blobFile.type, lastModified: Date.now()});
    return <File>file;
  }

  /**
   * saving the form information
   * @param labSettingsInfo formData
   */
  public saveLabSettings(labSettingsInfo: FormGroup) {
    console.log(this.labSettingsInfo.value);
    console.log(this.labSettingsInfo);
    const translations = this._translate.translations[this._translate.currentLang];
    if (this.labSettingsInfo.dirty) {
      const expip = updateResponse(labSettingsInfo.value);
      console.log(expip , 'expip');
      this._adminSvc.saveUpdateLabSettings(expip).subscribe(response => {
        this.saveLabImage(null);
        console.log(response);
      }, error => {
        const message = `${translations.labSettings.connectivityIssue}`;
        console.log(error);
        this._snackbarsvc.showErrorSnackBar(message, 'failed-snackbar-bottom2');
      });

    } else {
      const warningMessage = `${translations.labSettings.warnMessage}`;
      this._dialogBox.open(ConfirmDialogComponent, {
        width: '486px',
        height: '185px',
        data: {onlyWarn: `${warningMessage}` },
      });
    }
  }

  /**
   * Storing the image by DB by passing report settings.
   */
  public saveLabImage(labSettingsInfo: FormGroup) {
    if (this.labSettingsInfo.get('reportSettings').valid) {
      const translations = this._translate.translations[this._translate.currentLang];
      const formData = new FormData();
      console.log(this.fileInfo);
      formData.append(LabSettingConstants.FILE, this.fileInfo);
      this._adminSvc.addLabLogo(formData).subscribe(response => {
        this.labSettingsInfo.controls.reportSettings.markAsPristine();
        this.labSettingsInfo.controls.localisationSettings.markAsPristine();
        const message = `${translations.labSettings.successMessage}`;
        console.log('updated image successfully', response);
        this._snackbarsvc.showSuccessSnackBar(message, 'success-snackbar-bottom2');
      }, error => {
        console.log(error, 'updating image error');
        const message = `${translations.labSettings.connectivityIssue}`;
      console.log(error);
      this._snackbarsvc.showErrorSnackBar(message, 'failed-snackbar-bottom2');
      });
    }
  }

  public getLabInfoPreview(labSettingsInfo: FormGroup) {
    // console.log(this.labSettingsInfo.get('reportSettings').valid, 'form validity');
    if (this.labSettingsInfo.get('reportSettings').valid) {
      const translations = this._translate.translations[this._translate.currentLang];
      const value = labSettingsInfo.value;
      const reportsettingsinfo = value.reportSettings;
      const reportsettings = {...reportsettingsinfo};
      delete reportsettings.labLogoImageName;
      const formData = new FormData();
      formData.append(LabSettingConstants.IMAGE, this.fileInfo);
      formData.append(LabSettingConstants.DATA, JSON.stringify(reportsettings));

      this._adminSvc.labInfoPreview(formData).subscribe(res => {
        // console.log(res);
        const fileURL = URL.createObjectURL(res);
        window.open(fileURL);
      }, error => {
        console.log('Blob Error', JSON.parse(error));
        const error1 = JSON.parse(error);
        if (error1.status === 400) {
          const message = error1.message;
          this._snackbarsvc.showErrorSnackBar(message, 'failed-snackbar-bottom2');
        } else {
          const message = `${translations.labSettings.connectivityIssue}`;
          console.log(error);
          console.log(error, 'error fetching records');
          this._snackbarsvc.showErrorSnackBar(message, 'failed-snackbar-bottom2');
        }
      });
    }
  }


  /**
   * reading the file from user change.
   * @param event fileEvent
   */
  public onFileChange(event) {
    if (event) {
      // console.log(event);
      this.errorMsg = null;
      this.fileName = null;
      this.url = null;
      this.labSettingsInfo.get('reportSettings.labLogoImageName').setValue('');
      this.labSettingsInfo.get('reportSettings.labLogoImageName').markAsDirty();
      this.fileInfo = new Blob();
      const fileData = event.target.files[0];
      const fileName = event.target.files[0].name;
      this.validateImage(fileData, fileName);
    }
  }

  /**
   * validating the image by passing filedata and filename
   * @param fileData fileInfo
   * @param fileName fileName
   */
  public validateImage(fileData, fileName) {
    const translations = this._translate.translations[this._translate.currentLang];
    // const reader = new FileReader();
    if (this.validateFileType(fileData)) {
      if (!this.validateFileSize(fileData)) {
        this.error = true;
        this.errorMsg = null;
        this.fileInfo = fileData;
        this.fileName = fileName;
        this.labSettingsInfo.get('reportSettings.labLogoImageName').setValue(fileName);
        // reader.readAsDataURL(fileData);
        // reader.onload = (event1: any) => {
        //   this.url = event1.target.result;
        // };
      } else {
        this.error = false;
        this.errorMsg = `${translations.labSettings.invalidFileSize}`;
      }
    } else {
      this.error = false;
      this.errorMsg = `${translations.labSettings.invalidFileFormat}`;
    }
  }

  /**
   * validating file sie
   * @param fileSizeInfo filesize of image
   */
  public validateFileSize(fileSizeInfo) {
    const fileSizeinMB = fileSizeInfo.size / (1024 * 1000);
    const size = Math.round(fileSizeinMB * 100) / 100; // convert upto 2 decimal place
    console.log(size);
    if (this.fileSize > size) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * validating the file type
   * @param fileData fileInformation
   */
  public validateFileType(fileData) {
    const fileType: string = fileData.type;
    if (fileType.indexOf(LabSettingConstants.IMAGE) > -1) {
      const imgType = fileType.substring(fileType.lastIndexOf('/') + 1).toLowerCase();
      const expImgTypes = expImages;
      if (expImgTypes.indexOf(imgType) > -1) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public deleteFileInfo() {
    this.fileName = null;
    this.url = null;
    this.labSettingsInfo.get('reportSettings.labLogoImageName').setValue('');
    this.labSettingsInfo.get('reportSettings.labLogoImageName').markAsDirty();
    this.fileInfo = new Blob();
  }
}
