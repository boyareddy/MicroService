import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from 'node_modules/@angular/forms';
import { BackupService } from '../services/backup-service';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';
import { MatDialog } from '../../../node_modules/@angular/material';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { PermissionService } from '../services/permission.service';
import { TranslateService } from '@ngx-translate/core';
import { Moment } from '../../../node_modules/moment';
import moment from 'moment-timezone';

@Component({
  selector: 'app-backupandrestore',
  templateUrl: './backupandrestore.component.html',
  styleUrls: ['./backupandrestore.component.scss']
})
export class BackupandrestoreComponent implements OnInit {

  backupform: FormGroup;
  submitted = false;
  havingAccess = false;
  subscribeBackup;
  backupDetails;
  backupStatus;
  progressDetails;
  isSaveEnabled = true;
  isBackupDisabled = false;
  isOutputLocationEnabled = false;
  public defaultMessage = 'Backup process is already running, please submit it later';
  response = {
    'backup_interval': 'weekly',
    'LastBackupDate': '2018-11-11T18:30:00.000Z',
    'NextBackupDate': '2018-11-11T18:30:00.000Z',
    'Status': 'inprogress',
    'backup_location': '/home'
  };
  isOptLocSaved = false;
  parsedDate;

  autoBackup = [
    'Daily',
    'Weekly',
    'Monthly',
  ];

  constructor(
    private formBuilder: FormBuilder,
    private backupService: BackupService,
    private _permission: PermissionService,
    private _snackBarSvc: SnackBarService,
    private _dialogBox: MatDialog,
    private translateSvc: TranslateService
  ) { }

  ngOnInit() {
    // const translations = this.translateSvc.translations[this.translateSvc.currentLang];
    // const message = `${translations.backupandrestore.validation.downloadInprogress}`;
    this.backupform = this.formBuilder.group({
      'choose': [null, [Validators.required]],
      'backup_interval': ''
    });
    this.onLoadingBackup();
    // this.backupform.get('choose').setValue(this.backupDetails.backup_location);
    // this.backupform.get('choose').updateValueAndValidity();
  }

  onSubmit() {
    this.submitted = true;
    if (this.backupform.invalid) {
      return;
    }
    if (this.backupform.get('choose').dirty && this.isOptLocSaved) {
      this.isBackupDisabled = false;
      this.createBackup();
    } else if (!this.backupform.get('choose').dirty) {
      this.createBackup();
    } else {
      this.isBackupDisabled = true;
    }
  }

