import { Component, OnInit } from '@angular/core';
import { AuditDateRange, AuditLog, AuditBtn } from '../models/audit.model';
import { AlService } from '../services/al.service';
import { expJsonToCsv } from '../utils/json-to-csv.util';
import { auditLogHeaders } from '../utils/audit-log.util';
import * as moment from 'moment';
import { Moment } from 'moment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { vldRequired, vldRequiredSelect } from '../utils/validation.util';
import { errFieldNames } from '../utils/validation-error-fields.const';
import { HttpResponse } from '@angular/common/http';
import { fileDownload, MIME_TYPE } from '../utils/misc.util';
import { SnackBarService } from '../services/snack-bar.service';
import { SnackbarClasses } from '../utils/query-strings';
import { PermissionService } from '../services/permission.service';

@Component({
  selector: 'app-audit-log',
  templateUrl: './audit-log.component.html',
  styleUrls: ['./audit-log.component.scss']
})
export class AuditLogComponent implements OnInit {

  auditForm: FormGroup;
  dateRanges: AuditBtn = {} as AuditBtn;
  startDateRange: AuditDateRange = {} as AuditDateRange;
  endDateRange: AuditDateRange = {} as AuditDateRange;
  resposeContDispSeparator: string = 'filename=';
  havingAccess = false;

  constructor(
    private _alSvc: AlService,
    private _fb: FormBuilder,
    private _snackBarSvc: SnackBarService,
    private _permission: PermissionService
    ) {
    this.startDateRange.toDate = this.endDateRange.toDate = moment().format();
  }

  ngOnInit() {
    this._permission.checkPermissionObs('Get_Audit_Details').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    this.initAuditForm();
  }

  public getAuditLogs(dateRange: AuditDateRange) {
    this._alSvc.getAuditLogs(dateRange).toPromise().then((res: HttpResponse<any>) => {
      if(res){
        const contentDisposition = res.headers.get('Content-Disposition');
        const contentDispArray = contentDisposition ? contentDisposition.split(this.resposeContDispSeparator) : null;
        const fileName = dateRange.shLabel.toString();
        const blob = new Blob([res.body], {
          type: MIME_TYPE.zip
        });
        fileDownload(blob, fileName);
        if(contentDispArray && contentDispArray[1]){
          this._alSvc.deleteFile(contentDispArray[1]).subscribe();
        }
      }else{
        this._snackBarSvc.showErrorSnackBar('Download failed', SnackbarClasses.errorBottom1);
      }
    }).catch(error => {
      this._snackBarSvc.showErrorSnackBar('Error occurred', SnackbarClasses.errorBottom1);
    });
  }

  public initAuditForm(): void {
    this.auditForm = this._fb.group({
      startDate: ['', vldRequiredSelect("audit.validation.startDateReq")],
      endDate: ['', vldRequiredSelect("audit.validation.endDateReq")]
    });
    // this.regDeviceForm.controls.protocol.setValue([{'HL7': true}, {'Rest': true}]);
  }

  public onDownloadingAudit(): void {
    const dateRange: AuditDateRange = this.getDateRange();
    this.getAuditLogs(dateRange);
  }

  public getDateRange(): AuditDateRange {
    const dateFormat = 'YYYY-MM-DD HH:mm:ss';
    const dateLabelFormat = 'MM-DD-YYYY';
    const startDate: Moment = moment(this.auditForm.value.startDate);
    const endDate: Moment = moment(this.auditForm.value.endDate);
    const downloadLbl = 'Audit log';

    const dateRange: AuditDateRange = {} as AuditDateRange;
    dateRange.shLabel = `${downloadLbl}_${startDate.format(dateLabelFormat)}_${endDate.format(dateLabelFormat)}`;

    //  Previously UTC
    // dateRange.fromDate = startDate.utc().startOf('day').format(dateFormat);
    // dateRange.toDate = endDate.utc().endOf('day').format(dateFormat);

    //  Now without UTC
    dateRange.fromDate = startDate.startOf('day').format(dateFormat);
    dateRange.toDate = endDate.endOf('day').format(dateFormat);

    return dateRange;
  }

  public onDateChange(dateType, rangeType, event): void {
    this[dateType][rangeType] = moment(event.value).format();
  }
}
