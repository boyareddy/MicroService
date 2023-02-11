import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from 'node_modules/@angular/forms';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { PermissionService } from 'src/app/services/permission.service';
import { vldRequiredSelect } from 'src/app/utils/validation.util';
import * as moment from 'moment';
import { ProblemReportService } from '../services/problem-report.service';
import { MatSnackBar } from 'node_modules/@angular/material';
import { Moment } from 'moment';
import { ProblemRptDateRange } from '../models/problemreport.model';
import { SnackbarClasses } from 'src/app/utils/query-strings';
import { SharedService } from '../services/shared.service';
import { errorFields } from '../utils/validation-error-fields.const';
import { TranslateService } from '../../../node_modules/@ngx-translate/core';


@Component({
  selector: 'app-problem-report',
  templateUrl: './problem-report.component.html',
  styleUrls: ['./problem-report.component.scss']
})
export class ProblemReportComponent implements OnInit {

  problemReportForm: FormGroup;
  minFromDate: Date;
  maxFromDate: Date;
  minToDate: Date;
  maxToDate: Date;
  generateReport;
  responseCode;
  currentDate;
  tempDate;
  endDateValue;
  disabledate = true;
  // disablebtn = true;
  problemReportMonth;
  problemReportDays;
  getFromDate;
  getToDate;

  constructor(
    private _fb: FormBuilder,
    private _permission: PermissionService,
    private _sharedSvc: SharedService,
    private _snackBarSvc: SnackBarService,
    private _report: ProblemReportService,
    private translateSvc: TranslateService
  ) {
    this.minFromDate = new Date();
    this.maxFromDate = new Date();
    this.minToDate = new Date();
    this.maxToDate = new Date();
  }

  ngOnInit() {
    this.initProblemReport();
    this.minMaxMonthdate();
  }

  public minMaxMonthdate() {
    const appProperties = this._sharedSvc.getSharedData('appProperties');
    this.problemReportMonth = 6;
    this.problemReportDays = 21;
    if (appProperties && appProperties.problemReportMonth) {
      this.problemReportMonth = appProperties.problemReportMonth;
    }
    if (appProperties && appProperties.problemReportDays) {
      this.problemReportDays = appProperties.problemReportDays;
    }
    this.minFromDate = new Date(this.minFromDate.setMonth(this.minFromDate.getMonth() - this.problemReportMonth));
    this.maxFromDate.setDate(this.maxFromDate.getDate());
  }


  public initProblemReport(): void {
    this.problemReportForm = this._fb.group({
      startDate: ['', vldRequiredSelect('problemReport.validation.startDateReq')],
      endDate: [{ value: '', disabled: this.disabledate }, vldRequiredSelect('problemReport.validation.endDateReq')]
    });
    // this.regDeviceForm.controls.protocol.setValue([{'HL7': true}, {'Rest': true}]);
  }

  public onGeneratingReport() {
    const translations = this.translateSvc.translations[this.translateSvc.currentLang];
    const dateRange = this.getDateRange();
    this.generateReport = this._report.getProblemReports(dateRange).subscribe(res => {
      // tslint:disable-next-line:max-line-length
      const successMsg = `${translations.problemReport.validation.successProblemReport} ${dateRange.fromDate} ${translations.problemReport.validation.to} ${dateRange.toDate}.`;
      this._snackBarSvc.showSuccessSnackBar(successMsg, SnackbarClasses.successBottom2);
      //  this.disablebtn = true;
    }, error => {
      if (error.status === 201) {
        // tslint:disable-next-line:max-line-length
        const successMsg = `${translations.problemReport.validation.successProblemReport} ${dateRange.fromDate} ${translations.problemReport.validation.to} ${dateRange.toDate}.`;
        this._snackBarSvc.showSuccessSnackBar(successMsg, SnackbarClasses.successBottom2);
      } else {
        // tslint:disable-next-line:max-line-length
        console.log(error, 'Error occurred while creating the problem report for the date range <Start date> to <End date>. Please try again.');
        // tslint:disable-next-line:max-line-length
        const ErrorMsg = `${translations.problemReport.validation.errorProblemReport} ${dateRange.fromDate} ${translations.problemReport.validation.to} ${dateRange.toDate}. ${translations.problemReport.validation.tryAgain}`;
        this._snackBarSvc.showErrorSnackBar(ErrorMsg, SnackbarClasses.errorBottom1);
        //  this.disablebtn = false;
      }
    });
  }

  public getDateRange() {
    const dateFormat = 'YYYY-MM-DD HH:mm:ss';
    const startDate: Moment = moment(this.problemReportForm.value.startDate);
    const endDate: Moment = moment(this.problemReportForm.value.endDate);
    const dateRange: ProblemRptDateRange = {} as ProblemRptDateRange;

    dateRange.fromDate = startDate.startOf('day').format(dateFormat);
    dateRange.toDate = endDate.endOf('day').format(dateFormat);

    return dateRange;
  }

  public onDateChange(dateType, event): void {
    if (dateType === 'startDateRange') {
      this[dateType] = moment(event.value).format();
      this.problemReportForm.get('endDate').enable();
      this.getToDate = this.problemReportForm.get('endDate').value;
      this.getendDate(event.value);
      if (this.getToDate !== '') {
        this.getToDate = this.getToDate.toLocaleString();
        this.getFromDate = event.value.toLocaleString();
        this.getToDate = new Date(this.getToDate);
        this.getFromDate = new Date(this.getFromDate);
        const timeDiffOnChange = this.getToDate - this.getFromDate;
        const tempDateOnChange = Math.round(timeDiffOnChange / (1000 * 3600 * 24));
        if (tempDateOnChange > 21 || tempDateOnChange < 0) {
          setTimeout(() => {
            this.problemReportForm.get('endDate').setErrors({
              errorCode: errorFields.range.errorCode
            });
          }, 100);
        }
      }
    } else {
      this[dateType] = moment(event.value).format();
    }
  }

  public getendDate(startDateValue) {
    if (startDateValue) {
      this.currentDate = new Date().toLocaleString();
      startDateValue = startDateValue.toLocaleString();
      this.currentDate = new Date(this.currentDate);
      startDateValue = new Date(startDateValue);
      const timeDiff = Math.abs(this.currentDate - startDateValue);
      this.tempDate = Math.ceil(timeDiff / (1000 * 3600 * 24));
      if (this.tempDate < 21) {
        this.minToDate = new Date(startDateValue);
        this.endDateValue = this.currentDate;
        this.maxToDate = this.endDateValue;
      } else {
        this.minToDate = new Date(startDateValue);
        this.endDateValue = startDateValue.setDate(startDateValue.getDate() + this.problemReportDays);
        this.endDateValue = new Date(this.endDateValue);
        this.maxToDate = this.endDateValue;
      }
    }
  }

}