  createBackup() {
    const outputPath = { 'backup_location': this.backupform.get('choose').value };
    const translations = this.translateSvc.translations[this.translateSvc.currentLang];
    this.backupService.generateBackup(outputPath).subscribe((response: any) => {
      const timezone = this.gettimeZone();
      const date = moment(response.DateAndTime).format('DD-MMM-YYYY h:mm:ss ');
      console.log(date + timezone, 'date');
      this.parsedDate = date + timezone;
      if (response && response.Message === 'Backup is in progress') {
        // tslint:disable-next-line:max-line-length
        const errorMsg1 = translations.backupandrestore.validation.downloadInprogress ? translations.backupandrestore.validation.downloadInprogress : 'backupandrestore.validation.downloadInprogress';
        this._snackBarSvc.showErrorSnackBar(errorMsg1, SnackbarClasses.errorBottom1);
      } else if (response && response.Message === 'Destination directory is not available') {
        // tslint:disable-next-line:max-line-length
        const errorMsg2 = translations.backupandrestore.validation.directoryNotAvailable ? translations.backupandrestore.validation.directoryNotAvailable : 'backupandrestore.validation.directoryNotAvailable';
        this._snackBarSvc.showErrorSnackBar(errorMsg2, SnackbarClasses.errorBottom1);
      } else if (response && response.Message === 'Backup started successfully') {
        // tslint:disable-next-line:max-line-length
        const successMsg = translations.backupandrestore.validation.backUpStarted ? translations.backupandrestore.validation.backUpStarted : 'backupandrestore.validation.backUpStarted';
        this._snackBarSvc.showSuccessSnackBar(`${successMsg} ${this.parsedDate}`, SnackbarClasses.successBottom2);
      } else if (response && response.Message === 'Insufficient storage') {
        // tslint:disable-next-line:max-line-length
        const errorMsg3 = translations.backupandrestore.validation.insufficientStorage ? translations.backupandrestore.validation.insufficientStorage : 'backupandrestore.validation.insufficientStorage';
        this._snackBarSvc.showErrorSnackBar(`${errorMsg3} ${this.parsedDate}`, SnackbarClasses.errorBottom1);
      } else {
        // tslint:disable-next-line:max-line-length
        const successMsg = translations.backupandrestore.validation.backUpStarted ? translations.backupandrestore.validation.backUpStarted : 'backupandrestore.validation.backUpStarted';
        this._snackBarSvc.showSuccessSnackBar(`${successMsg} ${this.parsedDate}`, SnackbarClasses.successBottom2);
      }
    }, error => {
      const timezone = this.gettimeZone();
      const date = moment(error.DateAndTime).format('DD-MMM-YYYY h:mm:ss ');
      this.parsedDate = date + timezone;
      console.log(date + timezone, 'date');
      if (error && error.Message === 'Destination directory is not available') {
        // tslint:disable-next-line:max-line-length
        const errorMsg1 = translations.backupandrestore.validation.directoryNotAvailable ? translations.backupandrestore.validation.directoryNotAvailable : 'backupandrestore.validation.directoryNotAvailable';
        this._snackBarSvc.showErrorSnackBar(errorMsg1, SnackbarClasses.errorBottom1);
      } else if (error && error.Message === 'Write access is restricted in the selected folder') {
        // tslint:disable-next-line:max-line-length
        const errorMsg2 = translations.backupandrestore.validation.writeAccessRestricted ? translations.backupandrestore.validation.writeAccessRestricted : 'backupandrestore.validation.writeAccessRestricted';
        this._snackBarSvc.showErrorSnackBar(errorMsg2, SnackbarClasses.errorBottom1);
      } else if (error && error.Message === 'Backup started successfully') {
        // tslint:disable-next-line:max-line-length
        const successMsg = translations.backupandrestore.validation.backUpStarted ? translations.backupandrestore.validation.backUpStarted : 'backupandrestore.validation.backUpStarted';
        this._snackBarSvc.showSuccessSnackBar(`${successMsg} ${this.parsedDate}`, SnackbarClasses.successBottom2);
      } else if (error && error.Message === 'Insufficient storage') {
        // tslint:disable-next-line:max-line-length
        const errorMsg4 = translations.backupandrestore.validation.insufficientStorage ? translations.backupandrestore.validation.insufficientStorage : 'backupandrestore.validation.insufficientStorage';
        this._snackBarSvc.showErrorSnackBar(`${errorMsg4} ${this.parsedDate}`, SnackbarClasses.errorBottom1);
      } else {
        // tslint:disable-next-line:max-line-length
        const errorMsg3 = translations.backupandrestore.validation.backUpFailed ? translations.backupandrestore.validation.backUpFailed : 'backupandrestore.validation.backUpFailed';
        // tslint:disable-next-line:max-line-length
        this._snackBarSvc.showErrorSnackBar(`${errorMsg3} ${this.parsedDate}`, SnackbarClasses.errorBottom1);
        console.log('Error on Manual backup. ', error);
      }
    });
  }

  onLoadingBackup() {
    this.subscribeBackup = this.backupService.getBackupDetails().subscribe(resp => {
      this.backupDetails = resp;
      this.backupform.get('choose').setValue(this.backupDetails.backup_location);
      console.log(this.backupDetails, 'backupwer');
    }, error => {
      //  this.backupDetails = this.response;
      console.log('Error on getting BackupDetails');
    });
  }

  onOutputLocatiosave(backup) {
    console.log(backup, 'backup_interval');
    const intervaltime = { 'backup_interval': backup.value.backup_interval, 'backup_location': backup.value.choose };
    this.backupService.updateScheduleBckup(intervaltime).subscribe(response => {
      this.isOptLocSaved = true;
      this.isBackupDisabled = false;
      this.isSaveEnabled = true;
    }, error => {
      this.isOptLocSaved = true;
      this.isBackupDisabled = false;
      this.isSaveEnabled = true;
      console.log('Error on Changing the scheduled backup.', error);
    });
  }

  onOutputLocationChange() {
    // const translations = this.translateSvc.translations[this.translateSvc.currentLang];
    const optLoc = this.backupform.get('choose').value;
    this.isBackupDisabled = true;
    //   const message = `${translations.backupandrestore.validation.outputLocationSaved}`;
    this.defaultMessage = 'Please save the output location and proceed with backup';
    console.log(optLoc, 'optLosdd');
    this.isSaveEnabled = optLoc ? false : true;
  }

  gettimeZone() {
    const timeZone = moment.tz.guess();
    const currentTime = new Date();
    const timeZoneOffset = currentTime.getTimezoneOffset();
    const timeZoneFormat = moment.tz.zone(timeZone).abbr(timeZoneOffset);
    return timeZoneFormat;
  }
}

