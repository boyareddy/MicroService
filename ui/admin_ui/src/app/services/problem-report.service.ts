import { Injectable } from '@angular/core';
import { SharedService } from './shared.service';
import { Observable, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { AuditDateRange, AuditLog } from '../models/audit.model';

@Injectable({
  providedIn: 'root'
})
export class ProblemReportService {

  appProperties: any;
  uiConf: string;
  host: string;
  userName: string = sessionStorage.getItem('userName');
  admAE: string;

  constructor(private _httpClient: HttpClient,
              private _sharedSvc: SharedService) {
    this.appProperties = this._sharedSvc.getSharedData('appProperties');

    // tslint:disable-next-line:max-line-length
    this.admAE = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admmApi.port}/${this.appProperties.admmApi.module}/`;
  }

  /**
  * To fetch list of devices.
  */
  public getProblemReports(dateRange) {
    if (dateRange) {
      // tslint:disable-next-line:max-line-length
      return this._httpClient.post(`${this.admAE}json/rest/api/v1/createReport?fromDate=${dateRange.fromDate}&toDate=${dateRange.toDate}`, {withCredentials: true});
    } else {
      // tslint:disable-next-line:max-line-length
    //  return this._httpClient.get(`${this.auditAE}getmyaudittrail&userid=${this.userName}&sortorder=${this.sortOrder}`, { headers:  new HttpHeaders({ 'Content-Type': 'application/json;charset=utf-8' }), withCredentials: true}).pipe(catchError(this.handleError));
    }
  }


  public handleError(err: HttpErrorResponse) {
      let errMsg = '';
      if (err.error instanceof Error) {
        errMsg = err.error.message;
      } else {
        errMsg = err.error;
      }
      return throwError(errMsg);
  }
}
