import { Injectable } from '@angular/core';
import { SharedService } from './shared.service';
import { Observable, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { AuditDateRange, AuditLog } from '../models/audit.model';

@Injectable({
  providedIn: 'root'
})
export class AlService {

  appProperties: any;
  dmApiEnd: string;
  uiConf: string;
  host: string;
  userName: string = sessionStorage.getItem('userName');
  sortOrder: string = 'DESC';
  auditAE: string;
  admAE: string;

  constructor(private _httpClient: HttpClient,
              private _sharedSvc: SharedService) { 
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    this.dmApiEnd = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.dmmApi.port}/${this.appProperties.dmmApi.module}/json`;
    this.auditAE = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.auditApi.port}/${this.appProperties.auditApi.module}/`;
    this.admAE = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.admmApi.port}/${this.appProperties.admmApi.module}/`;
    this.uiConf = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.appPort}/admin_ui/assets/JsonFiles/dmm/`;
    this.host = `${this.appProperties.protocol}://${this.appProperties.host}`;
  } 

  /**
  * To fetch list of devices.
  */
  public getAuditLogs(dateRange: AuditDateRange) {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/zip');
    if(dateRange){
      return this._httpClient.get(`${this.admAE}json/rest/api/v1/getAuditLog?fromDate=${dateRange.fromDate}&toDate=${dateRange.toDate}&companydomainname=*`, {responseType: 'blob', observe: 'response', withCredentials: true});
    }else{
      return this._httpClient.get(`${this.auditAE}getmyaudittrail&userid=${this.userName}&sortorder=${this.sortOrder}`, { headers:  new HttpHeaders({ 'Content-Type': 'application/json;charset=utf-8' }), withCredentials: true}).pipe(catchError(this.handleError));
    }
  }

  public deleteFile(fileName: string){
    return this._httpClient.get(`${this.admAE}json/rest/api/v1/removeAuditFile?fileName=${fileName}`, { headers:  new HttpHeaders({ 'Content-Type': 'application/json;charset=utf-8' }), withCredentials: true}).pipe(catchError(this.handleError));
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
