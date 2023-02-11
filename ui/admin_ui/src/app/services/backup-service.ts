import { Injectable } from '@angular/core';
import { SharedService } from './shared.service';
import { throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BackupService {

  appProperties: any;
  uiConf: string;
  host: string;
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
 public getBackupDetails() {
    // tslint:disable-next-line:max-line-length
    return this._httpClient.get(`${this.admAE}json/rest/api/v1/backup`);
}

 public generateBackup(outputPath) {
  const headers = new HttpHeaders();
  headers.set('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.post(`${this.admAE}json/rest/api/v1/manualBackup`, JSON.stringify(outputPath), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true}).pipe(catchError(this.handleError));
 }

public updateScheduleBckup(interval) {
  const headers = new HttpHeaders();
  headers.set('Content-Type', 'application/json');
  // tslint:disable-next-line:max-line-length
  return this._httpClient.put(`${this.admAE}json/rest/api/v1/scheduledBackup`, JSON.stringify(interval), { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }).pipe(catchError(this.handleError));
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
